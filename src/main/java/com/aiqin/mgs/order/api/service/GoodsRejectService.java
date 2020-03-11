package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.request.purchase.RejectQueryRequest;
import com.aiqin.mgs.order.api.domain.response.purchase.*;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author xieq
 * @Date 2020/2/28
 */
public interface GoodsRejectService {

    HttpResponse<PageResData<RejectRecordInfo>> rejectList(RejectQueryRequest rejectQueryRequest);

    HttpResponse<RejectResponseInfo> rejectInfo(String reject_record_code);
}
