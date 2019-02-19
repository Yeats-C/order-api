package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.component.OrderStatusEnum;
import com.aiqin.mgs.order.api.component.ParamUnit;
import com.aiqin.mgs.order.api.component.SequenceService;
import com.aiqin.mgs.order.api.dao.OrderListDao;
import com.aiqin.mgs.order.api.dao.OrderListLogisticsDao;
import com.aiqin.mgs.order.api.dao.OrderListProductDao;
import com.aiqin.mgs.order.api.dao.OrderStatusDao;
import com.aiqin.mgs.order.api.domain.OrderList;
import com.aiqin.mgs.order.api.domain.OrderListLogistics;
import com.aiqin.mgs.order.api.domain.OrderListProduct;
import com.aiqin.mgs.order.api.domain.OrderStatus;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.request.stock.StockLockReqVo;
import com.aiqin.mgs.order.api.domain.request.stock.StockLockSkuReqVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderSaveRespVo;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
import com.aiqin.mgs.order.api.domain.response.stock.StockLockRespVo;
import com.aiqin.mgs.order.api.service.BridgeStockService;
import com.aiqin.mgs.order.api.service.OrderListService;
import com.aiqin.ground.util.id.IdUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-04 15:27
 */
@SuppressWarnings("all")
@Service
@Slf4j
public class OrderListServiceImpl implements OrderListService {
    @Resource
    private OrderListDao orderListDao;
    @Resource
    private OrderListLogisticsDao orderListLogisticsDao;
    @Resource
    private OrderListProductDao orderListProductDao;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private OrderStatusDao orderStatusDao;
    @Resource
    private BridgeStockService bridgeStockService;


    //供应链项目地址
    @Value("${purchase_ip}")
    public String purchase_ip;

    /**
     * 订单列表后台
     *
     * @param param
     * @return
     */
    @Override
    public PageResData<OrderList> searchOrderList(OrderListVo param) {
        List<OrderList> inventories = orderListDao.searchOrderList(param);
        int count = orderListDao.searchOrderListCount(param);
        return new PageResData<>(count, inventories);
    }

    /**
     * 订单列表前台
     *
     * @param param
     * @return
     */
    @Override
    public PageResData<OrderList> searchOrderReceptionList(OrderListVo2 param) {
        ParamUnit.isNotNull(param, "storeId");
        List<OrderList> inventories = orderListDao.searchOrderReceptionList(param);
        int count = orderListDao.searchOrderReceptionListCount(param);
        return new PageResData<>(count, inventories);
    }

    @Override
    public Boolean addLogistics(OrderListLogistics param) {
        ParamUnit.isNotNull(param, "orderCode", "invoiceCode", "logisticsCentreCode", "logisticsCentreName", "implementBy", "implementTime", "implementContent");
        Boolean re= orderListLogisticsDao.insertLogistics(param);
        return re;
    }

    @Override
    public Boolean updateOrderStatus(String code, Integer status) {
        if (code == null || code.length() == 0 || status == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        OrderStatusEnum orderStatus = OrderStatusEnum.getOrderStatusEnum(status);
//        OrderStatus orderStatus = orderStatusDao.searchStatus(status);
        if (orderStatus == null) {
            throw new IllegalArgumentException("状态值未找到");
        }
        Boolean br = false;
        if (status != 2) {
            br = orderListDao.updateStatusByCode(code, status);
        }


        //将订单状态改完支付,将订单发送给供应链
        if (status == 2) {
            br = orderListDao.updateByCode(code, status, 1);
            if (br == true) {
                //获取订单信息
                List<SupplyOrderInfoReqVO> vo = orderListDao.searchOrderByCodeOrOriginal(code);
                List<SupplyOrderProductItemReqVO> list2 = orderListProductDao.searchOrderListProductByCodeOrOriginal(code);
                for (SupplyOrderInfoReqVO reqVO : vo) {
                    List<SupplyOrderProductItemReqVO> supplylist = new ArrayList<>();
                    for (SupplyOrderProductItemReqVO itemReqVO : list2) {
                        if (reqVO.getOrderCode().equals(itemReqVO.getOrderCode())) {
                            supplylist.add(itemReqVO);
                        }
                    }
                    reqVO.setOrderItems(supplylist);
                }
                SupplyOrderMainReqVO svo = new SupplyOrderMainReqVO();
                svo.setSubOrderInfo(vo);
                HttpClient httpPost = HttpClient.post("http://" + purchase_ip + "/purchase/order/add").json(svo);
                HttpResponse<List<OrderStockReVo>> result =
                        httpPost.action().result(new TypeReference<HttpResponse<Boolean>>() {
                        });
                String c = result.getCode();
            }
        }
        return br;
    }

    @Override
    public List<OrderStockReVo> getStockValue(OrderStockVo vo) {
        return orderListDao.getStockValue(vo);
    }

    @Override
    public PageResData<OrderListFather> searchOrderReceptionListFather(OrderListVo2 param) {
        ParamUnit.isNotNull(param, "storeId");
        List<OrderListFather> inventories = orderListDao.searchOrderReceptionListFather(param);
        for (OrderListFather inventory : inventories) {
            for (OrderList orderList : inventory.getOrderList()) {
                orderList.setOrderStatusShow(OrderStatusEnum.getOrderStatusEnum(orderList.getOrderStatus()).getReceptionStatus());
            }
        }
        int count = orderListDao.searchOrderReceptionListFatherCount(param);
        return new PageResData<>(count, inventories);
    }

    @Override
    public PageResData<OrderListFather> searchOrderListFather(OrderListVo param) {
        List<OrderListFather> inventories = orderListDao.searchOrderListFather(param);
        for (OrderListFather inventory : inventories) {
            for (OrderList orderList : inventory.getOrderList()) {
                orderList.setOrderStatusShow(OrderStatusEnum.getOrderStatusEnum(orderList.getOrderStatus()).getBackstageStatus());
            }
        }
        int count = orderListDao.searchOrderListFatherCount(param);
        return new PageResData<>(count, inventories);
    }

    @Override
    public Boolean updateOrderRefund(String code) {
        return orderListDao.updateOrderPaymentStatus(code, 2);
    }

    @Override
    public List<OrderListDetailsVo> getOrderByCodeFather(String code) {
        List<OrderListDetailsVo> list = orderListDao.searchOrderByCodeFather(code);
        List<String> orderCode=new ArrayList<>();
        for (OrderListDetailsVo vo : list) {
            OrderStatusEnum statusEnum =OrderStatusEnum.getOrderStatusEnum(vo.getOrderStatus());
            vo.setReceptionStatus( statusEnum.getReceptionStatus());
            vo.setBackstageStatus( statusEnum.getBackstageStatus());
            orderCode.add(vo.getOrderCode());
        }
        if (orderCode.size()>0) {
            List<OrderListProduct> orderListProducts = orderListProductDao.searchOrderListProductByCodeList(orderCode);
            for (OrderListDetailsVo vo : list) {
                for (OrderListProduct orderListProduct : orderListProducts) {
                    if (vo.getOrderCode().equals(orderListProduct.getOrderCode())){
                        if (vo.getOrderListProductList()==null){
                            vo.setOrderListProductList(new ArrayList<>());
                        }
                        vo.getOrderListProductList().add(orderListProduct);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public OrderListDetailsVo getOrderByCode(String code) {

        OrderListDetailsVo vo = orderListDao.searchOrderByCode(code);
        vo.setReceptionStatus( OrderStatusEnum.getOrderStatusEnum(vo.getOrderStatus()).getReceptionStatus());
        vo.setBackstageStatus( OrderStatusEnum.getOrderStatusEnum(vo.getOrderStatus()).getBackstageStatus());
        List<OrderListLogistics> list1 = orderListLogisticsDao.searchOrderListLogisticsByCode(code);
        vo.setOrderListLogisticsList(list1);
        List<OrderListProduct> list2 = orderListProductDao.searchOrderListProductByCode(code);
        vo.setOrderListProductList(list2);
        return vo;
    }

    @Override
    public List<String> add(List<OrderListDetailsVo> paramList) {
        List<String> reList = new ArrayList<>();
        for (OrderListDetailsVo param : paramList) {
            //验证添加数据
            ParamUnit.isNotNull(param, "orderCode", "orderType", "orderStatus", "storeId", "storeCode", "placeOrderTime");

            List<OrderListProduct> orderListProductList = param.getOrderListProductList();
            if (orderListProductList == null || orderListProductList.size() == 0) {
                throw new IllegalArgumentException("商品不可为空");
            }
            for (OrderListProduct orderListProduct : orderListProductList) {
                ParamUnit.isNotNull(orderListProduct, "skuCode", "skuName", "productNumber", "gift");
            }

            String orderCode = param.getOrderCode();
            String orderId = IdUtil.uuid();
            param.setId(orderId);
            Boolean re = orderListDao.insertOrderListDetailsVo(param);
            for (OrderListProduct orderListProduct : orderListProductList) {
                orderListProduct.setId(IdUtil.uuid());
                orderListProduct.setOrderCode(orderCode);
            }
            Boolean re2 = orderListProductDao.insertList(param.getOrderListProductList());
            reList.add(orderCode);
        }
        return reList;
    }

    @Transactional
    @Override
    public OrderSaveRespVo save(OrderReqVo reqVo) {
        String orderCode = sequenceService.generateOrderCode(reqVo.getCompanyCode(), reqVo.getOrderType());
        List<StockLockSkuReqVo> skuReqVos = reqVo.getProducts().stream().map(product -> {
            StockLockSkuReqVo skuReqVo = new StockLockSkuReqVo();
            skuReqVo.setNum(product.getProductNumber());
            skuReqVo.setSku_code(product.getSkuCode());
            return skuReqVo;
        }).collect(Collectors.toList());
        StockLockReqVo stockLockReqVo = new StockLockReqVo();
        stockLockReqVo.setSkuList(skuReqVos);
        stockLockReqVo.setCityId(reqVo.getCityCode());
        stockLockReqVo.setCompanyCode(reqVo.getCompanyCode());
        stockLockReqVo.setProvinceId(reqVo.getProvinceCode());
        stockLockReqVo.setOrderCode(orderCode);
        List<StockLockRespVo> lockRespVos = bridgeStockService.lock(stockLockReqVo);
        Map<String, List<StockLockRespVo>> stockMap = lockRespVos.stream().collect(Collectors.toMap(StockLockRespVo::getSkuCode, Lists::newArrayList, (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        }));
        Map<String, List<OrderListProduct>> productMap = Maps.newLinkedHashMap();
        for (OrderProductReqVo product : reqVo.getProducts()) {
            List<StockLockRespVo> lockRespVoList = stockMap.get(product.getSkuCode());
            Assert.notEmpty(lockRespVoList, "锁定库存异常");
            //商品价格（单品合计成交价)
            long totalProductPrice = 0L;
            long totalDiscountMoney = 0L;
            long totalPreferentialAllocation = 0;
            for (int i = 0; i < lockRespVoList.size(); i++) {
                StockLockRespVo stockLock = lockRespVoList.get(i);
                OrderListProduct productDTO = new OrderListProduct();
                BeanUtils.copyProperties(product, productDTO);
                productDTO.setId(IdUtil.uuid());
                //重新计算价格
                productDTO.setProductNumber(stockLock.getLockNum());
                productDTO.setAmount(stockLock.getLockNum() * product.getOriginalProductPrice());
                //单品合计成交价
                long productPrice;
                //优惠额度抵扣金额（单品合计）
                long discountMoney;
                //优惠分摊
                long preferentialAllocation;
                if (i < lockRespVoList.size() - 1) {
                    productPrice = stockLock.getLockNum() * product.getProductPrice() / product.getProductNumber();
                    totalProductPrice += productPrice;
                    discountMoney = stockLock.getLockNum() * product.getDiscountMoney() / product.getProductNumber();
                    totalDiscountMoney += discountMoney;
                    preferentialAllocation = stockLock.getLockNum() * product.getPreferentialAllocation() / product.getProductNumber();
                    totalPreferentialAllocation += preferentialAllocation;
                } else {
                    productPrice = product.getProductPrice() - totalProductPrice;
                    discountMoney = product.getDiscountMoney() - totalDiscountMoney;
                    preferentialAllocation = product.getPreferentialAllocation() - totalPreferentialAllocation;
                }
                productDTO.setProductPrice(productPrice);
                productDTO.setDiscountMoney(discountMoney);
                productDTO.setPreferentialAllocation(preferentialAllocation);
                //优惠明细分摊
                if (productDTO.getUseDiscountAmount() == 1) {
                    List<DiscountAmountInfo> infos = product.getDiscountAmountInfo().stream().map(info -> {
                        DiscountAmountInfo newInfo = new DiscountAmountInfo();
                        newInfo.setCode(info.getCode());
                        newInfo.setAmount(info.getAmount() * stockLock.getLockNum() / product.getProductNumber());
                        return newInfo;
                    }).collect(Collectors.toList());
                    productDTO.setDiscountAmountInfo(JSON.toJSONString(infos));
                }
                List<OrderListProduct> orderListProducts = productMap.get(stockLock.getWarehouseCode());
                if (CollectionUtils.isEmpty(orderListProducts)) {
                    productMap.put(stockLock.getWarehouseCode(), Lists.newArrayList(productDTO));
                } else {
                    orderListProducts.add(productDTO);
                }
            }
        }
        List<OrderListProduct> orderListProducts = Lists.newLinkedList();
        List<OrderList> orders = Lists.newArrayList();
        List<String> chirldOrderCodes = Lists.newArrayList();
        productMap.forEach((warehouseCode, products) -> {
            OrderList order = new OrderList();
            BeanUtils.copyProperties(reqVo, order);
            if (productMap.size() > 1) {
                String chirldOrderCode = sequenceService.generateOrderCode(reqVo.getCompanyCode(), reqVo.getOrderType());
                order.setOrderCode(chirldOrderCode);
                long totalOrders = products.stream().mapToLong(OrderListProduct::getAmount).sum();
                order.setTotalOrders(totalOrders);
                long actualAmountPaid = products.stream().mapToLong(OrderListProduct::getProductPrice).sum();
                order.setActualAmountPaid(actualAmountPaid);
                long activityAmount = products.stream().mapToLong(OrderListProduct::getPreferentialAllocation).sum();
                order.setActivityAmount(activityAmount);
                long preferentialQuota = products.stream().mapToLong(OrderListProduct::getDiscountMoney).sum();
                order.setPreferentialQuota(preferentialQuota);
                int productNum = products.stream().mapToInt(OrderListProduct::getProductNumber).sum();
                order.setProductNum(productNum);
                int weight = products.stream().mapToInt(product -> product.getWeight() * product.getProductNumber()).sum();
                chirldOrderCodes.add(chirldOrderCode);
                products.forEach(product -> {
                    product.setOrderCode(chirldOrderCode);
                });
            } else {
                order.setOrderCode(orderCode);
                products.forEach(product -> {
                    product.setOrderCode(orderCode);
                });
            }
            order.setId(IdUtil.uuid());
            order.setOriginal(orderCode);
            orderListProducts.addAll(products);
            orders.add(order);
        });
        orders.forEach(order -> {
            orderListDao.insertSelective(order);
        });
        orderListProductDao.insertList(orderListProducts);
        OrderSaveRespVo respVo = new OrderSaveRespVo();
        respVo.setOrderCode(orderCode);
        respVo.setSplitOrder(orders.size() > 1);
        respVo.setChildOrderCode(chirldOrderCodes);
        return respVo;
    }
}
