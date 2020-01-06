package com.aiqin.mgs.order.api.service.bill;


import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;

public interface CreateRejectRecordService {
    //创建采购单
    void addRejectRecord(ReturnOrderInfo returnOrderInfo);
}
