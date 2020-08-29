package com.aiqin.mgs.order.api.service.bridge;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.returnenums.ReturnOrderTypeEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.BatchInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.dto.ProductDistributorOrderDTO;
import com.aiqin.mgs.order.api.domain.po.gift.NewStoreGradient;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.InventoryDetailRequest;
import com.aiqin.mgs.order.api.domain.request.activity.*;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartProductRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.product.*;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailVO;
import com.aiqin.mgs.order.api.domain.request.statistical.ProductDistributorOrderRequest;
import com.aiqin.mgs.order.api.domain.request.stock.ProductSkuStockRespVo;
import com.aiqin.mgs.order.api.domain.response.NewFranchiseeResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetail;
import com.aiqin.mgs.order.api.domain.response.gift.StoreAvailableGiftQuotaResponse;
import com.aiqin.mgs.order.api.domain.response.order.ProductSkuRespVo2;
import com.aiqin.mgs.order.api.domain.response.order.StoreFranchiseeInfoResponse;
import com.aiqin.mgs.order.api.domain.wholesale.JoinMerchant;
import com.aiqin.mgs.order.api.domain.wholesale.MerchantAccount;
import com.aiqin.mgs.order.api.domain.wholesale.MerchantPaBalanceRespVO;
import com.aiqin.mgs.order.api.service.order.ErpOrderLogisticsService;
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
import java.math.RoundingMode;
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

@Resource
private ErpOrderLogisticsService erpOrderLogisticsService;

@Resource
private BatchInfoDao batchInfoDao;

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
        log.info("detail2 接口参数为：{}"+JsonUtil.toJson(shoppingCartProductRequest));
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartProductRequest);
        HttpResponse<List<ErpSkuDetail>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ErpSkuDetail>>>() {
        });
        log.info("detail2 接口 请求结果为：{}"+JsonUtil.toJson(response));
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
    if (response.getCode().equals(MessageId.SUCCESS_CODE) && "2".equals(type)) {
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
        Integer warehouseType=null;
        if(null!= item.getBatchList()&&item.getBatchList().size()>0){
            for(BatchRespVo batchRespVo:item.getBatchList()){
                batchInfoCode=batchRespVo.getBatchInfoCode();
                warehouseType=batchRespVo.getWarehouseType();
                skuDetailMap.put(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+warehouseType+"BATCH_INFO_CODE"+batchInfoCode, item);
            }
        }else{
            warehouseType=item.getWarehouseType();
            skuDetailMap.put(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+warehouseType+"BATCH_INFO_CODE"+batchInfoCode, item);
        }

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

    MerchantPaBalanceRespVO merchantPaBalanceRespVO=(MerchantPaBalanceRespVO)httpResponse.getData();
    if(null!=merchantPaBalanceRespVO){
        if(null!=merchantPaBalanceRespVO.getAvailableBalance()&&merchantPaBalanceRespVO.getAvailableBalance().compareTo(BigDecimal.ZERO)>0){
            merchantPaBalanceRespVO.setAvailableBalance(merchantPaBalanceRespVO.getAvailableBalance().divide(new BigDecimal(100),2, RoundingMode.HALF_UP));
        }
        if(null!=merchantPaBalanceRespVO.getFrozenBalance()&&merchantPaBalanceRespVO.getFrozenBalance().compareTo(BigDecimal.ZERO)>0){
            merchantPaBalanceRespVO.setFrozenBalance(merchantPaBalanceRespVO.getFrozenBalance().divide(new BigDecimal(100),2, RoundingMode.HALF_UP));
        }
        if(null!=merchantPaBalanceRespVO.getCreditAmount()&&merchantPaBalanceRespVO.getCreditAmount().compareTo(BigDecimal.ZERO)>0){
            merchantPaBalanceRespVO.setCreditAmount(merchantPaBalanceRespVO.getCreditAmount().divide(new BigDecimal(100),2, RoundingMode.HALF_UP));
        }
    }
    return httpResponse;
}

    /**
     * 结算保存erp销售订单
     * @param list
     * @return
     */
    public void settlementSaveOrder(List<ErpOrderInfo> list,Integer orderStatus) {
        try {
            List<ErpOrderVo> erpOrderVos = new ArrayList<>();
            for(ErpOrderInfo order:list) {
                log.info("结算保存erp销售订单  参数 order=[{}]" + JsonUtil.toJson(order));
                if (null == order) {
                    log.error("结算保存erp销售订单失败，传入参数为空" + JsonUtil.toJson(order));
                    return;
                }
                if (null == order.getItemList() || order.getItemList().size() <= 0) {
                    log.error("结算保存erp销售订单失败，订单详情数据为空" + JsonUtil.toJson(order));
                    return;
                }
                if (null == order.getOrderFee()) {
                    log.error("结算保存erp销售订单失败，订单费用数据为空" + JsonUtil.toJson(order));
                    return;
                }
                //商品map
                Map<String, Integer> productMap=new HashMap();

                //订单物流信息
                ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
                log.info("根据订单编号查询订单详情信息 订单物流信息：orderLogistics={}"+JsonUtil.toJson(orderLogistics));
                ErpOrderVo erpOrderVo = new ErpOrderVo();
                //商品列表
                List<ErpOrderProductInfo> erpOrderProductInfoList = new ArrayList<>();
                int productCount = 0;
                if (null != order.getItemList() && order.getItemList().size() > 0) {
                    for (ErpOrderItem item : order.getItemList()) {
                        productCount += item.getProductCount();
                        if (productMap.containsKey(item.getSkuCode()+"product_type"+item.getProductType())){
                            //结算商品list里面已经有此sku，需要合并
                            ErpOrderProductInfo orderProductInfo=erpOrderProductInfoList.get(productMap.get(item.getSkuCode()+"product_type"+item.getProductType()));
                            orderProductInfo.setProductCount(orderProductInfo.getProductCount()+item.getProductCount().intValue());
                            if(null==item.getActualProductCount()){
                                item.setActualProductCount(0L);
                            }
                            if(null==orderProductInfo.getActualProductCount()){
                                orderProductInfo.setActualProductCount(0);
                            }
                            orderProductInfo.setActualProductCount(orderProductInfo.getActualProductCount()+item.getActualProductCount().intValue());
                            if(null!=item.getBatchInfoCode()) {
                                ErpBatchInfo batchInfo = new ErpBatchInfo();
                                batchInfo.setBatchInfoCode(item.getBatchInfoCode());
                                batchInfo.setBatchNo(item.getBatchCode());
                                batchInfo.setTotalProductCount(item.getProductCount().intValue());
                                orderProductInfo.getBatchList().add(batchInfo);

                            }

                            continue;
                        }
                        //订单商品实体
                        ErpOrderProductInfo productInfo = new ErpOrderProductInfo();
                        //批次信息集合
                        List<ErpBatchInfo> batchList=new ArrayList<>();
                        ErpBatchInfo batchInfo=null;
                        if(null!=item.getBatchInfoCode()){
                            batchInfo=new ErpBatchInfo();
                            batchInfo.setBatchInfoCode(item.getBatchInfoCode());
                            batchInfo.setBatchNo(item.getBatchCode());
                            batchInfo.setTotalProductCount(item.getProductCount().intValue());
                            batchList.add(batchInfo);
                        }else{
                            BatchInfo batchInfoDate=new BatchInfo();
                            batchInfoDate.setBasicId(item.getOrderInfoDetailId());
                            List<BatchInfo> batchInfos= batchInfoDao.selectBatchInfoList(batchInfoDate);
                            if(null!=batchInfos&&batchInfos.size()>0){
                                for(BatchInfo batch:batchInfos){
                                    batchInfo=new ErpBatchInfo();
                                    batchInfo.setBatchInfoCode(item.getBatchInfoCode());
                                    batchInfo.setBatchNo(item.getBatchCode());
                                    batchInfo.setTotalProductCount(item.getProductCount().intValue());
                                    batchList.add(batchInfo);
                                }
                            }
                        }
                        productInfo.setBatchList(batchList);
                        //订单编码
                        productInfo.setOrderCode(item.getOrderStoreCode());
                        //sku编号
                        productInfo.setSkuCode(item.getSkuCode());
                        //sku名称
                        productInfo.setSkuName(item.getSkuName());
                        //商品品类编码
                        productInfo.setProductCategoryCode(item.getProductCategoryCode());
                        //商品品类名称
                        productInfo.setProductCategoryName(item.getProductCategoryName());
                        //商品品牌编码
                        productInfo.setProductBrandCode(item.getProductBrandCode());
                        //商品品牌名称
                        productInfo.setProductBrandName(item.getProductBrandName());
                        //商品属性编码
                        productInfo.setProductPropertyCode(item.getProductPropertyCode());
                        //商品属性名称
                        productInfo.setProductPropertyName(item.getProductPropertyName());
                        //商品类型 0商品（本品） 1赠品 2兑换赠品
                        productInfo.setProductType(item.getProductType());
                        //渠道售价（分销价）
                        productInfo.setPriceTax(item.getPriceTax());
                        //活动价
                        productInfo.setActivityPrice(item.getActivityPrice());
                        //渠道采购价
                        productInfo.setPurchaseAmount(item.getPurchaseAmount());
                        //商品数量
                        productInfo.setProductCount(item.getProductCount().intValue());
                        //实发商品数量
                        if (null != item.getActualProductCount()) {
                            productInfo.setActualProductCount(item.getActualProductCount().intValue());
                        } else {
                            productInfo.setActualProductCount(0);
                        }
                        //商品总价
                        productInfo.setTotalProductAmount(item.getTotalProductAmount());
                        //分摊后金额
                        if (null != item.getTotalPreferentialAmount()) {
                            productInfo.setTotalPreferentialAmount(item.getTotalPreferentialAmount());
                        } else {
                            productInfo.setTotalPreferentialAmount(BigDecimal.ZERO);
                        }
                        //分摊后单价
                        if (null != item.getPreferentialAmount()) {
                            productInfo.setPreferentialAmount(item.getPreferentialAmount());
                        } else {
                            productInfo.setPreferentialAmount(BigDecimal.ZERO);
                        }
                        //A品券抵扣
                        if (null != item.getTopCouponAmount()) {
                            productInfo.setTopCouponMoney(item.getTopCouponAmount());
                        } else {
                            productInfo.setTopCouponMoney(BigDecimal.ZERO);
                        }
                        //服纺劵抵扣
                        productInfo.setSuitCouponMoney(BigDecimal.ZERO);
                        //活动优惠
                        if (null != item.getTotalAcivityAmount()) {
                            productInfo.setTotalAcivityAmount(item.getTotalAcivityAmount());
                        } else {
                            productInfo.setTotalAcivityAmount(BigDecimal.ZERO);
                        }
                        //使用赠品额度
                        if (null != item.getUsedGiftQuota()) {
                            productInfo.setComplimentaryAmount(item.getUsedGiftQuota());
                        } else {
                            productInfo.setComplimentaryAmount(BigDecimal.ZERO);
                        }
                        //销项税率
                        productInfo.setTaxRate(item.getTaxRate());


                        erpOrderProductInfoList.add(productInfo);
                        productMap.put(item.getSkuCode()+"product_type"+item.getProductType(),erpOrderProductInfoList.indexOf(productInfo));
                    }
                    erpOrderVo.setProdcutList(erpOrderProductInfoList);
                }
                //订单编码
                erpOrderVo.setOrderCode(order.getOrderStoreCode());
                //所属主订单编码
                erpOrderVo.setMainOrderCode(order.getMainOrderCode());
                //订单状态  1:已支付 2：已发货  3:已收货

                if (!ErpOrderTypeEnum.ASSIST_PURCHASING.getValue().equals(order.getOrderTypeCode())) {
                    erpOrderVo.setOrderStatus(orderStatus);
                }else{
                    //如果是货架订单，直接变成已签收状态
                    erpOrderVo.setOrderStatus(3);
                }

                //客户编码
                erpOrderVo.setFranchiseeCode(order.getFranchiseeCode());
                //客户名称
                erpOrderVo.setFranchiseeName(order.getFranchiseeName());
                //门店编码
                erpOrderVo.setStoreCode(order.getStoreCode());
                //门店名称
                erpOrderVo.setStoreName(order.getStoreName());
                //订单类型编码 2直送 1配送 3辅采直送
                erpOrderVo.setOrderTypeCode(order.getOrderTypeCode());
                //订单类型名称
                erpOrderVo.setOrderTypeName(order.getOrderTypeName());
                //订单类别编码 1:首单配送 2:首单赠送 3:首单货架 4:货架补货 5:配送补货 6:游乐设备 7:首单直送 8直送补货
                erpOrderVo.setOrderCategoryCode(order.getOrderCategoryCode());
                //订单类别名称
                erpOrderVo.setOrderCategoryName(order.getOrderCategoryName());
                //订单总额
                erpOrderVo.setTotalProductAmount(order.getTotalProductAmount());
                //实付金额
                erpOrderVo.setActualTotalProductAmount(order.getTotalProductAmount().subtract(order.getDiscountAmount()));
                //订单商品总数量
                erpOrderVo.setTotalProductCount(productCount);
                //实发商品总数量
                if (null != order.getActualProductCount()) {
                    erpOrderVo.setActualTotalProductCount(order.getActualProductCount().intValue());
                } else {
                    erpOrderVo.setActualTotalProductCount(0);
                }
                //总物流费
                if (null != orderLogistics) {
                    erpOrderVo.setDeliverAmount(orderLogistics.getLogisticsFee());
                    erpOrderVo.setGoodsCoupon(orderLogistics.getCouponPayFee());
                    erpOrderVo.setAccountGoodsCoupon(orderLogistics.getBalancePayFee());
                } else {
                    erpOrderVo.setDeliverAmount(BigDecimal.ZERO);
                    erpOrderVo.setGoodsCoupon(BigDecimal.ZERO);
                    erpOrderVo.setAccountGoodsCoupon(BigDecimal.ZERO);
                }


                //活动抵减
                if (null != order.getOrderFee().getActivityMoney()) {
                    erpOrderVo.setActivityMoney(order.getOrderFee().getActivityMoney());
                } else {
                    erpOrderVo.setActivityMoney(BigDecimal.ZERO);
                }
                //A品券抵减
                if (null != order.getOrderFee().getTopCouponMoney()) {
                    erpOrderVo.setTopCouponMoney(order.getOrderFee().getTopCouponMoney());
                } else {
                    erpOrderVo.setTopCouponMoney(BigDecimal.ZERO);
                }
                //服纺券抵减
                if (null != order.getSuitCouponMoney()) {
                    erpOrderVo.setSuitCouponMoney(order.getSuitCouponMoney());
                } else {
                    erpOrderVo.setSuitCouponMoney(BigDecimal.ZERO);
                }
                //仓库编码
                erpOrderVo.setTransportCenterCode(order.getTransportCenterCode());
                //仓库名称
                erpOrderVo.setTransportCenterName(order.getTransportCenterName());
                //库房编码
                erpOrderVo.setWarehouseCode(order.getWarehouseCode());
                //库房名称
                erpOrderVo.setWarehouseName(order.getWarehouseName());
                //下单时间
                erpOrderVo.setOrderTime(order.getCreateTime());
                //出库时间
                erpOrderVo.setOutTime(order.getDeliveryTime());
                //所属渠道
                erpOrderVo.setCompanyCode(order.getCompanyCode());
                //所属渠道名称
                erpOrderVo.setCompanyName(order.getCompanyName());
                //物流单号
                erpOrderVo.setTransportCode(order.getTransportCode());
                //物流公司编码
                erpOrderVo.setTransportCompanyCode(order.getTransportCompanyCode());
                //物流公司名称
                erpOrderVo.setTransportCompanyName(order.getTransportCompanyName());
                //使用赠品额度
                if (null != order.getOrderFee().getUsedGiftQuota()) {
                    erpOrderVo.setComplimentaryAmount(order.getOrderFee().getUsedGiftQuota());
                } else {
                    erpOrderVo.setComplimentaryAmount(BigDecimal.ZERO);
                }
                //A品券作废金额
                if (null != order.getOrderFee().getNullifyTopCouponMoney()) {
                    erpOrderVo.setNullifyTopCouponMoney(order.getOrderFee().getNullifyTopCouponMoney());
                } else {
                    erpOrderVo.setNullifyTopCouponMoney(BigDecimal.ZERO);
                }
                //收货时间
                erpOrderVo.setTakeTime(order.getReceiveTime());
                //所属合伙人编码
                erpOrderVo.setCopartnerCode(order.getCopartnerAreaCode());
                //所属合伙人名称
                erpOrderVo.setCopartnerName(order.getCopartnerAreaName());
                erpOrderVos.add(erpOrderVo);
            }

            log.info("结算保存erp销售订单,参数为={}" + JsonUtil.toJson(erpOrderVos));

            StringBuilder sb = new StringBuilder();
            sb.append(settlement).append("/erp/order/save");
            HttpClient httpClient = HttpClient.post(sb.toString()).json(erpOrderVos);
            HttpResponse httpResponse = httpClient.action().result(new TypeReference<HttpResponse>() {
            });
            log.info("结算保存erp销售订单结束,返回信息为为={}" + JsonUtil.toJson(httpResponse));
            if (!httpResponse.getCode().equals(MessageId.SUCCESS_CODE)) {
                log.error("结算保存erp销售订单失败，返回参数为" + JsonUtil.toJson(httpResponse));
            }
        }catch (Exception e){
            log.error("结算保存erp销售订单异常，错误信息为" + e.getMessage());
        }
    }



    /**
 * 结算保存erp退货订单
 * @param order
 * @return
 */
public void settlementSaveReturnOrder(ReturnOrderDetailVO order) {
    log.info("结算保存erp退货订单  参数 order=[{}]"+JsonUtil.toJson(order));
    ReturnOrderInfo returnOrderInfo=order.getReturnOrderInfo();
    if(null==order||null==returnOrderInfo){
        log.error("结算保存erp退货订单失败，传入参数为空"+JsonUtil.toJson(order));
        return;
    }
    if(null==order.getDetails()||order.getDetails().size()<=0){
        log.error("结算保存erp销售退货失败，订单详情数据为空"+JsonUtil.toJson(order));
        return;
    }
    try {

        ErpReturnOrderVo erpOrderVo = new ErpReturnOrderVo();
        List<ErpReturnOrderVo> erpOrderList = new ArrayList<>();

            //退货单号
            erpOrderVo.setReturnOrderCode(order.getReturnOrderCode());
            //原订单编码
            erpOrderVo.setOrderCode(returnOrderInfo.getOrderStoreCode());
            //退货状态 1:已验收
            erpOrderVo.setOrderStatus(returnOrderInfo.getReturnOrderStatus());
            //客户编码
            erpOrderVo.setFranchiseeCode(returnOrderInfo.getFranchiseeCode());
            //客户名称
            erpOrderVo.setFranchiseeName(returnOrderInfo.getFranchiseeName());
            //门店编码
            erpOrderVo.setStoreCode(returnOrderInfo.getStoreCode());
            //门店名称
            erpOrderVo.setStoreName(returnOrderInfo.getStoreName());
            //退货类型编码 1:售后退货 2:缺货取消 3:客户拒收
            erpOrderVo.setOrderTypeCode(ReturnOrderTypeEnum.getEnums(returnOrderInfo.getReturnOrderType()).getCode().toString());
            //退货类型名称
            erpOrderVo.setOrderTypeName(ReturnOrderTypeEnum.getEnums(returnOrderInfo.getReturnOrderType()).getName());
            //退货仓库编码
            erpOrderVo.setTransportCenterCode(returnOrderInfo.getTransportCenterCode());
            //仓库名称
            erpOrderVo.setTransportCenterName(returnOrderInfo.getTransportCenterName());
            //库房编码
            erpOrderVo.setWarehouseCode(returnOrderInfo.getWarehouseCode());
            //库房名称
            erpOrderVo.setWarehouseName(returnOrderInfo.getWarehouseName());
            //退货总额
            erpOrderVo.setTotalProductAmount(returnOrderInfo.getReturnOrderAmount());
            //退A品券总额
            erpOrderVo.setTopCouponMoney(returnOrderInfo.getTopCouponDiscountAmount());
            //退服纺券总额
            erpOrderVo.setSuitCouponMoney(BigDecimal.ZERO);
            //所属渠道
            erpOrderVo.setCompanyCode(returnOrderInfo.getCompanyCode());
            //所属渠道名称
            erpOrderVo.setCompanyName(returnOrderInfo.getCompanyName());
            //当前时间
            erpOrderVo.setInputTime(returnOrderInfo.getReceiveTime() );
            //爱亲成本总额
            erpOrderVo.setAiqinCost(returnOrderInfo.getAiqinCost());
            //所属合伙人
            erpOrderVo.setCopartnerCode(returnOrderInfo.getCopartnerAreaId());
            erpOrderVo.setCopartnerName(returnOrderInfo.getCopartnerAreaName());

        //商品列表
        List<ErpReturnOrderProductInfo> erpReturnOrderProductInfoList = new ArrayList<>();
        Map<String,ErpReturnOrderProductInfo> returnOrderMap = new HashMap<>();
        for (ReturnOrderDetail detail : order.getDetails()) {
            ErpReturnOrderProductInfo info = new ErpReturnOrderProductInfo();
            //退货单号
            info.setReturnOrderCode(detail.getReturnOrderCode());
            //商品编号
            info.setSkuCode(detail.getSkuCode());
            //商品名称
            info.setSkuName(detail.getSkuName());
            //商品品类编码
            info.setProductCategoryCode(detail.getProductCategoryCodes());
            //商品品类名称
            info.setProductCategoryName(detail.getProductCategoryNames());
            //商品品牌编码
            info.setProductBrandCode(detail.getProductBrandCode());
          //  商品品牌名称
            info.setProductBrandName(detail.getProductBrandName());
            //商品属性编码
            info.setProductPropertyCode(detail.getProductPropertyCode());
            // 商品属性名称
            info.setProductPropertyName(detail.getProductPropertyName());
            //商品类型 0商品（本品） 1赠品 2兑换赠品
            info.setProductType(detail.getProductType());
            //熙耘采购价
//            info.setScmpPurchaseAmount();
                //渠道采购价
                info.setPurchaseAmount(detail.getPurchaseAmount() == null ? BigDecimal.ZERO : detail.getPurchaseAmount());
                //退货数量
                info.setProductCount(detail.getReturnProductCount().intValue());
                //退货金额
                info.setTotalProductAmount(detail.getTotalProductAmount());
                //退货单价
                info.setPreferentialAmount(detail.getPreferentialAmount());
                //退还A品券
                if(null!=detail.getTopCouponDiscountAmount()){
                    info.setTopCouponMoney(detail.getTopCouponDiscountAmount());
                }else{
                    info.setTopCouponMoney(BigDecimal.ZERO);
                }
                //批次集合
                info.setBatchList(detail.getBatchList());
                //退还服纺金
                info.setSuitCouponMoney(BigDecimal.ZERO);
                if (returnOrderMap.isEmpty()){
                    returnOrderMap.put(info.getSkuCode(),info);
                }else if (returnOrderMap.size() > 0){
                    for (Map.Entry<String,ErpReturnOrderProductInfo> maps: returnOrderMap.entrySet()){
                         if (!info.getSkuCode().equals(maps.getKey())){
                             returnOrderMap.put(info.getSkuCode(),info);
                         }else {
                             ErpReturnOrderProductInfo value = maps.getValue();
                             Integer productCount = value.getProductCount();
                             Integer productCount1 = info.getProductCount();
                             value.setProductCount(productCount + productCount1);
                             value.setBatchList(info.getBatchList());
                         }
                    }
                }
                log.info("退货商品Map集合： " + returnOrderMap);
//                erpReturnOrderProductInfoList.add(info);
            }
           for (Map.Entry<String,ErpReturnOrderProductInfo> returnMap : returnOrderMap.entrySet()){
               erpReturnOrderProductInfoList.add(returnMap.getValue());
           }
            erpOrderVo.setProdcutList(erpReturnOrderProductInfoList);
            erpOrderList.add(erpOrderVo);

        log.info("结算保存erp退货订单,参数为={}" + JsonUtil.toJson(erpOrderList));
        StringBuilder sb = new StringBuilder();
        sb.append(settlement).append("/erp/return/order/save");
        HttpClient httpClient = HttpClient.post(sb.toString()).json(erpOrderList);
        HttpResponse httpResponse = httpClient.action().result(new TypeReference<HttpResponse>() {
        });
        log.info("结算保存erp退货订单结束,返回信息为为={}" + JsonUtil.toJson(httpResponse));
        if (!httpResponse.getCode().equals(MessageId.SUCCESS_CODE)) {
            log.error("结算保存erp退货订单失败，返回参数为" + JsonUtil.toJson(httpResponse));
        }
    }catch (Exception e){
        log.error("结算保存erp退货订单y异常，异常信息为：" + e.getMessage());
    }
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
    public List<ProductSkuRespVo2> detail4(String provinceCode, String cityCode, String companyCode, List<ProductSkuRequest2> productSkuRequest2List) {

        List<ProductSkuRespVo2> list = new ArrayList<>();
        try {
            ShoppingCartProductRequest shoppingCartProductRequest = new ShoppingCartProductRequest();
            shoppingCartProductRequest.setCityCode(cityCode);
            shoppingCartProductRequest.setProvinceCode(provinceCode);
            shoppingCartProductRequest.setCompanyCode(companyCode);
            shoppingCartProductRequest.setProductSkuRequest2List(productSkuRequest2List);
            String path = "/search/spu/sku/detail4";
            log.info("detail2 接口参数为：{}"+JsonUtil.toJson(shoppingCartProductRequest));
            HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartProductRequest);
            HttpResponse<List<ProductSkuRespVo2>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductSkuRespVo2>>>() {
            });
            log.info("detail2 接口 请求结果为：{}"+JsonUtil.toJson(response));
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



public static void main(String[] args) {
    BridgeProductService bridgeProductService=new BridgeProductService();

    System.out.println(JsonUtil.toJson(bridgeProductService.accountBalance("EBCC55A75ED842CD9B83B7181F789EC4")));
}
}
