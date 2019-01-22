/*****************************************************************

* 模块名称：订单明细后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.domain.request.DetailCouponRequest;
import com.aiqin.mgs.order.api.domain.request.DistributorMonthRequest;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
     * 添加订单主数据+添加订单明细数据+返回订单编号
     * @param 
     * @return
     */
    @PostMapping("/addordta")
    @ApiOperation(value = "添加订单主数据+添加订单明细数据+返回订单编号")
    public HttpResponse addOrdta(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        LOGGER.info("添加新的订单主数据以及其他订单关联数据......");
		
        return orderService.addOrdta(orderAndSoOnRequest);
    }
    
    
    /**
     * 添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据
     * @param 
     * @return
     */
    @PostMapping("/addpamo")
    @ApiOperation(value = "添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据")
    public HttpResponse addPamo(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        LOGGER.info("添加新的订单主数据以及其他订单关联数据......");
		
        return orderService.addPamo(orderAndSoOnRequest);
    }
    
    
    /**
     * 删除订单主数据+删除订单明细数据
     * @param 
     * @return
     */
    @GetMapping("/deordta")
    @ApiOperation(value = "删除订单主数据+删除订单明细数据")
    public HttpResponse deordta(@Valid @RequestParam(name = "order_id", required = true) String orderId){
        LOGGER.info("删除订单主数据+删除订单明细数据......");
		
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
        
    	
    	LOGGER.info("查询订单列表...");    	
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
        
    	
    	LOGGER.info("导出订单列表...");    	
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
        
    	
    	LOGGER.info("模糊查询订单列表+订单中商品sku数量...");    	
        return orderService.oradsku(orderQuery);
    }
    
 
    /**
     * 添加新的订单主数据以及其他订单关联数据
     * @param orderAndSoOnRequest
     * @return
     * @throws Exception 
     */
    @PostMapping("")
    @ApiOperation(value = "添加新的订单主数据以及其他订单关联数据")
    public HttpResponse addOrderList(@Valid @RequestBody OrderAndSoOnRequest orderAndSoOnRequest){
        LOGGER.info("添加新的订单主数据以及其他订单关联数据......");
		
        return orderService.addOrderList(orderAndSoOnRequest);
    }
    
    /**
     * 已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)
     * @param orderAndSoOnRequest
     * @return
     */
    @PostMapping("/repast")
    @ApiOperation(value = "已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)")
    public HttpResponse repast(@Valid @RequestParam(name = "order_id", required = true) String orderId,
    		@Valid @RequestParam(name = "pay_type", required = true) String payType,
    		@Valid @RequestBody(required = true) List<OrderPayInfo> orderPayList
    		) {
        LOGGER.info("已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表).....");
        return orderService.repast(orderId,payType,orderPayList);
    }
    
    
//    @PostMapping("")
//    @ApiOperation(value = "添加新的订单主数据以及其他订单关联数据")
//    public HttpResponse addOrderList(@Valid @RequestBody OrderInfo orderInfo
//    		) {
//        LOGGER.info("添加新的订单主数据以及其他订单关联数据......");
//        return orderService.addOrderInfo(orderInfo);
//    }
    

//    /**
//     * 添加新的订单日志数据
//     * @param logInfo
//     * @param orderId
//     * @return
//     */
//    @PostMapping("/addorderlog")
//    @ApiOperation(value = "添加新的订单日志数据")
//    public HttpResponse addOrderLog(@Valid @RequestBody OrderLog logInfo) {
//        LOGGER.info("添加新的订单日志数据......");
//        return orderService.addOrderLog(logInfo);
//    }
    
    /**
     * 查询订单日志数据
     * @param logInfo
     * @param orderId
     * @return
     */
    @PostMapping("/orog")
    @ApiOperation(value = "查询订单日志数据")
    public HttpResponse orog(@Valid @RequestParam(name = "order_id", required = true) String orderId) {
        LOGGER.info("查询订单日志数据......");
        return orderService.orog(orderId);
    }
    
    
//    /**
//     * 添加新的订单优惠券关系表数据
//     * @param orderCouponList
//     * @param orderId
//     * @return
//     */
//    @PostMapping("/addordercoupon")
//    @ApiOperation(value = "添加新的订单优惠券关系表数据")
//    public void addOrderCoupon(@Valid @RequestBody List<OrderRelationCouponInfo> orderCouponList,@Valid @RequestParam(name = "order_id", required = false) String orderId) {
//        
//    	
//    	LOGGER.info("添加新的订单优惠券关系表数据......");
//    	orderService.addOrderCoupon(orderCouponList,orderId);
//    }
    
    
//    /**
//     * 接口-分销机构维度-总销售额 返回INT
//     * @param 
//     * @return
//     */
//    @GetMapping("/selectorderamt")
//    @ApiOperation(value = "接口-分销机构维度-总销售额 返回INT....")
//    public HttpResponse selectOrderAmt(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,@RequestParam(name = "origin_type", required = true) String originType) {
//        
//    	
//    	LOGGER.info("接口-分销机构维度-总销售额 返回INT...");    	
//        return orderService.selectOrderAmt(distributorId,originType);
//    } 
    
    
    /**
     * 概览菜单-分销机构+当月维度-总销售额\当月销售额、当月实收、当月支付订单数
     * @param 
     * @return
     */
    @PostMapping("/selectorderbyoverview")
    @ApiOperation(value = "概览菜单-分销机构+当月维度-总销售额\\当月销售额、当月实收、当月支付订单数 ....")
    public HttpResponse selectorderbyoverview(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
    		@Valid @RequestBody List<Integer> originTypeList) {
        
    	
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
        
    	
    	LOGGER.info("概览菜单-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量...");    	
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
        
    	
    	LOGGER.info("概览菜单-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量...");    	
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
        
    	
    	LOGGER.info("概览菜单-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量...");    	
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
        
    	
    	LOGGER.info("接口-关闭订单...");    	
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
        
    	
    	LOGGER.info("接口-更新商户备注...");    	
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
        
    	
    	LOGGER.info("更改订单状态/支付状态/修改员...");    	
        return orderService.updateOrderStatus(orderId,orderStatus,payStatus,updateBy);
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
        
    	
    	LOGGER.info("仅变更订单状态....");    	
        return orderService.onlyStatus(orderId,orderStatus,updateBy);
    } 
    
//    /**
//     * 
//     * 仅更改退货状态(订单主表+订单售后表) 业务逻辑不同 废弃
//     * @param 
//     * @return
//     */
//    @PutMapping("/return_status")
//    @ApiOperation(value = "更改退货状态/修改员....")
//    public HttpResponse returnStatus(@Valid @RequestParam(name = "order_id", required = true) String orderId,
//    		@RequestParam(name = "return_status", required = true) Integer returnStatus,
//    		@RequestParam(name = "update_by", required = true) String updateBy
//    		) {
//        
//    	
//    	LOGGER.info("更改退货状态/修改员...");    	
//        return orderService.returnStatus(orderId,returnStatus,updateBy);
//    }
    
    
    /**
     * 
     * 接口-收银员交班收银情况统计(param:cashier_id、begin_time、end_time、 return:list-OrderbyReceiptSumResponse)
     * @param 
     * @return
     */
    @GetMapping("/cashier")
    @ApiOperation(value = "接口-收银员交班收银情况统计(param:cashier_id、begin_time、end_time、 return:list-OrderbyReceiptSumResponse)....")
    public HttpResponse cashier(@Valid @RequestParam(name = "cashier_id", required = true) String cashierId,
    		@RequestParam(name = "begin_time", required = true) String beginTime,
    		@RequestParam(name = "end_time", required = true) String endTime
    		) {
        
    	
    	LOGGER.info("接口-收银员交班收银情况统计(param:cashier_id、begin_time、end_time、 return:list-OrderbyReceiptSumResponse)...");    	
        return orderService.cashier(cashierId,beginTime,endTime);
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

    	LOGGER.info("接口-通过会员查询最后一次的消费记录...");    	
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

    	LOGGER.info("接口-通过当前门店,等级会员list、 查询会员数、返回7天内的统计....");  
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

    	LOGGER.info("接口-生成提货码....");    	
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

    	LOGGER.info("接口-注销提货码....");    	
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

    	LOGGER.info("接口-可退货的订单查询....");    	
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

    	LOGGER.info("微商城-销售总览....");    	
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

    	LOGGER.info("微商城-事务总览....");    	
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
        
    	
        return orderService.selectDistributorMonth(DistributorMonthRequest);
    } 
}
