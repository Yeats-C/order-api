package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.PrestorageOrderSupply;
import com.aiqin.mgs.order.api.domain.response.PrestorageOrderInfo;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2019/11/5 19:43
 * Description:
 */
public interface PrestorageOrderSupplyDao {
    /**
     * 通过ID修改
     * @param prestorageOrderSupply
     */
    void updateById(PrestorageOrderSupply prestorageOrderSupply);

    /**
     * 插入
     * @param prestorageOrderSupply
     */
    void addPrestorageOrder(PrestorageOrderSupply prestorageOrderSupply);

    /**
     * 查询预存订单列表
     * @param trans
     * @return
     */
    List<PrestorageOrderInfo> selectPrestorageOrderList(OrderQuery trans);

    Integer selectPrestorageOrderListCount(OrderQuery trans);
}
