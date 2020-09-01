package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.LogisticsRuleRequest;
import com.aiqin.mgs.order.api.domain.LogisticsRuleType;
import com.aiqin.mgs.order.api.domain.logisticsRule.LogisticsRuleInfo;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewAllLogistics;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewLogisticsInfo;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewReduceInfo;
import com.aiqin.mgs.order.api.domain.response.LogisticsAllResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogisticsRuleDao {

     void saveRule(LogisticsRuleType logisticsRuleType);

     void updateStatus(LogisticsRuleInfo logisticsRuleInfo);

     void deleteLogisticsRule( @Param("rultCode") String rultCode);

     List<LogisticsRuleInfo> getLogisticsRule(@Param("rultCode") String rultCode);

     void saveProduct(LogisticsRuleInfo logisticsRuleInfo1);

     void deleteLogisticsProduct(String rultCode);

     void updateSingle(LogisticsRuleInfo logisticsRuleInfo);

     void addProduct(@Param("newlyLogisticsRuleInfo") List<LogisticsRuleInfo> newlyLogisticsRuleInfo);

    void deleteProduct(@Param("delecteProductList") List<LogisticsRuleInfo> delecteProductList);

    void deleteAll(String rultCode);

    void updatefareSill(LogisticsRuleInfo logisticsRuleInfos);

    List<LogisticsAllResponse> selectAll(LogisticsRuleRequest logisticsRuleRequest);

    int addLogistics(NewLogisticsInfo newLogisticsInfo);

    int addLogisticsList(@Param("newReduceInfoList") List<NewReduceInfo> newReduceInfoList);

    List<NewAllLogistics> selectAllLogistics();

    int updateLogisticsStatus(NewAllLogistics newAllLogistics);

    int updateByCodeAndId(@Param("rultCode") String rultCode, @Param("rultId") String rultId);

    NewLogisticsInfo selecLogisticsInfo(@Param("rultCode") String rultCode, @Param("rultType") String rultType);

    List<NewReduceInfo> selectLogisticsDetail(String rultCode);

    int deleteOneLogistics(@Param("rultCode") String rultCode, @Param("rultType") Integer rultType);

    int deleteOnewLogisticsProduct(String rultCode);
}
