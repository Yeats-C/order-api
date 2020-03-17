package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.StoreLockDetails;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderItemSplitGroupResponse;

import java.util.List;

/**
 * description: ErpStoreLockDetailsService
 * date: 2020/3/17 15:25
 * author: hantao
 * version: 1.0
 */
public interface ErpStoreLockDetailsService {
    /**
     * 批量存入商品锁库信息
     * @param list
     */
    void insertStoreLockDetails(List<StoreLockDetails> list);

    /**
     * 根据skuCode、lineCode、lockCount查询商品锁库信息
     * @param entity
     * @return
     */
    StoreLockDetails selectByLineCodeAndSkuCodeAndLockCount(StoreLockDetails entity);

    /**
     * 查询商品锁库信息，从本地查询
     *
     * @param order
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:48
     */
    List<ErpOrderItemSplitGroupResponse> getNewRepositorySplitGroup(ErpOrderInfo order);

}
