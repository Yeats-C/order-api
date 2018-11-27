/*****************************************************************

* 模块名称：订单明细后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderService;

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
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    
 
    @PostMapping("")
    @ApiOperation(value = "添加新的订单主数据")
    public HttpResponse addNewActivity(@Valid @RequestBody OrderInfo orderInfo) {
        LOGGER.info("添加新的订单主数据......");
        return orderService.addOrderInfo(orderInfo);
    }
    
    @PostMapping("/addorderlog")
    @ApiOperation(value = "添加新的订单日志数据")
    public HttpResponse addOrderLog(@Valid @RequestBody OrderLog logInfo) {
        LOGGER.info("添加新的订单日志数据......");
        return orderService.addOrderLog(logInfo);
    }
    
    @PostMapping("/addordercoupon")
    @ApiOperation(value = "添加新的订单优惠券关系表数据")
    public HttpResponse addOrderCoupon(@Valid @RequestBody List<OrderRelationCouponInfo> orderCouponList,@Valid @RequestParam(name = "order_id", required = false) String orderId) {
        
    	
    	LOGGER.info("添加新的订单优惠券关系表数据......");
        return orderService.addOrderCoupon(orderCouponList,orderId);
    }
    
    
    //3.更改订单状态  “待提货”的订单“取消”是要给客户退款的
    
    /**
     * 接口-分销机构维度-总销售额 返回INT
     * @param 
     * @return
     */
    @PostMapping("/selectorderamt")
    @ApiOperation(value = "接口-分销机构维度-总销售额 返回INT....")
    public HttpResponse selectOrderAmt(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,@RequestParam(name = "origin_type", required = true) String originType) {
        
    	
    	LOGGER.info("接口-分销机构维度-总销售额 返回INT...");    	
        return orderService.selectOrderAmt(distributorId,originType);
    } 
    
    
    /**
     * 接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
     * @param 
     * @return
     */
    @PostMapping("/selectorderbymonth")
    @ApiOperation(value = "接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数....")
    public HttpResponse selectorderbymonth(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,@RequestParam(name = "origin_type", required = true) String originType) {
        
    	
    	LOGGER.info("接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数...");    	
        return orderService.selectorderbymonth(distributorId,originType);
    } 

}
