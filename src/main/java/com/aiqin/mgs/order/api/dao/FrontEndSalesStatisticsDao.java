package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;

import java.util.List;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatisticsDao
 * @Description
 * @Date 2020/2/15 14:34
 */
public interface FrontEndSalesStatisticsDao {

    /**
     * 根据条件查询前台销售统计
     * @param frontEndSalesStatistics
     * @return
     */
    List<FrontEndSalesStatistics> selectByCondition(FrontEndSalesStatistics frontEndSalesStatistics);

    /**
     * 分组查询当前月销售统计（按门店 skuCode category_id分组查询的）
     * @return
     */
    List<FrontEndSalesStatistics> selectCurrentMonthSalesStatistics();


    /**
     * 根据月份批量删除数据
     * @return
     */
    Integer deleteSalesStatisticsByMonth(Integer month);

    /**
     * 批量插入数据
     * @param list
     * @return
     */
    Integer insertSalesStatisticsList(List<FrontEndSalesStatistics> list);

    Integer updateBatchByMonthAndStoreId(List<FrontEndSalesStatistics> list);
}
