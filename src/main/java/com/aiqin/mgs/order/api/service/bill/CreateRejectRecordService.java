package com.aiqin.mgs.order.api.service.bill;



/**
 * 退供单 接口
 */
public interface CreateRejectRecordService {
    //根据ERP退货单生成爱亲退供单
    void addRejectRecord(String returnOrderCode);

}
