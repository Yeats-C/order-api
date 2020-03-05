package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderLockStockTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.*;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.PayRequest;
import com.aiqin.mgs.order.api.domain.response.ProductSkuDetailResponse;
import com.aiqin.mgs.order.api.domain.response.order.*;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ErpOrderRequestServiceImpl implements ErpOrderRequestService {

    @Resource
    private UrlProperties urlProperties;

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderRequestServiceImpl.class);

    @Override
    public StoreInfo getStoreInfoByStoreId(String storeId) {
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
            FranchiseeInfo franchiseeInfo = data.getFranchiseeInfo();
            if (storeInfo != null && franchiseeInfo != null) {
                storeInfo.setFranchiseeName(franchiseeInfo.getFranchiseeName());
            }
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
            product.setProductBrandCode(data.getProductBrandCode());
            product.setProductBrandName(data.getProductBrandName());
            product.setProductCategoryCode(data.getProductCategoryCode());
            product.setProductCategoryName(data.getProductCategoryName());
            if (data.getProductSkuBoxPackings() != null && data.getProductSkuBoxPackings().size() > 0) {
                product.setBoxGrossWeight(data.getProductSkuBoxPackings().get(0).getBoxGrossWeight());
                product.setBoxVolume(data.getProductSkuBoxPackings().get(0).getBoxVolume());
            }

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
    public CouponDetail getCouponDetailByCode(String couponCode) {
        String url = urlProperties.getSlcsApi() + "/franchiseeVirtual/getLogistics";
        url += "?couponCode=" + couponCode;
        CouponDetail logisticsDetail = null;
        try {
            HttpClient httpClient = HttpClient.get(url);
            HttpResponse<CouponDetail> response = httpClient.action().result(new TypeReference<HttpResponse<CouponDetail>>() {
            });
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("获取物流券信息失败");
            }
            CouponDetail data = response.getData();
            if (data == null) {
                throw new BusinessException("无效的物流券编码");
            }
            logisticsDetail = data;
        } catch (BusinessException e) {
            logger.info("获取物流券信息失败：{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            logger.info("获取物流券信息失败：{}", e);
            throw new BusinessException("获取物流券信息失败");
        }
        return logisticsDetail;
    }

    @Override
    public boolean applyToCancelOrderRequest(String orderCode, AuthToken auth) {
        boolean flag = false;
        try {
            HttpClient httpClient = HttpClient.get(urlProperties.getScmpApi() + "/order/cancel")
                    .addParameter("operator_id", auth.getPersonId())
                    .addParameter("operator_name", auth.getPersonName())
                    .addParameter("order_code", orderCode);
            AuthUtil.addParameter(httpClient, auth);
            HttpResponse<Boolean> response = httpClient.action().result(new TypeReference<HttpResponse<Boolean>>() {
            });
            if (RequestReturnUtil.validateHttpResponse(response)) {
                if (response.getData()) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("取消订单请求失败：{}", e);
            throw new BusinessException("取消订单请求失败");
        }
        return flag;
    }

    @Override
    public void turnOffCouponsByOrderId(String orderCode) {
        try {
            HttpClient httpClient = HttpClient.put(urlProperties.getSlcsApi() + "/franchiseeVirtual/updateStatus")
                    .addParameter("orderId", orderCode);
            HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse>() {
            });
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException(response.getMessage());
            }
        } catch (BusinessException e) {
            logger.error("注销订单关联优惠券失败：{}", e);
            throw new BusinessException("注销订单关联优惠券失败" + e.getMessage());
        }catch (Exception e) {
            logger.error("注销订单关联优惠券失败：{}", e);
            throw new BusinessException("注销订单关联优惠券失败");
        }
    }

    @Override
    public void updateCouponStatus(String franchiseeId, String couponCode, String businessCode, String storeName) {
        try {
            Map<String, Object> paramMap = new HashMap<>(16);
            paramMap.put("franchisee_Id", franchiseeId);
            paramMap.put("coupon_code", couponCode);
            paramMap.put("Logistics_number", businessCode);
            paramMap.put("store_name", storeName);

            HttpClient httpClient = HttpClient.put(urlProperties.getSlcsApi() + "/franchiseeVirtual/update").json(paramMap);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException(response.getMessage());
            }
        } catch (Exception e) {
            logger.error("修改优惠券状态失败：{}", e);
        }
    }

    @Override
    public boolean lockStockInSupplyChain(ErpOrderInfo order, List<ErpOrderItem> itemList, AuthToken auth) {

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

            logger.info("锁库请求参数：", paramMap);
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/stock/lock/info").json(paramMap);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException(response.getMessage());
            }
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
    public boolean unlockStockInSupplyChainByDetail(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum, AuthToken auth) {
        boolean flag = true;
        try {

            List<Map<String, Object>> list = new ArrayList<>();
            for (ErpOrderItem item :
                    order.getItemList()) {
                Map<String, Object> paramItemMap = new HashMap<>(16);
                paramItemMap.put("change_count", item.getProductCount());
                paramItemMap.put("sku_code", item.getSkuCode());
                paramItemMap.put("transport_center_code", order.getTransportCenterCode());
                paramItemMap.put("warehouse_code", order.getWarehouseCode());
                paramItemMap.put("company_code", order.getCompanyCode());
                paramItemMap.put("company_name", order.getCompanyName());
                list.add(paramItemMap);
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("company_code", order.getCompanyCode());
            paramMap.put("company_name", order.getCompanyName());
            paramMap.put("operation_person_id", auth.getPersonId());
            paramMap.put("operation_person_name", auth.getPersonName());
            paramMap.put("operation_type", orderLockStockTypeEnum.getCode());
            paramMap.put("order_code", order.getOrderStoreCode());
            paramMap.put("order_type", order.getOrderTypeCode());
            paramMap.put("stock_vo", list);

            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/stock/change/stock").json(paramMap);
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
    public boolean unlockStockInSupplyChainByOrderCode(ErpOrderInfo order, AuthToken auth) {
        boolean flag = true;
        try {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("company_code", order.getCompanyCode());
            paramMap.put("company_name", order.getCompanyName());
            paramMap.put("operation_person_id", auth.getPersonId());
            paramMap.put("operation_person_name", auth.getPersonName());
            paramMap.put("operation_type", ErpOrderLockStockTypeEnum.UNLOCK.getCode());
            paramMap.put("order_code", order.getOrderStoreCode());
            paramMap.put("order_type", order.getOrderTypeCode());

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

            //请求支付中心接口查询订单支付状态
            HttpClient httpClient = HttpClient.get(urlProperties.getPaymentApi() + "/payment/pay/searchPayOrder");
            httpClient.addParameter("orderNo", orderCode);
            HttpResponse<ErpPayPollingResponse> httpResponse = httpClient.action().result(new TypeReference<HttpResponse<ErpPayPollingResponse>>() {
            });
            if (RequestReturnUtil.validateHttpResponse(httpResponse)) {
                ErpPayPollingResponse data = httpResponse.getData();
                payStatusResponse.setRequestSuccess(true);
                ErpPayPollingBackStatusEnum payPollingBackStatusEnum = ErpPayPollingBackStatusEnum.getEnum(data.getOrderStatus());
                if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_0) {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.PAYING);
                } else if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_1) {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.SUCCESS);
                    payStatusResponse.setPayCode(data.getPayNum());
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
    public ErpOrderPayStatusResponse getOrderLogisticsPayStatus(String logisticsCode) {
        ErpOrderPayStatusResponse payStatusResponse = new ErpOrderPayStatusResponse();
        payStatusResponse.setOrderCode(logisticsCode);
        payStatusResponse.setRequestSuccess(false);
        try {

            //请求支付中心接口查询订单支付状态
            HttpClient httpClient = HttpClient.get(urlProperties.getPaymentApi() + "/payment/pay/searchPayOrder");
            httpClient.addParameter("orderNo", logisticsCode);
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
            logger.error("获取订单物流费支付状态失败：{}", e);
        }
        return payStatusResponse;
    }

    @Override
    public ErpOrderPayStatusResponse getOrderRefundStatus(String refundCode) {
        ErpOrderPayStatusResponse payStatusResponse = new ErpOrderPayStatusResponse();
        payStatusResponse.setOrderCode(refundCode);
        payStatusResponse.setRequestSuccess(false);
        try {

            //请求支付中心接口查询订单支付状态
            HttpClient httpClient = HttpClient.get(urlProperties.getPaymentApi() + "/payment/pay/searchPayOrder");
            httpClient.addParameter("orderNo", refundCode);
            HttpResponse<ErpPayPollingResponse> httpResponse = httpClient.action().result(new TypeReference<HttpResponse<ErpPayPollingResponse>>() {
            });

            if (RequestReturnUtil.validateHttpResponse(httpResponse)) {
                ErpPayPollingResponse data = httpResponse.getData();
                payStatusResponse.setRequestSuccess(true);
                ErpPayPollingBackStatusEnum payPollingBackStatusEnum = ErpPayPollingBackStatusEnum.getEnum(data.getOrderStatus());
                if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_4) {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.SUCCESS);
                    payStatusResponse.setPayCode(data.getPayNum());
                } else if (payPollingBackStatusEnum == ErpPayPollingBackStatusEnum.STATUS_3) {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.PAYING);
                } else {
                    payStatusResponse.setPayStatusEnum(ErpPayStatusEnum.FAIL);
                }
            }

        } catch (Exception e) {
            logger.error("获取订单退款状态失败：{}", e);
        }
        return payStatusResponse;
    }

    @Override
    public boolean sendOrderPayRequest(ErpOrderInfo order, ErpOrderFee orderFee) {
        boolean flag = true;
        try {

            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
            PayRequest payRequest = new PayRequest();
            payRequest.setOrderNo(order.getOrderStoreCode());
            payRequest.setOrderAmount(Long.valueOf(orderFee.getPayMoney().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).toString()));
            payRequest.setFee(0L);
            payRequest.setOrderTime(order.getCreateTime());
            payRequest.setPayType(ErpRequestPayTypeEnum.PAY_10.getCode());
            payRequest.setOrderSource(ErpRequestPayOrderSourceEnum.WEB.getCode());
            payRequest.setCreateBy(order.getCreateById());
            payRequest.setCreateName(order.getCreateByName());

            payRequest.setPayOriginType(orderTypeEnum.getPayOriginType().getCode());
            payRequest.setOrderType(ErpRequestPayOperationTypeEnum.TYPE_2.getCode());
            payRequest.setFranchiseeId(order.getFranchiseeId());
            payRequest.setStoreName(order.getStoreName());
            payRequest.setStoreId(order.getStoreId());
            payRequest.setTransactionType(processTypeEnum.getPayTransactionTypeEnum().getValue());
            payRequest.setPayOrderType(orderTypeEnum.getPayOrderType().getCode());
            payRequest.setBackUrl(urlProperties.getOrderApi() + "/erpOrderPayController/orderPayCallback");

            //请求支付中心接口发起支付
            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/payment/pay/payTobAll").json(payRequest);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("发起支付失败：" + response.getMessage());
            }
        } catch (BusinessException e) {
            flag = false;
            logger.error("发起支付失败：{}", e.getMessage());
            throw new BusinessException("发起支付失败:" + e.getMessage());
        } catch (Exception e) {
            flag = false;
            logger.error("发起支付失败：{}", e);
            throw new BusinessException("发起支付失败");
        }
        return flag;
    }

    @Override
    public boolean sendLogisticsPayRequest(ErpOrderInfo order, List<ErpOrderInfo> orderList, ErpOrderLogistics orderLogistics, AuthToken auth) {
        boolean flag = true;
        try {

            //订单号
            String collect = orderList.stream().map(ErpOrderInfo::getOrderStoreCode).collect(Collectors.joining(","));

            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            PayRequest payRequest = new PayRequest();
            payRequest.setOrderNo(orderLogistics.getLogisticsCode());
            payRequest.setOrderAmount(Long.valueOf(orderLogistics.getBalancePayFee().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).toString()));
            payRequest.setFee(0L);
            payRequest.setOrderTime(orderLogistics.getCreateTime());
            payRequest.setPayType(ErpRequestPayTypeEnum.PAY_10.getCode());
            payRequest.setOrderSource(ErpRequestPayOrderSourceEnum.WEB.getCode());
            payRequest.setCreateBy(order.getCreateById());
            payRequest.setCreateName(order.getCreateByName());
            payRequest.setPayOriginType(ErpRequestPayOriginTypeEnum.TYPE_24.getCode());
            payRequest.setOrderType(ErpRequestPayOperationTypeEnum.TYPE_2.getCode());
            payRequest.setFranchiseeId(order.getFranchiseeId());
            payRequest.setStoreName(order.getStoreName());
            payRequest.setStoreId(order.getStoreId());
            payRequest.setTransactionType(ErpRequestPayTransactionTypeEnum.LOGISTICS_PAYMENT.getValue());
            payRequest.setPayOrderType(orderTypeEnum.getPayOrderType().getCode());
            payRequest.setBackUrl(urlProperties.getOrderApi() + "/erpOrderPayController/orderLogisticsPayCallback");

            //支付物流费用所需参数
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            payRequest.setCouponPaymentAmount(Long.valueOf(orderLogistics.getCouponPayFee().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).toString()));
            payRequest.setLogisticsCompany(orderLogistics.getLogisticsCentreName());
            payRequest.setPaymentFreightName(auth.getPersonName());
            payRequest.setRelationOrderCode(collect);
            payRequest.setPaymentFreightTime(sdf.format(new Date()));
            payRequest.setStoreCode(order.getStoreCode());
            payRequest.setPaymentFreightAmount(Long.valueOf(orderLogistics.getLogisticsFee().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).toString()));

            //请求支付中心接口发起支付
            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/payment/pay/payTobAll").json(payRequest);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("发起支付失败：" + response.getMessage());
            }

        } catch (BusinessException e) {
            flag = false;
            logger.error("发起支付物流费失败：{}", e.getMessage());
            throw new BusinessException("发起支付失败：" + e.getMessage());
        } catch (Exception e) {
            flag = false;
            logger.error("发起支付物流费失败：{}", e);
            throw new BusinessException("发起支付失败");
        }
        return flag;
    }

    @Override
    public boolean sendOrderRefundRequest(ErpOrderInfo order, ErpOrderRefund orderRefund, ErpRequestPayTransactionTypeEnum payTransactionTypeEnum, AuthToken auth) {
        boolean flag = true;
        try {

            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            PayRequest payRequest = new PayRequest();
            payRequest.setOrderNo(orderRefund.getRefundCode());
            payRequest.setOrderAmount(Long.valueOf(orderRefund.getRefundFee().multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).toString()));
            payRequest.setFee(0L);
            payRequest.setOrderTime(order.getCreateTime());
            payRequest.setPayType(ErpRequestPayTypeEnum.PAY_10.getCode());
            payRequest.setOrderSource(ErpRequestPayOrderSourceEnum.WEB.getCode());
            payRequest.setCreateBy(order.getCreateById());
            payRequest.setCreateName(order.getCreateByName());

            payRequest.setPayOriginType(orderTypeEnum.getPayOriginType().getCode());
            payRequest.setOrderType(ErpRequestPayOperationTypeEnum.TYPE_4.getCode());
            payRequest.setFranchiseeId(order.getFranchiseeId());
            payRequest.setStoreName(order.getStoreName());
            payRequest.setStoreId(order.getStoreId());
            payRequest.setTransactionType(payTransactionTypeEnum.getValue());
            payRequest.setPayOrderType(orderTypeEnum.getPayOrderType().getCode());
            payRequest.setBackUrl(urlProperties.getOrderApi() + "/erpOrderPayController/orderRefundCallback");

            //请求支付中心接口退款
            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/payment/pay/payTobAll").json(payRequest);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException("发起退款失败：" + response.getMessage());
            }
        } catch (BusinessException e) {
            flag = false;
            logger.error("发起退款失败：{}", e.getMessage());
            throw new BusinessException("发起退款失败:" + e.getMessage());
        } catch (Exception e) {
            flag = false;
            logger.error("发起退款失败：{}", e);
            throw new BusinessException("发起退款失败");
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
            paramMap.put("orderId", order.getOrderStoreCode());

            HttpClient httpClient = HttpClient.post(urlProperties.getMarketApi() + "/logisticsVoucher/getBySkuCodes").json(paramMap);
            HttpResponse<ErpOrderGoodsCouponResponse> response = httpClient.action().result(new TypeReference<HttpResponse<ErpOrderGoodsCouponResponse>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException(response.getMessage());
            }
            goodsCoupon = response.getData().getMoney();
        } catch (BusinessException e) {
            logger.error("获取返回物流券失败：{}", e);
            throw new BusinessException("获取返还物流券失败" + e.getMessage());
        } catch (Exception e) {
            logger.error("获取返回物流券失败：{}", e);
            throw new BusinessException("获取返还物流券失败");
        }
        return goodsCoupon;
    }

    @Override
    public List<ErpOrderItemSplitGroupResponse> getRepositorySplitGroup(ErpOrderInfo order) {
        List<ErpOrderItemSplitGroupResponse> list = new ArrayList<>();
        try {

            List<Map<String, Object>> paramList = new ArrayList<>();
            for (ErpOrderItem item :
                    order.getItemList()) {
                Map<String, Object> paramItemMap = new HashMap<>(16);
                paramItemMap.put("line_code", item.getLineCode());
                paramItemMap.put("sku_code", item.getSkuCode());
                paramItemMap.put("lock_count", item.getProductCount());
                paramList.add(paramItemMap);
            }
            Map<String, Object> paramMap = new HashMap<>(16);
            paramMap.put("order_code", order.getOrderStoreCode());
            paramMap.put("detail_list", paramList);

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
    public boolean areaCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = true;
        try {
            for (ErpOrderItem item :
                    orderProductItemList) {
                HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + "/stock/area/sale")
                        .addParameter("province_code", storeInfo.getProvinceId())
                        .addParameter("store_code", storeInfo.getStoreCode())
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
            throw new BusinessException("商品销售区域校验失败：");
        }
        return flag;
    }

    @Override
    public boolean repertoryCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        boolean flag = true;
        try {
            for (ErpOrderItem item :
                    orderProductItemList) {
                HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + "/stock/available/search")
                        .addParameter("province_code", storeInfo.getProvinceId())
                        .addParameter("city_code", storeInfo.getCityId())
                        .addParameter("sku_code", item.getSkuCode());
                HttpResponse<Integer> response = httpClient.action().result(new TypeReference<HttpResponse<Integer>>() {
                });
                if (!RequestReturnUtil.validateHttpResponse(response)) {
                    throw new BusinessException(item.getSkuName() + "商品库存数量查询失败");
                }
                Integer data = response.getData();
                if (item.getProductCount() > data) {
                    throw new BusinessException(item.getSkuName() + "库存数量不足");
                }
            }
        } catch (BusinessException e) {
            logger.error("商品库存校验失败：{}", e);
            flag = false;
            throw new BusinessException("商品库存校验失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error("商品库存校验失败：{}", e);
            flag = false;
            throw new BusinessException("商品库存校验失败：");
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
    public void updateStoreBusinessStage(String storeId, String origCode, String destCode) {
        try {
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/store/updateStoreBusinessStage")
                    .addParameter("store_id", storeId)
                    .addParameter("orig_code", origCode)
                    .addParameter("dest_code", destCode);
            HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse>() {
            });
        } catch (Exception e) {
            logger.error("修改门店营业状态失败：{}", e);
        }
    }

    @Override
    public void updateStoreStatus(String storeId, String s) {

        try {
            HttpClient httpPost = HttpClient.get(urlProperties.getSlcsApi() + "/store/open/Status");
            httpPost.addParameter("store_id", storeId);
            httpPost.addParameter("business_stage_code", s);
            httpPost.action().status();
            httpPost.action().result(new TypeReference<HttpResponse>() {
            });
        } catch (BusinessException e) {
            logger.info("首单，修改门店状态：{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            logger.info("首单，修改门店状态：{}", e);
            throw new BusinessException("首单，修改门店状态");
        }
    }

    @Override
    public void accountRole(String franchiseeCode) {
        try {
            HttpClient httpClient = HttpClient.post(urlProperties.getSlcsApi() + "/conten/updateFranchiseeRoleByCode")
                    .addParameter("franchisee_code", franchiseeCode);
            HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse>() {
            });
        } catch (Exception e) {
            logger.error("首单修改加盟商角色-权限失败：{}", e);
        }
    }
}
