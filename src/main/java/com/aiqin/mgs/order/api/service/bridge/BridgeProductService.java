package com.aiqin.mgs.order.api.service.bridge;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.dto.ProductDistributorOrderDTO;
import com.aiqin.mgs.order.api.domain.request.OperateStockVo;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.statistical.ProductDistributorOrderRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    public HttpResponse changeStock(List<OperateStockVo> stockReqVos) {
        StringBuilder sb = new StringBuilder();
        sb.append(urlProperties.getProductApi()).append("/inventory/update/record");
        HttpClient orderClient =HttpClient.post(sb.toString()).json(stockReqVos);
        return orderClient.action().result(HttpResponse.class);
    }

    /**
     * 获取商品信息
     * @param shoppingCartRequest
     * @return
     */
    public HttpResponse<List<CartOrderInfo>> getProduct(ShoppingCartRequest shoppingCartRequest){
//        String path = "/product/productInfo";
//        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartRequest);
//        HttpResponse<List<CartOrderInfo>> response = httpClient.action().result(new TypeReference<HttpResponse<CartOrderInfo>>() {
//        });
        //测试接口
        HttpResponse<List<CartOrderInfo>> cartOrderInfoHttpResponse = new HttpResponse<>();
        CartOrderInfo data = new CartOrderInfo();
        data.setProductId("12345");
        data.setStoreId("12345");
        data.setSkuId("12345");
        data.setAmount(20);
        data.setPrice(new BigDecimal(29.10));
        data.setProductType(1);
        ArrayList<CartOrderInfo> list = new ArrayList<>();
        list.add(data);
        cartOrderInfoHttpResponse.setData(list);
        return cartOrderInfoHttpResponse;
    }


    /**
     * 获取门店的信息
     * @param shoppingCartRequest
     * @return
     */
    public HttpResponse<CartOrderInfo> getStoreInfo(ShoppingCartRequest shoppingCartRequest){
        String path = "/store/getStoreInfo";
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + path).json(shoppingCartRequest);
        HttpResponse<CartOrderInfo> response = httpClient.action().result(new TypeReference<HttpResponse<CartOrderInfo>>() {
        });
//        HttpResponse<CartOrderInfo> cartOrderInfoHttpResponse = new HttpResponse<>();
//        CartOrderInfo data = new CartOrderInfo();
//        data.setStoreAddress("北京市海淀区海淀南路35号");
//        data.setStoreContacts("胡金英");
//        data.setStoreContactsPhone("18513854421");
//        cartOrderInfoHttpResponse.setData(data);
        return response;
    }

}
