package com.aiqin.mgs.order.api.service.builder;

import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
import com.aiqin.mgs.order.api.domain.statistical.BusinessStatistical;

import java.util.List;
import java.util.Objects;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
public class BusinessStatisticalBuilder {

    private BusinessStatistical yesterdayStatistical;

    private BusinessStatistical todayStatistical;

    private List<BusinessStatistical> list;

    public static BusinessStatisticalBuilder create(BusinessStatistical yesterdayStatistical,
                                                    BusinessStatistical todayStatistical,
                                                    List<BusinessStatistical> list) {
        BusinessStatisticalBuilder builder = new BusinessStatisticalBuilder();
        builder.list = list;
        builder.todayStatistical = todayStatistical;
        builder.yesterdayStatistical = yesterdayStatistical;
        return builder;
    }

    public BusinessStatisticalResponse builder() {
        BusinessStatisticalResponse response = new BusinessStatisticalResponse();
        response.setMonthData(list);
        response.setTodayAmount(Objects.isNull(todayStatistical) ? 0 : todayStatistical.getAmount());
        response.setYesterdayAmount(Objects.isNull(yesterdayStatistical) ? 0 : yesterdayStatistical.getAmount());
        response.setYesterdayPrice(Objects.isNull(yesterdayStatistical) ? 0L : (Objects.isNull(yesterdayStatistical.getTotalPaidPrice()) ? 0L : yesterdayStatistical.getTotalPaidPrice()));
        if (Objects.equals(0, response.getYesterdayAmount())) {
            response.setYesterdayAvgPrice(0L);
        } else {
            response.setYesterdayAvgPrice(response.getYesterdayPrice() / response.getYesterdayAmount());
        }
        return response;
    }
}
