package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PrestorageOrderSupply;

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
}
