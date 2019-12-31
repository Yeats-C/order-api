package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderEditRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSignRequest;

/**
 * 订单新增修改封装原子操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 11:00
 */
public interface ErpOrderInfoService {

    /**
     * 保存订单数据
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:38
     */
    void saveOrder(ErpOrderInfo po, AuthToken auth);

    /**
     * 保存订单数据（不保存日志）
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:38
     */
    void saveOrderNoLog(ErpOrderInfo po, AuthToken auth);

    /**
     * 根据主键更新订单数据
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:38
     */
    void updateOrderByPrimaryKeySelective(ErpOrderInfo po, AuthToken auth);

    /**
     * 根据主键更新订单数据（不保存日志）
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 10:19
     */
    void updateOrderByPrimaryKeySelectiveNoLog(ErpOrderInfo po, AuthToken auth);

    /**
     * 待支付订单新增赠品行
     *
     * @param erpOrderEditRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 10:56
     */
    void addProductGift(ErpOrderEditRequest erpOrderEditRequest);

    /**
     * 订单拆单逻辑
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:50
     */
    void orderSplit(String orderCode, AuthToken auth);

    /**
     * 同步订单到供应链
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/30 15:26
     */
    void orderSendToSupply(String orderCode, AuthToken auth);

    /**
     * 订单签收
     *
     * @param erpOrderSignRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:39
     */
    void orderSign(ErpOrderSignRequest erpOrderSignRequest);
}
