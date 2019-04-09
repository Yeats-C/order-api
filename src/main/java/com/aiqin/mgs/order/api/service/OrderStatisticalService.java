package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.response.statistical.BusinessStatisticalResponse;
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
     * 获取当前日期前30天有销售记录的门店集合
     *
     * @param day
     * @return
     */
    List<String> existsSalesDistributorIn30Days(Date day);

    /**
     * 刷新指定门店的畅缺商品数据
     *
     * @param day
     * @param distributorId
     */
    void refreshDistributorSoldOutOfStockProduct(Date day, String distributorId);


    /**
     * 获取畅销商品集合
     *
     * @param distributorId
     * @return
     */
    List<SoldOutOfStockProduct> getSoldOutOfStockProduct(String distributorId);

}
