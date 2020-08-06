package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.request.MemberSaleRequest;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/8/6 18:27
 * Description:
 */
public interface BridgeMemberService {
    List<OrderbyReceiptSumResponse> cashier( OrderQuery orderQuery);

    /**
     * 更新会员消费时间记录
     * @param memberSaleRequest
     */
    void updateMemberSale(MemberSaleRequest memberSaleRequest);
}
