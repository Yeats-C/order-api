/*****************************************************************

* 模块名称：合伙人销售报表
* 开发人员: huangzy
* 开发时间: 2020-02-13 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaDetail;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaListReq;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleDetail;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaSave;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.NewStoreTreeResponse;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
import com.aiqin.mgs.order.api.domain.copartnerArea.SystemResource;
import com.aiqin.mgs.order.api.domain.request.*;
import com.aiqin.mgs.order.api.domain.request.returnorder.AreaReq;
import com.aiqin.mgs.order.api.domain.response.LatelyResponse;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.PartnerPayGateRep;
import com.aiqin.mgs.order.api.domain.response.ReportCopartnerSaleInfo;
import com.aiqin.mgs.order.api.jobs.ReportCopartnerSaleJob;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.service.ReportCopartnerSaleService;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/copartner/area")
@Api(tags = "合伙人销售报表")
@SuppressWarnings("all")
public class ReportCopartnerSaleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopartnerAreaController.class);
    
    @Resource
    private ReportCopartnerSaleService reportCopartnerSaleService;
    @Resource
    private ReportCopartnerSaleJob reportCopartnerSaleJob;
    
    
    @GetMapping("/qry/list")
    @ApiOperation(value = "合伙人销售报表-分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "Integer", paramType = "query", required = false)
    })
    public HttpResponse<ReportCopartnerSaleInfo> qryReportPageList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth,
            @RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {

    	LOGGER.info("合伙人销售报表-分页请求参数[[reportYear={},reportMonth={},pageNo={},pageSize={}",reportYear,reportMonth,pageNo,pageSize);

        return reportCopartnerSaleService.qryReportPageList(reportYear,reportMonth,pageNo,pageSize);
    }
    
    
    @GetMapping("/export")
    @ApiOperation(value = "合伙人销售报表-导出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "report_year", value = "年份", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "report_month", value = "月份", dataType = "String", paramType = "query", required = false)
    })
    public HttpResponse qryReportPageList(
            @RequestParam(value = "report_year", required = false) String reportYear,
            @RequestParam(value = "report_month", required = false) String reportMonth) {

    	LOGGER.info("合伙人销售报表-导出请求参数[[reportYear={},reportMonth={}",reportYear,reportMonth);

        return reportCopartnerSaleService.qryReportPageList(reportYear,reportMonth,0,100000);
    }
    
    
    @GetMapping("/run")
    @ApiOperation(value = "手动跑批")
    public HttpResponse qryReportPageList() {
    	reportCopartnerSaleJob.saveReportCopartnerSale();
        return HttpResponse.success();
    }
}
