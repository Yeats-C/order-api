package com.aiqin.mgs.order.api.service;

/**
 * @author jinghaibo
 * Date: 2019/12/6 14:10
 * Description:
 */
public interface BridgeOmsService {
    /**
     * 在门店用商品数
     * @param storeId
     * @return
     */
    Integer countUseStoreProjectByStoreId(String storeId);
}
