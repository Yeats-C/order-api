package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.LogisticsRuleInfoList;
import com.aiqin.mgs.order.api.domain.LogisticsRuleRequest;
import com.aiqin.mgs.order.api.domain.echoLogisticsRule;
import com.aiqin.mgs.order.api.domain.logisticsRule.LogisticsRuleInfo;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewAllLogistics;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewLogisticsRequest;
import com.aiqin.mgs.order.api.domain.response.LogisticsAllResponse;
import com.aiqin.mgs.order.api.service.LogisticsRuleService;
import com.aiqin.mgs.order.api.util.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/selectRuleBuSpuCode")
    @ApiOperation("通过spuList查询规则")
    public HttpResponse<List<LogisticsAllResponse>> selectRuleBuSpuCode(@RequestBody List<String> spuCodes){
        return logisticsRuleService.selectRuleBuSpuCode(spuCodes);
    }


    @PostMapping("/add/new")
    @ApiOperation("新规则-物流减免规则")
    public HttpResponse addNewLogistics(@RequestBody NewLogisticsRequest newLogisticsRequest){
        return logisticsRuleService.addNewLogisticsRule(newLogisticsRequest);
    }

    @GetMapping("/getList")
    @ApiOperation("新规则-物流减免列表")
    public HttpResponse<List<NewAllLogistics>> selectList(Integer pageNo, Integer pageSize){
        return logisticsRuleService.selectAll(pageNo,pageSize);
    }

    @GetMapping("/update/status")
    @ApiOperation("新规则-修改生效状态")
    public HttpResponse updateStatus(@RequestParam(value = "rult_code") String rultCode,
                                     @RequestParam(value = "rult_id") String rultId,
                                     @RequestParam(value = "effective_status" ) Integer effectiveStatus){
        return logisticsRuleService.updateStatusByCode(rultCode,rultId,effectiveStatus);
    }

    @DeleteMapping("/delete")
    @ApiOperation("新规则-删除物流减免")
    public HttpResponse deleteLogistics(@RequestParam(value = "rult_code") String rultCode,
                                        @RequestParam(value = "rult_id") String rultId){
        return logisticsRuleService.deleteLogisticsByCodeAndId(rultCode,rultId);
    }


    @GetMapping("/select/logistics")
    @ApiOperation("新规则-物流减免详情")
    public HttpResponse<NewLogisticsRequest> selectLogistics(
                                        @RequestParam(value = "rult_code") String rultCode,
                                        @RequestParam(value = "rult_type") String rultType){
        return logisticsRuleService.selectLogisticsDetail(rultCode,rultType);

    }

    @PutMapping("/update/logistics")
    @ApiOperation("新规则-编辑物流减免")
    public HttpResponse updateLogistics(@RequestBody NewLogisticsRequest newLogisticsRequest){
        return logisticsRuleService.updateLogisticsByCode(newLogisticsRequest);
    }
}
