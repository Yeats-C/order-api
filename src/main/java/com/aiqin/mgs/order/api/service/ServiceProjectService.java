package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.service.ReduceAndAssetRequest;
import com.aiqin.mgs.order.api.domain.request.service.ReduceDetailRequest;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectAsset;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectReduceDetail;

import java.util.List;


/**
 * @author jinghaibo
 * Date: 2019/12/5 16:03
 * Description:
 */
public interface ServiceProjectService {


    HttpResponse insertServiceProjectAsset(ServiceProjectAsset serviceProjectAsset);

    HttpResponse insertList(List<ServiceProjectAsset> serviceProjectAssetList);

    HttpResponse updateAsset(ServiceProjectAsset serviceProjectAsset);

    HttpResponse selectServiceProjectAssetByPhone(String customerPhone,String storeId);

    HttpResponse selectServiceProjectAssetByAssetId(String sourceId);

    HttpResponse serviceProjectTransformStatistics(String storeId);

    HttpResponse serviceProjectTypeTransformStatistics(ReduceDetailRequest reduceDetailRequest);

    HttpResponse serviceProjectTransformStatistics(ReduceDetailRequest reduceDetailRequest);

    HttpResponse serviceProjectTopTransformStatistics(ReduceDetailRequest reduceDetailRequest);

    HttpResponse selectServiceProjectOrder(ReduceDetailRequest reduceDetailRequest);

    HttpResponse selectOrderInfo(String orderId,Integer orderType,String storeId);

    HttpResponse insertReduceDetail(ServiceProjectReduceDetail serviceProjectReduceDetail);

    HttpResponse insertReduceDetailList(List<ServiceProjectReduceDetail> serviceProjectReduceDetailList);



    HttpResponse insertAssetAndReduceDetailList(ReduceAndAssetRequest reduceAndAssetRequest);

    HttpResponse selectAssetByOrderCode(String orderCode);

    HttpResponse selectServiceProjectReduceDetail(String assetId);

    HttpResponse insertReduceAndUpdateAsset(ReduceAndAssetRequest reduceDetailAsset);

    HttpResponse selectServiceProjectAssetCodeByOrderCode(String orderCode);
}
