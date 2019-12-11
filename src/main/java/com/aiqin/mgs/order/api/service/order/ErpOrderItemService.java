package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;

import java.util.List;

/**
 * 订单明细行操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/10 9:39
 */
public interface ErpOrderItemService {

    /**
     * 保存订单明细行
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:39
     */
    void saveOrderItem(ErpOrderItem po, AuthToken auth);

    /**
     * 批量保存订单明细行
     *
     * @param list
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:40
     */
    void saveOrderItemList(List<ErpOrderItem> list, AuthToken auth);

    /**
     * 根据主键更新订单明细行
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:40
     */
    void updateOrderItem(ErpOrderItem po, AuthToken auth);

    /**
     * 批量更新订单明细行
     *
     * @param list
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:40
     */
    void updateOrderItemList(List<ErpOrderItem> list, AuthToken auth);

    /**
     * 根据订单id查询订单明细行列表
     *
     * @param orderId
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:41
     */
    List<ErpOrderItem> selectOrderItemListByOrderId(String orderId);
}
