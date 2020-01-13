package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;

/**
 * 订单取消
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/12 17:46
 */
public interface ErpOrderCancelService {

    /**
     * 取消订单后续操作流程
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 10:10
     */
    void cancelOrderRequestGroup(String orderCode, AuthToken auth);

    /**
     * 缺货终止交易
     *
     * @param erpOrderCancelRequest
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:45
     */
    void cancelOrderWithoutStock(ErpOrderCancelRequest erpOrderCancelRequest, AuthToken auth);

    /**
     * 申请取消订单
     *
     * @param erpOrderCancelRequest
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:46
     */
    void applyCancelOrder(ErpOrderCancelRequest erpOrderCancelRequest, AuthToken auth);

    /**
     * 注销订单关联的优惠券
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 10:07
     */
    void turnOffCoupon(String orderCode, AuthToken auth);

    /**
     * 解锁库存
     *
     * @param orderCode       订单号
     * @param processTypeEnum 流程操作类型
     * @param skipStep        跳过解锁库存（缺货终止交易不需要解锁）
     * @param auth            操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 10:08
     */
    void unlockStock(String orderCode, ErpOrderNodeProcessTypeEnum processTypeEnum, boolean skipStep, AuthToken auth);

    /**
     * 取消采购单
     *
     * @param orderCode       订单号
     * @param processTypeEnum 流程节点控制
     * @param auth            操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 10:09
     */
    void cancelPurchaseOrder(String orderCode, ErpOrderNodeProcessTypeEnum processTypeEnum, AuthToken auth);

    /**
     * 修改订单状态为取消
     *
     * @param orderCode           订单号
     * @param orderStatusEnum     订单状态
     * @param orderNodeStatusEnum 订单节点状态
     * @param auth                操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 10:09
     */
    void cancelOrderStatus(String orderCode, ErpOrderStatusEnum orderStatusEnum, ErpOrderNodeStatusEnum orderNodeStatusEnum, AuthToken auth);

}
