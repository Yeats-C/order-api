package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReqVo;
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

}
