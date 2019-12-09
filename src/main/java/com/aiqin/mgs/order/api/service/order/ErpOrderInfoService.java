package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;

/**
 * 订单新增修改封装原子操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 11:00
 */
public interface ErpOrderInfoService {


    void saveOrder(ErpOrderInfo po, AuthToken auth);

    void saveOrderNoLog(ErpOrderInfo po, AuthToken auth);

    void updateOrderByPrimaryKeySelective(ErpOrderInfo po, AuthToken auth);
}
