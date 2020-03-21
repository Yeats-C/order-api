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
    public HttpResponse<ActivityReportOrderResp> selectActivityReportOrderInfo(String storeId, String activityId, String beginTime, String finishTime) {
        // 获取订单数
        Long orderCount  = storeActivityDao.selectActivityReportOrder(storeId, activityId, beginTime, finishTime);
        // 获取会员订单数
        Long memberOrderCount  = storeActivityDao.selectActivityReportMemberOrder(storeId, activityId, beginTime, finishTime);
        // 获取销售额
        Long saleAmount  = storeActivityDao.selectActivityReportOrderSale(storeId, activityId, beginTime, finishTime);
        ActivityReportOrderResp activityReportOrderResps = new ActivityReportOrderResp();
        activityReportOrderResps.setOrderCount(orderCount);
        activityReportOrderResps.setMemberOrderCount(memberOrderCount);
        activityReportOrderResps.setSaleAmount(saleAmount);
        return HttpResponse.success(activityReportOrderResps);
    }

    /**
     *  查询套餐包列表销量
     * @param packageProductId
     * @return
     */
    @Override
    public HttpResponse selectActivityOrderPackageSale(String packageProductId) {
        Long packageSale = storeActivityDao.selectActivityOrderPackageSale(packageProductId);
        return HttpResponse.success(packageSale);
    }
}
