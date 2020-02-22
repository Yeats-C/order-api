/*****************************************************************
* 模块名称：门店会员报表 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.aiqin.mgs.order.api.domain.ReportMemberSaleCateVo;
import com.aiqin.mgs.order.api.domain.ReportMemberSaleVo;
import com.aiqin.mgs.order.api.domain.ReportMemberVo;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.service.impl.ReportMemberServiceImpl;

@RestController

@RequestMapping("/report/member")
@Api(tags = "门店会员报表")
@SuppressWarnings("all")
public class ReportMemberController {
	
	private static final Logger log = LoggerFactory.getLogger(ReportMemberServiceImpl.class);

    @Autowired
    private ReportMemberService service;

    @GetMapping("/qry/list")
    @ApiOperation(value = "门店会员概况-分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "Integer", paramType = "query", required = false)
    })
    public HttpResponse<ReportMemberVo> qryReportPageList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth,
            @RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {

    	log.info("门店会员概况-分页请求参数[[reportYear={},reportMonth={},pageNo={},pageSize={}",reportYear,reportMonth,pageNo,pageSize);

        return service.qryReportPageList(reportYear,reportMonth,pageNo,pageSize);
    }
    
    
    @GetMapping("/export")
    @ApiOperation(value = "门店会员概况-导出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false)
    })
    public HttpResponse exportReportList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth) {

    	log.info("门店会员概况-导出请求参数[[reportYear={},reportMonth={}",reportYear,reportMonth);

        return service.qryReportPageList(reportYear,reportMonth,0,100000);
    }
    
    
    @GetMapping("/qry/list/sale")
    @ApiOperation(value = "门店会员消费报表-分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "Integer", paramType = "query", required = false)
    })
    public HttpResponse<ReportMemberSaleVo> qryReportSalePageList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth,
            @RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {

    	log.info("门店会员消费报表-分页请求参数[[reportYear={},reportMonth={},pageNo={},pageSize={}",reportYear,reportMonth,pageNo,pageSize);

        return service.qryReportSalePageList(reportYear,reportMonth,pageNo,pageSize);
    }
    
    
    @GetMapping("/export/sale")
    @ApiOperation(value = "门店会员消费报表-导出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false)
    })
    public HttpResponse exportReportSaleList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth) {

    	log.info("门店会员消费报表-导出请求参数[[reportYear={},reportMonth={}",reportYear,reportMonth);

        return service.qryReportSalePageList(reportYear,reportMonth,0,100000);
    }
    
    
    @GetMapping("/qry/list/sale/categoryp")
    @ApiOperation(value = "门店会员品类消费报表(人数)-分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "Integer", paramType = "query", required = false)
    })
    public HttpResponse<ReportMemberSaleCateVo> qryReportSaleCatepPageList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth,
            @RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {

    	log.info("门店会员品类消费报表(人数)-分页请求参数[[reportYear={},reportMonth={},pageNo={},pageSize={}",reportYear,reportMonth,pageNo,pageSize);

        return service.qryReportSaleCatepPageList(reportYear,reportMonth,pageNo,pageSize);
    }
    
    
    @GetMapping("/export/sale/categoryp")
    @ApiOperation(value = "门店会员品类消费报表(人数)-导出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false)
    })
    public HttpResponse exportReportSaleCatepList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth) {

    	log.info("门店会员品类消费报表(人数)-导出请求参数[[reportYear={},reportMonth={}",reportYear,reportMonth);

        return service.qryReportSaleCatepPageList(reportYear,reportMonth,0,100000);
    }
    
    
    @GetMapping("/qry/list/sale/categorym")
    @ApiOperation(value = "门店会员品类消费报表(金额)-分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "Integer", paramType = "query", required = false)
    })
    public HttpResponse<ReportMemberSaleVo> qryReportSaleCatemPageList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth,
            @RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {

    	log.info("门店会员消费报表-分页请求参数[[reportYear={},reportMonth={},pageNo={},pageSize={}",reportYear,reportMonth,pageNo,pageSize);

        return service.qryReportSaleCatemPageList(reportYear,reportMonth,pageNo,pageSize);
    }
    
    
    @GetMapping("/export/sale/categorym")
    @ApiOperation(value = "门店会员消费报表-导出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false)
    })
    public HttpResponse exportReportSaleCatemList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth) {

    	log.info("门店会员消费报表-导出请求参数[[reportYear={},reportMonth={}",reportYear,reportMonth);

        return service.qryReportSaleCatemPageList(reportYear,reportMonth,0,100000);
    }
    
    
    
}