package com.aiqin.mgs.order.api.service.gift;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;

public interface GiftPoolService {

    /**
     * 添加兑换赠品池赠品
     * @param giftPool
     * @return
     */
    HttpResponse add(GiftPool giftPool);

    /**
     * 查询赠品池列表
     * @param giftPool
     * @return
     */
    HttpResponse<PageResData<GiftPool>> getGiftPoolList(GiftPool giftPool);
}
