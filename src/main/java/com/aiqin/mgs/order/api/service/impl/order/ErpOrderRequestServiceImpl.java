package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.ProductRepertoryQuantity;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.response.ProductSkuDetailResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.domain.response.order.StoreFranchiseeInfoResponse;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.alibaba.fastjson.JSON;
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
    public ProductInfo getProductDetail(String spuCode, String skuCode) {

        String url = urlProperties.getProductApi() + "/search/spu/sku/detail";
        url += "?companyCode=09";
        url += "&skuCode=1001";
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

        } catch (BusinessException e) {
            logger.info("获取商品信息失败：{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            logger.info("获取商品信息失败：{}", e);
            throw new BusinessException("获取商品信息失败");
        }
//        product.setSpuCode(spuCode);
//        product.setSpuName(spuCode + "名称");
//        product.setSkuCode(skuCode);
//        product.setSkuName(skuCode + "名称");
//        product.setSupplierCode("123456");
//        product.setSupplierName("供应商1");
//
//        product.setPictureUrl("https://www.baidu.com/img/bd_logo1.png");
//        product.setProductSpec("32K");
//        product.setColorCode("101");
//        product.setColorName("红色");
//        product.setModelCode("1234");
//        product.setUnitCode("1001");
//        product.setUnitName("盒");
//        product.setPrice(BigDecimal.TEN);
//        product.setBarCode("987456321156156");
//        product.setTaxRate(new BigDecimal(0.15));

        return product;
    }

    @Override
    public void lockStockInSupplyChain(ErpOrderInfo order) {

    }

    @Override
    public void unlockStockInSupplyChain(ErpOrderInfo order) {

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
    public boolean sendPayRequest(ErpOrderInfo order, ErpOrderPay orderPay) {
        boolean flag = false;
        try {
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
        BigDecimal goodsCoupon = null;
        try {
            //TODO CT 获取本次支付成功返回物流券
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            goodsCoupon = BigDecimal.TEN;
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
