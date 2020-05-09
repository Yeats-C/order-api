package com.aiqin.mgs.order.api.service.gift;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetail;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetailPageRequest;

import java.util.List;

public interface GiftQuotasUseDetailService {


    /**
     * 通过门店id查询兑换赠品积分账户使用明细
     * @param store_id
     * @return
     */
    HttpResponse<List<GiftQuotasUseDetail>> getListByStoreId(String store_id);

    /**
     * 添加兑换赠品积分账户使用明细
     * @param giftQuotasUseDetail
     * @return
     */
    HttpResponse add(GiftQuotasUseDetail giftQuotasUseDetail);

    /**
     * 通过门店id查询兑换赠品积分账户使用明细（爱掌柜使用，分页）
     * @param giftQuotasUseDetailPageRequest
     * @return
     */
    HttpResponse<PageResData<GiftQuotasUseDetail>> getPageByStoreId(GiftQuotasUseDetailPageRequest giftQuotasUseDetailPageRequest);
}
