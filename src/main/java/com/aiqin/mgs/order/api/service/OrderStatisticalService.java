package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
public interface OrderStatisticalService {
    /**
     * 营业数据统计
     * @param distributorId
     * @return
     */
    BusinessStatisticalResponse businessStatistical(String distributorId);
}
