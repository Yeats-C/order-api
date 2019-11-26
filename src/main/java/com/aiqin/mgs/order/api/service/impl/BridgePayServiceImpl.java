package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.domain.response.PartnerGetCodeUrlRep;
import com.aiqin.mgs.order.api.service.BridgePayService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author jinghaibo
 * Date: 2019/11/12 14:21
 * Description:
 */
@Service
public class BridgePayServiceImpl implements BridgePayService {
    @Resource
    private UrlProperties urlProperties;
    /**
     * 支付
     * @param vo
     * @return
     */
    @Override
    public HttpResponse<PartnerGetCodeUrlRep> mainSwept(PayReq vo) {
        //StringBuilder sb = new StringBuilder();
        //sb.append(urlProperties.getPayApi()).append("/payment/pay/payTocAll");
        HttpClient client = HttpClient.post(urlProperties.getPayApi() + "/payment/pay/payTocAll").json(vo);
        HttpResponse<PartnerGetCodeUrlRep> response = client.action().result(new TypeReference<HttpResponse<PartnerGetCodeUrlRep>>() {  });

        return response;
    }

    @Override
    public void toRefund(PayReq payReq) {
        HttpClient client = HttpClient.post(urlProperties.getPayApi() + "/payment/pay/payTocAll").json(payReq);
        HttpResponse<PartnerGetCodeUrlRep> response = client.action().result(new TypeReference<HttpResponse<PartnerGetCodeUrlRep>>() {  });


    }
}
