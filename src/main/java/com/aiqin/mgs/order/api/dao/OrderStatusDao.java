package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStatus;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-08 16:14
 */
public interface OrderStatusDao {
    OrderStatus searchStatus(Integer status);
}
