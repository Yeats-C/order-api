/*****************************************************************

* 模块名称：服务订单后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2019-02-15 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderNoCodeInfo;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderRelationCouponInfo;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.DetailCouponRequest;
import com.aiqin.mgs.order.api.domain.request.DistributorMonthRequest;
import com.aiqin.mgs.order.api.domain.request.MemberByDistributorRequest;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.OrderNoCodeRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse.SelectSaleViewResonse;
import com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse.SelectSumByStoreIdResonse;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderNoCodeService;
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
@RequestMapping("/orderNoCode")
@Api(tags = "服务订单相关操作接口")
@SuppressWarnings("all")
public class OrderNoCodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderNoCodeController.class);
    
    @Resource
    private OrderNoCodeService orderNoCodeService;
    
    
    /**
     * 订单概览统计
     * @param 
     * @return
     */
    @GetMapping("/ncass")
    @ApiOperation(value = "订单概览统计")
    public HttpResponse<SelectSumByStoreIdResonse> ncass(@Valid @RequestParam(name = "distributor_id", required = false) String distributorId){
        
    	
    	LOGGER.info("订单概览统计参数distributorId：{}",distributorId);
        return HttpResponse.success(orderNoCodeService.selectSumByStoreId(distributorId));
    }
    
    /**
     * 商品类别销售概况
     * @param 
     * @return
     */
    @GetMapping("/ncbss")
    @ApiOperation(value = "商品类别销售概况")
    public HttpResponse<SelectSaleViewResonse> ncbss(@Valid @RequestParam(name = "distributor_id", required = false) String distributorId,
    		@Valid @RequestParam(name = "begin_date", required = false) String beginDate,
    		@Valid @RequestParam(name = "end_date", required = false) String endDate){
        
    	
    	LOGGER.info("商品类别销售概况参数 distributorId：{},beginDate：{},endDate：{}",distributorId,beginDate,endDate);
        return HttpResponse.success(orderNoCodeService.selectSaleView(distributorId,beginDate,endDate));
    }
    
//    /**
//     * 订单列表  --如打开使用请分析SQL
//     * @param 
//     * @return
//     */
//    @PostMapping("/nccss")
//    @ApiOperation(value = "订单列表")
//    public HttpResponse<List<OrderNoCodeInfo>> ncbss(@Valid @RequestBody OrderNoCodeRequest orderNoCodeBuyRequest){
//        LOGGER.info("订单列表.");
//        return HttpResponse.success(orderNoCodeService.selectNoCodeList(orderNoCodeBuyRequest));
//    }
    
    
    /**
     * 编号查询订单
     * @param 
     * @return
     */
    @GetMapping("/ncdss")
    @ApiOperation(value = "编号查询订单.")
    public HttpResponse<OrderInfo> selde(@Valid @RequestParam(name = "order_code", required = true) String orderCode) {
        
    	
    	LOGGER.info("编号查询订单参数：{}",orderCode);    	
        return orderNoCodeService.selectorderByCode(orderCode);
    }

    /**
     * 编号查询订单
     * @param
     * @return
     */
    @GetMapping("/ncdssPre")
    @ApiOperation(value = "编号查询订单.")
    public HttpResponse<OrderInfo> seldePre(@Valid @RequestParam(name = "order_code", required = true) String orderCode) {


        LOGGER.info("编号查询订单参数：{}",orderCode);
        return orderNoCodeService.selectorderPreByCode(orderCode);
    }
}
