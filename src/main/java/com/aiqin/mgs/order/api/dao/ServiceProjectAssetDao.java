package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.request.service.ReduceDetailRequest;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectAsset;
import com.aiqin.mgs.order.api.domain.response.service.ServiceProjectTransformResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jinghaibo
 */
public interface ServiceProjectAssetDao {

    Integer insertServiceProjectAsset(ServiceProjectAsset serviceProjectAsset);

    Integer insertList(List<ServiceProjectAsset> serviceProjectAssetList);

    Integer updateSource(ServiceProjectAsset serviceProjectAsset);

    List<ServiceProjectAsset> selectServiceProjectAssetByPhone(@Param("customer_phone") String customerPhone, @Param("store_id") String storeId, @Param("order_code") String orderCode, @Param("order_id") String orderId);

    ServiceProjectAsset selectServiceProjectAssetBySourceId(String assetId);

    List<ServiceProjectAsset> selectAllUseSource(@Param("store_id") String storeId, @Param("project_id") String projectId);

    Integer deleteList(List<ServiceProjectAsset> serviceProjectAssetList);

    List<ServiceProjectAsset> assetOutTime();

    /**
     * 过期修改状态
     * @param serviceProjectAssetList
     * @return
     */
    Integer updateAssetUseStatus(List<ServiceProjectAsset> serviceProjectAssetList);

    Long yesTodayTotalAmount(ReduceDetailRequest reduceDetailRequest);

    Long TotalAmount(ReduceDetailRequest reduceDetailRequest);

    Integer TotalCount(ReduceDetailRequest reduceDetailRequest);

    Integer yesTodayTotalCount(ReduceDetailRequest reduceDetailRequest);

    List<String> TotalConsumer(ReduceDetailRequest reduceDetailRequest);

    List<String> yesTodayTotalConsumer(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectAsset> selectAssetByCondition(ReduceDetailRequest reduceDetailRequest);

    Integer countAssetByCondition(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectAsset> selectAssetByConditions(ReduceDetailRequest reduceDetailRequest);

    Integer countAssetByConditions(ReduceDetailRequest reduceDetailRequest);

    Integer selectServiceProjectCount(@Param("store_id") String storeId, @Param("project_id") String projectId);

    Long countAmount(String orderId);

    List<String> buyCount(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectTransformResponse> totalCountAndAmountByType(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectTransformResponse> totalCountAndAmountByProject(ReduceDetailRequest reduceDetailRequest);

    List<ServiceProjectAsset> selectServiceProjectAssetByAssetId(@Param("assetId")String assetId);
}
