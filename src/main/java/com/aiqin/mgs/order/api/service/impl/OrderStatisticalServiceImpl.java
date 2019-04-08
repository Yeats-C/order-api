package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.domain.request.statistical.BusinessStatisticalRequest;
import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
import com.aiqin.mgs.order.api.domain.statistical.BusinessStatistical;
import com.aiqin.mgs.order.api.service.OrderStatisticalService;
import com.aiqin.mgs.order.api.service.builder.BusinessStatisticalBuilder;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
@Service
@Slf4j
public class OrderStatisticalServiceImpl implements OrderStatisticalService {

    @Resource
    private OrderDao orderDao;

    @Override
    public BusinessStatisticalResponse businessStatistical(String distributorId) {
        BusinessStatisticalResponse response;
        try {
            //昨日数据统计
            BusinessStatisticalRequest yesterday = new BusinessStatisticalRequest();
            yesterday.setDistributorId(distributorId);
            yesterday.setStartDate(DateUtil.getBeginYesterday(DateUtil.getCurrentDate()));
            yesterday.setEndDate(DateUtil.getEndYesterDay(DateUtil.getCurrentDate()));
            CompletableFuture<BusinessStatistical> yesterdayStatistical = CompletableFuture.supplyAsync(() -> {
                try {
                    List<BusinessStatistical> list = orderDao.statisticalBusiness(yesterday);
                    if (CollectionUtils.isNotEmpty(list)) {
                        return list.get(0);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
            //今日数据统计
            BusinessStatisticalRequest today = new BusinessStatisticalRequest();
            today.setDistributorId(distributorId);
            today.setStartDate(DateUtil.getDayBegin(DateUtil.getCurrentDate()));
            today.setEndDate(DateUtil.getDayEnd(DateUtil.getCurrentDate()));
            CompletableFuture<BusinessStatistical> todayStatistical = CompletableFuture.supplyAsync(() -> {
                try {
                    List<BusinessStatistical> list = orderDao.statisticalBusiness(today);
                    if (CollectionUtils.isNotEmpty(list)) {
                        return list.get(0);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            //当前数据统计
            BusinessStatisticalRequest year = new BusinessStatisticalRequest();
            year.setDistributorId(distributorId);
            year.setStartDate(DateUtil.getCurrentYearBeginTime());
            year.setEndDate(DateUtil.getCurrentDate());
            year.setGroupByFlag(true);
            CompletableFuture<List<BusinessStatistical>> yearStatistical = CompletableFuture.supplyAsync(() -> {
                try {
                    List<BusinessStatistical> list = orderDao.statisticalBusiness(year);
                    if (CollectionUtils.isNotEmpty(list)) {
                        return list;
                    } else {
                        return Lists.newArrayList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            CompletableFuture.allOf(yesterdayStatistical, todayStatistical, yearStatistical);
            response = BusinessStatisticalBuilder.create(yesterdayStatistical.get(), todayStatistical.get(), yearStatistical.get()).builder();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("error [{}]", ex);
        }
        return null;
    }


}
