package com.aiqin.mgs.order.api.service.impl.market;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.market.StoreActivityDao;
import com.aiqin.mgs.order.api.domain.response.market.ActivityReportOrderResp;
import com.aiqin.mgs.order.api.service.market.StoreActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StoreActivityServiceImpl implements StoreActivityService{

    @Resource
    private StoreActivityDao storeActivityDao;


    @Override
    public HttpResponse<List<ActivityReportOrderResp>> selectActivityReportOrderInfo(String storeId, String activityId, String beginTime, String finishTime) {
        storeActivityDao.selectActivityReportOrderInfo(storeId, activityId, beginTime, finishTime);
        return null;
    }
}
