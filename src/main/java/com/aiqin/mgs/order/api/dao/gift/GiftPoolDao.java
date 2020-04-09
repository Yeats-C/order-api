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
}








