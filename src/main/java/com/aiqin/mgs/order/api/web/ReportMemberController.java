/*****************************************************************
* 模块名称：门店会员报表 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/qry/month/list")
    @ApiOperation(value = "门店会员概况-月报")
    public HttpResponse<ReportMemberVo> qryReportMonthPageList(
    		@RequestParam(value = "stat_year", required = true) Integer statYear,
    		@RequestParam(value = "stat_month", required = true) Integer statMonth,
    		@RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {
    	
        return service.qryReportMonthPageList(statYear,statMonth,pageNo,pageSize);
    }
    
    @GetMapping("/qry/year/list")
    @ApiOperation(value = "门店会员概况-年报")
    public HttpResponse<ReportMemberVo> qryReportYearList(
    		@RequestParam(value = "stat_year", required = true) Integer statYear,
    		@RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {
    	
        return service.qryReportYearList(statYear,pageNo,pageSize);
    }
    
    
    @GetMapping("/qry/sale/month/list")
    @ApiOperation(value = "门店会员消费-月报")
    public HttpResponse<ReportMemberSaleVo> qrySaleMonthList(
    		@RequestParam(value = "stat_year_month", required = true) String statYearMonth,
    		@RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {
    	
        return service.qrySaleMonthList(statYearMonth,pageNo,pageSize);
    }
    
    @GetMapping("/qry/sale/year/list")
    @ApiOperation(value = "门店会员消费-年报")
    public HttpResponse<ReportMemberSaleVo> qrySaleYearList(
    		@RequestParam(value = "stat_year", required = true) String statYear,	
    		@RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {
    	
        return service.qrySaleYearList(statYear,pageNo,pageSize);
    }
    
    
    
}