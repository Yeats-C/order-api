package com.aiqin.mgs.order.api.service.impl.gift;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.gift.GiftPoolDao;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.service.gift.GiftPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}