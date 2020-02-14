/*****************************************************************

* 模块名称：经营区域设置
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
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaListReq;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.request.*;
import com.aiqin.mgs.order.api.domain.response.LatelyResponse;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.PartnerPayGateRep;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
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
@Api(tags = "经营区域设置")
@SuppressWarnings("all")
public class CopartnerAreaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopartnerAreaController.class);
    
    @Resource
    private CopartnerAreaService copartnerAreaService;
    
    
    @PostMapping("/list")
    @ApiOperation(value = "经营区域列表-分页")
    public HttpResponse<CopartnerAreaList> copartnerAreaList(@Valid @RequestBody CopartnerAreaListReq param){
        
    	LOGGER.info("经营区域列表请求参数：{}",param);
        
    	return copartnerAreaService.copartnerAreaList(param);
    }
    
    
    @GetMapping("/list/up")
    @ApiOperation(value = "查询上级合伙人")
    public HttpResponse<CopartnerAreaUp> copartnerAreaUp(){    
    	return copartnerAreaService.copartnerAreaUp();
    }
    
    
    @GetMapping("/store/tree")
    @ApiOperation(value = "所辖门店树")
    public HttpResponse storeTree(){    
    	//TODO
    	return null;
    }
    
    @GetMapping("/person/list")
    @ApiOperation(value = "选择公司人员")
    public HttpResponse<CopartnerAreaRoleList> getPersonList(@Valid @RequestParam(name = "person_team", required = true) String personTeam){
    	return copartnerAreaService.getPersonList(personTeam);
    }
    
    @PostMapping("/role/list")
    @ApiOperation(value = "公司人员查询权限列表")
    public HttpResponse getRoleList(@Valid @RequestBody List<CopartnerAreaRoleList> param){
    	return copartnerAreaService.getRoleList(param);
    }
    
    @GetMapping("/role/detail")
    @ApiOperation(value = "权限详情")
    public HttpResponse roledetail(@Valid @RequestParam(name = "copartner_area_id", required = false) String copartnerAreaId,
    		@Valid @RequestParam(name = "person_id", required = false) String personId){
    	return copartnerAreaService.roledetail(copartnerAreaId,personId);
    }
    
    
    
    
    
    
    
    
    
    @GetMapping("/detail/info")
    @ApiOperation(value = "经营区域详情基本信息")
    public HttpResponse getCopartnerAreaDetail(
    		@Valid @RequestParam(name = "copartner_area_id", required = true) String copartnerAreaId){
        
    	LOGGER.info("经营区域详情基本信息请求参数：{}",copartnerAreaId);
        
    	return copartnerAreaService.getCopartnerAreaDetail(copartnerAreaId);
    }
    
    
    @GetMapping("/detail/store")
    @ApiOperation(value = "经营区域详情-门店列表分页")
    public HttpResponse getCopartnerAreaStore(
    		@Valid @RequestParam(name = "copartner_area_id", required = true) String copartnerAreaId,
    		@RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize){
        
    	LOGGER.info("经营区域详情基本信息请求参数：copartnerAreaId={},pageNo={},pageSize={}",copartnerAreaId,pageNo,pageSize);
    	CopartnerAreaStoreVo vo = new CopartnerAreaStoreVo();
    	vo.setCopartnerAreaId(copartnerAreaId);
    	vo.setPageNo(pageNo);
    	vo.setPageSize(pageSize);
    	return copartnerAreaService.getCopartnerAreaStore(vo);
    }
    
    
    @GetMapping("/detail/role")
    @ApiOperation(value = "经营区域详情-权限列表分页")
    public HttpResponse getCopartnerAreaRole(
    		@Valid @RequestParam(name = "copartner_area_id", required = true) String copartnerAreaId,
    		@RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize){
        
    	LOGGER.info("经营区域详情基本信息请求参数：copartnerAreaId={},pageNo={},pageSize={}",copartnerAreaId,pageNo,pageSize);
    	CopartnerAreaRoleVo vo = new CopartnerAreaRoleVo();
    	vo.setCopartnerAreaId(copartnerAreaId);
    	vo.setPageNo(pageNo);
    	vo.setPageSize(pageSize);
    	return copartnerAreaService.getCopartnerAreaRole(vo);
    }
    
    
}
