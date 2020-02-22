package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.FirstReportInfo;
import com.aiqin.mgs.order.api.service.FirstReportService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "首单报表")
@RequestMapping("/firstReport")
public class FirstReportController {

    @Autowired
    private FirstReportService firstReportService;

    /**
     * 首单报表定时任务
     * @return
     */
    @ApiOperation("首单报表定时任务")
    @PostMapping("/first")
    public HttpResponse reportTimedTask(){
        firstReportService.reportTimedTask();
        return HttpResponse.success();
    }

    @ApiOperation("获取首单报表表格数据")
    @GetMapping("/gitList")
    public HttpResponse<List<FirstReportInfo>> getList(@RequestParam(value = "report_time") String reportTime,
                                                       @RequestParam(value = "page_no", required = false) Integer pageNo,
                                                       @RequestParam(value = "page_size", required = false) Integer pageSize){
        return firstReportService.getLists(reportTime,pageNo,pageSize);
    }
}
