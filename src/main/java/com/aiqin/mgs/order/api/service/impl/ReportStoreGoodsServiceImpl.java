package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDao;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDetailDao;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ReportOrderAndStoreResponse;
import com.aiqin.mgs.order.api.domain.response.report.ReportStoreGoodsCountResponse;
import com.aiqin.mgs.order.api.service.ReportStoreGoodsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;

/**
 * description: ReportStoreGoodsServiceImpl
 * date: 2020/2/19 17:25
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class ReportStoreGoodsServiceImpl implements ReportStoreGoodsService {

    @Value("${bridge.url.slcs_api}")
    private String slcsiHost;
    @Autowired
    private ReportStoreGoodsDao reportStoreGoodsDao;
    @Autowired
    private ReportStoreGoodsDetailDao reportStoreGoodsDetailDao;

    @Override
    public Boolean insert(ReportStoreGoods entity) {
        entity.setCreateTime(new Date());
        return reportStoreGoodsDao.insertSelective(entity)>0;
    }

    @Override
    public Boolean update(ReportStoreGoods entity) {
        return reportStoreGoodsDao.updateByPrimaryKeySelective(entity)>0;
    }

    @Override
    public PageResData<ReportStoreGoods> getList(PageRequestVO<ReportStoreGoodsVo> searchVo) {
        PageResData result=new PageResData();
        if(searchVo.getSearchVO()!=null&&StringUtils.isNotBlank(searchVo.getSearchVO().getStoreCode())&&StringUtils.isNotBlank(searchVo.getSearchVO().getCountTime())){
            PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
            List<ReportStoreGoods> reportStoreGoods = reportStoreGoodsDao.selectList(searchVo.getSearchVO());
            result.setTotalCount((int)((Page)reportStoreGoods).getTotal());
            result.setDataList(reportStoreGoods);
        }
        return result;
    }

    @Override
    public List<ReportStoreGoodsDetail> getCountDetailList(ReportStoreGoodsDetailVo searchVo) {
        List<ReportStoreGoodsDetail> list=new ArrayList<>();
        if(StringUtils.isNotBlank(searchVo.getStoreCode())&&StringUtils.isNotBlank(searchVo.getBrandId())&&StringUtils.isNotBlank(searchVo.getCountTime())){
            list =reportStoreGoodsDetailDao.selectList(searchVo);
        }
        return list;
    }

    @Override
    @Transactional
    public void dealWith() {
        long start=System.currentTimeMillis();
        log.info("开始执行门店补货定时任务");
        String url=slcsiHost+"/store/getAllStoreCode";
        log.info("查询所有门店编码,请求url={}", url);
        HttpClient httpClient = HttpClient.get(url);
        Map<String, Object> result = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
        log.info("调用slcs系统返回结果，result={}", JSON.toJSON(result));
        if(result==null){
            log.info("调用门店系统--查询所有门店编码失败");
            return;
        }
        List<String> jsonMap=new ArrayList<>();
        if (StringUtils.isNotBlank(result.get("code").toString()) && "0".equals(String.valueOf(result.get("code")))) {
            jsonMap = JSONArray.parseArray(JSON.toJSONString(result.get("data")), String.class);
            log.info("jsonMap:"+jsonMap);
        } else {
            log.info("查询所有门店编码为空");
            return;
        }
//        jsonMap.add("60000028");
        //根据门店编码批量查询是否在本月下过订单
        List<ReportOrderAndStoreResponse> reportOrderAndStoreResponses = reportStoreGoodsDao.selectOrderByStoreCodes(jsonMap);
        if(CollectionUtils.isEmpty(reportOrderAndStoreResponses)){
            log.info("所有门店在本月均未补过货");
            return;
        }
        log.info("根据门店编码批量查询是否在本月下过订单，一共下单:"+reportOrderAndStoreResponses.size()+"次，消耗时间:"+(System.currentTimeMillis()-start));
        log.info("根据门店编码批量查询是否在本月下过订单，返回结果:"+reportOrderAndStoreResponses);
        Map<String,String> map=new HashMap<>();
        for(ReportOrderAndStoreResponse re:reportOrderAndStoreResponses){
            if(re!=null&&null!=re.getStoreCode()&&null!=re.getOrderStoreCode()){
                map.put(re.getStoreCode(),re.getOrderStoreCode());
            }
        }
        log.info("查询所有下过单的门店编码集合:"+map);
        //判断门店编码集合是否为空，不为空，遍历查询信息
        if(map!=null && map.size()>0){
//        if(CollectionUtils.isNotEmpty(strings)){
//            for(String storeCode:jsonMap){
//            for(String orderStoreCode:strings){
            for(Entry<String, String> entry:map.entrySet()){
                String storeCode3=entry.getKey();
                //查询门店下补货品牌、数量（主表）
                List<ReportStoreGoods> reportStoreGoodsCountResponses = reportStoreGoodsDao.selectProductCount(storeCode3);
//                List<ReportStoreGoods> reportStoreGoodsCountResponses = reportStoreGoodsDao.selectProductCount1(orderStoreCode);
                if(CollectionUtils.isEmpty(reportStoreGoodsCountResponses)){//未查询到订单数据
//                    log.info("查询"+storeCode+"该门店下补货品牌、数量为空");
                    continue;
                }
                //查询门店下补货，各个品牌下商品信息
                List<ReportStoreGoodsDetail> reportStoreGoodsDetails = reportStoreGoodsDao.selectReportStoreGoodsDetail(storeCode3);
//                List<ReportStoreGoodsDetail> reportStoreGoodsDetails = reportStoreGoodsDao.selectReportStoreGoodsDetail1(orderStoreCode);
                if(CollectionUtils.isEmpty(reportStoreGoodsDetails)){
//                    log.info("查询"+storeCode+"该门店下补货，各个品牌下商品信息为空");
                    continue;
                }
                //根据订单编码反向查找门店编码
//                String storeCode2=reportStoreGoodsDao.selectStoreCodeByOrderCode(orderStoreCode);

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
                Date time=new Date();
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(time);
                rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                Date d=rightNow.getTime();
                String countTime = sdf.format(d);
                reportStoreGoodsDetails.forEach(p -> p.setStoreCode(storeCode3));
                reportStoreGoodsDetails.forEach(p -> p.setCreateTime(new Date()));
                reportStoreGoodsDetails.forEach(p -> p.setCountTime(countTime));
                //根据门店编码、统计时间清空数据
                ReportStoreGoodsDetailVo vo=new ReportStoreGoodsDetailVo();
                vo.setStoreCode(storeCode3);
                vo.setCountTime(countTime);
                List<ReportStoreGoodsDetail> reportStoreGoodsDetails1 = reportStoreGoodsDetailDao.selectList(vo);
                if(CollectionUtils.isNotEmpty(reportStoreGoodsDetails1)){
                    reportStoreGoodsDetailDao.deleteByStoreCodeAndCountTime(vo);
                }
                //插入门店补货列表统计detail报表
                reportStoreGoodsDetailDao.insertBatch(reportStoreGoodsDetails);
                reportStoreGoodsCountResponses=reportStoreGoodsCountResponses.stream().map(detailVo -> {
                    ReportStoreGoods detail = new ReportStoreGoods();
                    BeanUtils.copyProperties(detailVo,detail);
                    String productBrandCode = detail.getBrandId();
                    ReportStoreGoodsDetailVo reportStoreGoodsDetailVo=new ReportStoreGoodsDetailVo();
                    reportStoreGoodsDetailVo.setStoreCode(storeCode3);
                    reportStoreGoodsDetailVo.setBrandId(productBrandCode);
                    reportStoreGoodsDetailVo.setCountTime(countTime);
                    //各个品牌总金额
                    BigDecimal amount = reportStoreGoodsDetailDao.sumAmountByBrandId(reportStoreGoodsDetailVo);
                    detail.setAmount(amount);
                    detail.setCreateTime(new Date());
                    rightNow.setTime(time);
                    rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                    rightNow.add(Calendar.YEAR,-1);//日期减1年
                    Date dt1=rightNow.getTime();
                    String reStr = sdf.format(dt1);
                    ReportStoreGoods r=new ReportStoreGoods();
                    r.setStoreCode(storeCode3);
                    r.setCountTime(reStr);
                    r.setBrandId(productBrandCode);
                    //计算同比
                    ReportStoreGoods reportStoreGoods = reportStoreGoodsDao.selectReportStoreGoods(r);
                    BigDecimal a=new BigDecimal(100.0000);
                    BigDecimal b=new BigDecimal(100.0000);
                    if(reportStoreGoods!=null&&reportStoreGoods.getNum()>0){
                        if(null!=detail.getAmount()){
                            BigDecimal cha=detail.getAmount().subtract(reportStoreGoods.getAmount());
                            a=cha.divide(reportStoreGoods.getAmount());
                        }
                    }
                    rightNow.setTime(time);
                    rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                    rightNow.add(Calendar.MONTH,-1);//日期减1个月
                    Date dt2=rightNow.getTime();
                    String reStr1 = sdf.format(dt2);
                    r.setCountTime(reStr1);
                    //计算环比
                    reportStoreGoods = reportStoreGoodsDao.selectReportStoreGoods(r);
                    if(reportStoreGoods!=null&&reportStoreGoods.getNum()>0){
                        if(null!=detail.getNum()){
                            BigDecimal cha=detail.getAmount().subtract(reportStoreGoods.getAmount());
                            b=cha.divide(reportStoreGoods.getAmount());
                        }
                    }
                    detail.setChainRatio(b);
                    detail.setTongRatio(a);
                    detail.setCountTime(countTime);
                    detail.setStoreCode(storeCode3);
                    return detail;
                }).collect(Collectors.toList());
                //根据门店编码、统计时间清空数据
                ReportStoreGoodsVo vo1=new ReportStoreGoodsVo();
                BeanUtils.copyProperties(vo,vo1);
                List<ReportStoreGoods> reportStoreGoods = reportStoreGoodsDao.selectList(vo1);
                if(CollectionUtils.isNotEmpty(reportStoreGoods)){
                    reportStoreGoodsDao.deleteByStoreCodeAndCountTime(vo);
                }
                //插入数据
                reportStoreGoodsDao.insertBatch(reportStoreGoodsCountResponses);
                //查询各个sku对应的总金额
//                List<BigDecimal> amounts=reportStoreGoodsDetails.stream().map(ReportStoreGoodsDetail::getAmount).collect(Collectors.toList());
//                BigDecimal totalAmount=new BigDecimal(0);
//                for(BigDecimal amount:amounts){
//                    totalAmount=totalAmount.add(amount);
//                }
            }
        }else{
            log.info("校验查询所有门店编码为空");
        }
        log.info("执行门店补货定时任务结束，消耗时间:"+(System.currentTimeMillis()-start));
    }

    @Override
    public void test() {
        long start=System.currentTimeMillis();
        log.info("开始执行门店补货定时任务");
        String url="http://slcs.api.aiqin.com/store/getAllStoreCode";
        log.info("查询所有门店编码,请求url={}", url);
        HttpClient httpClient = HttpClient.get(url);
        Map<String, Object> result = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
        log.info("调用slcs系统返回结果，result={}", JSON.toJSON(result));
        if(result==null){
            log.info("调用门店系统--查询所有门店编码失败");
            return;
        }
        List<String> jsonMap=new ArrayList<>();
        if (StringUtils.isNotBlank(result.get("code").toString()) && "0".equals(String.valueOf(result.get("code")))) {
            jsonMap = JSONArray.parseArray(JSON.toJSONString(result.get("data")), String.class);
            log.info("jsonMap:"+jsonMap);
        } else {
            log.info("查询所有门店编码为空");
            return;
        }
        List<ReportOrderAndStoreResponse> strings = reportStoreGoodsDao.selectOrderByStoreCodes(jsonMap);
        log.info("测试结束，一共:"+strings.size()+"条，消耗时间:"+(System.currentTimeMillis()-start));

    }

    public static void main(String[] args) {
//        StringBuilder sb = new StringBuilder("http://slcs.api.aiqin.com/store/getAllStoreCode");
//        log.info("查询dl闭店审批信息,请求url为{}", sb);
//        HttpClient httpClient = HttpClient.get(sb.toString());
//        Map<String, Object> result;
//        result = httpClient.action().result(new TypeReference<Map<String, Object>>() {
//        });
//        log.info("调用主控反参result{}", JSON.toJSON(result));
//        if (StringUtils.isNotBlank(String.valueOf(result.get("code"))) && "0".equals(String.valueOf(result.get("code")))) {
//            List<String> jsonMap = JSONArray.parseArray(JSON.toJSONString(result.get("data")), String.class);
//            log.info("jsonMap:"+jsonMap);
//        } else {
//            log.info("33333333333333333333333");
//    }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Date time=new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(time);
        rightNow.add(Calendar.DAY_OF_MONTH,-19);//日期减1年
        rightNow.add(Calendar.YEAR,-1);//日期减1年
        Date dt1=rightNow.getTime();
        String reStr = sdf.format(dt1);
        System.out.println(reStr);
    }
}
