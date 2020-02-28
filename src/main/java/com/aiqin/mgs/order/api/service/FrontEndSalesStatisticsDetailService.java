package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatisticsDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatisticsDetailService
 * @Description 前台销售每日明细统计service
 * @Date 2020/2/16 15:30
 */
public interface FrontEndSalesStatisticsDetailService {

    /**
     * 得到昨天销售统计（按门店 skuCode和category_id分组查询的）
     * @return
     */
    List<FrontEndSalesStatisticsDetail> selectYesterdaySalesStatistics();

    /**
     * 批量插入每日新增数据
     * @param list
     * @return
     */
    Integer insertSalesStatisticsDetailList(List<FrontEndSalesStatisticsDetail> list);

    /**
     * 查询某店某月份的前台销售统计数据
     * @param month  eg: 202001 2020年1月
     * @param storeId
     * @return
     */
    HttpResponse selectStoreMonthSaleStatisticsByMonthAndStoreId(String month,String storeId);

}
