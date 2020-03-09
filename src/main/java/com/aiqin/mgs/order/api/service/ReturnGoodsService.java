package com.aiqin.mgs.order.api.service;


import com.aiqin.mgs.order.api.base.BasePage;
import com.aiqin.mgs.order.api.domain.request.returngoods.QueryReturnOrderManagementReqVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.QueryReturnOrderManagementRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderDetailRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderInfoApplyInboundDetailRespVO;

import java.util.List;

public interface ReturnGoodsService {
    /**
     * 退货管理
     */
    BasePage<QueryReturnOrderManagementRespVO> returnOrderManagement(QueryReturnOrderManagementReqVO reqVO);

    /**
     * 退货详情
     * @param code
     * @return
     */
    ReturnOrderDetailRespVO returnOrderDetail(String code);

    List<ReturnOrderInfoApplyInboundDetailRespVO> inboundInfo(String code);
}
