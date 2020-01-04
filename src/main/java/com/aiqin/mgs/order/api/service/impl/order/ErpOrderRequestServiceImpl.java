package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.pay.*;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.PayRequest;
import com.aiqin.mgs.order.api.domain.response.ProductSkuDetailResponse;
import com.aiqin.mgs.order.api.domain.response.order.*;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ErpOrderRequestServiceImpl implements ErpOrderRequestService {

    @Resource
    private UrlProperties urlProperties;

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderRequestServiceImpl.class);

    @Override
    public StoreInfo getStoreInfoByStoreId(String storeId) {
        //TODO CT 临时测试使用门店
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

        //TODO CT 临时测试使用
        companyCode = "01";
        skuCode = "102423";

        String url = urlProperties.getProductApi() + "/search/spu/sku/detail";
        url += "?company_code=" + companyCode;
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
    public boolean lockStockInSupplyChain(ErpOrderInfo order,List<ErpOrderItem> itemList, AuthToken auth) {

        boolean flag = true;
        try {

            List<Map<String, Object>> list = new ArrayList<>();
            for (ErpOrderItem item :
                    itemList) {
                Map<String, Object> paramItemMap = new HashMap<>(16);
                paramItemMap.put("change_count", item.getProductCount());
                paramItemMap.put("city_code", order.getCityId());
                paramItemMap.put("province_code", order.getProvinceId());
                paramItemMap.put("sku_code", item.getSkuCode());
                list.add(paramItemMap);
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("company_code", order.getCompanyCode());
            paramMap.put("company_name", order.getCompanyName());
            paramMap.put("operation_person_id", auth.getPersonId());
            paramMap.put("operation_person_name", auth.getPersonName());
            paramMap.put("operation_type", ErpOrderLockStockTypeEnum.LOCK.getCode());
            paramMap.put("order_code", order.getOrderStoreCode());
            paramMap.put("order_type", order.getOrderTypeCode());
            paramMap.put("detail_list", list);

            //TODO CT 已经调通，暂时跳过锁库操作
//            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/stock/lock/info").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
//
//            if (!RequestReturnUtil.validateHttpResponse(response)) {
//                throw new BusinessException(response.getMessage());
//            }
        } catch (BusinessException e) {
            flag = false;
            logger.error("锁定库存失败：{}", e.getMessage());
        } catch (Exception e) {
            flag = false;
            logger.error("锁定库存失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean unlockStockInSupplyChain(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum, AuthToken auth) {

        boolean flag = true;
        try {

//            List<Map<String, Object>> list = new ArrayList<>();
//            for (ErpOrderItem item :
//                    order.getItemList()) {
//                Map<String, Object> paramItemMap = new HashMap<>(16);
//                paramItemMap.put("change_count", item.getProductCount());
//                paramItemMap.put("city_code", order.getCityId());
//                paramItemMap.put("province_code", order.getProvinceId());
//                paramItemMap.put("sku_code", item.getSkuCode());
//                list.add(paramItemMap);
//            }
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("company_code", order.getCompanyCode());
//            paramMap.put("company_name", order.getCompanyName());
//            paramMap.put("operation_person_id", auth.getPersonId());
//            paramMap.put("operation_person_name", auth.getPersonName());
//            paramMap.put("operation_type", orderLockStockTypeEnum.getCode());
//            paramMap.put("order_code", order.getOrderStoreCode());
//            paramMap.put("order_type", order.getOrderTypeCode());
//            paramMap.put("detail_list", list);
//
            Map<String, Object> paramItemMap1 = new HashMap<>(16);
            paramItemMap1.put("change_count", 10);
            paramItemMap1.put("city_code", "1001");
            paramItemMap1.put("province_code", "1001");
            paramItemMap1.put("sku_code", "102423");
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(paramItemMap1);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("company_code", "01");
            paramMap.put("company_name", "北京爱亲科技股份有限公司");
            paramMap.put("operation_person_id", "12345");
            paramMap.put("operation_person_name", "张三");
            paramMap.put("operation_type", 1);
            paramMap.put("order_code", "20191228000001");
            paramMap.put("order_type", 3);
            paramMap.put("detail_list", list);

            System.out.println(JSON.toJSON(paramMap));

            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/stock/unlock/info").json(paramMap);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException(response.getMessage());
            }
        } catch (BusinessException e) {
            flag = false;
            logger.error("解锁库存失败：{}", e.getMessage());
        } catch (Exception e) {
            flag = false;
            logger.error("解锁库存失败：{}", e);
        }

        return flag;
    }

    @Override
    public ErpOrderPayStatusResponse getOrderPayStatus(String orderCode) {
        ErpOrderPayStatusResponse payStatusResponse = new ErpOrderPayStatusResponse();
        payStatusResponse.setOrderCode(orderCode);
        payStatusResponse.setRequestSuccess(false);
        try {

            //TODO CT 临时测试
            payStatusResponse.setRequestSuccess(true);
            payStatusResponse.setPayCode(System.currentTimeMillis() + "");
            payStatusResponse.setOrderCode(orderCode);
            payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.SUCCESS);

            //请求支付中心接口查询订单支付状态
//            HttpClient httpClient = HttpClient.get(urlProperties.getPaymentApi() + "/payment/pay/searchPayOrder");
//            httpClient.addParameter("orderNo", orderCode);
//            HttpResponse<ErpPayPollingResponse> httpResponse = httpClient.action().result(new TypeReference<HttpResponse<ErpPayPollingResponse>>() {
//            });
//
//            if (RequestReturnUtil.validateHttpResponse(httpResponse)) {
//                ErpPayPollingResponse data = httpResponse.getData();
//                payStatusResponse.setRequestSuccess(true);
//                ErpPayPollingBackStatusEnum payPollingBackStatusEnum = ErpPayPollingBackStatusEnum.getEnum(data.getOrderStatus());
//                if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_0) {
//                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.PAYING);
//                } else if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_1) {
//                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.SUCCESS);
//                    payStatusResponse.setPayCode(data.getPayNum());
//                } else {
//                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.FAIL);
//                }
//            }

        } catch (Exception e) {
            logger.error("获取订单支付状态失败：{}", e);
        }
        return payStatusResponse;
    }

    @Override
    public ErpOrderPayStatusResponse getOrderLogisticsPayStatus(String logisticsCode) {
        ErpOrderPayStatusResponse payStatusResponse = new ErpOrderPayStatusResponse();
        payStatusResponse.setOrderCode(logisticsCode);
        payStatusResponse.setRequestSuccess(false);
        try {
            //请求支付中心接口查询订单支付状态
            HttpClient httpClient = HttpClient.get(urlProperties.getPaymentApi() + "/payment/pay/searchPayOrder");
//            httpClient.addParameter("orderNo", orderCode);
            httpClient.addParameter("orderNo", "20191226191116542103");
            HttpResponse<ErpPayPollingResponse> httpResponse = httpClient.action().result(new TypeReference<HttpResponse<ErpPayPollingResponse>>() {
            });

            if (RequestReturnUtil.validateHttpResponse(httpResponse)) {
                ErpPayPollingResponse data = httpResponse.getData();
                payStatusResponse.setRequestSuccess(true);
                ErpPayPollingBackStatusEnum payPollingBackStatusEnum = ErpPayPollingBackStatusEnum.getEnum(data.getOrderStatus());
                if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_1) {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.SUCCESS);
                    payStatusResponse.setPayCode(data.getPayNum());
                } else if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_0) {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.PAYING);
                } else {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.FAIL);
                }
            }

        } catch (Exception e) {
            logger.error("获取订单支付状态失败：{}", e);
        }
        return payStatusResponse;
    }

    @Override
    public boolean sendOrderPayRequest(ErpOrderInfo order, ErpOrderFee orderFee) {
        boolean flag = true;
        try {

            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            ErpOrderCategoryEnum orderCategoryEnum = ErpOrderCategoryEnum.getEnum(order.getOrderCategoryCode());
//            PayRequest payRequest = new PayRequest();
//            payRequest.setOrderNo(order.getOrderStoreCode());
//            payRequest.setOrderAmount(Long.valueOf(orderFee.getPayMoney().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).toString()));
//            payRequest.setFee(0L);
//            payRequest.setOrderTime(order.getCreateTime());
//            payRequest.setPayType(ErpRequestPayTypeEnum.PAY_10.getCode());
//            payRequest.setOrderSource(ErpRequestPayOrderSourceEnum.WEB.getCode());
//            payRequest.setCreateBy(order.getCreateById());
//            payRequest.setCreateName(order.getCreateByName());
//
//            payRequest.setPayOriginType(orderTypeEnum.getPayOriginType());
//            payRequest.setOrderType(ErpRequestPayOperationTypeEnum.TYPE_2.getCode());
//            payRequest.setFranchiseeId(order.getFranchiseeId());
//            payRequest.setStoreName(order.getStoreName());
//            payRequest.setStoreId(order.getStoreId());
//            payRequest.setTransactionType(orderCategoryEnum.getPayTransactionTypeEnum().getValue());
//            payRequest.setPayOrderType(orderTypeEnum.getPayOrderType());
//            payRequest.setBackUrl("/erpOrderPayController/orderPayCallback");

            PayRequest payRequest = new PayRequest();
            payRequest.setOrderNo(order.getOrderStoreCode());
            payRequest.setOrderAmount(1L);
            payRequest.setFee(0L);
            payRequest.setOrderTime(new Date());
            payRequest.setPayType(10);
            payRequest.setOrderSource(0);
            payRequest.setCreateBy("13140");
            payRequest.setCreateName("张富月");

            payRequest.setPayOriginType(orderTypeEnum.getPayOriginType());
            payRequest.setOrderType(2);
            payRequest.setFranchiseeId("BGF0A27812BD8E450CB51D1AC81F3FE5F5");
            payRequest.setStoreName("tob门店配送账户充值测试");
            payRequest.setStoreId("tobtranspaytest");
            payRequest.setTransactionType("STORE_RECHARGE");
            payRequest.setPayOrderType(2);
            payRequest.setBackUrl("http://order.api.aiqin.com/erpOrderPayController/orderPayCallback");

            System.out.println(JSON.toJSON(payRequest));

            //请求支付中心接口查询订单支付状态
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/payment/pay/payTobAll").json(payRequest);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });

//            System.out.println(JSON.toJSON(response));
//            if (!RequestReturnUtil.validateHttpResponse(response)) {
//                throw new BusinessException("发起支付失败：" + response.getMessage());
//            }
        } catch (BusinessException e) {
            flag = false;
            logger.error("发起支付失败：{}", e.getMessage());
//            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            flag = false;
            logger.error("发起支付失败：{}", e);
//            throw new BusinessException("发起支付失败");
        }
        return flag;
    }

    @Override
    public boolean sendLogisticsPayRequest(ErpOrderInfo order, ErpOrderLogistics orderLogistics) {
        boolean flag = true;
        try {

            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            ErpOrderCategoryEnum orderCategoryEnum = ErpOrderCategoryEnum.getEnum(order.getOrderCategoryCode());
            PayRequest payRequest = new PayRequest();
            payRequest.setOrderNo(orderLogistics.getLogisticsCode());
            payRequest.setOrderAmount(Long.valueOf(orderLogistics.getLogisticsFee().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).toString()));
            payRequest.setFee(0L);
            payRequest.setOrderTime(orderLogistics.getCreateTime());
            payRequest.setPayType(ErpRequestPayTypeEnum.PAY_10.getCode());
            payRequest.setOrderSource(ErpRequestPayOrderSourceEnum.WEB.getCode());
            payRequest.setCreateBy(order.getCreateById());
            payRequest.setCreateName(order.getCreateByName());

            payRequest.setPayOriginType(ErpRequestPayOriginTypeEnum.TYPE_24.getCode());
            payRequest.setOrderType(ErpRequestPayOperationTypeEnum.TYPE_2.getCode());
//            payRequest.setFranchiseeId("BG895ED81C04D445EE9CB554945098922B");
            payRequest.setFranchiseeId(order.getFranchiseeId());
            payRequest.setStoreName(order.getStoreName());
//            payRequest.setStoreId("AB988458F192C747478210CC01D4D4135C");
            payRequest.setStoreId(order.getStoreId());
            payRequest.setTransactionType("LOGISTICS_PAYMENT");
            payRequest.setPayOrderType(orderTypeEnum.getPayOrderType());
            payRequest.setBackUrl("/erpOrderPayController/orderLogisticsPayCallback");


            //请求支付中心接口查询订单支付状态
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/payment/pay/payTobAll").json(payRequest);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
//            if (!RequestReturnUtil.validateHttpResponse(response)) {
//                throw new BusinessException("发起支付失败：" + response.getMessage());
//            }
        } catch (BusinessException e) {
            flag = false;
            logger.error("发起支付失败：{}", e.getMessage());
            throw new BusinessException("发起支付失败：" + e.getMessage());
        } catch (Exception e) {
            flag = false;
            logger.error("发起支付失败：{}", e);
            throw new BusinessException("发起支付失败");
        }
        return flag;
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
    public List<ErpOrderItemSplitGroupResponse> getRepositorySplitGroup(ErpOrderInfo order) {
        List<ErpOrderItemSplitGroupResponse> list = new ArrayList<>();
        try {

//            List<Map<String, Object>> paramList = new ArrayList<>();
//            for (ErpOrderItem item :
//                    order.getItemList()) {
//                Map<String, Object> paramItemMap = new HashMap<>(16);
//                paramItemMap.put("line_code", item.getLineCode());
//                paramItemMap.put("sku_code", item.getSkuCode());
//                paramList.add(paramItemMap);
//            }
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("order_code", order.getOrderStoreCode());
//            paramMap.put("detail_list", paramList);

            List<Map<String, Object>> paramList = new ArrayList<>();
            Map<String, Object> paramItemMap1 = new HashMap<>(16);
            paramItemMap1.put("line_code", 1L);
            paramItemMap1.put("sku_code", "0000109");
            paramList.add(paramItemMap1);
            Map<String, Object> paramItemMap2 = new HashMap<>(16);
            paramItemMap2.put("line_code", 2L);
            paramItemMap2.put("sku_code", "295110");
            paramList.add(paramItemMap2);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("order_code", "1234567");
            paramMap.put("detail_list", paramList);

            System.out.println(JSON.toJSON(paramMap));


            //获取商品库房分组
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/stock/product/warehouse/info").json(paramMap);
            HttpResponse<List<ErpOrderItemSplitGroupResponse>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ErpOrderItemSplitGroupResponse>>>() {
            });
            if (RequestReturnUtil.validateHttpResponse(response)) {
                list = response.getData();
            }
        } catch (Exception e) {
            logger.error("获取商品库存仓库分组失败：{}", e);
        }
        return list;
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
        boolean flag = true;
        try {
            for (ErpOrderItem item :
                    orderProductItemList) {
                HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + "/stock/area/sale")
                    .addParameter("province_code",storeInfo.getProvinceId())
                    .addParameter("store_code",storeInfo.getStoreCode())
                        .addParameter("sku_code", item.getSkuCode());
                HttpResponse<Boolean> response = httpClient.action().result(new TypeReference<HttpResponse<Boolean>>() {
                });
                if (!RequestReturnUtil.validateHttpResponse(response)) {
                    throw new BusinessException(item.getSkuName() + "商品销售区域校验失败");
                }
                Boolean data = response.getData();
                if (!data) {
                    throw new BusinessException(item.getSkuName() + "不在当前区域销售");
                }
            }
        } catch (BusinessException e) {
            logger.error("商品销售区域配置校验失败：{}", e);
            flag = false;
            throw new BusinessException("商品销售区域配置校验失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error("商品销售区域配置校验失败：{}", e);
            flag = false;
            throw new BusinessException("商品销售区域配置校验失败：");
        }
        return flag;
    }

    @Override
    public boolean repertoryCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = true;
        try {
            //TODO 商品可用库存校验

        } catch (Exception e) {
            flag = false;
            logger.error("商品库存校验失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean priceCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = true;
        try {
            //TODO 商品销售价格校验
        } catch (Exception e) {
            flag = false;
            logger.error("商品销售价格校验失败：{}", e);
        }
        return flag;
    }

    @Override
    public boolean activityCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = true;
        try {
            //TODO 促销活动校验
        } catch (Exception e) {
            flag = false;
            logger.error("促销活动校验失败：{}", e);
        }
        return flag;
    }

    @Override
    public void updateStoreStatus(String storeId, String s) {

        try {
            HttpClient httpPost = HttpClient.get(urlProperties.getSlcsApi() + "/store/open/Status" );
            httpPost.addParameter("store_id", storeId);
            httpPost.addParameter("business_stage_code ", s);
            httpPost.action().status();
           httpPost.action().result(new TypeReference<HttpResponse>() {  });
        } catch (BusinessException e) {
            logger.info("首单，修改门店状态：{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            logger.info("首单，修改门店状态：{}", e);
            throw new BusinessException("首单，修改门店状态");
        }
    }
}
