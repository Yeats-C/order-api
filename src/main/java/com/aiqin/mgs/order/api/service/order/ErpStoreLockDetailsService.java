package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.StoreLockDetails;

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

}
