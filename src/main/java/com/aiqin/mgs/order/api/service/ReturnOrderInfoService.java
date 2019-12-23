package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailVO;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewApiReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import com.aiqin.mgs.order.api.domain.response.returnorder.ReturnOrderListVo;

/**
 * description: ReturnOrderInfoService
 * date: 2019/12/19 17:40
 * author: hantao
 * version: 1.0
 */
public interface ReturnOrderInfoService {

    /**
     * 退货单保存
     *
     * @param reqVo
     * @return
     */
    Boolean save(ReturnOrderReqVo reqVo);

    /**
     * 退货单列表
     * @param searchVo
     * @return
     */
//    PageResData<ReturnOrderListVo> list(OrderAfterSaleSearchVo searchVo);

    /**
     * 审核操作-erp使用
     * @param reqVo
     * @return
     */
    Boolean updateReturnStatus(ReturnOrderReviewReqVo reqVo);

    /**
     * 修改退货单详情
     * @param records
     * @return
     */
    Boolean updateOrderAfterSaleDetail(ReturnOrderDetailVO records);

    /**
     * 提供给供应链--退货单状态修改
     * @param reqVo
     * @return
     */
    Boolean updateReturnStatusApi(ReturnOrderReviewApiReqVo reqVo);

}
