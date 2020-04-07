package com.aiqin.mgs.order.api.service.impl.gift;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.gift.GiftQuotasUseDetailDao;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetail;
import com.aiqin.mgs.order.api.service.gift.GiftQuotasUseDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author csf
 */
@Service
public class GiftQuotasUseDetailServiceImpl implements GiftQuotasUseDetailService {


    private static final Logger LOGGER = LoggerFactory.getLogger(GiftQuotasUseDetailServiceImpl.class);

    @Resource
    private GiftQuotasUseDetailDao giftQuotasUseDetailDao;


    @Override
    public HttpResponse<List<GiftQuotasUseDetail>> getListByStoreId(String store_id) {
        LOGGER.info("通过门店id查询兑换赠品积分账户使用明细 getListByStoreId 参数store_id为：{}", store_id);
        HttpResponse httpResponse=HttpResponse.success();
        GiftQuotasUseDetail giftQuotasUseDetail=new GiftQuotasUseDetail();
        giftQuotasUseDetail.setStoreId(store_id);
        List<GiftQuotasUseDetail> giftQuotasUseDetailServiceList=  giftQuotasUseDetailDao.select(giftQuotasUseDetail);
        httpResponse.setData(giftQuotasUseDetailServiceList);
        return httpResponse;
    }

    @Override
    public HttpResponse add(GiftQuotasUseDetail giftQuotasUseDetail) {
        LOGGER.info("添加兑换赠品积分账户使用明细 add 参数 giftQuotasUseDetail 为：{}", giftQuotasUseDetail);
        HttpResponse httpResponse=HttpResponse.success();
        giftQuotasUseDetailDao.add(giftQuotasUseDetail);
        return httpResponse;
    }
}