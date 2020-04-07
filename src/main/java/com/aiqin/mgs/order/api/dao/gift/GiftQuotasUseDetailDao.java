package com.aiqin.mgs.order.api.dao.gift;

import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetail;

import java.util.List;

public interface GiftQuotasUseDetailDao {
    /**
     * 查询兑换赠品积分账户使用明细
     * @param giftQuotasUseDetail
     * @return
     */
    List<GiftQuotasUseDetail> select(GiftQuotasUseDetail giftQuotasUseDetail);

    int add(GiftQuotasUseDetail giftQuotasUseDetail);
}








