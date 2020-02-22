package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.service.FirstReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "首单报表")
@RequestMapping("/firstReport")
public class FirstReportController {

    @Autowired
    private FirstReportService firstReportService;

    @ApiOperation("首单报表定时任务")
    @PostMapping("/first")
    public HttpResponse reportTimedTask(){
        firstReportService.reportTimedTask();
        return HttpResponse.success();
    }


}
