package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.RejectRecordInfo;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 爱亲退供单 接口
 */
public interface RejectRecordService {
    /**
     * 根据ERP退货单生成爱亲退供单
     * @return
     */
    HttpResponse createRejectRecord(String returnOrderCode);

    HttpResponse<List<RejectRecordInfo>> selectPurchaseInfo();
}
