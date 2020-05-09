package com.aiqin.mgs.order.api.dao.gift;

import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;

import java.util.List;

public interface GiftPoolDao {
    /**
     * 添加兑换赠品池赠品
     * @param giftPool
     * @return
     */
    int add(GiftPool giftPool);

    /**
     * 查询赠品池列表
     * @param giftPool
     * @return
     */
    List<GiftPool> getGiftPoolList(GiftPool giftPool);

    /**
     * 查询赠品池列表总数
     * @param giftPool
     * @return
     */
    int getTotalNum(GiftPool giftPool);
    /**
     * 修改兑换赠品池赠品
     * @param giftPool
     * @return
     */
    int updateUseStatus(GiftPool giftPool);

    /**
     * 通过仓库code List 查询门店拥有权限的赠品池数据
     * @param giftPool
     * @return
     */
    List<GiftPool> getGiftPoolListByWarehouseCodeList(GiftPool giftPool);

    /**
     * 通过仓库code List 查询门店拥有权限的赠品池数据总数
     * @param giftPool
     * @return
     */
    int getTotalNumByWarehouseCodeList(GiftPool giftPool);
}








