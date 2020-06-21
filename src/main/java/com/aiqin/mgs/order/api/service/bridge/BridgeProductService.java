package com.aiqin.mgs.order.api.service.bridge;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.FranchiseeInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.dto.ProductDistributorOrderDTO;
import com.aiqin.mgs.order.api.domain.po.gift.NewStoreGradient;
import com.aiqin.mgs.order.api.domain.request.InventoryDetailRequest;
import com.aiqin.mgs.order.api.domain.request.activity.*;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartProductRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.product.NewStoreCategory;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRespVo6;
import com.aiqin.mgs.order.api.domain.request.product.SkuProductReqVO;
import com.aiqin.mgs.order.api.domain.request.statistical.ProductDistributorOrderRequest;
import com.aiqin.mgs.order.api.domain.request.stock.ProductSkuStockRespVo;
import com.aiqin.mgs.order.api.domain.response.NewFranchiseeResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetail;
import com.aiqin.mgs.order.api.domain.response.gift.StoreAvailableGiftQuotaResponse;
import com.aiqin.mgs.order.api.domain.response.order.StoreFranchiseeInfoResponse;
import com.aiqin.mgs.order.api.domain.wholesale.JoinMerchant;
import com.aiqin.mgs.order.api.domain.wholesale.MerchantAccount;
import com.aiqin.mgs.order.api.domain.wholesale.MerchantPaBalanceRespVO;
import com.aiqin.mgs.order.api.util.MathUtil;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Createed by sunx on 2019/4/8.<br/>
 */
@Service
@Slf4j
public class BridgeProductService<main> {

    @Resource
    private UrlProperties urlProperties;

    @Value("${center.main.url}")
    private String centerMainUrl;

    @Value("${center.main.settlement}")
    private String settlement;

    /**
     * 获取低库存畅缺商品明细信息
     *
     * @param request
     * @return
     */
    public List<ProductDistributorOrderDTO> getProductDistributorOrderDTO(ProductDistributorOrderRequest request) {
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/inventory/sold/out/stock").json(request);
        HttpResponse<List<ProductDistributorOrderDTO>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductDistributorOrderDTO>>>() {
        });

        if (Objects.nonNull(response) && Objects.equals("0", response.getCode())) {
            return response.getData();
        }
        return Lists.newArrayList();
    }

    public HttpResponse changeStock(List<InventoryDetailRequest> stockReqVos) {
        log.info("修改库存{}", JsonUtil.toJson(stockReqVos));
        StringBuilder sb = new StringBuilder();
        sb.append(urlProperties.getProductApi()).append("/inventory/update/detail");
        HttpClient orderClient =HttpClient.post(sb.toString()).json(stockReqVos);
        return orderClient.action().result(HttpResponse.class);
    }

    /**
     * 获取商品信息
     * @param shoppingCartProductRequest
     * @return
     */
    public HttpResponse<List<CartOrderInfo>> getProduct(ShoppingCartProductRequest shoppingCartProductRequest){
        shoppingCartProductRequest.setCompanyCode("14");
        String path = "/search/spu/sku/detail2";
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartProductRequest);
        HttpResponse<List<CartOrderInfo>> response = httpClient.action().result(new TypeReference<HttpResponse<List<CartOrderInfo>>>() {
        });
//        //测试接口
//        HttpResponse<List<CartOrderInfo>> cartOrderInfoHttpResponse = new HttpResponse<>();
//        CartOrderInfo data = new CartOrderInfo();
//        data.setProductId("12345");
//        data.setStoreId("12345");
//        data.setSkuCode("12345");
//        data.setAmount(20);
//        data.setPrice(BigDecimal.valueOf(29.10));
//        data.setProductType(1);
//        ArrayList<CartOrderInfo> list = new ArrayList<>();
//        list.add(data);
//        cartOrderInfoHttpResponse.setData(list);
        return response;
    }

    /**
     * 获取sku详情列表
     *
     * @param provinceCode 省编码
     * @param cityCode     市编码
     * @param companyCode  公司编码
     * @param productSkuRequest2List  sku信息集合
     * @return java.util.List<com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetailResponse>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/3/10 10:54
     */
    public List<ErpSkuDetail> getProductSkuDetailList(String provinceCode, String cityCode, String companyCode, List<ProductSkuRequest2> productSkuRequest2List) {

        List<ErpSkuDetail> list = new ArrayList<>();
        try {
            ShoppingCartProductRequest shoppingCartProductRequest = new ShoppingCartProductRequest();
            shoppingCartProductRequest.setCityCode(cityCode);
            shoppingCartProductRequest.setProvinceCode(provinceCode);
            shoppingCartProductRequest.setCompanyCode(companyCode);
            shoppingCartProductRequest.setProductSkuRequest2List(productSkuRequest2List);
            String path = "/search/spu/sku/detail2";
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartProductRequest);
            HttpResponse<List<ErpSkuDetail>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ErpSkuDetail>>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException(response.getMessage());
            }
            list = response.getData();

        } catch (BusinessException e) {
            log.info("获取商品信息失败：{}", e.getMessage());
            throw new BusinessException("获取商品信息失败：" + e.getMessage());
        } catch (Exception e) {
            log.info("获取商品信息失败：{}", e);
            throw new BusinessException("获取商品信息失败");
        }
        return list;
    }

    /**
     * 获取单个sku详情
     *
     * @param provinceCode 省编码
     * @param cityCode     市编码
     * @param companyCode  公司编码
     * @param skuCode      sku编码
     * @return
     */
    public ErpSkuDetail getProductSkuDetail(String provinceCode, String cityCode, String companyCode, String skuCode) {

        ErpSkuDetail skuDetail = null;
        try {
            ShoppingCartProductRequest shoppingCartProductRequest = new ShoppingCartProductRequest();
            shoppingCartProductRequest.setCityCode(cityCode);
            shoppingCartProductRequest.setProvinceCode(provinceCode);
            shoppingCartProductRequest.setCompanyCode(companyCode);
            List<ProductSkuRequest2> productSkuRequest2List=new ArrayList<>();
            ProductSkuRequest2 productSkuRequest2=new ProductSkuRequest2();
            productSkuRequest2.setSkuCode(skuCode);
            productSkuRequest2.setWarehouseTypeCode("1");
            productSkuRequest2List.add(productSkuRequest2);
            shoppingCartProductRequest.setProductSkuRequest2List(productSkuRequest2List);
            String path = "/search/spu/sku/detail2";
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartProductRequest);
            HttpResponse<List<ErpSkuDetail>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ErpSkuDetail>>>() {
            });

            if (!RequestReturnUtil.validateHttpResponse(response)) {
                throw new BusinessException(response.getMessage());
            }
            List<ErpSkuDetail> list = response.getData();
            if (list != null && list.size() > 0) {
                skuDetail = list.get(0);
            }

        } catch (BusinessException e) {
            log.info("获取商品信息失败：{}", e.getMessage());
//            throw new BusinessException("获取商品信息失败：" + e.getMessage());
        } catch (Exception e) {
            log.info("获取商品信息失败：{}", e);
//            throw new BusinessException("获取商品信息失败");
        }
        return skuDetail;
    }

    public CartOrderInfo getCartProductDetail(String provinceId, String cityId, String skuCode) {
        ShoppingCartProductRequest shoppingCartProductRequest = new ShoppingCartProductRequest();
        shoppingCartProductRequest.setCityCode(cityId);
        shoppingCartProductRequest.setProvinceCode(provinceId);
        shoppingCartProductRequest.setCompanyCode(OrderConstant.SELECT_PRODUCT_COMPANY_CODE);
        List<String> skuCodeList = new ArrayList<>();
        skuCodeList.add(skuCode);
        shoppingCartProductRequest.setSkuCodes(skuCodeList);
        String path = "/search/spu/sku/detail2";
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartProductRequest);
        HttpResponse<List<CartOrderInfo>> response = httpClient.action().result(new TypeReference<HttpResponse<List<CartOrderInfo>>>() {
        });

        CartOrderInfo cartOrderInfo = new CartOrderInfo();
        if (!RequestReturnUtil.validateHttpResponse(response)) {

        } else {
            List<CartOrderInfo> data = response.getData();
            if (data != null && data.size() > 0) {
                cartOrderInfo = data.get(0);
            }
        }
        return cartOrderInfo;
    }


    /**
     * 获取门店的信息
     * @param shoppingCartRequest
     * @return
     */
    public HttpResponse<StoreInfo> getStoreInfo(ShoppingCartRequest shoppingCartRequest){

        String path = "/store/getStoreInfo";
        StringBuilder codeUrl = new StringBuilder();
        codeUrl.append(urlProperties.getSlcsApi()).append(path);
        HttpClient httpGet = HttpClient.get(codeUrl.toString() + "?store_id=" + shoppingCartRequest.getStoreId());
        httpGet.action().status();
        HttpResponse result = httpGet.action().result(new TypeReference<HttpResponse<StoreInfo>>() {
        });

//        HttpResponse<CartOrderInfo> cartOrderInfoHttpResponse = new HttpResponse<>();
//        CartOrderInfo data = new CartOrderInfo();
//        data.setAddress("北京市海淀区海淀南路35号");
//        data.setContacts("胡金英");
//        data.setContactsPhone("18513854421");
//        cartOrderInfoHttpResponse.setData(data);
        return result;
    }

    public NewFranchiseeResponse getStoreFranchiseeData(String distributorId) {
        HttpClient httpClient = HttpClient.get(urlProperties.getSlcsApi() + "/franchisee/getData?store_id=" + distributorId);//http://slcs.api.aiqin.com
        HttpResponse httpResponse = httpClient.action().result(new TypeReference<HttpResponse>() {
        });
        if(httpResponse.getCode().equals(MessageId.SUCCESS_CODE)&& Objects.nonNull(httpResponse.getData())){
            Map<String,Objects> result = JsonUtil.fromJson(JsonUtil.toJson(httpResponse.getData()), HashMap.class);
            log.info("通过门店查询加盟商信息:{}",JsonUtil.toJson(result));
            NewFranchiseeResponse franchiseeResponse = JsonUtil.fromJson(JsonUtil.toJson(result),NewFranchiseeResponse.class);
            return franchiseeResponse;
        }
        throw new GroundRuntimeException("查询门店对应的加盟商信息异常,无法下单");
    }

    /**
     * 活动商品信息分页
     * @param spuProductReqVO
     * @return
     */
    public HttpResponse<PageResData<ProductSkuRespVo5>> getSkuPage(SpuProductReqVO spuProductReqVO){
        String path = "/search/spu/skuPage";
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(spuProductReqVO);
        HttpResponse<PageResData<ProductSkuRespVo5>> response = httpClient.action().result(new TypeReference<HttpResponse<PageResData<ProductSkuRespVo5>>>() {
        });

        return response;
    }

    /**
     * 商品门店可用库存
     * @param req
     * @return
     */
    public Integer getStoreStockSkuNum(ShoppingCartRequest req){
        Integer num=0;
        String path = "/store/stock/sku/info";
        HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + path+"?store_id=" + req.getStoreId()+"&sku_code="+req.getProductId());
        Map<String, Object> result = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
        if (StringUtils.isNotBlank(String.valueOf(result.get("code"))) && "0".equals(String.valueOf(result.get("code")))) {
            Map data = (Map)result.get("data");
            if(data!=null&&data.get("data")!=null){
                num= Integer.valueOf(data.get("data").toString());
            }
        }

        return num;
    }

    /**
     * 供应链提供查询品类树接口
     * @param req
     * @return
     */
    public HttpResponse productCategoryList(ActivityBrandCategoryRequest req){
        String path = urlProperties.getProductApi() +"/product/category/list/categoryCodes";
        StringBuilder sb=new StringBuilder();
        String a="";
        if(req.getCategoryCodes()!=null&&0!=req.getCategoryCodes().size()){
            a=req.getCategoryCodes().get(0);
            sb.append("?category_codes="+a);
        }else{
            sb.append("?category_codes=");
        }

        if(req.getCategoryCodes()!=null&&req.getCategoryCodes().size()>=1){
            for(int i=1;i<req.getCategoryCodes().size();i++){
                sb.append("&category_codes="+req.getCategoryCodes().get(i));
            }
        }
        HttpClient httpClient = HttpClient.get( path+sb.toString());
        HttpResponse<List<ProductCategoryRespVO>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductCategoryRespVO>>>() {
        });
        if (Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")) {
            List<ProductCategoryRespVO> lists=response.getData();
            //门店端不展示物料，德明居，其他，只展示1到12到品类
            Iterator<ProductCategoryRespVO> it = lists.iterator();
            while(it.hasNext()){
                ProductCategoryRespVO str = it.next();
                if(str.getCategoryId().compareTo("12")>0){
                    it.remove();
                }
            }
        }
        return response;
    }

    /**
     * 供应链提供查询品类树接口
     * @param req
     * @return
     */
    public HttpResponse excludeCategoryCodes(ActivityBrandCategoryRequest req){
        String path = urlProperties.getProductApi() +"/product/category/list/excludeCategoryCodes";
        StringBuilder sb=new StringBuilder();
        String a="";
        if(req.getExcludeCategoryCodes()!=null&&0!=req.getExcludeCategoryCodes().size()){
            a=req.getExcludeCategoryCodes().get(0);
            sb.append("?exclude_category_codes="+a);
        }else{
            sb.append("?exclude_category_codes=");
        }

        if(req.getExcludeCategoryCodes()!=null&&req.getExcludeCategoryCodes().size()>=1){
            for(int i=1;i<req.getExcludeCategoryCodes().size();i++){
                sb.append("&exclude_category_codes="+req.getExcludeCategoryCodes().get(i));
            }
        }
        HttpClient httpClient = HttpClient.get( path+sb.toString());
        HttpResponse<List<ProductCategoryRespVO>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductCategoryRespVO>>>() {
        });
        if (Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")) {
            List<ProductCategoryRespVO> lists=response.getData();
            //门店端不展示物料，德明居，其他，只展示1到12到品类
            Iterator<ProductCategoryRespVO> it = lists.iterator();
            while(it.hasNext()){
                ProductCategoryRespVO str = it.next();
                if(str.getCategoryId().compareTo("12")>0){
                    it.remove();
                }
            }
        }

        return response;
    }

    /**
     * 供应链提供查询品牌接口
     * @param req
     * @return
     */
    public HttpResponse productBrandList(ActivityBrandCategoryRequest req){
        String path = urlProperties.getProductApi()+"/product/brand/list";
        if(StringUtils.isNotEmpty(req.getName())){
            path=path+"?name="+req.getName();
        }else{
            path=path+"?name=";
        }

        StringBuilder sb=new StringBuilder();
        if(req.getBrandIds()!=null&&req.getBrandIds().size()>0){
            for(int i=0;i<req.getBrandIds().size();i++){
                sb.append("&brand_ids="+req.getBrandIds().get(i));
            }
        }

        StringBuilder exsb=new StringBuilder();
        if(req.getExcludeBrandIds()!=null&&req.getExcludeBrandIds().size()>0){
            for(int i=0;i<req.getExcludeBrandIds().size();i++){
                exsb.append("&no_brand_ids="+req.getExcludeBrandIds().get(i));
            }
        }
        HttpClient httpClient = HttpClient.post(path+sb);
        HttpResponse<List<QueryProductBrandRespVO>>  response = httpClient.action().result(new TypeReference<HttpResponse<List<QueryProductBrandRespVO>>>() {
        });
        return response;
    }


    /**
     * 供应链提供查询品类树接口
     * @param req
     * @return
     */
    public HttpResponse categoryCodes(ActivityBrandCategoryRequest req){
        String str=req.getCategoryCodes().get(0);
        String path = "/product/category/list/categoryCodes?category_codes="+str;
        HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + path);
        HttpResponse<List<ProductCategoryRespVO>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductCategoryRespVO>>>() {
        });
        if (Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")) {
            List<ProductCategoryRespVO> lists=response.getData();
            //门店端不展示物料，德明居，其他，只展示1到12到品类
            Iterator<ProductCategoryRespVO> it = lists.iterator();
            while(it.hasNext()){
                ProductCategoryRespVO pro = it.next();
                if(pro.getCategoryId().compareTo("12")>0){
                    it.remove();
                }
            }
        }

        return response;
    }

    /**
     * 品牌和品类关系,condition_code为查询条件，type=2 通过品牌查品类,type=1 通过品类查品牌
     * @param condition_code
     * @param type
     * @return
     */
    public HttpResponse selectCategoryByBrandCode(String condition_code,String type){
        String path = "/product/brand/selectCategoryByBrandCode?condition_code="+condition_code+"&type="+type;
        HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + path);
        HttpResponse<ProductCategoryAndBrandResponse2> response = httpClient.action().result(new TypeReference<HttpResponse<ProductCategoryAndBrandResponse2>>() {
        });
        if (Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0") && type=="2") {
            List<ProductCategoryRespVO> lists=response.getData().getProductCategoryRespVOList();
            //门店端不展示物料，德明居，其他，只展示1到12到品类
            Iterator<ProductCategoryRespVO> it = lists.iterator();
            while(it.hasNext()){
                ProductCategoryRespVO str = it.next();
                if(str.getCategoryId().compareTo("12")>0){
                    it.remove();
                }
            }
        }

        return response;
    }


    /**
     * erp商品信息分页
     * @param skuProductReqVO
     * @return
     */
    public HttpResponse<PageResData<ProductSkuRespVo6>> getSkuPage2(SkuProductReqVO skuProductReqVO){
        String path = "/search/spu/skuPage2";
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(skuProductReqVO);
        HttpResponse<PageResData<ProductSkuRespVo6>> response = httpClient.action().result(new TypeReference<HttpResponse<PageResData<ProductSkuRespVo6>>>() {
        });

        return response;
    }


    /**
     * 通过门店id集合获取门店的省市集合--slcs
     * @param storeIdList
     * @return
     */
    public List<NewStoreCategory> selectProvincesByStoreList(List<String> storeIdList){
        String path = "/store/selectProvincesByStoreList";
        HttpClient httpClient = HttpClient.post(urlProperties.getSlcsApi() + path).json(storeIdList);
        HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse<List<NewStoreCategory>>>() {
        });
        List<NewStoreCategory> provinceList=new ArrayList<>();
        if(Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")){
            provinceList=(List<NewStoreCategory>)response.getData();
        }

        return provinceList;
    }

    /**
     * 通过门店id返回可用赠品额度--slcs
     * @param storeId
     * @return
     */
    public BigDecimal getStoreAvailableGiftGuota(String storeId){
        String path = "/store/getStoreAvailableGiftGuota?store_id="+storeId;
        HttpClient httpClient = HttpClient.get(urlProperties.getSlcsApi() + path);
        HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse>() {
        });
        BigDecimal availableGiftQuota=BigDecimal.ZERO;
        if(Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")){
            availableGiftQuota= MathUtil.getBigDecimal(response.getData());
        }

        return availableGiftQuota;
    }
    /**
     * 修改门店可用赠品额度--slcs
     * @param storeId
     * @param availableGiftQuota
     * @return
     */
    public HttpResponse updateAvailableGiftQuota(String storeId,BigDecimal availableGiftQuota){
        String path = "/store/updateAvailableGiftQuota";
        StoreAvailableGiftQuotaResponse storeAvailableGiftQuotaResponse=new StoreAvailableGiftQuotaResponse();
        storeAvailableGiftQuotaResponse.setStoreId(storeId);
        storeAvailableGiftQuotaResponse.setAvailableGiftQuota(availableGiftQuota);
        HttpClient httpClient = HttpClient.post(urlProperties.getSlcsApi() + path).json(storeAvailableGiftQuotaResponse);
        HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse>() {
        });

        return response;
    }

    /**
     * 根据skucode获取库存详情--product
     * @param skuCodes
     * @return
     */
    public List<ProductSkuStockRespVo> findStockDetail(List<String> skuCodes){
        String path = "/search/spu/findStockDetail";
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(skuCodes);
        HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductSkuStockRespVo>>>() {
        });
        List<ProductSkuStockRespVo> stockRespVoList=new ArrayList<>();
        if(Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")){
            stockRespVoList= (List<ProductSkuStockRespVo>)response.getData();
        }

        return stockRespVoList;
    }

    /**
     * 通过省市code查出对应的运输中心--product
     * @param provinceCode
     * @param cityCode
     * @return
     */
    public List<String> findTransportCenter(String provinceCode,String cityCode){
        String path = "/search/spu/findTransportCenter?province_code="+provinceCode+"&city_code="+cityCode;
        HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + path);
        HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse<List<String>>>() {
        });
        List<String> transportCenterList=new ArrayList<>();
        if(Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")){
            transportCenterList= (List<String>)response.getData();
        }

        return transportCenterList;
    }

    /**
     * 通过门店code查询门店赠品比例信息--slcs
     * @param storeCode
     * @return
     */
    public NewStoreGradient selectStoreGiveawayByStoreCode(String storeCode){
        String path = "/storeGiveaway/selectStoreGiveawayByStoreCode?store_code="+storeCode;
        HttpClient httpClient = HttpClient.get(urlProperties.getSlcsApi() + path);
        HttpResponse response = httpClient.action().result(new TypeReference<HttpResponse<NewStoreGradient>>() {
        });
        NewStoreGradient gradient=null;
        if(Objects.nonNull(response) && Objects.nonNull(response.getData()) && Objects.equals(response.getCode(), "0")){
            gradient= (NewStoreGradient)response.getData();
        }

        return gradient;
    }

    /**
     * 获取sku详情，返回map
     *
     * @param provinceCode 省编码
     * @param cityCode     市编码
     * @param productSkuRequest2List  sku信息list
     * @return
     */
    public Map<String, ErpSkuDetail> getProductSkuDetailMap(String provinceCode, String cityCode, List<ProductSkuRequest2> productSkuRequest2List) {
        Map<String, ErpSkuDetail> skuDetailMap = new HashMap<>(16);
        List<ErpSkuDetail> productSkuDetailList = getProductSkuDetailList(provinceCode, cityCode, OrderConstant.SELECT_PRODUCT_COMPANY_CODE, productSkuRequest2List);
        for (ErpSkuDetail item : productSkuDetailList) {
            String batchInfoCode=null;
            if(null!= item.getBatchList()&&item.getBatchList().size()>0&&null!=item.getBatchList().get(0).getBatchInfoCode()){
                batchInfoCode=item.getBatchList().get(0).getBatchInfoCode();
            }
            skuDetailMap.put(item.getSkuCode()+"BATCH_INFO_CODE"+batchInfoCode, item);
        }
        return skuDetailMap;
    }
    /**
     * 获取sku详情，返回map
     *
     * @param provinceCode 省编码
     * @param cityCode     市编码
     * @param productSkuRequest2List  sku信息list
     * @return
     */
    public Map<String, ErpSkuDetail> getProductSkuDetailMap1(String provinceCode, String cityCode, List<ProductSkuRequest2> productSkuRequest2List) {
        Map<String, ErpSkuDetail> skuDetailMap = new HashMap<>(16);
        List<ErpSkuDetail> productSkuDetailList = getProductSkuDetailList(provinceCode, cityCode, OrderConstant.SELECT_PRODUCT_COMPANY_CODE, productSkuRequest2List);
        for (ErpSkuDetail item : productSkuDetailList) {

            skuDetailMap.put(item.getSkuCode(), item);
        }
        return skuDetailMap;
    }


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
            log.info("获取门店信息失败：{}", e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            log.info("获取门店信息失败：{}", e);
            throw new BusinessException("获取门店信息失败");
        }
        return storeInfo;
    }

    /**
     * 批发客户添加主控组织架构
     * @param joinMerchant
     * @return
     */
    public HttpResponse<JoinMerchant> addFranchisee(JoinMerchant joinMerchant){
        StringBuilder sb = new StringBuilder();
        sb.append(centerMainUrl).append("/franchisee/add/franchisee");
//        sb.append("http://control.api.aiqin.com").append("/franchisee/add/franchisee");
        log.info("添加批发客户组织架构[{}]", JsonUtil.toJson(joinMerchant));
        HttpClient httpClient = HttpClient.post(sb.toString()).json(joinMerchant);

        HttpResponse<JoinMerchant> httpResponse = httpClient.action().result(new TypeReference<HttpResponse<JoinMerchant>>() {
        });
        if (!httpResponse.getCode().equals(MessageId.SUCCESS_CODE)) {
            log.error("批发客户创建主控架构失败，返回信息 httpResponse ={}"+httpResponse);
            return HttpResponse.failure(ResultCode.FAILED_TO_CREATE_MASTER_ACCOUNT_FAILED);
        }

        return httpResponse;
    }

    /**
     * 批发客户添加主控账户
     * @param joinMerchant
     * @return
     */
    public HttpResponse<JoinMerchant> addFranchiseeAccount(JoinMerchant joinMerchant){
        StringBuilder sb = new StringBuilder();
//        sb.append(centerMainUrl).append("/franchisee/add/franchisee/account");
        sb.append("http://control.api.aiqin.com").append("/franchisee/add/franchisee/account");
        log.info("添加批发客户组织架构[{}]", JsonUtil.toJson(joinMerchant));
        HttpClient httpClient = HttpClient.post(sb.toString()).json(joinMerchant);

        HttpResponse<JoinMerchant> httpResponse = httpClient.action().result(new TypeReference<HttpResponse<JoinMerchant>>() {
        });
        if (!httpResponse.getCode().equals(MessageId.SUCCESS_CODE)) {
            log.error("批发客户创建主控账户失败，返回信息 httpResponse ={}"+httpResponse);
            return HttpResponse.failure(ResultCode.FAILED_TO_CREATE_MASTER_ACCOUNT_FAILED);
        }

        return httpResponse;
    }

    /**
     * 批发客户新建结算账户接口
     * @param merchantAccount
     * @return
     */
    public HttpResponse accountRegister(MerchantAccount merchantAccount) {
        log.info("批发客户新建结算账户接口  参数 merchantAccount=[{}]"+JsonUtil.toJson(merchantAccount));
        HttpResponse httpResponse = HttpResponse.success();

        StringBuilder sb = new StringBuilder();
        sb.append(settlement).append("/merchant/account/register");
        HttpClient httpClient = HttpClient.post(sb.toString()).json(merchantAccount);
        httpResponse = httpClient.action().result(new TypeReference<HttpResponse>() {
        });
        if (!httpResponse.getCode().equals(MessageId.SUCCESS_CODE)) {
            return HttpResponse.failure(ResultCode.INSERT_FRANCHISEE_ACCOUNT_FAILED);
        }
        return httpResponse;
    }


    /**
     * 批发客户结算账户查询账户余额接口
     * @param franchiseeId
     * @return
     */
    public HttpResponse<MerchantPaBalanceRespVO> accountBalance(String franchiseeId) {
        log.info("批发客户结算账户查询账户余额接口  参数 franchiseeId=[{}]"+franchiseeId);
        HttpResponse<MerchantPaBalanceRespVO> httpResponse = HttpResponse.success();

        HttpClient httpClient = HttpClient.get("http://settlement.api.aiqin.com" + "/cardinfolink/merchant/platform/account/balance?franchiseeId=" + franchiseeId);
//        HttpClient httpClient = HttpClient.get(settlement + "/cardinfolink/merchant/platform/account/balance?franchiseeId=" + franchiseeId);
        httpResponse = httpClient.action().result(new TypeReference<HttpResponse<MerchantPaBalanceRespVO>>() {
        });
        if (!httpResponse.getCode().equals(MessageId.SUCCESS_CODE)) {
            return HttpResponse.failure(ResultCode.SELECT_FRANCHISEE_ACCOUNT_FAILED);
        }
        return httpResponse;
    }



    public static void main(String[] args) {
        BridgeProductService bridgeProductService=new BridgeProductService();

        System.out.println(JsonUtil.toJson(bridgeProductService.accountBalance("EBCC55A75ED842CD9B83B7181F789EC4")));
    }
}
