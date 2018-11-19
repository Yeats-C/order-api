/*****************************************************************

* 模块名称：购物车后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    @GetMapping("/selectorder")
    @ApiOperation(value = "模糊查询订单列表")
    public HttpResponse selectOrder(@Valid @RequestBody OrderInfo orderInfo) {
        
    	
    	LOGGER.info("模糊查询订单列表......");    	
        return orderService.selectOrder(orderInfo);//查询订单
    }
    
    
    /**
     * 查询订单详细列表-带结算、退货信息
     * @param 
     * @return
     */
    @GetMapping("/selectorderDetail")
    @ApiOperation(value = "查询订单详细列表-带结算、退货信息")
    public HttpResponse selectorderDetail(@Valid @RequestBody OrderInfo orderInfo) {
        
    	
    	LOGGER.info("查询订单详细列表-带结算、退货信息......");    	
        return orderService.selectorderDetail(orderInfo);//查询订单详细列表-带结算、退货信息
    }
    
    
    
    //3.更改订单状态  “待提货”的订单“取消”是要给客户退款的
    

    
    
    

}
