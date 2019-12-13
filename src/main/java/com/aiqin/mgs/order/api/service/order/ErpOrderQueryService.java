package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderQueryRequest;

import java.util.List;

/**
 * 订单查询service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 10:51
 */
public interface ErpOrderQueryService {

    /**
     * 根据订单id查询订单
     *
     * @param orderId 订单id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:51
     */
    ErpOrderInfo getOrderByOrderId(String orderId);

    /**
     * 根据订单编号查询订单
     *
     * @param orderCode 订单编号
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:52
     */
    ErpOrderInfo getOrderByOrderCode(String orderCode);

    /**
     * 根据以及订单编码获取拆分列表
     *
     * @param orderCode
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 10:30
     */
    List<ErpOrderInfo> getSecondOrderListByPrimaryCode(String orderCode);

    /**
     * 根据订单编号查询订单详情信息
     *
     * @param orderCode 订单编号
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:52
     */
    ErpOrderInfo getOrderDetailByOrderCode(String orderCode);

    /**
     * 根据物流单id查询订单列表
     *
     * @param logisticsId 物流单id
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:53
     */
    List<ErpOrderInfo> getOrderByLogisticsId(String logisticsId);

    /**
     * 查询订单列表
     *
     * @param erpOrderQueryRequest
     * @return com.aiqin.mgs.order.api.base.PageResData<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 21:44
     */
    PageResData<ErpOrderInfo> findOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

    /**
     * 查询货架订单列表
     *
     * @param erpOrderQueryRequest
     * @return com.aiqin.mgs.order.api.base.PageResData<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/13 15:27
     */
    PageResData<ErpOrderInfo> findRackOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

}
