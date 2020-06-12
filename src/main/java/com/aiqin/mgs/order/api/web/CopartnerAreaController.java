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
import com.aiqin.mgs.order.api.domain.copartnerArea.*;
import com.aiqin.mgs.order.api.domain.request.*;
import com.aiqin.mgs.order.api.domain.request.returnorder.AreaReq;
import com.aiqin.mgs.order.api.domain.response.LatelyResponse;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.PartnerPayGateRep;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    
    
    @GetMapping("/list")
    @ApiOperation(value = "经营区域列表-分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "copartner_area_name", value = "经营区域名称", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "copartner_area_company", value = "管理归属", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "copartner_area_level", value = "管理层级 1:一级 2：二级", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_no", value = "当前页", dataType = "Integer", paramType = "query", required = false),
        @ApiImplicitParam(name = "page_size", value = "每页条数", dataType = "Integer", paramType = "query", required = false)
    })
    public HttpResponse<CopartnerAreaList> copartnerAreaList(
            @RequestParam(value = "copartner_area_name", required = false) String copartnerAreaName,
            @RequestParam(value = "copartner_area_company", required = false) String copartnerAreaCompany,
            @RequestParam(value = "copartner_area_level", required = false) Integer copartnerAreaLevel,
            @RequestParam(value = "page_no", required = false) Integer pageNo,
            @RequestParam(value = "page_size", required = false) Integer pageSize) {

        LOGGER.info("经营区域列表-分页请求参数[[copartnerAreaName={},copartnerAreaCompany={},copartnerAreaLevel={},pageNo={},pageSize={}",copartnerAreaName,copartnerAreaCompany,copartnerAreaLevel,pageNo,pageSize);

        CopartnerAreaVo vo = new CopartnerAreaVo();
        vo.setCopartnerAreaName(copartnerAreaName);
        vo.setCopartnerAreaCompany(copartnerAreaCompany);
        vo.setCopartnerAreaLevel(copartnerAreaLevel);
        vo.setPageNo(pageNo);
        vo.setPageSize(pageSize);
        return copartnerAreaService.copartnerAreaList(vo);
    }
    
    
    @GetMapping("/list/up")
    @ApiOperation(value = "新建页面-查询上级合伙人接口")
    public HttpResponse<CopartnerAreaUp> copartnerAreaUp(){    
    	return copartnerAreaService.copartnerAreaUp();
    }
    
    
//    @GetMapping("/store/tree")
//    @ApiOperation(value = "新建页面-所辖门店树弹框")
//    public HttpResponse storeTree(){    
//    	//TODO
//    	return null;
//    }
    
    @GetMapping("/person/list")
    @ApiOperation(value = "新建页面-选择公司负责人、选择公司人员")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "person_team", value = "人员编码名称组合查询", dataType = "String", required = true)
    })
    public HttpResponse<CopartnerAreaRoleList> getPersonList(@Valid @RequestParam(name = "person_team", required = true) String personTeam){
    	return copartnerAreaService.getPersonList(personTeam);
    }
    
    
    @GetMapping("/role/detail")
    @ApiOperation(value = "权限树")
    @ApiImplicitParams({
    })
    public HttpResponse<CopartnerAreaRoleDetail> roledetail(){
    	return copartnerAreaService.roledetail(null,null);
    }
    
    @GetMapping("/role/detail/exist")
    @ApiOperation(value = "根据区域公司人员查询权限树")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "copartner_area_id", value = "经营区域ID", dataType = "String", required = true),
        @ApiImplicitParam(name = "company_person_id", value = "公司人员编码", dataType = "String", required = true)
    })
    public HttpResponse<CopartnerAreaRoleDetail> roledetail(
    		@Valid @RequestParam(name = "copartner_area_id", required = true) String copartnerAreaId,
    		@Valid @RequestParam(name = "company_person_id", required = true) String personId){
    	return copartnerAreaService.roledetail(copartnerAreaId,personId);
    }
    
    
    
    @PostMapping("/save")
    @ApiOperation(value = "新建/修改区域-保存")
    public HttpResponse saveCopartnerArea(@Valid @RequestBody CopartnerAreaSave param){
    	return copartnerAreaService.saveCopartnerArea(param);
    }
    
    
    @GetMapping("/detail/info")
    @ApiOperation(value = "经营区域详情基本信息")
    public HttpResponse<CopartnerAreaDetail> getCopartnerAreaDetail(
    		@Valid @RequestParam(name = "copartner_area_id", required = true) String copartnerAreaId){
        
    	LOGGER.info("经营区域详情基本信息请求参数：{}",copartnerAreaId);
        
    	return copartnerAreaService.getCopartnerAreaDetail(copartnerAreaId);
    }
    
    
    @GetMapping("/detail/store")
    @ApiOperation(value = "经营区域详情-门店列表")
    public HttpResponse<CopartnerAreaStoreList> getCopartnerAreaStore(
    		@Valid @RequestParam(name = "copartner_area_id", required = true) String copartnerAreaId){
        
    	LOGGER.info("经营区域详情基本信息请求参数：copartnerAreaId={}",copartnerAreaId);
    	return copartnerAreaService.getCopartnerAreaStore(copartnerAreaId);
    }
    
    
    @GetMapping("/detail/role")
    @ApiOperation(value = "经营区域详情-权限列表")
    public HttpResponse<CopartnerAreaRoleList> getCopartnerAreaRole(
    		@Valid @RequestParam(name = "copartner_area_id", required = true) String copartnerAreaId){
        
    	LOGGER.info("经营区域详情基本信息请求参数：copartnerAreaId={}",copartnerAreaId);
    	return copartnerAreaService.getCopartnerAreaRole(copartnerAreaId);
    }

    @GetMapping("/detail/down/company")
    @ApiOperation(value ="经营区域详情-下辖公司")
    public HttpResponse<CopartnerAreaDetail> getCopartnerAreaDownCompany(@RequestParam(value = "copartner_area_id",required = true)
                                                    String copartnerAreaId){
        return copartnerAreaService.getCopartnerAreaCompany(copartnerAreaId);
    }

    
    @GetMapping("/delete")
    @ApiOperation(value = "删除区域设置")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "copartner_area_id", value = "经营区域ID", dataType = "String", required = true),
    })
    public HttpResponse deleteMainById(
            @RequestParam(value = "copartner_area_id", required = true) String copartnerAreaId
            ) {

    	LOGGER.info("删除区域设置请求参数[[copartnerAreaId={}",copartnerAreaId);

        return copartnerAreaService.deleteMainById(copartnerAreaId);
    }
    
    
    @GetMapping("/store/by/person")
    @ApiOperation(value = "合伙人数据权限控制公共接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "person_id", value = "人员编码", dataType = "String", required = true),
        @ApiImplicitParam(name = "resource_code", value = "菜单编码", dataType = "String", required = true)
    })
    public HttpResponse<PublicAreaStore> selectStoreByPerson(
            @RequestParam(value = "person_id", required = true) String personId,
            @RequestParam(value = "resource_code", required = true) String resourceCode
            ) {

    	LOGGER.info("合伙人数据权限控制公共接口请求参数[[personId={},resourceCode={}",personId,resourceCode);

        return copartnerAreaService.selectStoreByPerson(personId,resourceCode);
    }
    
    
    @GetMapping("/no-authority/type/{area_type}")
    @ApiOperation(value = "根据类型查询区域", notes = "根据类型查询区域")
    public HttpResponse areaTypeInfo(@PathVariable(name = "area_type") Integer areaType) {
        return copartnerAreaService.areaTypeInfo(areaType);
    }
    
    @GetMapping("/no-authority/{parent_id}")
    @ApiOperation(value = "根据上级编码查询所有的子集", notes = "根据上级编码查询所有的子集")
    public HttpResponse childrenInfo(@PathVariable(name = "parent_id") String parentId) {
        return copartnerAreaService.childrenInfo(parentId);
    }
    
    /**
     * 促销活动--根据省市区编码查询所有门店
     */
    @PostMapping("/getStoresByAreaCode")
    @ApiOperation("根据省市区编码查询所有门店")
    public HttpResponse<List<NewStoreTreeResponse>> getStoresByAreaCode(@RequestBody AreaReq areaReq) {
        return copartnerAreaService.getStoresByAreaCode(areaReq);
    }

    /**
     * 促销活动--根据门店名称或编码模糊查询门店
     */
    @GetMapping("/getStoresByCodeOrName")
    @ApiOperation("根据门店名称或编码模糊查询门店")
    public HttpResponse<List<NewStoreTreeResponse>> getStoresByCodeOrName(
    		@RequestParam(value = "parm", required = true) String parm) {
        return copartnerAreaService.getStoresByCodeOrName(parm);
    }
    
    /**
     * 对外接口
     * @return
     */
    @GetMapping("/out/list")
    @ApiOperation(value = "对外接口-查询区域列表")
    public HttpResponse<CopartnerAreaUp> qryCopartnerAreaList(){    
    	return copartnerAreaService.qryCopartnerAreaList();
    }
    
    /**
     * 对外接口
     * @return
     */
    @GetMapping("/out/list/by/person")
    @ApiOperation(value = "对外接口-根据人员编码查询区域列表")
    public HttpResponse<CopartnerAreaUp> qryCopartnerAreaListById(
    		@RequestParam(value = "person_id", required = true) String personId){    
    	return copartnerAreaService.qryCopartnerAreaListBypersonId(personId);
    }
    
    
    
    /**
     * 保存门店与区域的对应关系
     */
    @PostMapping("/out/save/store")
    @ApiOperation("对外接口-保存门店与区域的对应关系")
    public HttpResponse saveAreaStore(@RequestBody CopartnerAreaStoreVo param) {
        return copartnerAreaService.saveAreaStore(param);
    }


    /**
     * 新建区域-下辖公司-新增二级公司-查询二级公司
     */
    @GetMapping("/get/two/company")
    @ApiOperation("新建区域-下辖公司-新增-查询二级公司")
    public HttpResponse<CopartnerAreaDetail> saveTwoCompany(@RequestParam(value = "copartner_area_company") String copartnerAreaCompany){
        return copartnerAreaService.selcetCompany(copartnerAreaCompany);
    }

    /**
     * 新建区域-下辖公司-删除
     */
    @PostMapping("/delect/two/company")
    @ApiOperation("新建区域-下辖公司-删除")
    public HttpResponse delectCompany(@RequestBody List<String> copartnerAreaIds ){
        return copartnerAreaService.delectTwoCompany(copartnerAreaIds);
    }
}
