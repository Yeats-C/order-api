package com.aiqin.mgs.order.api.service.impl.market;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.market.StoreActivityDao;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.response.market.ActivityReportOrderResp;
import com.aiqin.mgs.order.api.service.market.StoreActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreActivityServiceImpl implements StoreActivityService{

    @Resource
    private StoreActivityDao storeActivityDao;


    @Override
    public HttpResponse<List<ActivityReportOrderResp>> selectActivityReportOrderInfo(String storeId, String activityId, String beginTime, String finishTime) {
        // 获取订单数
        List<ActivityReportOrderResp> activityReportOrderResps  = storeActivityDao.selectActivityReportOrder(storeId, activityId, beginTime, finishTime);
        for (ActivityReportOrderResp a : activityReportOrderResps) {
            a.setOrderCount(a.getMemberOderCount() + a.getOrderCount());
        }
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

    /**
     *  查询活动数据报表实时订单情况
     * @param storeId
     * @param activityId
     * @return
     */
    @Override
    public HttpResponse<ActivityReportOrderResp> selectActivityReportOrder(String storeId, String activityId) {
        // 获取订单数 会员订单数 总销售额
        ActivityReportOrderResp activityReportOrderResp = storeActivityDao.selectActivityOrder(storeId, activityId);

        // 获取订单code skuCode
        // 查询出所有订单code
        List<OrderInfo> orderCodes = storeActivityDao.selectActivityOrderCode(storeId, activityId);
        // 查询订单对应活动的商品
        for (OrderInfo oi : orderCodes) {
            List<String> skuCodes = storeActivityDao.selectActivitySkuCode(oi.getOrderCode(), activityId);
            oi.setSkuCodes(skuCodes);
    }


        activityReportOrderResp.setOrderCode(orderCodes);

        return HttpResponse.success(activityReportOrderResp);
    }
}
