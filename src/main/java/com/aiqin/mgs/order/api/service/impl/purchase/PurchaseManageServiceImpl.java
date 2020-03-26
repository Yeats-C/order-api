package com.aiqin.mgs.order.api.service.impl.purchase;

import com.aiqin.ground.util.protocol.http.HttpResponse;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDao;
import com.aiqin.mgs.order.api.dao.PurchaseOrderDetailDao;
import com.aiqin.mgs.order.api.domain.PurchaseOrderInfo;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseApplyRequest;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseOrderProductRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOperationLog;
import com.aiqin.mgs.order.api.domain.response.purchase.*;
import com.aiqin.mgs.order.api.service.purchase.PurchaseManageService;
import com.aiqin.mgs.order.api.util.BeanCopyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class PurchaseManageServiceImpl implements PurchaseManageService {
    private static final BigDecimal big = BigDecimal.valueOf(0);

    @Resource
    private PurchaseOrderDao purchaseOrderDao;
    @Resource
    private PurchaseOrderDetailDao purchaseOrderDetailDao;
    @Resource
    private OperationLogDao operationLogDao;

    @Override
    public HttpResponse<PageResData<PurchaseOrder>> purchaseOrderList(PurchaseApplyRequest purchaseApplyRequest){
        PageResData pageResData = new PageResData();
        List<PurchaseOrder> list = purchaseOrderDao.purchaseOrderList(purchaseApplyRequest);
        Integer count = purchaseOrderDao.purchaseOrderCount(purchaseApplyRequest);
        pageResData.setDataList(list);
        pageResData.setTotalCount(count);
        return HttpResponse.success(pageResData);
    }

    @Override
    public HttpResponse<PurchaseDetailResponse> purchaseOrderDetails(String purchaseOrderId){
        if(StringUtils.isBlank(purchaseOrderId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        PurchaseOrderInfo detail = purchaseOrderDao.purchaseOrderInfo(purchaseOrderId);

        PurchaseDetailResponse response = BeanCopyUtils.copy(detail, PurchaseDetailResponse.class);
        // 查询采购单日志
        List<OrderOperationLog> logList = operationLogDao.list(detail.getPurchaseOrderCode());
        if(CollectionUtils.isNotEmpty(logList)){
            response.setLogList(logList);
        }

        return HttpResponse.successGenerics(response);
    }

    @Override
    public HttpResponse<PageResData<PurchaseOrderProduct>> purchaseOrderProduct(PurchaseOrderProductRequest request){
        PageResData pageResData = new PageResData();
        if(StringUtils.isBlank(request.getPurchaseOrderId())){
            return HttpResponse.success(pageResData);
        }
        List<PurchaseOrderProduct> list = purchaseOrderDetailDao.purchaseOrderList(request);
        Integer count = purchaseOrderDetailDao.purchaseOrderCount(request);
        pageResData.setDataList(list);
        pageResData.setTotalCount(count);
        return HttpResponse.success(pageResData);
    }

}
