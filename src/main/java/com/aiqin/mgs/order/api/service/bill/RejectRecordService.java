package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.RejectRecordInfo;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;

import java.util.List;

/**
 * 根据退货单，生成爱亲采购单 接口
 */
public interface RejectRecordService {
    /**
     * 根据退货单，生成爱亲采购单
     * @return
     */
    HttpResponse createRejectRecord(RejectRecordReq RejectRecordReq);

    HttpResponse<List<RejectRecordInfo>> selectPurchaseInfo();
}
