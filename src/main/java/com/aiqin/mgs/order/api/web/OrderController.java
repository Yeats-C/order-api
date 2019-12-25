/*****************************************************************

* 模块名称：订单明细后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.*;
import com.aiqin.mgs.order.api.domain.response.LatelyResponse;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.PartnerPayGateRep;
import com.aiqin.mgs.order.api.service.CartService;
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
//import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
@Api("订单相关操作接口")
@SuppressWarnings("all")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    
    @Resource
    private OrderService orderService;
    
    
    /**
     * 普通订单
     * 门店新增TOC订单step1-添加订单主数据+添加订单明细数据+返回订单编号
     * @param 
     * @return
     */
    @PostMapping("/addordta")
    @ApiOperation(value = "门店新增TOC订单step1-添加订单主数据+添加订单明细数据+返回订单编号")
    public HttpResponse addOrdta(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        
    	
    	LOGGER.info("添加新的订单主数据以及其他订单关联数据参数：{}",orderAndSoOnRequest);
        
    	//添加TOC订单标识
        if(orderAndSoOnRequest !=null) {
            //返回订单信息
        	OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
          if(orderInfo !=null) {
              if(orderInfo.getOrderType()==4){
                  orderInfo.setOrderType(Global.ORDER_TYPE_4);
              }else {
                  orderInfo.setOrderType(Global.ORDER_TYPE_1);
              }

        	  orderAndSoOnRequest.setOrderInfo(orderInfo);
        	  return orderService.addOrdta(orderAndSoOnRequest);
          }else {
        	  LOGGER.warn("添加新的订单主数据以及其他订单关联数据为null：{}",orderAndSoOnRequest);
        	  return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
          }
        }else {
        	LOGGER.warn("添加新的订单主数据以及其他订单关联数据为null：{}",orderAndSoOnRequest);
        	return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
    }
    
    
    /**
     * 门店新增TOC订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据
     * 现金结算，创建订单信息
     * @param
     * @return
     */
    @PostMapping("/addpamo")
    @ApiOperation(value = "门店新增TOC订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据")
    public HttpResponse addPamo(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        
    	
    	LOGGER.info("门店新增TOC订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据参数：{}",orderAndSoOnRequest);
		
        //添加TOC订单标识
        if(orderAndSoOnRequest !=null) {
        	OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
          if(orderInfo !=null) {
              //预存订单
              if (orderInfo.getOrderType()!=4){
                  orderInfo.setOrderType(Global.ORDER_TYPE_1);
              }

              if (orderInfo.getOrderType()!=4){
                  orderInfo.setOrderType(Global.ORDER_TYPE_1);
              }


        	  orderAndSoOnRequest.setOrderInfo(orderInfo);
        	  return orderService.addPamo(orderAndSoOnRequest);
          }else {
        	  LOGGER.warn("门店新增TOC订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据为null：{}",orderAndSoOnRequest);
        	  return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
          }
        }else {
        	LOGGER.warn("门店新增TOC订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据为null：{}",orderAndSoOnRequest);
        	return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
    }
    
    
    /**
     * 门店新增TOC订单step3-已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)
     * @param orderAndSoOnRequest
     * @return
     */
    @PostMapping("/repast")
    @ApiOperation(value = "门店新增TOC订单step3-已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)")
    public HttpResponse repast(@Valid @RequestParam(name = "order_id", required = true) String orderId,
    		@Valid @RequestParam(name = "pay_type", required = true) String payType,
    		@Valid @RequestParam(name = "order_status", required = true) Integer orderStatus,
    		@Valid @RequestBody(required = true) List<OrderPayInfo> orderPayList
    		) {
    	
    	
        LOGGER.info("已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)参数orderId:{},payType:{},orderPayList:{}",orderId,payType,orderPayList);
        return orderService.repast(orderId,payType,orderStatus,orderPayList);
    }
    
    
    /**
     * 门店新增服务订单step1-添加订单主数据+添加订单明细数据+返回订单编号
     * @param 
     * @return
     */
    @PostMapping("/zaao")
    @ApiOperation(value = "门店新增服务订单step1-添加订单主数据+添加订单明细数据+返回订单编号")
    public HttpResponse zaao(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        
    	
    	LOGGER.info("门店新增服务订单step1-添加订单主数据+添加订单明细数据+返回订单编号参数：{}",orderAndSoOnRequest);
		
        //添加TOC订单标识
        if(orderAndSoOnRequest !=null) {
        	OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
          if(orderInfo !=null) {
        	  orderInfo.setOrderType(Global.ORDER_TYPE_3);
        	  orderAndSoOnRequest.setOrderInfo(orderInfo);
        	  return orderService.addOrdta(orderAndSoOnRequest);
          }else {
        	  LOGGER.warn("门店新增服务订单step1-订单主数据为null：{}",orderAndSoOnRequest);
        	  return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
          }
        }else {
        	LOGGER.warn("门店新增服务订单step1-参数为null：{}",orderAndSoOnRequest);
        	return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
    }
    
    
    /**
     * 门店新增服务订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据
     * @param 
     * @return
     */
    @PostMapping("/zabo")
    @ApiOperation(value = "门店新增TOC订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据")
    public HttpResponse zabo(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        
    	
    	LOGGER.info("门店新增服务订单step2-添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据参数：{}",orderAndSoOnRequest);


        //添加TOC订单标识
        if(orderAndSoOnRequest !=null) {
        	OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
          if(orderInfo !=null) {
              //判断是不是预存订单
              if (orderAndSoOnRequest.getOrderInfo().getOrderType()==4){
                  orderInfo.setOrderType(Global.ORDER_TYPE_4);
              }else {
                  orderInfo.setOrderType(Global.ORDER_TYPE_3);
              }
        	  orderAndSoOnRequest.setOrderInfo(orderInfo);
        	  return orderService.addPamo(orderAndSoOnRequest);
          }else {
        	  LOGGER.warn("门店新增TOC订单step2-订单主数据为null：{}",orderAndSoOnRequest);
        	  return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
          }
        }else {
        	LOGGER.warn("门店新增TOC订单step2-数据为null：{}",orderAndSoOnRequest);
        	return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
    }
    
    
    /**
     * 删除订单主数据+删除订单明细数据
     * @param 
     * @return
     */
    @GetMapping("/deordta")
    @ApiOperation(value = "删除订单主数据+删除订单明细数据")
    public HttpResponse deordta(@Valid @RequestParam(name = "order_id", required = true) String orderId){
        
    	
    	LOGGER.info("删除订单主数据+删除订单明细数据参数orderId: {}",orderId);
        return orderService.deordta(orderId);
    }
    
    
    /**
     * 模糊查询订单列表
     * @param 
     * @return
     */
    @PostMapping("/selectorder")
    @ApiOperation(value = "查询订单列表....")
    public HttpResponse selectOrder(@Valid @RequestBody OrderQuery orderQuery) {
        
    	
    	LOGGER.info("查询订单列表参数：{}",orderQuery);    	
        return orderService.selectOrder(orderQuery);
    }
    
    
    /**
     * 导出订单列表
     * @param 
     * @return
     */
    @PostMapping("/exorder")
    @ApiOperation(value = "导出订单列表....")
    public HttpResponse exorder(@Valid @RequestBody OrderQuery orderQuery) {
        
    	
    	LOGGER.info("导出订单列表参数：{}",orderQuery);    	
        return orderService.exorder(orderQuery);
    }
    
    
    /**
     * 模糊查询订单列表+订单中商品sku数量
     * @param 
     * @return
     */
    @PostMapping("/oradsku")
    @ApiOperation(value = "模糊查询订单列表+订单中商品sku数量....")
    public HttpResponse oradsku(@Valid @RequestBody OrderQuery orderQuery) {
        
    	
    	LOGGER.info("模糊查询订单列表+订单中商品sku数量参数：{}",orderQuery);    	
        return orderService.oradsku(orderQuery);
    }
    
 
    /**
     * 微商城新增TOC订单-添加新的订单主数据以及其他订单关联数据
     * @param orderAndSoOnRequest
     * @return
     * @throws Exception 
     */
    @PostMapping("")
    @ApiOperation(value = "微商城新增TOC订单-添加新的订单主数据以及其他订单关联数据")
    public HttpResponse addOrderList(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        
    	
    	LOGGER.info("添加新的订单主数据以及其他订单关联数据参数：{}",orderAndSoOnRequest);
        return orderService.addOrderList(orderAndSoOnRequest);
    }
    
    
    /**
     * 查询订单日志数据
     * @param logInfo
     * @param orderId
     * @return
     */
    @PostMapping("/orog")
    @ApiOperation(value = "查询订单日志数据")
    public HttpResponse orog(@Valid @RequestParam(name = "order_id", required = true) String orderId) {
        
    	
    	LOGGER.info("查询订单日志数据参数orderId：{}",orderId);
        return orderService.orog(orderId);
    }
     
    
    /**
     * 概览菜单-分销机构+当月维度-总销售额\当月销售额、当月实收、当月支付订单数
     * @param 
     * @return
     */
    @PostMapping("/selectorderbyoverview")
    @ApiOperation(value = "概览菜单-分销机构+当月维度-总销售额\\当月销售额、当月实收、当月支付订单数 ....")
    public HttpResponse selectorderbyoverview(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
    		@Valid @RequestBody List<Integer> originTypeList) {
        
    	
    	LOGGER.info("概览菜单-分销机构+当月维度-总销售额当月销售额、当月实收、当月支付订单数参数distributorId :{},originTypeList: {}",distributorId,originTypeList);
        return orderService.selectorderbymonth(distributorId,originTypeList);
    } 
    
    
    /**
     * 概览菜单-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量
     * @param 
     * @return
     */
    @PostMapping("/selectorderbyninedate")
    @ApiOperation(value = "概览菜单-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量....")
    public HttpResponse selectOrderByNineDate(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
    		@Valid @RequestBody List<Integer> originTypeList) {
        
    	
    	LOGGER.info("概览菜单-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量参数distributorId: {},originTypeList: {}",distributorId,originTypeList);    	
        return orderService.selectOrderByNineDate(distributorId,originTypeList);
    } 
    
    
    /**
     * 概览菜单-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量
     * @param 
     * @return
     */
    @PostMapping("/selectorderbynineweek")
    @ApiOperation(value = "概览菜单-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量....")
    public HttpResponse selectOrderByNineWeek(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
    		@Valid @RequestBody List<Integer> originTypeList) {
        
    	
    	LOGGER.info("概览菜单-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量参数 distributorId: {},originTypeList: {}",distributorId,originTypeList);    	
        return orderService.selectOrderByNineWeek(distributorId,originTypeList);
    } 
    
    
    /**
     * 概览菜单-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量
     * @param 
     * @return
     */
    @PostMapping("/selectorderbyninemonth")
    @ApiOperation(value = "概览菜单-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量....")
    public HttpResponse selectOrderByNineMonth(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
    		@Valid @RequestBody List<Integer> originTypeList) {
        
    	
    	LOGGER.info("概览菜单-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量参数 distributorId: {},originTypeList: {}",distributorId,originTypeList);  	
        return orderService.selectOrderByNineMonth(distributorId,originTypeList);
    } 
    
    
    /**
     * 接口-关闭订单
     * @param 
     * @return
     */
    @PutMapping("/closeorder")
    @ApiOperation(value = "接口-关闭订单....")
    public HttpResponse updateorderClose(@Valid @RequestParam(name = "order_id", required = true) String orderId,@RequestParam(name = "update_by", required = true) String updateBy) {
        
    	
    	LOGGER.info("接口-关闭订单参数orderId：{},updateBy:{}",orderId,updateBy);    	
        return orderService.closeorder(orderId,updateBy);
    } 
    
    
    /**
     * 接口-更新商户备注
     * @param 
     * @return
     */
    @PutMapping("/updateorderbusinessnote")
    @ApiOperation(value = "接口-更新商户备注....")
    public HttpResponse updateorderbusinessnote(@Valid @RequestParam(name = "order_id", required = true) String orderId,@RequestParam(name = "update_by", required = true) String updateBy,@RequestParam(name = "business_note", required = true) String businessNote) {
        
    	
    	LOGGER.info("接口-更新商户备注参数 orderId： {},updateBy：{},businessNote: {}",orderId,updateBy,businessNote);    	
        return orderService.updateorderbusinessnote(orderId,updateBy,businessNote);
    } 
    
    
    /**
     * 更改订单状态/支付状态/修改员 
     * @param 
     * @return
     */
    @PutMapping("/updateorderstatus")
    @ApiOperation(value = "更改订单状态/支付状态/修改员....")
    public HttpResponse updateOrderStatus(@Valid @RequestParam(name = "order_id", required = true) String orderId,
    		@RequestParam(name = "order_status", required = true) Integer orderStatus,
    		@RequestParam(name = "pay_status", required = true) Integer payStatus,
    		@RequestParam(name = "pay_type", required = false) String payType,
    		@RequestParam(name = "update_by", required = false) String updateBy
    		) {
        
    	
    	LOGGER.info("更改订单状态/支付状态/修改员参数 orderId：{},orderStatus: {},payStatus: {},payType: {},updateBy: {}",orderId,orderStatus,payStatus,payType,updateBy);    	
        return orderService.updateOrderStatus(orderId,orderStatus,payStatus,updateBy,payType);
    } 
    
    
    /**
     * 仅变更订单状态
     * @param 
     * @return
     */
    @PutMapping("/only_status")
    @ApiOperation(value = "仅变更订单状态....")
    public HttpResponse onlyStatus(@Valid @RequestParam(name = "order_id", required = true) String orderId,
    		@RequestParam(name = "order_status", required = true) Integer orderStatus,
    		@RequestParam(name = "update_by", required = true) String updateBy
    		) {
        
    	
    	LOGGER.info("仅变更订单状态参数 orderId：{},orderStatus: {},updateBy: {}",orderId,orderStatus,updateBy);    	
        return orderService.onlyStatus(orderId,orderStatus,updateBy);
    } 
    
    
    /**
     * 
     * 接口-收银员交班收银情况统计(param:cashier_id、begin_time、end_time、 return:list-OrderbyReceiptSumResponse)
     * @param 
     * @return
     */
    @GetMapping("/cashier")
    @ApiOperation(value = "接口-收银员交班收银情况统计(param:cashier_id、begin_time、end_time、 return:list-OrderbyReceiptSumResponse)....")
    public HttpResponse cashier(@Valid @RequestParam(name = "cashier_id", required = true) String cashierId,
    		@RequestParam(name = "end_time", required = true) String endTime
    		) {
        
    	
    	LOGGER.info("接口-收银员交班收银情况统计参数 cashierId:{},endTime:{}",cashierId,endTime);
        return orderService.cashier(cashierId,endTime);
    }

    /**
     *
     * 接口-收银员交班结束时间
     * @param
     * @return
     */
    @PostMapping("/cashier/query")
    @ApiOperation(value = "接口-收银员交班结束时间")
    public HttpResponse cashierQuery(@RequestBody CashierReqVo cashierReqVo) {


        LOGGER.info("接口-收银员交班结束时间参数 ",cashierReqVo);
        return orderService.cashierQuery(cashierReqVo);
    }
    
    /**
     * 
     * 接口-通过会员查询最后一次的消费记录.
     * @param 
     * @return
     */
    @GetMapping("/last")
    @ApiOperation(value = "接口-通过会员查询最后一次的消费记录.....")
    public HttpResponse last(@Valid @RequestParam(name = "member_id", required = true) String memberId
    		) {

    	
    	LOGGER.info("接口-通过会员查询最后一次的消费记录参数memberId:{} ",memberId);    	
        return orderService.last(memberId);
    }
    
    /**
     * 
     * 接口-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
     * @param 
     * @return
     */
    @PostMapping("/devel")
    @ApiOperation(value = "接口-通过当前门店,等级会员list、 查询会员数、返回7天内的统计......")
    public HttpResponse devel(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
    		@Valid @RequestBody List<String> detailList
    		) {

    	
    	LOGGER.info("接口-通过当前门店,等级会员list、 查询会员数、返回7天内的统计参数 distributorId：{},detailList: {}",distributorId,detailList);  
    	if(detailList !=null && detailList.size()>0) {
    	}else {
    		detailList =null;
    	}   	
        return orderService.devel(distributorId,detailList);
    }
    
    
    /**
     * 
     * 会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日周月.
     * @param 
     * @return
     */
    @GetMapping("/hydjm")
    @ApiOperation(value = "会员活跃情况-日周月date_type 1：日 2：周 3：月 ....")
    public HttpResponse hydjm(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
    		@Valid @RequestParam(name = "date_type", required = true) Integer dateType
    		) {

    	
    	LOGGER.info("会员活跃情况-日周月参数 distributorId：{},dateType: {}",distributorId,dateType);
        return orderService.selectByMemberPayCount(distributorId,dateType);
    }
    
    
    /**
     * 
     * 接口-生成提货码
     * @param 
     * @return
     */
    @PutMapping("/rede")
    @ApiOperation(value = "接口-生成提货码......")
    public HttpResponse rede(@Valid @RequestParam(name = "order_id", required = true) String orderId) {

    	
    	LOGGER.info("接口-生成提货码参数 orderId：{}",orderId);    	
        return orderService.rede(orderId);
    }
    
    /**
     * 
     * 接口-注销提货码
     * @param 
     * @return
     */
    @PutMapping("/reded")
    @ApiOperation(value = "接口-注销提货码......")
    public HttpResponse reded(@Valid @RequestParam(name = "order_id", required = true) String orderId) {

    	
    	LOGGER.info("接口-注销提货码参数 orderId：{}",orderId);    	
        return orderService.reded(orderId);
    }
    
    /**
     * 
     * 接口-可退货的订单查询
     * @param 
     * @return
     */
    @PutMapping("/reorer")
    @ApiOperation(value = "接口-可退货的订单查询......")
    public HttpResponse reorer(@Valid @RequestBody ReorerRequest reorerRequest) {

    	
    	LOGGER.info("接口-可退货的订单查询参数：{}",reorerRequest);    	
        return orderService.reorer(reorerRequest);
    }   
    
    
    /**
     * 
     * 微商城-销售总览
     * @param 
     * @return
     */
    @GetMapping("/wssev")
    @ApiOperation(value = "微商城-销售总览....")
    public HttpResponse wssev(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId) {

    	LOGGER.info("微商城-销售总览参数 distributorId: {}",distributorId);    	
        return orderService.wssev(distributorId);
    }
    
    
    /**
     * 
     * 微商城-事务总览
     * @param 
     * @return
     */
    @GetMapping("/wsswv")
    @ApiOperation(value = "微商城-事务总览....")
    public HttpResponse wsswv(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId) {

    	
    	LOGGER.info("微商城-事务总览参数 distributorId: {}",distributorId);    	
        return orderService.wsswv(distributorId);
    }
    
    
    /**
     * 销售目标管理-分销机构-月销售额
     * @param 
     * @return
     */
    @PostMapping("/sdm")
    @ApiOperation(value = "销售目标管理-分销机构-月销售额")
    public HttpResponse sdm(@Valid @RequestBody DistributorMonthRequest DistributorMonthRequest) {
        
    	
    	LOGGER.info("销售目标管理参数: {}",DistributorMonthRequest);
        return orderService.selectDistributorMonth(DistributorMonthRequest);
    } 
    
    
    /**
     * 判断会员是否在当前门店时候有过消费记录
     * @param 
     * @return
     */
    @PostMapping("/bmpy")
    @ApiOperation(value = "判断会员是否在当前门店时候有过消费记录")
    public HttpResponse bmpy(@Valid @RequestBody MemberByDistributorRequest memberByDistributorRequest) {
        
    	
    	LOGGER.info("判断会员是否在当前门店时候有过消费记录参数: {}",memberByDistributorRequest);
        return orderService.selectMemberByDistributor(memberByDistributorRequest);
    }


    /**
     * 查询预存订单
     * @param
     * @return
     */
    @PostMapping("/selectPrestorageOrder")
    @ApiOperation(value = "查询预存订单")
    public HttpResponse selectprestorageorder(@Valid @RequestBody OrderQuery orderQuery) {


        LOGGER.info("查询预存订单参数: {}",orderQuery);
        return orderService.selectPrestorageOrder(orderQuery);
    }

    /**
     * 查询预存订单详情
     * @param
     * @return
     */
    @GetMapping("/selectPrestorageOrderDetails")
    @ApiOperation(value = "查询预存订单详情")
    public HttpResponse selectprestorageorderDetails(@Valid @RequestParam(name = "prestorage_order_supply_detail_id", required = true) String prestorageOrderSupplyDetailId) {


        LOGGER.info("查询预存订单详情: {}",prestorageOrderSupplyDetailId);
        return orderService.selectprestorageorderDetails(prestorageOrderSupplyDetailId);
    }


    /**
     * 预存取货
     * @param
     * @return
     */
    @PostMapping("/prestorageOut")
    @ApiOperation(value = "预存取货")
    public HttpResponse prestorageOut(@Valid @RequestBody PrestorageOutInfo prestorageOutVo) {


        LOGGER.info("预存取货参数: {}",prestorageOutVo);
        return orderService.prestorageOut(prestorageOutVo);
    }

    @PostMapping("/back")
    @ApiOperation("支付回调修改订单状态和库存")
    public HttpResponse callback(@RequestBody PartnerPayGateRep payReq) {
        try {
            LOGGER.info("支付回调修改订单状态和库存: {}",payReq);
            return orderService.callback(payReq);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.failure(MessageId.create(Project.ORDER_API, -1, e.getMessage()));
        }
    }

    /**
     * 模糊查询预存订单列表
     * @param
     * @return
     */
    @PostMapping("/prestorageOrderList")
    @ApiOperation(value = "查询预存订单列表....")
    public HttpResponse selectPrestorageOrder(@Valid @RequestBody OrderQuery orderQuery) {


        LOGGER.info("查询预存订单列表：{}",orderQuery);

        return orderService.selectPrestorageOrderList(orderQuery);
    }
    @GetMapping("/prestorageOrderDetail")
    @ApiOperation(value = "查询预存订单详情列表....")
    public HttpResponse selectPrestorageOrderDetail(@Valid @RequestParam(name = "order_id", required = true) String orderId) {


        LOGGER.info("查询预存订单详情列表：{}",orderId);

        return orderService.selectPrestorageOrderDetail(orderId);
    }
    /**
     * 模糊查询预存订单取货日志列表
     * @param
     * @return
     */
    @PostMapping("/prestorageOrderLogs")
    @ApiOperation(value = "模糊查询预存订单取货日志列表....")
    public HttpResponse selectPrestorageOrderLogs(@Valid @RequestBody OrderQuery orderQuery) {


        LOGGER.info("模糊查询预存订单取货日志列表：{}",orderQuery);

        return orderService.selectPrestorageOrderLogs(orderQuery);
    }


    /**
     * 修改门店营业状态
     * @param
     * @return
     */
    @GetMapping("/bmpy")
    @ApiOperation(value = "修改门店营业状态")
    public void updateOpenStatus(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId) {


    	LOGGER.info("开始修改门店营业状态参数: {}",distributorId);
        orderService.updateOpenStatus(distributorId);
    }

    /**
     * 最近消费订单 (消费时间/消费金额)
     * @param
     * @return
     */
    @GetMapping("/lately")
    @ApiOperation(value = "最近消费订单 (消费时间/消费金额)")
    public HttpResponse<LatelyResponse> memberLately(@Valid @RequestParam(name = "member_id", required = false) String memberId,
    		@Valid @RequestParam(name = "distributor_id", required = false) String distributorId) {


    	LOGGER.info("最近消费订单 (消费时间/消费金额)参数:memberId: {},distributorId:{}",memberId,distributorId);
    	return orderService.memberLately(memberId,distributorId);
    }

    @PostMapping("/updateRejectPrestoragProduct")
    @ApiOperation(value = "修改预存商品退货数量....")
    public HttpResponse updateRejectPrestoragProduct(@Valid @RequestBody PrestorageOrderSupplyDetailVo vo) {


        LOGGER.info("修改预存商品退货数量：{}",vo);

        return orderService.updateRejectPrestoragProduct(vo);
    }

    @PostMapping("/updateRejectPrestoragState")
    @ApiOperation(value = "修改预存商品状态和订单状态")
    public HttpResponse updateRejectPrestoragState(@Valid @RequestBody RejectPrestoragStateVo vo) {


        LOGGER.info("修改预存商品状态和订单状态：{}",vo);

        return orderService.updateRejectPrestoragState(vo);
    }

    @PostMapping("/batchUpdateRejectPrestoragProduct")
    @ApiOperation(value = "批量修改预存商品")
    public HttpResponse batchUpdateRejectPrestoragProduct(@Valid @RequestBody PrestoragProductAfter vos) throws Exception {


        LOGGER.info("修改预存商品状态和订单状态：{}",vos);

        return orderService.batchUpdateRejectPrestoragProduct(vos);
    }

    @PostMapping("/getUnPayNum")
    @ApiOperation(value = "近期未购买的会员数")
    public HttpResponse getUnPayNum(@Valid  @RequestBody UnPayVo unPayVo) {


        LOGGER.info("近期未购买的会员数：{}",unPayVo);

        return orderService.getUnPayNum(unPayVo);
    }
    @PostMapping("/getUnPayMemberIdList")
    @ApiOperation(value = "近期未购买的会员")
    public HttpResponse getUnPayMemberIdList(@Valid @RequestBody UnPayVo unPayVo) {


        LOGGER.info("修改预存商品状态和订单状态：{}",unPayVo);

        return orderService.getUnPayMemberIdList(unPayVo);
    }

    /**
     * 订单中心获取近30天销量
     * @param skuCode
     * @param storeId
     * @param day
     * @return
     */
    @GetMapping("/cashier/orderCount")
    @ApiOperation(value = "接口-订单中心获取近30天销量")
    public HttpResponse orderCount(@Valid @RequestParam(name = "sku_code") String skuCode,
                                   @Valid @RequestParam(name = "store_id") String storeId,
                                   @Valid @RequestParam(name = "day") int day) {

        LOGGER.info("订单中心获取近30天销量：{}",skuCode,storeId,day);
        return orderService.orderCount(skuCode,storeId,day);
    }
}
