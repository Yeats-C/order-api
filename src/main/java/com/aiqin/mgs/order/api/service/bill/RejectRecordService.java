package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;

/**
 * 退货单 接口
 */
public interface RejectRecordService {
    /**
     * 创建退货单
     * @return
     */
    HttpResponse createRejectRecord(RejectRecordReq RejectRecordReq);
}
