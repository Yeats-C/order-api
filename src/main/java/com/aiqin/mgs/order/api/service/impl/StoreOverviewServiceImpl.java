package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.StoreOverviewDao;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.ProductOverViewReq;
import com.aiqin.mgs.order.api.domain.request.ProductSeachSkuReq;
import com.aiqin.mgs.order.api.domain.response.ProductBaseUnResp;
import com.aiqin.mgs.order.api.domain.response.ProductLabelStatusResp;
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
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class StoreOverviewServiceImpl implements StoreOverviewService {

    @Resource
    private StoreOverviewDao storeOverviewDao;

    /**
     * 爱掌柜首页概览总销售额
     *
     * @param storeId
     * @param text
     * @param year
     * @param month
     * @param day
     * @return
     */
    @Override
    public HttpResponse storeSalesAchieved(String storeId, Integer text, String year, String month, String day) {
        if (StringUtils.isEmpty(storeId)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "storeId=" + storeId);
        }

        StoreSalesAchievedResp storeSalesAchievedResp = new StoreSalesAchievedResp();
        // 根据状态获取不同数据的销售额--状态(0当月,1当年,2选择年月3,昨日)
        if (text != null && Global.STORE_STATUS_0.equals(text)) {
            month = DayUtil.getYearMonthStr();
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeSalesAchievedMonthly(storeId, month);
            storeSalesAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        } else if (text != null && Global.STORE_STATUS_1.equals(text)) {
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeSalesAchievedYear(storeId, year);
            storeSalesAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        } else if (text != null && Global.STORE_STATUS_2.equals(text)) {
            List<StoreSalesAchievedDaily> storeSalesAchievedDailies = storeOverviewDao.storeSalesAchievedYearMonthly(storeId, year, month);
            storeSalesAchievedResp.setStoreSalesAchievedDailies(storeSalesAchievedDailies);
        } else if (text != null && Global.STORE_STATUS_3.equals(text)) {
            List<StoreSalesAchievedDaily> storeSalesAchievedDailies = storeOverviewDao.storeSalesAchievedYesterday(storeId, year, month, day);
            storeSalesAchievedResp.setStoreSalesAchievedDailies(storeSalesAchievedDailies);
        } else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "text=" + text);
        }
        return HttpResponse.success(storeSalesAchievedResp);
    }

    /**
     * 爱掌柜首页概览18A销售额
     *
     * @param storeId
     * @param text
     * @param year
     * @param month
     * @return
     */
    @Override
    public HttpResponse storeEighteenSalesAchieved(String storeId, Integer text, String year, String month) {
        if (StringUtils.isEmpty(storeId)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "storeId=" + storeId);
        }

        StoreSalesEighteenAchievedResp storeSalesEighteenAchievedResp = new StoreSalesEighteenAchievedResp();
        // 根据状态获取不同数据的18A销售额--状态(0当月,1当年,2选择年月)
        if (text != null && Global.STORE_STATUS_0.equals(text) ) {
            month = DayUtil.getYearMonthStr();
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeEighteenSalesAchievedMonthly(storeId, month);
            storeSalesEighteenAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        } else if (text != null && Global.STORE_STATUS_1.equals(text)) {
            List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths = storeOverviewDao.storeEighteenSalesAchievedYear(storeId, year);
            storeSalesEighteenAchievedResp.setStoreSalesAchievedYearMonths(storeSalesAchievedYearMonths);
        } else if (text != null && Global.STORE_STATUS_2.equals(text)) {
            List<StoreSalesEighteenAchievedBrand> storeSalesEighteenAchievedBrands = storeOverviewDao.storeEighteenSalesAchievedBrand(storeId, year, month);
            storeSalesEighteenAchievedResp.setStoreSalesEighteenAchievedBrands(storeSalesEighteenAchievedBrands);
        } else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "text=" + text);
        }
        return HttpResponse.success(storeSalesEighteenAchievedResp);
    }

    /**
     * 首页客流情况
     *
     * @param storeId
     * @param text
     * @param year
     * @param month   @return
     */
    @Override
    public HttpResponse storeCustomerFlowMonthly(String storeId, Integer text, String year, String month) {
        if (StringUtils.isEmpty(storeId)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "storeId=" + storeId);
        }

        CustomreFlowResp customreFlowResps = new CustomreFlowResp();
        // 根据状态获取不同客流数据--状态(0当月,1当年,2选择年月)
        if (text != null && Global.STORE_STATUS_0.equals(text)) {
            month = DayUtil.getYearMonthStr();
            List<CustomreFlowYearMonth> customreFlowYearMonths = storeOverviewDao.storeCustomerFlowMonthly(storeId, month);
            customreFlowResps.setCustomreFlowYearMonthList(customreFlowYearMonths);
        } else if (text != null && Global.STORE_STATUS_1.equals(text)) {
            List<CustomreFlowYearMonth> customreFlowYearMonths = storeOverviewDao.storeCustomerFlowYear(storeId, year);
            customreFlowResps.setCustomreFlowYearMonthList(customreFlowYearMonths);
        } else if (text != null && Global.STORE_STATUS_2.equals(text)) {
            List<CustomreFlowDaily> customreFlowDailies = storeOverviewDao.storeCustomerFlowYearMonthly(storeId, year, month);
            customreFlowResps.setCustomreFlowDailyList(customreFlowDailies);
        } else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "text=" + text);
        }
        return HttpResponse.success(customreFlowResps);
    }

    /**
     * 首页客流转化率
     *
     * @param storeId
     * @param text
     * @param year
     * @param month   @return
     */
    @Override
    public HttpResponse storeTransforRate(String storeId, Integer text, String year, String month) {
        if (StringUtils.isEmpty(storeId)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "storeId=" + storeId);
        }

        StoreTransforRateResp storeTransforRateResp = new StoreTransforRateResp();
        // 根据状态获取不同门店转化率数据--状态(0当月,1当年,2选择年月)
        if (text != null && Global.STORE_STATUS_0.equals(text)) {
            month = DayUtil.getYearMonthStr();
            List<StoreTransforRateYearMonth> storeTransforRateYearMonths = storeOverviewDao.storeTransforRateMonthly(storeId, month);
            storeTransforRateResp.setStoreTransforRateYearMonths(storeTransforRateYearMonths);
        } else if (text != null && Global.STORE_STATUS_1.equals(text)) {
            List<StoreTransforRateYearMonth> storeTransforRateYearMonths = storeOverviewDao.storeTransforRateYear(storeId, year);
            storeTransforRateResp.setStoreTransforRateYearMonths(storeTransforRateYearMonths);
        } else if (text != null && Global.STORE_STATUS_2.equals(text)) {
            List<StoreTransforRateDaily> storeTransforRateDailies = storeOverviewDao.storeTransforRateYearMonthly(storeId, year, month);
            storeTransforRateResp.setStoreTransforRateDailies(storeTransforRateDailies);
        } else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "text=" + text);
        }
        return HttpResponse.success(storeTransforRateResp);
    }

    /**
     * 首页销售毛利
     *
     * @param storeId
     * @param text
     * @param year
     * @return
     */
    @Override
    public HttpResponse storeSaleMargin(String storeId, Integer text, String year) {
        if (StringUtils.isEmpty(storeId)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "storeId=" + storeId);
        }

        StoreSaleMarginResp storeSaleMarginResp = new StoreSaleMarginResp();
        // 根据状态获取不同销售毛利--状态(0当月,1总毛利额,2,18A毛利额)
        if (text != null && Global.STORE_STATUS_0.equals(text)) {
            // 获取当月的销售毛利和18a销售毛利
            String month = DayUtil.getYearMonthStr();
            BigDecimal saleMargin = storeOverviewDao.storeSaleMargin(storeId, month);
            BigDecimal eighteenSaleMargin = storeOverviewDao.storeEighteenSaleMargin(storeId, month);
            storeSaleMarginResp.setSaleMargin(saleMargin);
            storeSaleMarginResp.setEighteenSaleMargin(eighteenSaleMargin);
            return HttpResponse.success(storeSaleMarginResp);
        } else if (text != null && Global.STORE_STATUS_1.equals(text)) {
            // 获取当年销售毛利
            List<StoreSaleMarginResp> storeSaleMarginResps = storeOverviewDao.storeSaleMarginYear(storeId, year);
            return HttpResponse.success(storeSaleMarginResps);
        } else if (text != null && Global.STORE_STATUS_2.equals(text)) {
            // 获取当年18A销售毛利
            List<StoreSaleMarginResp> storeSaleMarginResps = storeOverviewDao.storeEighteenSaleMarginYear(storeId, year);
            return HttpResponse.success(storeSaleMarginResps);
        } else {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "text=" + text);
        }
    }

    /**
     * 首页订单概览
     *
     * @param storeId
     * @return
     */
    @Override
    public HttpResponse storeOrderOverView(String storeId) {
        if (StringUtils.isEmpty(storeId)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "storeId=" + storeId);
        }
        // 获取当年门店订单概览
        List<StoreSaleOverViewResp> storeSaleOverViewResps = storeOverviewDao.storeOrderOverView(storeId);

        return HttpResponse.success(storeSaleOverViewResps);
    }

    /**
     * 首页商品概览
     *
     * @param storeId
     * @return
     */
    @Override
    public HttpResponse storeProductOverView(String storeId) {
        if (StringUtils.isEmpty(storeId)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER, "storeId=" + storeId);
        }
        ProductLabelStatusResp psr = storeOverviewDao.selectProductCountByStoreId(storeId);
        return HttpResponse.success(psr);
    }

    @Override
    public HttpResponse productBaseUnInfo(ProductOverViewReq productOverViewReq) {

        if(StringUtils.isEmpty(productOverViewReq.getStoreId()) && productOverViewReq.getProductStatus() != null){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"storeId="+productOverViewReq.getStoreId()+",status="+productOverViewReq.getProductStatus());
        }
        List<ProductBaseUnResp> productBaseUnResps = storeOverviewDao.productBaseUnInfo(productOverViewReq);
        return HttpResponse.success(productBaseUnResps);
    }

    /**
     *  爱掌柜商品总库商品列表畅销滞销sku
     * @param storeId
     * @param status
     * @return
     */
    @Override
    public HttpResponse storeProductSku(String storeId, String status, String pageNo, String pageSize) {

        if(StringUtils.isEmpty(storeId) && StringUtils.isEmpty(status)){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER,"storeId="+storeId+",status="+status);
        }
        ProductSeachSkuReq productSeachSkuReq = new ProductSeachSkuReq();
        productSeachSkuReq.setStoreId(storeId);
        productSeachSkuReq.setStatus(Integer.parseInt(status));
        productSeachSkuReq.setPageNo(Integer.parseInt(pageNo));
        productSeachSkuReq.setPageSize(Integer.parseInt(pageSize));
        List<String> skuCodes = storeOverviewDao.storeProductSku(productSeachSkuReq);
        return HttpResponse.success(skuCodes);
    }
}
