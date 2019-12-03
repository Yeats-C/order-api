package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;

/**
 * 订单新增修改封装原子操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 11:00
 */
public interface ErpOrderOperationService {

    /**
     * 新建订单
     *
     * @param orderInfo 新增数据
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 11:01
     */
    void saveOrder(OrderStoreOrderInfo orderInfo, AuthToken auth);

    /**
     * 保存订单，不保存日志
     *
     * @param orderInfo
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/3 19:07
     */
    void saveOrderNoLog(OrderStoreOrderInfo orderInfo, AuthToken auth);

    /**
     * 修改订单数据
     *
     * @param orderInfo        根据主键修改非空字段
     * @param auth             操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 11:02
     */
    void updateOrderByPrimaryKeySelective(OrderStoreOrderInfo orderInfo, AuthToken auth);
}
