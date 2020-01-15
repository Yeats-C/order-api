package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.ReturnOrderDetailBatchDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo3;
import com.aiqin.mgs.order.api.domain.response.RejectVoResponse;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderDetailBySearchResponse;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderResponse;
import com.aiqin.mgs.order.api.service.ReturnOrderDetailService;
import com.aiqin.mgs.order.api.util.BeanHelper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReturnOrderDetailServiceImpl implements ReturnOrderDetailService {

    @Autowired
    private ReturnOrderDetailDao returnOrderDetailDao;

    @Autowired
    private ReturnOrderDetailBatchDao returnOrderDetailBatchDao;

    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;

    @Override
    public HttpResponse<List<ReturnOrderDetailBySearchResponse>> searchList(OrderListVo3 orderListVo3) {

        //PageResData pageResData = new PageResData();
        PageHelper.startPage(orderListVo3.getPageNo(),orderListVo3.getPageSize());
        //获取退货单列表
        List<ReturnOrderInfo> returnOrderInfoList = returnOrderInfoDao.selectByParames(orderListVo3);

        if (CollectionUtils.isEmpty(returnOrderInfoList)){
            //return HttpResponse.failure()
        }

        List<ReturnOrderDetailBySearchResponse> responses = BeanHelper.copyWithCollection(returnOrderInfoList, ReturnOrderDetailBySearchResponse.class);
//        pageResData.setDataList(responses);
//        pageResData.setTotalCount(responses.size());


        return HttpResponse.success(responses);
    }

    @Override
    public HttpResponse<ReturnOrderResponse> searchReturnOrderDetailByReturnOrderCode(String returnOrderCode) {
        //判断参数是否合法
        if (StringUtils.isBlank(returnOrderCode)){
            //返回无效参数
            return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
        if (returnOrderInfo == null){
            return HttpResponse.failure(ResultCode.NOT_FOUND_RETURN_ORDER_DATA);
        }
        ReturnOrderInfoExt returnOrderInfoExt = BeanHelper.copyProperties(returnOrderInfo, ReturnOrderInfoExt.class);
        List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);
        List<ReturnOrderDetailExt> returnOrderDetailExts = BeanHelper.copyWithCollection(returnOrderDetails, ReturnOrderDetailExt.class);
        List<ReturnOrderDetailBatch> returnOrderDetailBatches = returnOrderDetailBatchDao.selectListByReturnOrderCode(returnOrderCode);
        ReturnOrderResponse resp = new ReturnOrderResponse();
        resp.setReturnOrderInfoExt(returnOrderInfoExt);
        resp.setDetails(returnOrderDetailExts);
        resp.setReturnOrderDetailBatches(returnOrderDetailBatches);
        return HttpResponse.success(resp);
    }
}
