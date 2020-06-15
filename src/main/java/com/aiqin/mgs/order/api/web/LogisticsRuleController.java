package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.LogisticsRuleInfoList;
import com.aiqin.mgs.order.api.domain.LogisticsRuleRequest;
import com.aiqin.mgs.order.api.domain.echoLogisticsRule;
import com.aiqin.mgs.order.api.domain.logisticsRule.LogisticsRuleInfo;
import com.aiqin.mgs.order.api.domain.response.LogisticsAllResponse;
import com.aiqin.mgs.order.api.service.LogisticsRuleService;
import com.aiqin.mgs.order.api.util.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logistics/rule")
@Api(tags = "物流减免规则")
public class LogisticsRuleController {

    @Autowired
    private LogisticsRuleService logisticsRuleService;

    @PostMapping("/save")
    @ApiOperation(value = "新增物流减免")
    public HttpResponse saveLogisticsRule(@RequestBody LogisticsRuleInfoList logisticsRuleInfoList){
        return logisticsRuleService.saveLogistics(logisticsRuleInfoList);
    }

    @PutMapping("/update/status")
    @ApiOperation(value = "更改物流减免生效状态")
    public HttpResponse updateLogisticsStatus(@RequestBody LogisticsRuleInfo logisticsRuleInfo){
        return  logisticsRuleService.updateLogisticsStatus(logisticsRuleInfo);
    }

    @GetMapping("/delete")
    @ApiOperation("删除物流减免规则")
    public HttpResponse deleteLogisticsRule(@RequestParam(value = "rult_code") String rultCode){
        return logisticsRuleService.deleteLogistics(rultCode);
    }

    @GetMapping("/get/logistics")
    @ApiOperation("回显物流免减规则")
    public HttpResponse<echoLogisticsRule> selectLogisticsRule(@RequestParam(value = "rult_code") String rultCode){
        return logisticsRuleService.selectLogistics(rultCode);
    }

    @PostMapping("/update/logistics")
    @ApiOperation("编辑物流免减规则")
    public HttpResponse updateLogisticsRule(@RequestBody LogisticsRuleInfoList logisticsRuleInfoList ){
        return  logisticsRuleService.updateLogistics(logisticsRuleInfoList);
    }

    @PostMapping("/select/logistics")
    @ApiOperation("多条件查询列表")
    public HttpResponse<ResultModel<LogisticsAllResponse>> selectLogistics(@RequestBody LogisticsRuleRequest logisticsRuleRequest){
        return logisticsRuleService.selectLogisticsList(logisticsRuleRequest);
    }
}
