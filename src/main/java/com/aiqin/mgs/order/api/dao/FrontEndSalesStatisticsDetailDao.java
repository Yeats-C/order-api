package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatisticsDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatisticsDetailDao
 * @Description
 * @Date 2020/2/15 14:34
 */
public interface FrontEndSalesStatisticsDetailDao {

    /**
     * 查询某店某月份的前台销售统计数据
     * 查到当前月初到当前天时间范围内的数据
     * eg: 传入的是 20200105  则查 20200101 -- 20200105时间内的数据
     * @param beginDay  eg: 20200105 2020年1月5日
     * @param currentDay  eg: 20200105 2020年1月5日
     * @param storeId
     * @return
     */
    List<FrontEndSalesStatistics> selectStoreMonthSaleStatisticsByMonthAndStoreId(
            @Param("beginDay") Integer beginDay,
            @Param("currentDay") Integer currentDay,
            @Param("storeId") String storeId);

    /**
     * 得到昨天销售统计（按门店 skuCode和category_id分组查询的）
     * @return
     */
    List<FrontEndSalesStatisticsDetail> selectYesterdaySalesStatistics();
    /**
     * 批量插入数据
     * @param list
     * @return
     */
    Integer insertSalesStatisticsDetailList(List<FrontEndSalesStatisticsDetail> list);
}
