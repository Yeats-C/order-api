package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.domain.dto.ProductDistributorOrderDTO;
import com.aiqin.mgs.order.api.domain.request.statistical.BusinessStatisticalRequest;
import com.aiqin.mgs.order.api.domain.request.statistical.ProductDistributorOrderRequest;
import com.aiqin.mgs.order.api.domain.request.statistical.SkuSalesRequest;
import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
import com.aiqin.mgs.order.api.domain.response.statistical.Last10DaysOrderStatistical;
import com.aiqin.mgs.order.api.domain.statistical.BusinessStatistical;
import com.aiqin.mgs.order.api.domain.statistical.SkuSales;
import com.aiqin.mgs.order.api.domain.statistical.SoldOutOfStockProduct;
import com.aiqin.mgs.order.api.service.OrderStatisticalService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.builder.BusinessStatisticalBuilder;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
@Service
@Slf4j
public class OrderStatisticalServiceImpl implements OrderStatisticalService {

    private final static String SALE_OUT_OF_STOCK_PREFIX = "sale:prefix:";

    @Resource
    private OrderDao orderDao;

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public BusinessStatisticalResponse businessStatistical(String distributorId) {
        BusinessStatisticalResponse response;
        try {
            //昨日数据统计
            BusinessStatisticalRequest yesterday = new BusinessStatisticalRequest();
            yesterday.setDistributorId(distributorId);
            yesterday.setStartDate(DateUtil.getBeginYesterday(DateUtil.getCurrentDate()));
            yesterday.setEndDate(DateUtil.getEndYesterDay(DateUtil.getCurrentDate()));
            CompletableFuture<BusinessStatistical> yesterdayStatistical = CompletableFuture.supplyAsync(() -> {
                try {
                    List<BusinessStatistical> list = orderDao.statisticalBusiness(yesterday);
                    if (CollectionUtils.isNotEmpty(list)) {
                        return list.get(0);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
            //今日数据统计
            BusinessStatisticalRequest today = new BusinessStatisticalRequest();
            today.setDistributorId(distributorId);
            today.setStartDate(DateUtil.getDayBegin(DateUtil.getCurrentDate()));
            today.setEndDate(DateUtil.getDayEnd(DateUtil.getCurrentDate()));
            CompletableFuture<BusinessStatistical> todayStatistical = CompletableFuture.supplyAsync(() -> {
                try {
                    List<BusinessStatistical> list = orderDao.statisticalBusiness(today);
                    if (CollectionUtils.isNotEmpty(list)) {
                        return list.get(0);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            //当前数据统计
            BusinessStatisticalRequest year = new BusinessStatisticalRequest();
            year.setDistributorId(distributorId);
            year.setStartDate(DateUtil.getCurrentYearBeginTime());
            year.setEndDate(DateUtil.getCurrentDate());
            year.setGroupByFlag(true);
            CompletableFuture<List<BusinessStatistical>> yearStatistical = CompletableFuture.supplyAsync(() -> {
                try {
                    List<BusinessStatistical> list = orderDao.statisticalBusiness(year);
                    if (CollectionUtils.isNotEmpty(list)) {
                        return list;
                    } else {
                        return Lists.newArrayList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            CompletableFuture.allOf(yesterdayStatistical, todayStatistical, yearStatistical);
            response = BusinessStatisticalBuilder.create(yesterdayStatistical.get(), todayStatistical.get(), yearStatistical.get()).builder();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("error [{}]", ex);
        }
        return null;
    }


    @Override
    public List<String> existsSalesDistributorInNumDays(Date date, int day) {
        Date start = DateUtil.getBeforeDate(DateUtil.getBeginYesterday(date), day);
        Date end = DateUtil.getEndYesterDay(date);
        return orderDao.querySaleDistributor(start, end);
    }

    @Override
    public void refreshDistributorSoldOutOfStockProduct(Date date, String distributorId) {
        StopWatch sw = new StopWatch();
        Date start = DateUtil.getBeforeDate(DateUtil.getBeginYesterday(date), 29);
        Date end = DateUtil.getEndYesterDay(date);
        //获取销量前10的sku的集合
        sw.start("获取销量前10的sku的集合");
        List<String> top10SkuCodes = orderDao.queryTop10SaleSku(distributorId, start, end);
        sw.stop();

        //根据获取的sku集合获取低库存预警数据信息
        //此数据可能不准确，在统计之后人为改动库存预警值导致数据不统一
        sw.start("根据获取的sku集合获取低库存预警数据信息");
        ProductDistributorOrderRequest request = new ProductDistributorOrderRequest();
        request.setDistributorId(distributorId);
        request.setSkuCodes(top10SkuCodes);
        List<ProductDistributorOrderDTO> list = bridgeProductService.getProductDistributorOrderDTO(request);
        sw.stop();

        sw.start("获取畅缺商品14天销量");
        Date start1 = DateUtil.getBeforeDate(DateUtil.getBeginYesterday(date), 13);
        Date end1 = DateUtil.getEndYesterDay(date);
        SkuSalesRequest skuSalesRequest = new SkuSalesRequest();
        skuSalesRequest.setDistributorId(distributorId);
        skuSalesRequest.setStartDate(start1);
        skuSalesRequest.setEndDate(end1);
        List<SkuSales> skuSales = Lists.newArrayList();

        Map<String, SkuSales> mapSales = Maps.newHashMap();
        Map<String, ProductDistributorOrderDTO> mapProduct = Maps.newHashMap();

        if (CollectionUtils.isNotEmpty(list)) {
            List<String> skuCodes = list.stream().map(a -> a.getSkuCode()).collect(Collectors.toList());
            mapProduct = list.stream().collect(Collectors.toMap(ProductDistributorOrderDTO::getSkuCode, Function.identity(), (a, b) -> a));
            if (CollectionUtils.isNotEmpty(skuCodes)) {
                skuSalesRequest.setSkuCodes(skuCodes);
                skuSales = orderDao.querySkuSale(skuSalesRequest);
                mapSales = skuSales.stream().collect(Collectors.toMap(SkuSales::getSkuCode, Function.identity(), (a, b) -> a));
            }
        }
        sw.stop();

        sw.start("组装数据写入缓存");
        List<SoldOutOfStockProduct> redisProduct = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (String item : top10SkuCodes) {
                ProductDistributorOrderDTO curDTO = mapProduct.get(item);
                SkuSales curSale = mapSales.get(item);
                if (Objects.isNull(curDTO)) {
                    continue;
                }
                SoldOutOfStockProduct cur = new SoldOutOfStockProduct();
                cur.setLogo(curDTO.getLogo());
                cur.setProductName(curDTO.getProductName());
                cur.setSkuCode(item);
                cur.setStock(Optional.ofNullable(curDTO.getDisplayStock()).orElse(0)
                        - Optional.ofNullable(curDTO.getLockStock()).orElse(0));
                cur.setSales(Objects.isNull(curSale) ? 0 : curSale.getSales());
                redisProduct.add(cur);
            }
        }
        if (CollectionUtils.isNotEmpty(redisProduct)) {
            setSoldOutOfStockProductInRedis(redisProduct, distributorId);
        }
        sw.stop();

        log.info("执行统计:[{}]", sw.prettyPrint());
    }

    @Override
    public List<SoldOutOfStockProduct> getSoldOutOfStockProduct(String distributorId) {
        return getSoldOutOfStockProductFromRedisByDistributorId(distributorId);
    }

    @Override
    public void refreshDistributorDisUnsoldProduct(Date date, String distributorId) {
        StopWatch sw = new StopWatch();
        Date start = DateUtil.getBeforeDate(DateUtil.getBeginYesterday(date), 89);
        Date end = DateUtil.getEndYesterDay(date);
        //获取销量大于等于5的sku集合
        sw.start("获取销量大于等于5的sku集合");
        List<String> soldSkuCodes = orderDao.querySaleSkuGtNum(distributorId, start, end, 5);
        sw.stop();

        sw.start("写入缓存");
        if (CollectionUtils.isNotEmpty(soldSkuCodes)) {
            setSoldInRedis(soldSkuCodes, distributorId);
        }
        sw.stop();

        log.info("统计信息:[{}]", sw.prettyPrint());
    }

    @Override
    public List<String> getDisUnsoldProduct(String distributorId) {
        String key = "un:sale:prefix:" + distributorId;
        List<String> list = Lists.newArrayList();

        if (!redisTemplate.hasKey(key)) {
            return list;
        }
        list = redisTemplate.opsForList().range(key, 0, -1);
        return list;
    }

    @Override
    public List<Last10DaysOrderStatistical> getLast10DaysOrderStatisticals(String distributorId) {
        Date start = DateUtil.getBeforeDate(DateUtil.getCurrentDate(), 9);
        Date end = DateUtil.getDayEnd(DateUtil.getCurrentDate());
        return orderDao.queryLast10DaysOrderStatistical(distributorId, start, end);
    }

    /**
     * 畅缺商品数据存入缓存
     *
     * @param list
     */
    private void setSoldOutOfStockProductInRedis(List<SoldOutOfStockProduct> list, String distributorId) {
        String key1 = SALE_OUT_OF_STOCK_PREFIX + distributorId;
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //当天截止时间 30秒随机数内过期
        List<String> skuCodes = list.stream().map(a -> a.getSkuCode()).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key1, StringUtils.join(skuCodes, ","), DateUtil.getExpireEndRandomSeconds(30L), TimeUnit.SECONDS);
        for (SoldOutOfStockProduct item : list) {
            Map<String, Object> curMap = new HashMap();
            String curKey = key1 + ":" + item.getSkuCode();
            curMap.put("skuCode", item.getSkuCode());
            curMap.put("productName", item.getProductName());
            curMap.put("logo", item.getLogo());
            curMap.put("sales", item.getSales());
            curMap.put("stock", item.getStock());
            redisTemplate.opsForHash().putAll(curKey, curMap);
            redisTemplate.expire(curKey, DateUtil.getExpireEndRandomSeconds(30L), TimeUnit.SECONDS);
        }
    }

    /**
     * 从缓存中获取畅缺商品数据
     *
     * @param distributorId
     * @return
     */
    private List<SoldOutOfStockProduct> getSoldOutOfStockProductFromRedisByDistributorId(String distributorId) {

        String key = SALE_OUT_OF_STOCK_PREFIX + distributorId;
        String value = "";
        //skuCode集合
        List<String> list = Lists.newArrayList();
        List<SoldOutOfStockProduct> re = Lists.newArrayList();
        if (redisTemplate.hasKey(key)) {
            value = redisTemplate.opsForValue().get(key).toString();
        }

        if (StringUtils.isNotBlank(value)) {
            String[] arr = StringUtils.split(value, ",");
            if (Objects.nonNull(arr) && arr.length > 0) {
                list = Lists.newArrayList(arr);
            }
        }

        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }

        for (String item : list) {
            String curKey = key + ":" + item;
            Map<String, Object> cur = redisTemplate.opsForHash().entries(curKey);
            boolean isNull = Objects.isNull(cur);
            if (isNull) {
                continue;
            }
            SoldOutOfStockProduct curItem = new SoldOutOfStockProduct();
            if (Objects.nonNull(cur.get("skuCode"))) {
                curItem.setSkuCode(cur.get("skuCode").toString());
            }
            if (Objects.nonNull(cur.get("productName"))) {
                curItem.setProductName(cur.get("productName").toString());
            }
            if (Objects.nonNull(cur.get("logo"))) {
                curItem.setLogo(cur.get("logo").toString());
            }
            if (Objects.nonNull(cur.get("sales"))) {
                curItem.setSales((Integer) cur.get("sales"));
            }
            if (Objects.nonNull(cur.get("stock"))) {
                curItem.setStock((Integer) cur.get("stock"));
            }
            re.add(curItem);
        }
        return re;
    }

    /**
     * 写入缓存 非的滞销sku
     *
     * @param skuCodes
     */
    private void setSoldInRedis(List<String> skuCodes, String distributorId) {
        String key = "un:sale:prefix:" + distributorId;
        if (CollectionUtils.isEmpty(skuCodes)) {
            return;
        }
        redisTemplate.delete(key);
        redisTemplate.opsForList().leftPushAll(key, skuCodes);
        redisTemplate.expire(key, DateUtil.getExpireEndRandomSeconds(30L), TimeUnit.SECONDS);
    }
}
