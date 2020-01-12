package com.aiqin.mgs.order.api.jobs;

/**
 * description: ReturnOrderTaskService
 * date: 2020/1/3 15:12
 * author: hantao
 * version: 1.0
 */
public interface ReturnOrderTaskService {

    /**
     * 根据退货单号查询支付状态 支付成功返回true
     * @param orderCode
     * @return
     */
    boolean searchPayOrder(String orderCode);

}
