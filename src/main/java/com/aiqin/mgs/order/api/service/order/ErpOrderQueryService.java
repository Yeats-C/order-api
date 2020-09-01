package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.po.order.DownloadOrderInfoVo;
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
     * 根据订单号查询订单头和订单行
     *
     * @param orderCode 订单号
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/31 11:06
     */
    ErpOrderInfo getOrderAndItemByOrderCode(String orderCode);

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
     * 条件筛选订单
     *
     * @param erpOrderInfo
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/31 10:06
     */
    List<ErpOrderInfo> select(ErpOrderInfo erpOrderInfo);

    /**
     * 根据订单编号查询待签收订单和商品行详情
     *
     * @param orderCode
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/14 14:50
     */
    ErpOrderInfo getNeedSignOrderDetailByOrderCode(String orderCode);

    /**
     * 查询门店未签收订单数量
     *
     * @param storeId 门店id
     * @return int
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/14 14:20
     */
    int getNeedSignOrderQuantity(String storeId);

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
     * erp查询销售订单列表
     *
     * @param erpOrderQueryRequest
     * @return com.aiqin.mgs.order.api.base.PageResData<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 16:15
     */
    PageResData<ErpOrderInfo> findErpOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

    /**
     * erp查询货架订单列表
     *
     * @param erpOrderQueryRequest
     * @return com.aiqin.mgs.order.api.base.PageResData<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 16:15
     */
    PageResData<ErpOrderInfo> findErpRackOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

    /**
     * 爱掌柜查询订单列表
     *
     * @param erpOrderQueryRequest
     * @return com.aiqin.mgs.order.api.base.PageResData<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 16:15
     */
    PageResData<ErpOrderInfo> findStoreOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

    /**
     * 获取当前天最大的订单号
     *
     * @param currentDay 当前天 yyyyMMdd
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 18:53
     */
    String getMaxOrderCodeByCurrentDay(String currentDay);

    /**
     * 查询批发订单列表
     * @param erpOrderQueryRequest
     * @return
     */
    PageResData<ErpOrderInfo> findErpWholesaleOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

    /**
     * 查询导出订单列表信息
     * @param erpOrderQueryRequest
     * @return
     */
    List<DownloadOrderInfoVo> findDownloadOrderList(ErpOrderQueryRequest erpOrderQueryRequest);
}
