package com.aiqin.mgs.order.api.service.gift;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.gift.GiftCartQueryResponse;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartAddRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartQueryRequest;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartQueryResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpOrderCartAddResponse;

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

    /**
     * 添加兑换赠品到购物车
     *
     * @param erpCartAddRequest
     * @param auth
     * @return
     */
    ErpOrderCartAddResponse addProduct(ErpCartAddRequest erpCartAddRequest, AuthToken auth);

    /**
     *修改兑换赠品池赠品状态
     * @param giftPool
     * @return
     */
    HttpResponse updateUseStatus(GiftPool giftPool);

    /**
     * 爱掌柜通过门店id及筛选项查询赠品池列表
     * @param giftPool
     * @return
     */
    HttpResponse<GiftCartQueryResponse> getGiftPoolListByStoreId(GiftPool giftPool);

    /**
     * 爱掌柜查询赠品购物车列表
     * @param erpCartQueryRequest
     * @param auth
     * @return
     */
    ErpCartQueryResponse queryGiftCartList(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth);

    /**
     * 根据cartId删除行
     *
     * @param cartId
     */
    void deleteCartLine(String cartId);

    /**
     * 清空购物车
     *
     * @param erpCartQueryRequest
     */
    void deleteAllCartLine(ErpCartQueryRequest erpCartQueryRequest);
}
