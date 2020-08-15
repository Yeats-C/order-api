/*****************************************************************

* 模块名称：订单后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 
* 
* 2019-01-08 添加接口 -查询BYordercode-返回订单明细数据、订单数据、收货信息、结算数据 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.request.ProdisorRequest;
import com.aiqin.mgs.order.api.domain.request.ProductStoreRequest;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
        
    	
    	LOGGER.info("查询订单明细列表参数： {}",orderDetailQuery);    	
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
        
    	
    	LOGGER.info("查询BYorderid-返回订单明细数据、订单数据、收货信息参数orderId: {}",orderId);    	
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
        
    	
    	LOGGER.info("查询BYorderid-返回订单明细数据、订单数据、收货信息参数：{}",orderCode);    	
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
        
    	
    	LOGGER.info("查询会员下的全部订单 返回订单主数据+订单详细列表 参数 memberId:{},orderStatus:{}",memberId,orderStatus);    	
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


    	LOGGER.info("查询订单明细部分汇总-（支持活动ID汇总、）参数：{}",orderDetailQuery);
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
        
    	
    	LOGGER.info("商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)参数distributorId：{},year：{},month：{}",distributorId,year,month);    	
        return orderDetailService.productOverviewByMonth(distributorId,year,month);
    }
    

    /**
     * 接口--商品概览产品销量、销售额-前10名
     * @param distributor_id
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/productoverviewbyskuordertop")
    @ApiOperation(value = "商品概览菜单-产品销量、销售额-前10名")
    public HttpResponse productOverviewByOrderTop(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,@Valid @RequestParam(name = "year", required = true) String year,@RequestParam(name = "month", required = true) String month) {
        
    	
    	LOGGER.info("商品概览菜单-产品销量、销售额-前10名参数 distributor_id:{},year:{},month:{}",distributorId,year,month);    	
        return orderDetailService.productOverviewByOrderTop(distributorId,year,month);//商品概览产品销量
    }
    
    
    /**
     * 接口--商品概览产品销量、销售额-后10名
     * @param distributor_id
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/productoverviewbyskuorderlast")
    @ApiOperation(value = "商品概览菜单-产品销量、销售额-后10名")
    public HttpResponse productOverviewByOrderLast(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,@Valid @RequestParam(name = "year", required = true) String year,@RequestParam(name = "month", required = true) String month) {
            
    	
    	LOGGER.info("商品概览菜单-产品销量、销售额-后10名参数 distributorId：{},year：{},month：{}",distributorId,year,month);    	
        return orderDetailService.productOverviewByOrderLast(distributorId,year,month);//商品概览产品销量
    }

    /**
     * 接口--商品概览产品销量、销售额-前10名
     */
    @GetMapping("/product/fronttop10")
    @ApiOperation(value = "商品概览菜单-产品销量、销售额-前10名")
    public HttpResponse productFrontTop10(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
                                          @Valid @RequestParam(name = "begin_time", required = true) String beginTime,
                                          @RequestParam(name = "end_time", required = true) String endTime,
                                          @RequestParam(name = "category_id", required = true) String categoryId) {


        LOGGER.info("商品概览菜单-产品销量、销售额-前10名参数 distributorId：{},beginTime：{},endTime：{},categoryId：{}",distributorId,beginTime,endTime,categoryId);
        return orderDetailService.productFrontTop10(distributorId,beginTime,endTime,categoryId);//商品概览产品销量
    }

    /**
     * 接口--商品概览产品销量、销售额-后10名
     */
    @GetMapping("/product/aftertop10")
    @ApiOperation(value = "商品概览菜单-产品销量、销售额-后10名")
    public HttpResponse productAfterTop10(@Valid @RequestParam(name = "distributor_id", required = true) String distributorId,
                                          @Valid @RequestParam(name = "begin_time", required = true) String beginTime,
                                          @RequestParam(name = "end_time", required = true) String endTime,
                                          @RequestParam(name = "category_id", required = true) String categoryId) {


    	LOGGER.info("商品概览菜单-产品销量、销售额-后10名参数 distributorId：{},beginTime：{},endTime：{},categoryId：{}",distributorId,beginTime,endTime,categoryId);
        return orderDetailService.productAfterTop10(distributorId,beginTime,endTime,categoryId);//商品概览产品销量
    }
    
    
    /**
     * 接口--会员管理-会员消费记录
     * @param orderDetailQuery
     * @return
     */
    @PostMapping("/byber")
    @ApiOperation(value = "接口--会员管理-会员消费记录")
    public HttpResponse byMemberOrder(@Valid @RequestBody OrderDetailQuery orderDetailQuery) {
        
    	
    	LOGGER.info("接口--会员管理-会员消费记录参数 {}",orderDetailQuery);    	
    	return orderDetailService.byMemberOrder(orderDetailQuery);//接口--会员管理-会员消费记录
    }

     
    /**
     * 
     * 商品总库菜单-统计商品在各个渠道的订单数.
     * @param 
     * @return
     */
    @PostMapping("/prodisor")
    @ApiOperation(value = "商品总库菜单-统计商品在各个渠道的订单数....")
    public HttpResponse prodisor(@Valid @RequestBody ProdisorRequest info) {

    	
    	LOGGER.info("商品总库菜单-统计商品在各个渠道的订单数参数 {}",info);    	
        return orderDetailService.prodisor(info);
    }

    /**
     *  商品总库-统计商品在不同渠道的订单数
     */
    @PostMapping("/product/store")
    @ApiOperation(value = "商品总库菜单-统计商品在各个渠道的订单数....")
    public HttpResponse productStore(@Valid @RequestBody ProductStoreRequest info) {


        LOGGER.info("商品总库菜单-统计商品在各个渠道的订单数参数 {}",info);
        return orderDetailService.productStore(info);
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

    	
    	LOGGER.info("sku销量统计参数 sukList：{}",sukList);    	
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

    	
    	LOGGER.info("sku销量统计参数sukList {}",sukList);    	
        return orderDetailService.saveBatch(sukList);
    }
    
    /**
     * 顾客可能还想购买
     * sku销量统计 返回销量最高的sku
     * @param 
     * @return
     */
    @GetMapping("/want/buy")
    @ApiOperation(value = "顾客可能还想购买")
    public HttpResponse wantBuy(@Valid @RequestParam(required = true) List<String> sukList) {

    	
    	LOGGER.info("sku销量统计参数 sukList：{}",sukList);    	
        return orderDetailService.wantBuy(sukList);
    }

    /**
     *
     * @param sukList
     * @return
     */
    @GetMapping("/findOrderDetailById")
    @ApiOperation(value = "通过ID查询订单详情")
    public HttpResponse findOrderDetailById(@Valid @RequestParam("order_detail_id") String orderDetailId) {


        LOGGER.info("sku销量统计参数 sukList：{}",orderDetailId);
        return orderDetailService.findOrderDetailById(orderDetailId);
    }

    /**
     * 通过多个order code 查询订单
     */
    @PostMapping("/findListByOrderCode")
    @ApiOperation(value = "通过ID查询订单详情")
    public HttpResponse findListByOrderCode(@RequestBody List<String> orderCodeList) {

        String jsonString = JSON.toJSONString(orderCodeList);
        LOGGER.info("request data :{}",jsonString);
        return orderDetailService.findListByOrderCode(orderCodeList);
    }
}
