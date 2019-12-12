package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.StoreOverviewDao;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowDaily;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowResp;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowYearMonth;
import com.aiqin.mgs.order.api.service.StoreOverviewService;
import com.aiqin.mgs.order.api.util.DayUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreOverviewServiceImpl implements StoreOverviewService{

    @Resource
    private StoreOverviewDao storeOverviewDao;

    /**
     *  首页客流情况
     *
     * @param storeId
     * @param text
     *@param year
     * @param month  @return
     */
    @Override
    public HttpResponse storeCustomerFlowMonthly(String storeId, Integer text, String year, String month) {
        if(StringUtils.isEmpty(storeId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"storeId="+storeId);
        }

        CustomreFlowResp customreFlowResps = new CustomreFlowResp();
        // 根据状态获取不同客流数据--状态(0当月,1当年,2选择年月)
        if(Global.CUSTOMER_FLOW_0 == text){
            month = DayUtil.getYearMonthStr();
            List<CustomreFlowYearMonth> customreFlowYearMonths = storeOverviewDao.storeCustomerFlowMonthly(storeId, month);
            customreFlowResps.setCustomreFlowYearMonthList(customreFlowYearMonths);
        }else if(Global.CUSTOMER_FLOW_1 == text){
            List<CustomreFlowYearMonth> customreFlowYearMonths = storeOverviewDao.storeCustomerFlowYear(storeId, year);
            customreFlowResps.setCustomreFlowYearMonthList(customreFlowYearMonths);
        }else if(Global.CUSTOMER_FLOW_2 == text){
            List<CustomreFlowDaily> customreFlowDailies = storeOverviewDao.storeCustomerFlowYearMonthly(storeId, year, month);
            customreFlowResps.setCustomreFlowDailyList(customreFlowDailies);
        }else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"text="+text);
        }
        return HttpResponse.success(customreFlowResps);
    }
}
