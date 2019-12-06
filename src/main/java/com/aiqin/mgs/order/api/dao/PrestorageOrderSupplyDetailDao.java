package com.aiqin.mgs.order.api.dao;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.PrestorageOrderSupplyDetail;
import com.aiqin.mgs.order.api.domain.request.PrestorageOrderSupplyDetailVo;
import com.aiqin.mgs.order.api.domain.response.PrestorageOrderLogsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2019/11/6 11:01
 * Description:
 */
public interface PrestorageOrderSupplyDetailDao {
    /**
     * 通过prestorageOrderSupplyDetailId 查询
     * @param prestorageOrderSupplyDetailId
     * @return
     */
    PrestorageOrderSupplyDetail selectprestorageorderDetailsById(@Param("prestorageOrderSupplyDetailId") String prestorageOrderSupplyDetailId);

    void updateById(PrestorageOrderSupplyDetail prestorageOrderSupplyDetail);

    /**
     * 通过预存主订单ID查询详情列表
     * @param prestorageOrderSupplyId
     * @return
     */
    List<PrestorageOrderSupplyDetail> selectprestorageorderDetailsListBySupplyId(@Param("prestorageOrderSupplyId") String prestorageOrderSupplyId);

    /**
     * 添加
     * @param prestorageOrderSupplyDetail
     */
    void addPrestorageOrderDetail(PrestorageOrderSupplyDetail prestorageOrderSupplyDetail);

    /**
     *预存 提货日志
     * @param orderQuery
     * @return
     */
    List<PrestorageOrderLogsInfo> selectPrestorageOrderLogs(OrderQuery orderQuery);

    int selectPrestorageOrderLogsCount(OrderQuery orderQuery);

    /**
     * 订单中sku数量
     * @param query
     * @return
     */
    Integer getSkuSum(OrderDetailQuery query);

    /**
     *
     * @param orderId
     * @return
     */
    List<PrestorageOrderSupplyDetail> selectprestorageorderDetailsListByOrderId(@Param("orderId") String orderId);

    /**
     * 通过订单orderId 查询预存详情单
     * @param orderId 订单ID
     * @return
     */
    List<PrestorageOrderSupplyDetail> selectPrestorageOrderDetailByOrderId(@Param("orderId") String orderId);

    int updateRejectPrestoragProduct(PrestorageOrderSupplyDetailVo vo);
}
