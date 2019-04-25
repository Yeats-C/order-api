package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
import com.aiqin.mgs.order.api.domain.response.statistical.Last10DaysOrderStatistical;
import com.aiqin.mgs.order.api.domain.statistical.SoldOutOfStockProduct;

import java.util.Date;
import java.util.List;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
public interface OrderStatisticalService {
    /**
     * 营业数据统计
     *
     * @param distributorId
     * @return
     */
    BusinessStatisticalResponse businessStatistical(String distributorId);


    /**
     * 获取当前日期前day天有销售记录的门店集合
     *
     * @param date
     * @param day
     * @return
     */
    List<String> existsSalesDistributorInNumDays(Date date,int day);

    /**
     * 刷新指定门店的畅缺商品数据
     *
     * @param date
     * @param distributorId
     */
    void refreshDistributorSoldOutOfStockProduct(Date date, String distributorId);


    /**
     * 获取畅销商品集合
     *
     * @param distributorId
     * @return
     */
    List<SoldOutOfStockProduct> getSoldOutOfStockProduct(String distributorId);


    /**
     * 刷新指定门店不满足滞销商品sku集合
     *
     * @param date
     * @param distributorId
     */
    void refreshDistributorDisUnsoldProduct(Date date, String distributorId);

    /**
     * 获取门店不满足滞销商品的sku集合
     *
     * @param distributorId
     * @return
     */
    List<String> getDisUnsoldProduct(String distributorId);

    /**
     * 门店近10天订单完成情况
     *
     * @param distributorId
     * @return
     */
    List<Last10DaysOrderStatistical> getLast10DaysOrderStatisticals(String distributorId);

}
