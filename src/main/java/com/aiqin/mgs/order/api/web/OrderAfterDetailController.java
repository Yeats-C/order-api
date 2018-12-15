/*****************************************************************

* 模块名称：订单售后明细后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderAfterDetailService;
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
@RequestMapping("/orderafterdetail")
@Api("售后维权/退货明细操作接口")
@SuppressWarnings("all")
public class OrderAfterDetailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAfterDetailController.class);
    
    @Resource
    private OrderAfterDetailService orderAfterDetailService;    
    
    
    /**
     * 售后id、门店查询售后维权明细列表
     * @param 
     * @return
     */
    @PostMapping("/selectorderafterdetail")
    @ApiOperation(value = "售后id、门店查询售后维权明细列表")
    public HttpResponse OrderAfteIList(@Valid @RequestBody OrderAfterSaleQuery orderAfterSaleQuery) {
        
    	
    	LOGGER.info("售后id、门店查询售后维权明细列表......");    	
        return orderAfterDetailService.OrderAfteIList(orderAfterSaleQuery);//售后id、门店查询售后维权明细列表
    }
    
    
//    @PostMapping("")
//    @ApiOperation(value = "添加新的订单售后明细数据")
//    public HttpResponse addAfterOrderDetail(@Valid @RequestBody List<OrderAfterSaleDetailInfo> AfterDetailList,@Valid @RequestParam(name = "after_sale_id", required = false) String afterSaleId) {
//        LOGGER.info("添加新的订单售后明细数据......");
//        return orderAfterDetailService.addAfterOrderDetail(AfterDetailList,afterSaleId);
//    }
    
    
    

    
    
    

}
