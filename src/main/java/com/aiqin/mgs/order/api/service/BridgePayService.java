package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.domain.request.MemberSaleRequest;
import com.aiqin.mgs.order.api.domain.response.PartnerGetCodeUrlRep;

/**
 * @author jinghaibo
 * Date: 2019/11/12 14:20
 * Description:
 */
public interface BridgePayService {
    /**
     * 主扫
     */
    HttpResponse<PartnerGetCodeUrlRep> mainSwept(PayReq vo);

    /**
     * 退款
     * @param payReq
     */
    void toRefund(PayReq payReq);

    /**
     * 更新会员消费时间记录
     * @param memberSaleRequest
     */
    void updateMemberSale(MemberSaleRequest memberSaleRequest);
}
