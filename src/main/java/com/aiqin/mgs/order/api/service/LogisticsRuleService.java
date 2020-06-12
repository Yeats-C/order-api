package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.LogisticsRuleRequest;
import com.aiqin.mgs.order.api.domain.echoLogisticsRule;
import com.aiqin.mgs.order.api.domain.logisticsRule.LogisticsRuleInfo;
import com.aiqin.mgs.order.api.domain.LogisticsRuleInfoList;
import com.aiqin.mgs.order.api.util.ResultModel;

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
    HttpResponse<ResultModel> selectLogisticsList(LogisticsRuleRequest logisticsRuleRequest);
}
