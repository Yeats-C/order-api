package com.aiqin.mgs.order.api.service.bridge;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.dto.ProductDistributorOrderDTO;
import com.aiqin.mgs.order.api.domain.request.InventoryDetailRequest;
import com.aiqin.mgs.order.api.domain.request.activity.*;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartProductRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.statistical.ProductDistributorOrderRequest;
import com.aiqin.mgs.order.api.domain.response.NewFranchiseeResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Createed by sunx on 2019/4/8.<br/>
 */
@Service
@Slf4j
public class BridgeProductService {

    @Resource
    private UrlProperties urlProperties;

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
        HttpResponse<PageResData<ProductSkuRespVo5>> response = httpClient.action().result(new TypeReference<HttpResponse<PageResData>>() {
        });

        return response;
    }

    /**
     * 商品门店可用库存
     * @param req
     * @return
     */
    public HttpResponse getStoreStockSkuNum(ShoppingCartRequest req){
        String path = "/store/stock/sku/info";
        HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + path+"?store_id=" + req.getStoreId()+"&sku_code="+req.getProductId());
        HttpResponse<Integer> response = httpClient.action().result(new TypeReference<HttpResponse<Integer>>() {
        });

        return response;
    }

    /**
     * 供应链提供查询品类树接口
     * @param req
     * @return
     */
    public HttpResponse productCategoryList(ActivityBrandCategoryRequest req){
        String path = "/product/category/list/categoryCodes";
        HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + path+"?category_codes=" + req.getCategoryCodes());
        HttpResponse<List<ProductCategoryRespVO>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductCategoryRespVO>>>() {
        });
        return response;
    }

    /**
     * 供应链提供查询品牌树接口
     * @param req
     * @return
     */
    public HttpResponse productBrandList(ActivityBrandCategoryRequest req){
        String path = "/product/brand/list";
        HttpClient httpClient = HttpClient.get(urlProperties.getProductApi() + path+"?name=" + req.getName()+"&brand_ids="+req.getBrandIds());
        HttpResponse<List<QueryProductBrandRespVO>> response = httpClient.action().result(new TypeReference<HttpResponse<List<QueryProductBrandRespVO>>>() {
        });
        return response;
    }
}
