package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.domain.request.MemberSaleRequest;
import com.aiqin.mgs.order.api.domain.response.PartnerGetCodeUrlRep;
import com.aiqin.mgs.order.api.service.BridgePayService;
import com.aiqin.mgs.order.api.util.CopyBeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BridgePayServiceImpl.class);
    /**
     * 支付
     * @param vo
     * @returnorderafter
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
        HttpClient client = HttpClient.post(urlProperties.getPayApi() + "/refund/orderRefund").json(payReq);
        HttpResponse<PartnerGetCodeUrlRep> response = client.action().result(new TypeReference<HttpResponse<PartnerGetCodeUrlRep>>() {  });
        System.out.println(response);

    }

    @Override
    public void updateMemberSale(MemberSaleRequest memberSaleRequest) {
        HttpClient client = HttpClient.post(urlProperties.getMemberApi() + "/members/updateMemberSaleData").json(memberSaleRequest);
        try {
            HttpResponse response = client.action().result(new TypeReference<HttpResponse>() {  });
            logger.info("updateMemberSale:"+response);

        }catch (Exception e){
            logger.info("updateMemberSaleErr:"+memberSaleRequest);
            logger.error(e.toString());
        }


    }
}
