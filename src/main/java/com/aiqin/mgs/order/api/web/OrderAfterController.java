/*****************************************************************

* 模块名称：订单售后后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderAfterService;

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
import java.util.List;

@RestController
@RequestMapping("/orderafter")
@Api("售后维权/退货接口")
@SuppressWarnings("all")
public class OrderAfterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAfterController.class);
    
    @Resource
    private OrderAfterService orderAfterService;    
    
    /**
     * 支持-模糊查询售后维权列表 /条件查询退货信息
     * @param 
     * @return
     */
    @PostMapping("/selectorderafter")
    @ApiOperation(value = "支持-条件查询售后维权列表 /条件查询退货信息")
    public HttpResponse selectOrderAfter(@Valid @RequestBody OrderAfterSaleQuery orderAfterSaleQuery) {
        
    	
    	LOGGER.info("支持-条件查询售后维权列表 /条件查询退货信息......");    	
        return orderAfterService.selectOrderAfter(orderAfterSaleQuery);//支持-条件查询售后维权列表 /条件查询退货信息
    }
    
    
    @PostMapping("")
    @ApiOperation(value = "添加新的订单售后数据")
    public HttpResponse addAfterOrder(@Valid @RequestBody OrderAfterSaleInfo orderAfterSaleInfo) {
        LOGGER.info("添加新的订单售后数据......");
        return orderAfterService.addAfterOrder(orderAfterSaleInfo);
    }
    
    
    

    
    
    

}
