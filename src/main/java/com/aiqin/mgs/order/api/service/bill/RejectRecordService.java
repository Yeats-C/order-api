package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.RejectRecordInfo;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import com.aiqin.mgs.order.api.domain.request.bill.ReturnDLReq;
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

    /**
     * 耘链退货单回传
     * @param returnDLReq
     * @return
     */
    Boolean selectPurchaseInfo(ReturnDLReq returnDLReq);


    /**
     * 取消退供单
     */
    Boolean removeRejectRecordStatus(String rejectRecordCode);

}
