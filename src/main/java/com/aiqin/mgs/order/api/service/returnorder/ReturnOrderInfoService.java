package com.aiqin.mgs.order.api.service.returnorder;

import com.aiqin.mgs.order.api.domain.request.returnorder.*;

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

    /**
     * 退货单校验--查看此订单是否已经生成一条退货单，且流程未结束。如果已存在返回true
     * @param orderCode
     * @return
     */
    Boolean check(String orderCode);

    /**
     * 获取物流单信息（向爱掌柜提供接口）
     * @param returnOrderCode
     * @param logisticsCompanyCode
     * @param logisticsCompanyName
     * @param logisticsNo
     * @return
     */
    Boolean updateLogistics(String returnOrderCode,String logisticsCompanyCode,String logisticsCompanyName,String logisticsNo);

    /**
     * 支付中心---发起退款单回调
     * @param reqVo
     * @return
     */
    Boolean callback(RefundReq reqVo);


}
