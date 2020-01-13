package com.aiqin.mgs.order.api.dao;


import com.aiqin.mgs.order.api.domain.request.service.ReduceDetailRequest;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectReduceDetail;
import com.aiqin.mgs.order.api.domain.response.service.ServiceProjectTransformResponse;

import java.util.List;

public interface ServiceProjectReduceDetailDao {
    Integer insertReduceDetail(ServiceProjectReduceDetail serviceProjectReduceDetail);

    List<ServiceProjectReduceDetail> selectReduceDetailByCondition(ReduceDetailRequest reduceDetailRequest);

    Integer countReduceDetailByCondition(ReduceDetailRequest reduceDetailRequest);

    ServiceProjectReduceDetail selectReduceDetailByReduceId(ReduceDetailRequest reduceDetailRequest);

    Integer insertReduceDetailList(List<ServiceProjectReduceDetail> serviceProjectReduceDetailList);

    List<ServiceProjectReduceDetail> selectReduceDetailByAssetId(String assetId);

    List<String> TotalConsumer(ReduceDetailRequest reduceDetailRequest);

    List<String> yesTodayTotalConsumer(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectReduceDetail> selectReduceDetailByConditions(ReduceDetailRequest reduceDetailRequest);

    Long yesTodayTotalReturnAmount(ReduceDetailRequest reduceDetailRequest);

    Long TotalReturnAmount(ReduceDetailRequest reduceDetailRequest);

    Integer TotalReturnCount(ReduceDetailRequest reduceDetailRequest);

    Integer yesTodayTotalReturnCount(ReduceDetailRequest reduceDetailRequest);

    List<String> consumerCount(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectTransformResponse> totalReturnCountAndAmountByType(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectTransformResponse> totalReturnCountAndAmountByProject(ReduceDetailRequest reduceDetailRequest);
}
