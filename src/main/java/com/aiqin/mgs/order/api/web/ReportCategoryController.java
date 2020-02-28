/*****************************************************************
* 模块名称：品类报表 
* 开发人员: huangzy
* 开发时间: Mon Feb 24 10:04:13 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.ReportCategoryVo;
import com.aiqin.mgs.order.api.domain.response.ReportCopartnerSaleInfo;
import com.aiqin.mgs.order.api.jobs.ReportCategorySaleJob;
import com.aiqin.mgs.order.api.jobs.ReportCopartnerSaleJob;
import com.aiqin.mgs.order.api.service.*;

@RestController

@RequestMapping("/category/area")
@Api(tags = "品类报表")
@SuppressWarnings("all")
public class ReportCategoryController {

    @Autowired
    private ReportCategoryService service;
    
    @Resource
    private ReportCategorySaleJob reportCategorySaleJob;


    @GetMapping("/qry/list")
    @ApiOperation(value = "品类报表-分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "Integer", paramType = "query", required = false)
    })
    public HttpResponse<ReportCategoryVo> qryReportPageList(
            @RequestParam(value = "report_year", required = true) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth,
            @RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {

        return service.qryReportPageList(reportYear,reportMonth,pageNo,pageSize);
    }
    
    
    @GetMapping("/export")
    @ApiOperation(value = "品类报表-导出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false)
    })
    public HttpResponse qryReportPageList(
            @RequestParam(value = "report_year", required = true) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth) {

        return service.qryReportPageList(reportYear,reportMonth,0,100000);
    }
    
    
    @GetMapping("/run")
    @ApiOperation(value = "手动跑批")
    public HttpResponse qryReportPageList() {
    	reportCategorySaleJob.saveReportCategorySale();
        return HttpResponse.success();
    }
}