package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.StoreOverviewDao;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.response.conversionrate.StoreTransforRateDaily;
import com.aiqin.mgs.order.api.domain.response.conversionrate.StoreTransforRateResp;
import com.aiqin.mgs.order.api.domain.response.conversionrate.StoreTransforRateYearMonth;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowDaily;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowResp;
import com.aiqin.mgs.order.api.domain.response.customer.CustomreFlowYearMonth;
import com.aiqin.mgs.order.api.domain.response.sales.*;
import com.aiqin.mgs.order.api.service.StoreOverviewService;
import com.aiqin.mgs.order.api.util.DayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StoreOverviewServiceImpl implements StoreOverviewService{

    @Resource
    private StoreOverviewDao storeOverviewDao;

    /**
     *  爱掌柜首页概览总销售额
     * @param storeId
     * @param text
     * @param year
     * @param month
     * @param day
     * @return
     */
    @Override
    public HttpResponse storeSalesAchieved(String storeId, Integer text, String year, String month, String day) {
        if(StringUtils.isEmpty(storeId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"storeId="+storeId);
        }

        StoreSalesAchievedResp storeSalesAchievedResp = new StoreSalesAchievedResp();
        // 根据状态获取不同客流数据--状态(0当月,1当年,2选择年月)
        if(Global.STORE_STATUS_0 == text){
            month = DayUtil.getYearMonthStr();
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeSalesAchievedMonthly(storeId, month);
            storeSalesAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        }else if(Global.STORE_STATUS_1 == text){
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeSalesAchievedYear(storeId, year);
            storeSalesAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        }else if(Global.STORE_STATUS_2 == text){
            List<StoreSalesAchievedDaily> storeSalesAchievedDailies = storeOverviewDao.storeSalesAchievedYearMonthly(storeId, year, month);
            storeSalesAchievedResp.setStoreSalesAchievedDailies(storeSalesAchievedDailies);
        }else if(Global.STORE_STATUS_3 == text){
            List<StoreSalesAchievedDaily> storeSalesAchievedDailies = storeOverviewDao.storeSalesAchievedYesterday(storeId, year, month, day);
            storeSalesAchievedResp.setStoreSalesAchievedDailies(storeSalesAchievedDailies);
        }else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"text="+text);
        }
        return HttpResponse.success(storeSalesAchievedResp);
    }

    /**
     *  爱掌柜首页概览18A销售额
     * @param storeId
     * @param text
     * @param year
     * @param month
     * @return
     */
    @Override
    public HttpResponse storeEighteenSalesAchieved(String storeId, Integer text, String year, String month) {
        if(StringUtils.isEmpty(storeId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"storeId="+storeId);
        }

        StoreSalesEighteenAchievedResp storeSalesEighteenAchievedResp = new StoreSalesEighteenAchievedResp();
        // 根据状态获取不同客流数据--状态(0当月,1当年,2选择年月)
        if(Global.STORE_STATUS_0 == text){
            month = DayUtil.getYearMonthStr();
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeEighteenSalesAchievedMonthly(storeId, month);
            storeSalesEighteenAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        }else if(Global.STORE_STATUS_1 == text){
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeEighteenSalesAchievedYear(storeId, year);
            storeSalesEighteenAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        }else if(Global.STORE_STATUS_2 == text){
            List<StoreSalesEighteenAchievedBrand> storeSalesEighteenAchievedBrands = storeOverviewDao.storeEighteenSalesAchievedBrand(storeId, year, month);
            storeSalesEighteenAchievedResp.setStoreSalesEighteenAchievedBrands(storeSalesEighteenAchievedBrands);
        }else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"text="+text);
        }
        return HttpResponse.success(storeSalesEighteenAchievedResp);
    }

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
        if(Global.STORE_STATUS_0 == text){
            month = DayUtil.getYearMonthStr();
            List<CustomreFlowYearMonth> customreFlowYearMonths = storeOverviewDao.storeCustomerFlowMonthly(storeId, month);
            customreFlowResps.setCustomreFlowYearMonthList(customreFlowYearMonths);
        }else if(Global.STORE_STATUS_1 == text){
            List<CustomreFlowYearMonth> customreFlowYearMonths = storeOverviewDao.storeCustomerFlowYear(storeId, year);
            customreFlowResps.setCustomreFlowYearMonthList(customreFlowYearMonths);
        }else if(Global.STORE_STATUS_2 == text){
            List<CustomreFlowDaily> customreFlowDailies = storeOverviewDao.storeCustomerFlowYearMonthly(storeId, year, month);
            customreFlowResps.setCustomreFlowDailyList(customreFlowDailies);
        }else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"text="+text);
        }
        return HttpResponse.success(customreFlowResps);
    }

    /**
     *  首页客流转化率
     *
     * @param storeId
     * @param text
     *@param year
     * @param month  @return
     */
    @Override
    public HttpResponse storeTransforRate(String storeId, Integer text, String year, String month) {
        if(StringUtils.isEmpty(storeId)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"storeId="+storeId);
        }

        StoreTransforRateResp storeTransforRateResp = new StoreTransforRateResp();
        // 根据状态获取不同门店转化率数据--状态(0当月,1当年,2选择年月)
        if(Global.STORE_STATUS_0 == text){
            month = DayUtil.getYearMonthStr();
            List<StoreTransforRateYearMonth> storeTransforRateYearMonths = storeOverviewDao.storeTransforRateMonthly(storeId, month);
            storeTransforRateResp.setStoreTransforRateYearMonths(storeTransforRateYearMonths);
        }else if(Global.STORE_STATUS_1 == text){
            List<StoreTransforRateYearMonth> storeTransforRateYearMonths = storeOverviewDao.storeTransforRateYear(storeId, year);
            storeTransforRateResp.setStoreTransforRateYearMonths(storeTransforRateYearMonths);
        }else if(Global.STORE_STATUS_2 == text){
            List<StoreTransforRateDaily> storeTransforRateDailies = storeOverviewDao.storeTransforRateYearMonthly(storeId, year, month);
            storeTransforRateResp.setStoreTransforRateDailies(storeTransforRateDailies);
        }else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"text="+text);
        }
        return HttpResponse.success(storeTransforRateResp);
    }
}
