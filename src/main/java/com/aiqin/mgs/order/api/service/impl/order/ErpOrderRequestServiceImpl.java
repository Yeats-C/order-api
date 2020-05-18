package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderLockStockTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.aiqin.mgs.order.api.component.enums.pay.*;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.StockVoRequest;
import com.aiqin.mgs.order.api.domain.request.order.PayRequest;
import com.aiqin.mgs.order.api.domain.request.product.StockBatchInfoRequest;
import com.aiqin.mgs.order.api.domain.request.product.StockLockDetailRequest;
import com.aiqin.mgs.order.api.domain.response.ProductSkuDetailResponse;
import com.aiqin.mgs.order.api.domain.response.order.*;
import com.aiqin.mgs.order.api.domain.response.stock.StockChangeRequest;
import com.aiqin.mgs.order.api.domain.response.stock.StockInfoRequest;
import com.aiqin.mgs.order.api.service.order.ErpOrderItemService;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.service.order.ErpStoreLockDetailsService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ErpOrderRequestServiceImpl implements ErpOrderRequestService {

    @Resource
    private UrlProperties urlProperties;
    @Autowired
    private ErpStoreLockDetailsService erpStoreLockDetailsService;
    @Autowired
    private ErpOrderItemService erpOrderItemService;

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
    public ProductInfo getSkuDetail(String companyCode, String skuCode, String warehouseTypeCode, String batchInfoCode) {
        logger.info("查询sku详情入参,companyCode={},skuCode={},warehouseTypeCode={},batchInfoCode={}",companyCode,skuCode,warehouseTypeCode,batchInfoCode);
        String url = urlProperties.getProductApi() + "/search/spu/sku/detail";
        url += "?company_code=" + companyCode;
        url += "&sku_code=" + skuCode;
        url += "&warehouse_type_code=" + warehouseTypeCode;
        url += "&batch_Info_Code=" + batchInfoCode;
        ProductInfo product = new ProductInfo();
        try {
            logger.info("查询sku详情url={}",url);
            HttpClient httpClient = HttpClient.get(url);
            HttpResponse<ProductSkuDetailResponse> response = httpClient.action().result(new TypeReference<HttpResponse<ProductSkuDetailResponse>>() {
            });
            logger.info("查询sku详情返回结果response={}",response.toString());
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
            //如果批次信息List不为空
            if(null!=data.getBatchList()&&data.getBatchList().size()>0){
                product.setPrice(data.getBatchList().get(0).getBatchPrice());
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
        log.info("创建订单,锁库存入参order={}",order);
        log.info("创建订单,锁库存入参itemList={}",itemList);
        log.info("创建订单,锁库存入参auth={}",auth);
        boolean flag = true;
        try {

            // 按照sku汇总数量  但是得增加批次信息
            //锁库sku信息集合
            List<StockLockDetailRequest> list = new ArrayList<>();
            //sku总数量集合
            Map<String,Long> countMap=new HashMap<>();
            //sku批次信息集合
            Map<String,List<StockBatchInfoRequest>> productMap=new HashMap<>();
            //记录skuCode和行号对应关系
            Map<String,Long> skuCodeLineMap=new HashMap<>();
            //赠品
            List<ErpOrderItem> giftList=new ArrayList<>();
            //本品
            List<ErpOrderItem> productList=new ArrayList<>();
            //积分兑换赠品
            List<ErpOrderItem> integralGiftList=new ArrayList<>();
            //汇总sku
            for(ErpOrderItem item:itemList){
                Long count=countMap.get(item.getSkuCode());
                List<StockBatchInfoRequest> batchList=productMap.get(item.getSkuCode());
                //TODO 商品批次相关信息还得增加销售库特卖库标识
                StockBatchInfoRequest stockBatchInfoRequest=new StockBatchInfoRequest();
                stockBatchInfoRequest.setBatchCode(item.getBatchCode());
                stockBatchInfoRequest.setBatchInfoCode(item.getBatchInfoCode());
                stockBatchInfoRequest.setChangeCount(item.getProductCount());
                stockBatchInfoRequest.setSkuCode(item.getSkuCode());
                stockBatchInfoRequest.setSkuName(item.getSkuName());
                if(null!=countMap.get(item.getSkuCode())){
                    count=count+item.getProductCount();
                    batchList.add(stockBatchInfoRequest);
                }else {
                    count=item.getProductCount();
                    batchList=new ArrayList<>();
                    batchList.add(stockBatchInfoRequest);
                }
                countMap.put(item.getSkuCode(),count);
                productMap.put(item.getSkuCode(),batchList);
                //筛选出商品数据
                if(ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductType())){
                    productList.add(item);
                }
                //筛选出赠品数据
                if(ErpProductGiftEnum.GIFT.getCode().equals(item.getProductType())){
                    giftList.add(item);
                }
                //筛选出积分兑换赠品数据
                if(ErpProductGiftEnum.JIFEN.getCode().equals(item.getProductType())){
                    integralGiftList.add(item);
                }
            }
            log.info("创建订单,锁库存,sku汇总countMap={}",countMap);
            log.info("创建订单,锁库存,筛选出商品数据productList={}",productList);
            log.info("创建订单,锁库存,筛选出赠品数据giftList={}",giftList);
            //过滤商品和赠品sku和行号的对应关系
            for(ErpOrderItem eoi:productList){
                if(null==skuCodeLineMap.get(eoi.getSkuCode())){
                    skuCodeLineMap.put(eoi.getSkuCode(),eoi.getLineCode());
                }
            }
            for(ErpOrderItem eoi:giftList){
                if(null==skuCodeLineMap.get(eoi.getSkuCode())){
                    skuCodeLineMap.put(eoi.getSkuCode(),eoi.getLineCode());
                }
            }
            for(ErpOrderItem eoi:integralGiftList){
                if(null==skuCodeLineMap.get(eoi.getSkuCode())){
                    skuCodeLineMap.put(eoi.getSkuCode(),eoi.getLineCode());
                }
            }
            for(Map.Entry<String,Long> data:countMap.entrySet()){
                String skuCode=data.getKey();
                StockLockDetailRequest stockLockDetailRequest=new StockLockDetailRequest();
                stockLockDetailRequest.setChangeCount(countMap.get(skuCode).intValue());
                stockLockDetailRequest.setCityCode(order.getCityId());
                stockLockDetailRequest.setProvinceCode(order.getProvinceId());
                stockLockDetailRequest.setSkuCode(skuCode);
                stockLockDetailRequest.setBatchList(productMap.get(skuCode));
                list.add(stockLockDetailRequest);
            }
//            for (ErpOrderItem item :
//                    itemList) {
//                Map<String, Object> paramItemMap = new HashMap<>(16);
//                paramItemMap.put("change_count", item.getProductCount());
//                paramItemMap.put("city_code", order.getCityId());
//                paramItemMap.put("province_code", order.getProvinceId());
//                paramItemMap.put("sku_code", item.getSkuCode());
//                list.add(paramItemMap);
//            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("company_code", order.getCompanyCode());
            paramMap.put("company_name", order.getCompanyName());
            paramMap.put("operation_person_id", auth.getPersonId());
            paramMap.put("operation_person_name", auth.getPersonName());
            paramMap.put("operation_type", ErpOrderLockStockTypeEnum.LOCK.getCode());
            paramMap.put("order_code", order.getOrderStoreCode());
            paramMap.put("order_type", order.getOrderTypeCode());
            paramMap.put("detail_list", list);

            logger.info("锁库请求参数paramMap={}", paramMap);
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/stock/lock/info").json(paramMap);

//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
//            logger.info("锁库请求返回结果response={}", response);
//            if (!RequestReturnUtil.validateHttpResponse(response)) {
//                throw new BusinessException(response.getMessage());
//            }
            Map<String,Object> result1=httpClient.action().result(new TypeReference<Map<String,Object>>() {});
            log.info("锁库请求返回结果，result1={}", JSON.toJSON(result1));
            if(result1==null){
                log.info("锁库请求返回失败");
//                return false;
                throw new BusinessException("锁库请求失败");
            }
            if (StringUtils.isNotBlank(result1.get("code").toString()) && "0".equals(String.valueOf(result1.get("code")))) {
                List<StockVoRequest> list2 = JSONArray.parseArray(JSON.toJSONString(result1.get("data")), StockVoRequest.class);
                log.info("锁库请求返回list结果为list1={}",list2);
                List<StoreLockDetails> records=new ArrayList<>();
                for(StockVoRequest stockVoRequest:list2){
                    StoreLockDetails storeLockDetail=new StoreLockDetails();
                    BeanUtils.copyProperties(stockVoRequest,storeLockDetail);
                    storeLockDetail.setOrderCode(order.getOrderStoreCode());
                    if(null!=stockVoRequest.getSkuCode()&&null!=skuCodeLineMap.get(stockVoRequest.getSkuCode())){
                        Long loneCode=Long.valueOf(skuCodeLineMap.get(stockVoRequest.getSkuCode()).toString());
                        storeLockDetail.setLineCode(loneCode);
                    }
                    records.add(storeLockDetail);
                }
                erpStoreLockDetailsService.insertStoreLockDetails(records);
            } else {
                log.info("锁库请求返回list结果异常");
//                return false;
                throw new BusinessException("锁库请求失败");
            }
            //本地缓存锁库存信息
//            if(null!=response.getData()){
//                Map listMap=(Map)response.getData();
//                List<StockVoRequest> list1 =(List<StockVoRequest>)response.getData();
//                logger.info("锁库请求返回结果list1={}", list1);
//                List<StoreLockDetails> records=new ArrayList<>();
//                for(StockVoRequest stockVoRequest:list1){
//                    StoreLockDetails storeLockDetail=new StoreLockDetails();
//                    BeanUtils.copyProperties(stockVoRequest,storeLockDetail);
//                    if(null!=stockVoRequest.getSkuCode()&&null!=skuCodeLineMap.get(stockVoRequest.getSkuCode())){
//                        Long loneCode=Long.valueOf(skuCodeLineMap.get(stockVoRequest.getSkuCode()).toString());
//                        storeLockDetail.setLineCode(loneCode);
//                    }
//                    records.add(storeLockDetail);
//                }
//                erpStoreLockDetailsService.insertStoreLockDetails(records);
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
    public boolean unlockStockInSupplyChainByDetail(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum, AuthToken auth) {
        log.info("解锁库存（根据明细解锁）--入参order={}",order);
        log.info("解锁库存（根据明细解锁）--入参 orderLockStockTypeEnum={}",orderLockStockTypeEnum);
        log.info("解锁库存（根据明细解锁）--入参 auth={}",auth);
        boolean flag = true;
        try {

            List<Map<String, Object>> list = new ArrayList<>();

            Map<String,Long> countMap=new HashMap<>();
            List<String> skuCodes=new ArrayList<>();
            //汇总sku
            for(ErpOrderItem item:order.getItemList()){
                Long count=countMap.get(item.getSkuCode());
                if(null!=countMap.get(item.getSkuCode())){
                    count=count+item.getProductCount();
                }else {
                    count=item.getProductCount();
                }
                countMap.put(item.getSkuCode(),count);
            }
            log.info("解锁库存（根据明细解锁）--sku数量汇总 countMap={}",countMap);
            for(Map.Entry<String,Long> data:countMap.entrySet()){
                String skuCode=data.getKey();
                Map<String, Object> paramItemMap = new HashMap<>(16);
                paramItemMap.put("change_count", countMap.get(skuCode));
                paramItemMap.put("sku_code", skuCode);
                paramItemMap.put("transport_center_code", order.getTransportCenterCode());
                paramItemMap.put("warehouse_code", order.getWarehouseCode());
                paramItemMap.put("company_code", order.getCompanyCode());
                paramItemMap.put("company_name", order.getCompanyName());
                list.add(paramItemMap);
                skuCodes.add(skuCode);
            }

//            for (ErpOrderItem item :
//                    order.getItemList()) {
//                Map<String, Object> paramItemMap = new HashMap<>(16);
//                paramItemMap.put("change_count", item.getProductCount());
//                paramItemMap.put("sku_code", item.getSkuCode());
//                paramItemMap.put("transport_center_code", order.getTransportCenterCode());
//                paramItemMap.put("warehouse_code", order.getWarehouseCode());
//                paramItemMap.put("company_code", order.getCompanyCode());
//                paramItemMap.put("company_name", order.getCompanyName());
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
//            paramMap.put("stock_vo", list);




            StockChangeRequest stockChangeRequest =new StockChangeRequest();
            //操作类型：3.解锁库存
            stockChangeRequest.setOperationType(3);
            //库存集合
            List<StockInfoRequest> stockList=new ArrayList<>();
            //批次库存集合
            List<StockBatchInfoRequest> stockBatchList=new ArrayList<>();

            //修改成从锁库的时候保存的数据获取仓库库房分组信息
            List<ErpOrderItemSplitGroupResponse> splitGroupResponses=erpStoreLockDetailsService.getNewRepositorySplitGroup(order);

            for(ErpOrderItemSplitGroupResponse item:splitGroupResponses){
                StockInfoRequest stockInfoRequest=new StockInfoRequest();
                stockInfoRequest.setCompanyCode(order.getCompanyCode());
                stockInfoRequest.setCompanyName(order.getCompanyName());
                stockInfoRequest.setTransportCenterCode(item.getTransportCenterCode());
                stockInfoRequest.setTransportCenterName(item.getTransportCenterName());
                stockInfoRequest.setWarehouseCode(item.getWarehouseCode());
                stockInfoRequest.setWarehouseName(item.getWarehouseName());
                stockInfoRequest.setWarehouseType(Integer.valueOf(item.getWarehouseType()));
                stockInfoRequest.setSkuCode(item.getSkuCode());
                stockInfoRequest.setSkuName(item.getSkuName());
                stockInfoRequest.setChangeCount(item.getLockCount());
                stockList.add(stockInfoRequest);
            }

            for(ErpOrderItem item:order.getItemList()){
                StockBatchInfoRequest stockBatchInfoRequest=new StockBatchInfoRequest();
                stockBatchInfoRequest.setBatchCode(item.getBatchCode());
                stockBatchInfoRequest.setBatchInfoCode(item.getBatchInfoCode());
                stockBatchInfoRequest.setChangeCount(item.getProductCount());
                stockBatchInfoRequest.setSkuCode(item.getSkuCode());
                stockBatchInfoRequest.setSkuName(item.getSkuName());
                stockBatchList.add(stockBatchInfoRequest);
            }
            stockChangeRequest.setStockList(stockList);
            stockChangeRequest.setStockBatchList(stockBatchList);

            log.info("解锁库存（根据明细解锁）--调用供应链解锁接口,入参 stockChangeRequest={}",stockChangeRequest);
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/stock/change/stock").json(stockChangeRequest);
            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
            });
            log.info("解锁库存（根据明细解锁）--调用供应链解锁接口,返回结果 response={}",JSON.toJSONString(response));
            if (!RequestReturnUtil.validateHttpResponse(response)) {
                log.info("解锁库存（根据明细解锁）--调用供应链解锁接口,返回结果--解锁异常");
                throw new BusinessException(response.getMessage());
            }
            log.info("清除本地锁库存记录,入参skuCodes={}",skuCodes);
            if(skuCodes!=null&&skuCodes.size()>0){
                for(String skuCode:skuCodes){
                    erpStoreLockDetailsService.deleteBySkuCode(order.getOrderStoreCode(),skuCode);
                }
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

            List<ErpOrderItem> erpOrderItems = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
            log.info("清除本地锁库存记录,入参erpOrderItems{}",erpOrderItems);
            if(erpOrderItems!=null&&erpOrderItems.size()>0){
                for(ErpOrderItem eoi:erpOrderItems){
                    erpStoreLockDetailsService.deleteBySkuCode(order.getOrderStoreCode(),eoi.getSkuCode());
                }
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
            //TODO 按照sku汇总数量
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
