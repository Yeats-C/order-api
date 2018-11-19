/*****************************************************************

* 模块名称：订单售后后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
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
     * 条件查询售后维权列表
     * @param 
     * @return
     */
    @GetMapping("/selectorderafter")
    @ApiOperation(value = "查询售后维权列表")
    public HttpResponse selectOrderAfter(@Valid @RequestBody OrderAfterSaleInfo orderAfterInfo) {
        
    	
    	LOGGER.info("查询售后维权列表......");    	
        return orderAfterService.selectOrderAfter(orderAfterInfo);//查询售后维权列表
    }
    
    
    /**
     * 条件查询售后维权明细列表
     * @param 
     * @return
     */
    @GetMapping("/selectorderafterdetail")
    @ApiOperation(value = "条件查询售后维权明细列表")
    public HttpResponse selectOrderAfterDetail(@PathVariable(name = "after_sale_id") String afterSaleId) {
        
    	
    	LOGGER.info("条件查询售后维权明细列表......");    	
        return orderAfterService.selectOrderAfterDetail(afterSaleId);//条件查询售后维权明细列表
    }
    
    
  //1.退货退款 一期
  @PostMapping("")
  @ApiOperation(value = "添加退货退款信息")
  public HttpResponse addOrderAfter(@Valid @RequestBody OrderInfo orderInfo) {
  	
  	
  	return null;
  }
    
    
    

    
    
    

}
