package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.request.bill.ReturnDLReq;
import com.aiqin.mgs.order.api.domain.request.RejectRequest;
import com.aiqin.mgs.order.api.domain.request.purchase.RejectQueryRequest;
import com.aiqin.mgs.order.api.domain.response.RejectResponse;
import com.aiqin.mgs.order.api.domain.response.RejectVoResponse;

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


    //HttpResponse<List<RejectRecordInfo>> selectPurchaseInfo();

    /**
     * 根据条件查询退供单
     *
     * @param rejectRequest
     * @return
     */
    HttpResponse<List<RejectResponse>> selectByRejectRequest(RejectRequest rejectRequest);

    /**
     * 根据退供单号查询退供单详情
     *
     * @param rejectRecordCode
     * @return
     */
    HttpResponse<RejectVoResponse> searchRejectDetailByRejectCode(String rejectRecordCode);
}
