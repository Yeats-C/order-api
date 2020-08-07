package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.request.MemberSaleRequest;
import com.aiqin.mgs.order.api.domain.response.OrderSumResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.service.BridgeMemberService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/8/6 18:27
 * Description:
 */
@Service
@Slf4j
public class BridgeMemberServiceImpl implements BridgeMemberService {
    @Resource
    private UrlProperties urlProperties;
    @Override
    public OrderSumResponse cashier(OrderQuery orderQuery) {
        log.info("storeValueRecordList:{}", JsonUtil.toJson(orderQuery));
        HttpClient httpClient = HttpClient.post(urlProperties.getMemberApi() + "/store-value/product/statistics").json(orderQuery);
        HttpResponse<OrderSumResponse> response= httpClient.action().result(new TypeReference<HttpResponse<OrderSumResponse>>() {
        });
       return response.getData();
    }

    @Override
    public void updateMemberSale(MemberSaleRequest memberSaleRequest) {
        HttpClient client = HttpClient.post(urlProperties.getMemberApi() + "/members/updateMemberSaleData").json(memberSaleRequest);
        try {
            HttpResponse response = client.action().result(new TypeReference<HttpResponse>() {  });
            log.info("updateMemberSale:"+response);

        }catch (Exception e){
            log.info("updateMemberSaleErr:"+memberSaleRequest);
            log.error(e.toString());
        }


    }
}
