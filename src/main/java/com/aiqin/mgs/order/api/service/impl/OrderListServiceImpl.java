package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
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
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
import com.aiqin.mgs.order.api.domain.response.stock.StockLockRespVo;
import com.aiqin.mgs.order.api.service.BridgeStockService;
import com.aiqin.mgs.order.api.service.OrderListService;
import com.aiqin.ground.util.id.IdUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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


    //商品项目地址
    @Value("${product_ip}")
    public String product_ip;

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

        return orderListLogisticsDao.insertLogistics(param);
    }

    @Override
    public Boolean updateOrderStatus(String code, Integer status) {
        if (code == null || code.length() == 0 || status == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        OrderStatus orderStatus = orderStatusDao.searchStatus(status);
        if (orderStatus == null) {
            throw new IllegalArgumentException("状态值未找到");
        }

        Boolean br = orderListDao.updateByCode(code, status, orderStatus.getPaymentStatus());
        //将订单状态改完支付,将订单发送给供应链
        if (status == 2 && br == true) {
            //获取订单信息
            OrderListDetailsVo vo = orderListDao.searchOrderByCode(code);
            List<OrderListProduct> list2 = orderListProductDao.searchOrderListProductByCode(code);
            vo.setOrderListProductList(list2);


            HttpClient httpPost = HttpClient.post(product_ip + "/purchase/order/add").json("1");
            httpPost.action().status();
            HttpResponse result = httpPost.action().result(new TypeReference<HttpResponse>() {
            });
            String c = result.getCode();
        }
        return br;
    }

    @Override
    public List<OrderStockReVo> getStockValue(OrderStockVo vo) {
        return orderListDao.getStockValue(vo);
    }

    @Override
    public OrderListDetailsVo getOrderByCode(String code) {
        //todo
        OrderListDetailsVo vo = orderListDao.searchOrderByCode(code);

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
//           String orderCode= sequenceService.generateOrderCode(param.getCompanyCode(), param.getOrderType());
//            param.setOrderCode(orderCode);
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

    @Override
    public Boolean save(OrderReqVo reqVo) {
//        List<StockLockSkuReqVo> skuReqVos = reqVo.getProducts().stream().map(product -> {
//            StockLockSkuReqVo skuReqVo = new StockLockSkuReqVo();
//            skuReqVo.setNum(product.getProductNumber());
//            skuReqVo.setSku_code(product.getSkuCode());
//            return skuReqVo;
//        }).collect(Collectors.toList());
//        StockLockReqVo stockLockReqVo = new StockLockReqVo();
//        stockLockReqVo.setSkuList(skuReqVos);
//        stockLockReqVo.setCityId(reqVo.getCityCode());
//        stockLockReqVo.setCompanyCode(reqVo.getCompanyCode());
//        stockLockReqVo.setProvinceId(reqVo.getProvinceCode());
//        stockLockReqVo.setOrderCode(reqVo.getOriginal());
//        List<StockLockRespVo> lockRespVos = bridgeStockService.lock(stockLockReqVo);
//        Map<String, List<StockLockRespVo>> stockMap = lockRespVos.stream().collect(Collectors.toMap(StockLockRespVo::getSkuCode, Lists::newArrayList, (l1, l2) -> {
//            l1.addAll(l2);
//            return l1;
//        }));
//        Map<String, OrderListProduct> productMap = Maps.newLinkedHashMap();
//        reqVo.getProducts().forEach(product -> {
//            List<StockLockRespVo> lockRespVoList = stockMap.get(product.getSkuCode());
//            Assert.notEmpty(lockRespVoList, "锁定库存异常");
//            //商品价格（单品合计成交价)
//            long totalProductPrice = 0L;
//            long pr
//            for (int i = 0; i < lockRespVoList.size() - 1; i++) {
//                StockLockRespVo stockLock = lockRespVoList.get(i);
//                OrderListProduct productDTO = new OrderListProduct();
//                BeanUtils.copyProperties(product, productDTO);
//                //重新计算价格
//                productDTO.setProductNumber(stockLock.getLockNum());
//                long productPrice = stockLock.getLockNum() * product.getProductPrice() / product.getProductNumber();
//                productDTO.setProductPrice(productPrice);
//                totalProductPrice += productPrice;
//                productDTO.setAmount();
//            }
//
//        });
        return null;
    }
}
