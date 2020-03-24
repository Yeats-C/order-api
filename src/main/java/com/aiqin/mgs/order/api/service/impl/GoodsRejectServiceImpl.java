package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDao;
import com.aiqin.mgs.order.api.dao.RejectRecordDetailDao;
import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.OperationOrderLog;
import com.aiqin.mgs.order.api.domain.request.purchase.RejectQueryRequest;
import com.aiqin.mgs.order.api.domain.response.purchase.*;
import com.aiqin.mgs.order.api.service.GoodsRejectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xieq
 * @Date 2020/2/28
 */
@Service
public class GoodsRejectServiceImpl implements GoodsRejectService {
    @Resource
    private RejectRecordDao rejectRecordDao;
    @Resource
    private RejectRecordDetailDao rejectRecordDetailDao;
    @Resource
    private OperationLogDao operationLogDao;

    @Override
    public HttpResponse<PageResData<RejectRecordInfo>> rejectList(RejectQueryRequest rejectApplyQueryRequest) {
        List<RejectRecordInfo> list = rejectRecordDao.list(rejectApplyQueryRequest);
        Integer count = rejectRecordDao.listCount(rejectApplyQueryRequest);
        return HttpResponse.successGenerics(new PageResData<>(count, list));
    }

    @Override
    public HttpResponse<RejectResponseInfo> rejectInfo(String rejectRecordCode) {
        RejectResponseInfo rejectResponse = new RejectResponseInfo();
        RejectRecordInfo rejectRecord = rejectRecordDao.selectByRejectCode(rejectRecordCode);
        BeanUtils.copyProperties(rejectRecord, rejectResponse);
        // 查询批次信息
        //List<RejectRecordDetail> batchList = rejectRecordDetailDao.selectByRejectId(rejectRecord.getRejectRecordId());
        // 查询商品信息
        List<RejectRecordDetailResponse> productList = rejectRecordDetailDao.selectProductByRejectCode(rejectRecord.getRejectRecordCode());
        // 查询日志
        List<OperationOrderLog> logList = operationLogDao.searchOrderLog(rejectRecord.getRejectRecordCode(), 3);
        rejectResponse.setLogList(logList);
        //rejectResponse.setBatchList(batchList);
        rejectResponse.setProductList(productList);
        return HttpResponse.successGenerics(rejectResponse);
    }
}
