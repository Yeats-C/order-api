package com.aiqin.mgs.order.api.service.test;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.OrderApiBootApplication;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderApiBootApplication.class)
public class TestApi {

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private UrlProperties urlProperties;


    @Test
    public void testGetStoreInfo(){
        ShoppingCartRequest shoppingCartRequest = new ShoppingCartRequest();
        shoppingCartRequest.setStoreId("ABD720FA0AFA064FD7AC1FD354C1D6D7AB");
        String path = "/store/getStoreInfo";
        StringBuilder codeUrl = new StringBuilder();
        codeUrl.append(urlProperties.getSlcsApi()).append(path);
        HttpClient httpGet = HttpClient.get(codeUrl.toString() + "?store_id=" + shoppingCartRequest.getStoreId());
        httpGet.action().status();
        HttpResponse result = httpGet.action().result(new TypeReference<HttpResponse<CartOrderInfo>>() {
        });
        System.out.println(result.getData().toString());
    }
}
