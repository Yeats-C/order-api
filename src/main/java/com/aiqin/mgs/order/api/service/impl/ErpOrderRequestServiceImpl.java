package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.response.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.domain.response.StoreBackInfoResponse;
import com.aiqin.mgs.order.api.service.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ErpOrderRequestServiceImpl implements ErpOrderRequestService {

    @Resource
    private UrlProperties urlProperties;

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderRequestServiceImpl.class);

    @Override
    public StoreInfo getStoreInfoByStoreId(String storeId) {
        StoreInfo storeInfo = new StoreInfo();
        try {
            HttpClient httpClient = HttpClient.get(urlProperties.getSlcsApi() + "/store/getStoreInfo?store_id=" + storeId);
            HttpResponse<StoreBackInfoResponse> response = httpClient.action().result(new TypeReference<HttpResponse<StoreBackInfoResponse>>() {
            });
            if (RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("获取门店信息失败");
            }
            StoreBackInfoResponse data = response.getData();
            storeInfo.setStoreId(data.getStoreId());
            storeInfo.setStoreCode(data.getStoreCode());
            storeInfo.setStoreName(data.getStoreName());
            storeInfo.setAddress(data.getAddress());
            storeInfo.setContacts(data.getContacts());
            storeInfo.setContactsPhone(data.getContactsPhone());
            storeInfo.setFranchiseeId("franchiseeId");
            storeInfo.setFranchiseeCode("franchiseeCode");
            storeInfo.setFranchiseeName("加盟商名称");
        } catch (Exception e) {
            logger.info("获取门店信息失败：{}", e);
            throw new BusinessException("获取门店信息失败");
        }

        if (StringUtils.isEmpty(storeInfo.getStoreId())) {
            //生成临时假数据
            storeInfo.setStoreId(storeId);
            storeInfo.setStoreCode("123456");
            storeInfo.setStoreName("门店1");
            storeInfo.setFranchiseeId("franchiseeId");
            storeInfo.setFranchiseeCode("franchiseeCode");
            storeInfo.setFranchiseeName("加盟商名称");
            storeInfo.setContacts("张三");
            storeInfo.setContactsPhone("12345678910");
            storeInfo.setAddress("北京市朝阳区");
        }

        return storeInfo;
    }

    @Override
    public ProductInfo getProductDetail(String storeId, String productId, String skuCode) {
        ProductInfo product = new ProductInfo();
        try {
            product.setProductId(productId);
            product.setProductCode(productId + System.currentTimeMillis());
            product.setProductName("商品名称" + System.currentTimeMillis());
            product.setSkuCode(skuCode);
            product.setSkuName(skuCode + System.currentTimeMillis());
            product.setSkuCode("123456");
            product.setSkuName("123456" + System.currentTimeMillis());
            product.setUnit("个");
            product.setPrice(BigDecimal.TEN);
            product.setTaxPurchasePrice(BigDecimal.TEN.add(BigDecimal.ONE));

            List<ProductRepertoryQuantity> productRepertoryQuantityList = new ArrayList<>();
            ProductRepertoryQuantity repertoryQuantity1 = new ProductRepertoryQuantity();
            repertoryQuantity1.setRepertoryCode("10001");
            repertoryQuantity1.setRepertoryName("仓库1");
            repertoryQuantity1.setQuantity(10);
            productRepertoryQuantityList.add(repertoryQuantity1);
            ProductRepertoryQuantity repertoryQuantity2 = new ProductRepertoryQuantity();
            repertoryQuantity2.setRepertoryCode("10002");
            repertoryQuantity2.setRepertoryName("仓库2");
            repertoryQuantity2.setQuantity(50);
            productRepertoryQuantityList.add(repertoryQuantity2);
            ProductRepertoryQuantity repertoryQuantity3 = new ProductRepertoryQuantity();
            repertoryQuantity3.setRepertoryCode("10003");
            repertoryQuantity3.setRepertoryName("仓库3");
            repertoryQuantity3.setQuantity(200);
            productRepertoryQuantityList.add(repertoryQuantity3);
            product.setRepertoryQuantityList(productRepertoryQuantityList);

            //TODO 从供应链获取商品详情
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("storeId", storeId);
//            paramMap.put("productId", productId);
//            paramMap.put("skuCode", skuCode);
//            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
        } catch (Exception e) {
            logger.error("获取商品信息失败：{}", e);
            throw new BusinessException("获取商品详情信息失败");
        }
        return product;
    }

    @Override
    public ErpOrderPayStatusResponse getOrderPayStatus(String orderCode) {
        ErpOrderPayStatusResponse response = new ErpOrderPayStatusResponse();
        response.setOrderCode(orderCode);
        try {
            //TODO 请求支付中心接口查询订单支付状态
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            response.setPayCode(OrderPublic.getUUID());
            response.setPayStatusEnum(PayStatusEnum.SUCCESS);
        } catch (Exception e) {
            response.setRequestSuccess(false);
            logger.error("获取订单支付信息失败：{}", e);
        }
        return response;
    }

    @Override
    public boolean sendPayRequest(OrderStoreOrderInfo order, OrderStoreOrderPay orderPay) {
        boolean flag = false;
        try {
            //TODO 请求支付中心接口查询订单支付状态
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("获取订单支付信息失败：{}", e);
        }
        return flag;
    }

    @Override
    public BigDecimal getGoodsCoupon(OrderStoreOrderInfo order) {
        BigDecimal goodsCoupon = null;
        try {
            //TODO 获取本次支付成功返回物流券
            Map<String, Object> paramMap = new HashMap<>();
            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });
        } catch (Exception e) {
            logger.error("获取返回物流券失败：{}", e);
        }
        return goodsCoupon;
    }

    @Override
    public boolean sendOrderPaySuccess(OrderStoreOrderInfo order) {
        boolean flag = false;
        try {
            //TODO 订单支付成功通知供应链
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("订单支付成功通知供应链失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean areaCheck(StoreInfo storeInfo, List<OrderStoreOrderProductItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO 商品销售区域配置校验
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("商品销售区域配置校验失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean repertoryCheck(StoreInfo storeInfo, List<OrderStoreOrderProductItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO 商品库存校验
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("商品库存校验失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean priceCheck(StoreInfo storeInfo, List<OrderStoreOrderProductItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO 商品销售价格校验
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("商品销售价格校验失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean activityCheck(StoreInfo storeInfo, List<OrderStoreOrderProductItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO 促销活动校验
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("促销活动校验失败：{}", e);
        }
        return flag;
    }
}
