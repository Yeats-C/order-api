/*****************************************************************

* 模块名称：订单售后后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.OrderAfteIListInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse.AddReturnOrderResonse;
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
     * 模糊查询售后维权列表 /条件查询退货信息-不带其他信息
     * @param 
     * @return
     */
    @PostMapping("/selectorderafter")
    @ApiOperation(value = "条件查询售后维权列表 /条件查询退货信息")
    public HttpResponse selectOrderAfter(@Valid @RequestBody OrderAfterSaleQuery orderAfterSaleQuery) {
        
    	
    	LOGGER.info("支持-条件查询售后维权列表 /条件查询退货信息参数：{}",orderAfterSaleQuery);    	
        return orderAfterService.selectOrderAfter(orderAfterSaleQuery);//条件查询售后维权列表 /条件查询退货信息
    }
    
    
  /**
   * TOC订单-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表
   * @param orderAfteIListInfo
   * @return
   */
  @PostMapping("")
  @ApiOperation(value = "TOC订单-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表...")
  public HttpResponse addAfterOrder(@Valid @RequestBody OrderAfterSaleInfo orderAfterSaleInfo) throws Exception {
      
	  
	  LOGGER.info("添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表参数：{}",orderAfterSaleInfo);
      
      //添加TOC订单标识
      if(orderAfterSaleInfo !=null) {
    	  orderAfterSaleInfo.setOrderType(Global.ORDER_TYPE_1);
      	  return orderAfterService.addAfterOrder(orderAfterSaleInfo);
      }else {
    	  LOGGER.warn("TOC订单-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表数据为null：{}",orderAfterSaleInfo);
      	  return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
      }
  }
  
  
  /**
   * 服务订单-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表
   * @param orderAfteIListInfo
   * @return
   */
  @PostMapping("/zaaf")
  @ApiOperation(value = "服务订单-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表...")
  public HttpResponse<AddReturnOrderResonse> zaaf(@Valid @RequestBody OrderAfterSaleInfo orderAfterSaleInfo) {
      
	  
	  LOGGER.info("添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表: {}",orderAfterSaleInfo);
      
      //添加服务订单标识
      if(orderAfterSaleInfo !=null) {
    	  orderAfterSaleInfo.setOrderType(Global.ORDER_TYPE_3);
      	  return orderAfterService.addAfterNoCodeOrder(orderAfterSaleInfo);
      }else {
    	  LOGGER.warn("服务订单-添加新的订单售后数据+订单售后明细数据+修改订单表+修改订单明细表数据为null：{}",orderAfterSaleInfo);
      	  return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
      }
  }
  
  
  /**
   * 查询BYorderid-返回订单订单主数据、退货数据、退货明细数据、订单明细数据、优惠券数据、20190401新增结算信息
   * @param 
   * @return
   */
  @GetMapping("/detail")
  @ApiOperation(value = "查询BYorderid-返回订单订单主数据、退货数据、退货明细数据、订单明细数据、优惠券数据...")
  public HttpResponse selectDetail(@Valid @RequestParam(name = "after_sale_id", required = true) String afterSaleId) {
      
  	
  	  LOGGER.info("查询BYorderid-返回订单订单主数据、退货数据、退货明细数据、订单明细数据、优惠券数据参数：{}",afterSaleId);    	
      return orderAfterService.selectDetail(afterSaleId);
      
  }
  
  /**
   * 模糊查询查询退货信息+退货明细+订单明细信息
   * @param 
   * @return
   */
  @PostMapping("/aftlst")
  @ApiOperation(value = "模糊查询查询退货信息+退货明细+订单明细信息...")
  public HttpResponse aftlst(@Valid @RequestBody OrderAfterSaleQuery orderAfterSaleQuery) {
      
  	
  	  LOGGER.info("模糊查询查询退货信息+退货明细+订单明细信息参数：{}",orderAfterSaleQuery);    	
      return orderAfterService.aftlst(orderAfterSaleQuery);
  }
  
  
  
  
//  /**
//   * 更改退货状态(售后明细表+售后表+订单表+订单明细表) 扩展
//   * @param 
//   * @return
//   */
//  @PutMapping("/return_status")
//  @ApiOperation(value = "更改退货状态(售后明细表+售后表+订单表+订单明细表)....")
//  public HttpResponse returus(@Valid @RequestParam(name = "after_sale_id", required = true) String afterSaleId,
//  		@RequestParam(name = "after_sale_status", required = true) Integer afterSaleStatus,
//  		@RequestParam(name = "update_by", required = true) String updateBy
//  		) {
//
//  	LOGGER.info("更改退货状态(售后明细表+售后表+订单表+订单明细表) ...");    	
//      return orderAfterService.returus(afterSaleId,afterSaleStatus,updateBy);
//  }
  
  /**
   * 更改退货状态(售后表)
   * @param 
   * @return
   */
   @PutMapping("/return_status")
   @ApiOperation(value = "更改退货状态(售后表+订单表)....")
   public HttpResponse returus(@Valid @RequestParam(name = "after_sale_id", required = true) String afterSaleId,
		@RequestParam(name = "after_sale_status", required = true) Integer afterSaleStatus,
		@RequestParam(name = "update_by", required = true) String updateBy,
		@RequestParam(name = "update_by_name", required = true) String updateByName
		) {
	   
	   
       //前台逻辑,退货的时候 先插入退货数据退款成功后修改退货状态.
	   LOGGER.info("更改退货状态(售后表)参数: afterSaleId:{},afterSaleStatus:{},updateBy:{},updateByName:{}",afterSaleId,afterSaleStatus,updateBy,updateByName);    	
       return orderAfterService.returus(afterSaleId,afterSaleStatus,updateBy,updateByName);
   }  
   }
