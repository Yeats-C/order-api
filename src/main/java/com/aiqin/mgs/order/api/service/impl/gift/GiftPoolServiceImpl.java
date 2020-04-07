package com.aiqin.mgs.order.api.service.impl.gift;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.gift.GiftPoolDao;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.service.gift.GiftPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author csf
 */
@Service
public class GiftPoolServiceImpl implements GiftPoolService {


    private static final Logger LOGGER = LoggerFactory.getLogger(GiftPoolServiceImpl.class);

    @Resource
    private GiftPoolDao giftPoolDao;




    @Override
    public HttpResponse add(GiftPool giftPool) {
        LOGGER.info("添加兑换赠品池赠品 add 参数 giftPool 为：{}", giftPool);
        HttpResponse httpResponse=HttpResponse.success();
        giftPoolDao.add(giftPool);
        //TODO  此处需调用供应链接口，通过skuCode查询仓库信息，并记录表
        return httpResponse;
    }

    @Override
    public HttpResponse<PageResData<GiftPool>> getGiftPoolList(GiftPool giftPool) {
        LOGGER.info("查询赠品池列表 getGiftPoolList 参数 giftPool 为：{}", giftPool);
        HttpResponse httpResponse=HttpResponse.success();
        PageResData pageResData=new PageResData();
        List<GiftPool> giftPoolList=giftPoolDao.getGiftPoolList(giftPool);
        int totalNum=giftPoolDao.getTotalNum(giftPool);
        pageResData.setDataList(giftPoolList);
        pageResData.setTotalCount(totalNum);
        httpResponse.setData(pageResData);
        return httpResponse;
    }
}