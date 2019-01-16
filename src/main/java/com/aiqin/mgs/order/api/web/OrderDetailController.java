/*****************************************************************

* 模块名称：订单后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 
* 
* 2019-01-08 添加接口 -查询BYordercode-返回订单明细数据、订单数据、收货信息、结算数据 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;

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
@RequestMapping("/orderdetail")
@Api("订单明细相关操作接口")
@SuppressWarnings("all")
public class OrderDetailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailController.class);
    
    @Resource
    private OrderDetailService orderDetailService;
       
    
    /**
     * 模糊查询订单明细列表
     * @param 
     * @return
     */
    @PostMapping("/selectorderdetail")
    @ApiOperation(value = "查询订单明细列表....")
    public HttpResponse selectorderDetail(@Valid @RequestBody OrderDetailQuery orderDetailQuery) {
        
    	
    	LOGGER.info("查询订单明细列表...");    	
        return orderDetailService.selectorderDetail(orderDetailQuery);
    }
    
    
    /**
     * 查询BYorderid-返回订单明细数据、订单数据、收货信息、结算数据
     * @param 
     * @return
     */
    @GetMapping("/selectorderany")
    @ApiOperation(value = "查询BYorderid-返回订单明细数据、订单数据、收货信息、结算数据....")
    public HttpResponse selectorderany(@Valid @RequestParam(name = "order_id", required = true) String orderId) {
        
    	
    	LOGGER.info("查询BYorderid-返回订单明细数据、订单数据、收货信息...");    	
        return orderDetailService.selectorderany(orderId);
    }
    
    
    /**
     * 查询BYordercode-返回订单明细数据、订单数据、收货信息、结算数据
     * @param 
     * @return
     */
    @GetMapping("/selde")
    @ApiOperation(value = "查询BYordercode-返回订单明细数据、订单数据、收货信息、结算数据....")
    public HttpResponse selde(@Valid @RequestParam(name = "order_code", required = true) String orderCode) {
        
    	
    	LOGGER.info("查询BYorderid-返回订单明细数据、订单数据、收货信息...");    	
        return orderDetailService.selectorderSelde(orderCode);
    }
    
    
    /**
     * 查询会员下的全部订单 返回订单主数据+订单详细列表
     * @param 
     * @return
     */
    @GetMapping("/selectorderdbumemberid")
    @ApiOperation(value = "查询会员下的全部订单 返回订单主数据+订单详细列表....")
    public HttpResponse selectorderdbumemberid(@Valid @RequestParam(name = "member_id", required = true) String memberId,@Valid @RequestParam(name = "order_status", required = false) Integer orderStatus,
    		@Valid @RequestParam(name = "page_size", required = true) String pageSize,
    		@Valid @RequestParam(name = "page_no", required = true) String pageNo
    		) {
        
    	
    	LOGGER.info("查询会员下的全部订单 返回订单主数据+订单详细列表...");    	
        return orderDetailService.selectorderdbumemberid(memberId,orderStatus,pageSize,pageNo);//查询会员下的全部订单 返回订单主数据+订单详细列表
    }
    
    
    /**
     * 查询订单明细部分汇总-（支持活动ID汇总、）
     * @param 
     * @return
     */
    @PostMapping("/selectorderdetailsum")
    @ApiOperation(value = "查询订单明细部分汇总-(支持活动ID汇总、)")
    public HttpResponse selectorderdetailsum(@Valid @RequestBody OrderDetailQuery orderDetailQuery) {
        
    	
    	LOGGER.info("查询订单明细部分汇总-（支持活动ID汇总、）......");    	
        return orderDetailService.selectorderdetailsum(orderDetailQuery);//查询订单明细部分汇总-（支持活动ID汇总、）
    }
    
    
    /**
     * 商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)
     * @param distributorId
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/productoverviewbymonth")
    @ApiOperation(value = "商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)....")
    public HttpResponse productOverviewByMonth(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,@Valid @RequestParam(name = "year", required = true) String year,@RequestParam(name = "month", required = true) String month) {
        
    	
    	LOGGER.info("商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)......");    	
        return orderDetailService.productOverviewByMonth(distributorId,year,month);
    }
    

    /**
     * 接口--商品概览产品销量、销售额-前5名
     * @param distributor_id
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/productoverviewbyskuordertop")
    @ApiOperation(value = "商品概览菜单-产品销量、销售额-前5名")
    public HttpResponse productOverviewByOrderTop(@Valid @RequestParam(name = "distributor_id", required = true) String distributor_id,@Valid @RequestParam(name = "year", required = true) String year,@RequestParam(name = "month", required = true) String month) {
        
    	LOGGER.info("商品概览菜单-产品销量、销售额-前5名......");    	
        return orderDetailService.productOverviewByOrderTop(distributor_id,year,month);//商品概览产品销量
    }
    
    
    /**
     * 接口--商品概览产品销量、销售额-后5名
     * @param distributor_id
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/productoverviewbyskuorderlast")
    @ApiOperation(value = "商品概览菜单-产品销量、销售额-后5名")
    public HttpResponse productOverviewByOrderLast(@Valid @RequestParam(name = "distributor_id", required = true) String distributor_id,@Valid @RequestParam(name = "year", required = true) String year,@RequestParam(name = "month", required = true) String month) {
               	
    	LOGGER.info("商品概览菜单-产品销量、销售额-后5名......");    	
        return orderDetailService.productOverviewByOrderLast(distributor_id,year,month);//商品概览产品销量
    }
    
    
    /**
     * 接口--会员管理-会员消费记录
     * @param orderDetailQuery
     * @return
     */
    @PostMapping("/byber")
    @ApiOperation(value = "接口--会员管理-会员消费记录")
    public HttpResponse byMemberOrder(@Valid @RequestBody OrderDetailQuery orderDetailQuery) {
        
    	
    	LOGGER.info("接口--会员管理-会员消费记录......");    	
        
    	return orderDetailService.byMemberOrder(orderDetailQuery);//接口--会员管理-会员消费记录
    }
    
    
//    /**
//     * 添加新的订单明细数据
//     * @param detailList
//     * @param orderId
//     * @return
//     */
//    @PostMapping("")
//    @ApiOperation(value = "添加新的订单明细数据")
//    public HttpResponse addDetailList(@Valid @RequestBody List<OrderDetailInfo> detailList,@Valid @RequestParam(name = "order_id", required = false) String orderId) {
//        
//    	
//    	LOGGER.info("添加新的订单明细数据......");
//        
//        return orderDetailService.addDetailList(detailList,orderId);
//    }
    
    
//    /**
//     * 查询会员下的所有订单ID下的商品集合...
//     * @param orderidslList
//     * @param memberId
//     * @return
//     */
//    @PostMapping("/selectproductbyorders")
//    @ApiOperation(value = "查询会员下的所有订单ID下的商品集合..")
//    public HttpResponse selectproductbyorders(@Valid @RequestBody List<String> orderidslList,@Valid @RequestParam(name = "member_id", required = true) String memberId) {
//        
//    	
//    	LOGGER.info("查询会员下的所有订单ID下的商品集合....");    	
//        
//    	return orderDetailService.selectproductbyorders(orderidslList,memberId);//查询会员下的所有订单ID下的商品集合...
//    }
     
    /**
     * 
     * 商品总库菜单-统计商品在各个渠道的订单数.
     * @param 
     * @return
     */
    @PostMapping("/prodisor")
    @ApiOperation(value = "商品总库菜单-统计商品在各个渠道的订单数....")
    public HttpResponse prodisor(@Valid @RequestBody(required = true) List<String> sukList,
    		@Valid @RequestBody List<Integer> originTypeList) {

    	LOGGER.info("商品总库菜单-统计商品在各个渠道的订单数....");    	
        return orderDetailService.prodisor(sukList,originTypeList);
    }
    
    /**
     * 
     * sku销量统计
     * @param 
     * @return
     */
    @GetMapping("/sksm")
    @ApiOperation(value = "sku销量统计....")
    public HttpResponse sksm(@Valid @RequestParam(required = true) List<String> sukList) {

    	LOGGER.info("sku销量统计....");    	
        return orderDetailService.skuSum(sukList);
    }
    
    
    /**
     * 
     * 批量添加sku销量
     * @param 
     * @return
     */
    @GetMapping("/savtch")
    @ApiOperation(value = "批量添加sku销量....")
    public HttpResponse savtch(@Valid @RequestParam(required = true) List<String> sukList) {

    	LOGGER.info("sku销量统计....");    	
        return orderDetailService.saveBatch(sukList);
    }

}
