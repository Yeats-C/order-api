package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo3;
import com.aiqin.mgs.order.api.domain.response.RejectVoResponse;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderDetailBySearchResponse;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderResponse;

import java.util.List;

public interface ReturnOrderDetailService {
    /**
     * 根据条件返回退货单列表
     *
     * @param orderListVo3
     * @param
     * @return
     */
    HttpResponse<List<ReturnOrderDetailBySearchResponse>> searchList(OrderListVo3 orderListVo3);

    HttpResponse<ReturnOrderResponse> searchReturnOrderDetailByReturnOrderCode(String returnOrderCode);
}
