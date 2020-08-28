package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.LogisticsRuleRequest;
import com.aiqin.mgs.order.api.domain.echoLogisticsRule;
import com.aiqin.mgs.order.api.domain.logisticsRule.LogisticsRuleInfo;
import com.aiqin.mgs.order.api.domain.LogisticsRuleInfoList;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewLogisticsRequest;
import com.aiqin.mgs.order.api.domain.response.LogisticsAllResponse;
import com.aiqin.mgs.order.api.util.ResultModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogisticsRuleService {
    //新增物流减免
    HttpResponse saveLogistics(LogisticsRuleInfoList logisticsRuleInfoList);
    //修改生效状态
    HttpResponse updateLogisticsStatus(LogisticsRuleInfo logisticsRuleInfo);
    //删除物流减免规则
    HttpResponse deleteLogistics(String rultCode);
    //回显物流减免规则
    HttpResponse<echoLogisticsRule> selectLogistics(String rultCode);
    //编辑物流减免规则
    HttpResponse updateLogistics(LogisticsRuleInfoList logisticsRuleInfoList);
    //多条件查询列表
    HttpResponse<ResultModel<LogisticsAllResponse>> selectLogisticsList(LogisticsRuleRequest logisticsRuleRequest);

    /**
     * 通过spuList查询规则
     * @param spuCodes
     * @return
     */
    HttpResponse<List<LogisticsAllResponse>> selectRuleBuSpuCode(List<String> spuCodes);
    //新规则-新增物流减免
    HttpResponse addNewLogisticsRule(NewLogisticsRequest newLogisticsRequest);
    //新规则-物流减免列表
    HttpResponse selectAll(Integer pageNo,Integer pageSize);
    //新规则-修改生效状态
    HttpResponse updateStatusByCode(String rultCode, String rultId,Integer effectiveStatus);
    //新规则-删除物流减免
    HttpResponse deleteLogisticsByCodeAndId(String rultCode, String rultId);
}
