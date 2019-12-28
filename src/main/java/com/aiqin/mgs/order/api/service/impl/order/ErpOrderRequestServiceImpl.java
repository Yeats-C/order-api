package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderLockStockTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayOrderSourceEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTypeEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.PayRequest;
import com.aiqin.mgs.order.api.domain.response.ProductSkuDetailResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderGoodsCouponResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.domain.response.order.StoreFranchiseeInfoResponse;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ErpOrderRequestServiceImpl implements ErpOrderRequestService {

    @Resource
    private UrlProperties urlProperties;

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderRequestServiceImpl.class);

    @Override
    public StoreInfo getStoreInfoByStoreId(String storeId) {
        storeId = "3604f41aba22481da201e0c3d7a7451a";
        StoreInfo storeInfo = new StoreInfo();
        try {
            HttpClient httpClient = HttpClient.get(urlProperties.getSlcsApi() + "/store/info?store_id=" + storeId);
            HttpResponse<StoreFranchiseeInfoResponse> response = httpClient.action().result(new TypeReference<HttpResponse<StoreFranchiseeInfoResponse>>() {
            });
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("获取门店信息失败");
            }
            StoreFranchiseeInfoResponse data = response.getData();
            if (data == null) {
                throw new BusinessException("无效的门店");
            }
            storeInfo = data.getStoreInfo();
        } catch (BusinessException e) {
            logger.info("获取门店信息失败：{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            logger.info("获取门店信息失败：{}", e);
            throw new BusinessException("获取门店信息失败");
        }

        return storeInfo;
    }

    @Override
    public ProductInfo getSkuDetail(String companyCode, String skuCode) {
        companyCode = "01";
        skuCode = "102423";

        String url = urlProperties.getProductApi() + "/search/spu/sku/detail";
        url += "?company_code="+companyCode;
        url += "&sku_code=" + skuCode;
        ProductInfo product = new ProductInfo();
        try {
            HttpClient httpClient = HttpClient.get(url);
            HttpResponse<ProductSkuDetailResponse> response = httpClient.action().result(new TypeReference<HttpResponse<ProductSkuDetailResponse>>() {
            });
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("获取商品信息失败");
            }
            ProductSkuDetailResponse data = response.getData();
            if (data == null) {
                throw new BusinessException("无效的商品");
            }

            //商品编码
            product.setSpuCode(data.getProductCode());
            product.setSpuName(data.getProductName());
            product.setSkuCode(data.getSkuCode());
            product.setSkuName(data.getSkuName());
            product.setSupplierCode(data.getSupplyUnitCode());
            product.setSupplierName(data.getSupplyUnitName());
            product.setPictureUrl(data.getProductPicturePath());
            product.setProductSpec(data.getSpec());
            product.setColorCode(data.getColorCode());
            product.setColorName(data.getColorName());
            product.setModelCode(data.getModelNumber());
            product.setUnitCode(data.getUnitCode());
            product.setUnitName(data.getUnitName());
            product.setPrice(data.getPriceTax());
            product.setBarCode(data.getBarCode());
            product.setTaxRate(data.getOutputTaxRate());
            product.setProductPropertyCode(data.getProductPropertyCode());
            product.setProductPropertyName(data.getProductPropertyName());

        } catch (BusinessException e) {
            logger.info("获取商品信息失败：{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            logger.info("获取商品信息失败：{}", e);
            throw new BusinessException("获取商品信息失败");
        }

        return product;
    }

    @Override
    public void lockStockInSupplyChain(ErpOrderInfo order) {

    }

    @Override
    public void unlockStockInSupplyChain(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum) {

        String personId = "";
        String personName = "";

        String url = urlProperties.getProductApi() + "/stock/change";

        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("operation_person_id", personId);
        paramMap.put("operation_person_name", personName);
        paramMap.put("order_code", order.getOrderStoreCode());
//        paramMap.put("order_type", null);
        paramMap.put("order_vo", order.getOrderStoreCode());

        List<Map<String, Object>> list = new ArrayList<>();
        for (ErpOrderItem item :
                order.getItemList()) {

            Map<String, Object> map = new HashMap<>();
            map.put("company_code", item.getCompanyCode());
            map.put("company_name", item.getCompanyName());
//            map.put("new_delivery_code", null);
//            map.put("new_delivery_name", null);
//            map.put("new_purchase_amount", null);
//            map.put("purchase_group_code", null);
//            map.put("purchase_group_name", null);
            map.put("sku_code", item.getSkuCode());
            map.put("sku_name", item.getSkuName());
            map.put("tax_rate", item.getTaxRate());
            map.put("transport_center_code", null);
            map.put("transport_center_name", null);
            map.put("warehouse_code", null);
            map.put("warehouse_name", null);
            map.put("warehouse_type", null);


        }

//        {
//            "operation_person_id": "string",
//                "operation_person_name": "string",
//                "operation_type": 0,
//                "order_code": "string",
//                "order_type": 0,
//                "stock_vo": [
//            {
//                "change_count": 0,
//                    "company_code": "string",
//                    "company_name": "string",
//                    "new_delivery_code": "string",
//                    "new_delivery_name": "string",
//                    "new_purchase_amount": 0,
//                    "purchase_group_code": "string",
//                    "purchase_group_name": "string",
//                    "sku_code": "string",
//                    "sku_name": "string",
//                    "tax_rate": 0,
//                    "transport_center_code": "string",
//                    "transport_center_name": "string",
//                    "warehouse_code": "string",
//                    "warehouse_name": "string",
//                    "warehouse_type": "string"
//            }
//  ]
//        }


    }

    @Override
    public ErpOrderPayStatusResponse getOrderPayStatus(String payId) {
        ErpOrderPayStatusResponse response = new ErpOrderPayStatusResponse();
        response.setOrderCode(payId);
        try {
            //TODO CT 请求支付中心接口查询订单支付状态
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            response.setPayCode(OrderPublic.getUUID());
            response.setPayStatusEnum(ErpPayStatusEnum.SUCCESS);
        } catch (Exception e) {
            response.setRequestSuccess(false);
            logger.error("获取订单支付状态失败：{}", e);
        }
        return response;
    }

    @Override
    public boolean sendOrderPayRequest(ErpOrderInfo order, ErpOrderPay orderPay) {
        boolean flag = false;
        try {

            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderStoreId());
            PayRequest payRequest = new PayRequest();
            payRequest.setOrderNo(order.getOrderStoreCode());
            payRequest.setOrderAmount(2000L);
            payRequest.setFee(0L);
            payRequest.setOrderTime(order.getCreateTime());
            payRequest.setPayType(ErpRequestPayTypeEnum.PAY_10.getCode());
            payRequest.setOrderSource(ErpRequestPayOrderSourceEnum.WEB.getCode());
            payRequest.setCreateBy(order.getCreateById());
            payRequest.setCreateName(order.getCreateByName());

            payRequest.setPayOriginType(1);
            payRequest.setOrderType(2);
            payRequest.setFranchiseeId("BG895ED81C04D445EE9CB554945098922B");
            payRequest.setStoreName("门店1");
            payRequest.setStoreId("AB988458F192C747478210CC01D4D4135C");
            payRequest.setTransactionType("STORE_ORDER");
            payRequest.setPayOrderType(14);
            payRequest.setBackUrl("http://order.api.aiqin.com/erpOrderPayController/orderPayCallback");


            //TODO CT 请求支付中心接口查询订单支付状态
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("发起支付失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean sendLogisticsPayRequest(ErpOrderLogistics orderLogistics, ErpOrderPay orderPay) {
        return false;
    }

    @Override
    public BigDecimal getGoodsCoupon(ErpOrderInfo order) {
        BigDecimal goodsCoupon = BigDecimal.ZERO;
        try {

            Map<String, Object> paramMap = new HashMap<>(16);
            List<Map<String, Object>> list = new ArrayList<>();
            for (ErpOrderItem item :
                    order.getItemList()) {
                Map<String, Object> paramItemMap = new HashMap<>(4);
                paramItemMap.put("skuCode", item.getSkuCode());
                paramItemMap.put("price", item.getTotalPreferentialAmount());
                list.add(paramItemMap);
            }
            paramMap.put("franchiseeId", order.getFranchiseeId());
            paramMap.put("logisticsVoucherModel", list);
            paramMap.put("orderId", order.getOrderStoreId());

//            Map<String, Object> paramMap2 = new HashMap<>(16);
//            List<Map<String, Object>> list2 = new ArrayList<>();
//            Map<String, Object> paramItemMap2 = new HashMap<>(4);
//            paramItemMap2.put("skuCode", "1003");
//            paramItemMap2.put("price", "2634");
//            list2.add(paramItemMap2);
//
//            Map<String, Object> paramItemMap3 = new HashMap<>(4);
//            paramItemMap3.put("skuCode", "1004");
//            paramItemMap3.put("price", "1234");
//            list2.add(paramItemMap3);
//
//            paramMap2.put("franchiseeId", "1001");
//            paramMap2.put("logisticsVoucherModel", list2);
//            paramMap2.put("orderId", "1002");


            HttpClient httpClient = HttpClient.post(urlProperties.getMarketApi() + "//logisticsVoucher/getBySkuCodes").json(paramMap);
            HttpResponse<ErpOrderGoodsCouponResponse> response = httpClient.action().result(new TypeReference<HttpResponse<ErpOrderGoodsCouponResponse>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("获取返还物流券失败");
            }
            goodsCoupon = response.getData().getMoney();
        } catch (Exception e) {
            logger.error("获取返回物流券失败：{}", e);
        }
        return goodsCoupon;
    }

    @Override
    public ErpOrderInfo sendOrderToSupplyChainAndGetSplitGroup(ErpOrderInfo order) {
        ErpOrderInfo resultInfo = new ErpOrderInfo();
        try {
            //TODO CT 订单支付成功通知供应链,返回商品库房分组
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
        } catch (Exception e) {
            logger.error("获取商品库存仓库分组失败：{}", e);
        }
        return resultInfo;
    }

    @Override
    public boolean sendSplitOrderToSupplyChain(ErpOrderInfo order, List<ErpOrderInfo> splitOrderList) {
        boolean flag = false;
        try {
            //TODO CT 推送拆分后的订单到供应链
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            flag = true;
        } catch (Exception e) {
            logger.error("推送供应链失败：{}", e);
        }
        return flag;
    }

    @Override
    public void applyToCancelOrderRequest(ErpOrderInfo order) {

    }

    @Override
    public boolean areaCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO CT 商品销售区域配置校验
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
    public boolean repertoryCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO CT 商品库存校验
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
    public boolean priceCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO CT 商品销售价格校验
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
    public boolean activityCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = false;
        try {
            //TODO CT 促销活动校验
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
