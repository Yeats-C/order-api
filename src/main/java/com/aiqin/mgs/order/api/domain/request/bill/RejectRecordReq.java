package com.aiqin.mgs.order.api.domain.request.bill;

import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.RejectRecordDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class RejectRecordReq implements Serializable {
    private RejectRecord rejectRecord;
    private List<RejectRecordDetail> rejectRecordDetail;
}
