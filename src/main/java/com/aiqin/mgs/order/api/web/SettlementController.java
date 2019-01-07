/*****************************************************************

* 模块名称：结算后台-入口
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.SettlementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/settlement")
@Api("结算相关操作接口")
@SuppressWarnings("all")
public class SettlementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementController.class);
    
    @Resource
    private SettlementService settlementService;
    
//    @Resource
//    private CartService cartService;
    
    

    
    //1.接口-结算数据查询
    @PostMapping("/jkselectsettlement")
    @ApiOperation(value = "接口-结算数据查询")
    public HttpResponse jkselectsettlement(@Valid @RequestBody OrderQuery orderQuery) {
        
    	LOGGER.info("接口-结算数据查询......");
    	
        return settlementService.jkselectsettlement(orderQuery);
    
    }
    
    
//    //添加新的结算数据
//    @PostMapping("")
//    @ApiOperation(value = "添加新的结算数据....")
//    public HttpResponse addSettlement(@Valid @RequestBody SettlementInfo settlementInfo,@Valid @RequestParam(name = "order_id", required = false) String orderId) {
//    	
//    	
//        LOGGER.info("添加新的结算数据...");
//        return settlementService.addSettlement(settlementInfo,orderId);
//    
//    }
    
    
//    //添加新的支付数据
//    @PostMapping("/addorderpaylist")
//    @ApiOperation(value = "添加新的支付数据....")
//    public HttpResponse addOrderPayList(@Valid @RequestBody List<OrderPayInfo> OrderPayList,@Valid @RequestParam(name = "order_id", required = false) String orderId) {
//    	
//    	
//        LOGGER.info("添加新的支付数据...");
//        return settlementService.addOrderPayList(OrderPayList,orderId);
//    
//    }
    
    
  //查询支付数据通过Order_id
    @GetMapping("/pay")
    @ApiOperation(value = "查询支付数据通过Order_id....")
    public HttpResponse pay(@Valid @RequestParam(name = "order_id", required = true) String orderId) {
    	
    	
        LOGGER.info("查询支付数据通过Order_id...");
        return settlementService.pay(orderId);
    
    }
    
    
    
    
//    //1.結算頁面列表  仅支持 购物车跳转结账页面/选择优惠类型.
//    @GetMapping("/selectsettlement")
//    @ApiOperation(value = "結算頁面列表")
//    public HttpResponse selectSettlement(@Valid @RequestParam(name = "member_id", required = true) String memberId,@RequestParam(name = "agio_type", required = true) String agioType) {
//        
//    	LOGGER.info("結算頁面列表......");
//
//    	List<CartInfo> infolist = cartService.getCartInfoList(memberId,agioType);//购物车展示列表
//    	
//        return settlementService.selectSettlement(infolist);
//    
//    }    
//    //3.结算页面动态刷新元素  支持 输入金额/输入积分/舍零 返回结算信息
//    @PostMapping("/ajaxsettlement")
//    @ApiOperation(value = "结算页面动态刷新元素")
//    public HttpResponse ajaxsettlement(@Valid @RequestBody SettlementInfo settlementInfo) {
//        
//    	LOGGER.info("结算页面动态刷新元素......");
//    	
//        return settlementService.ajaxsettlement(settlementInfo);
//    
//    }
    
    
//    //5.跟据前端传过来的支付方式,如果是现金支付 直接存库生成已支付订单, 非现金支付调用第三方判断生成什么类型的订单.
//    @PostMapping("/settlement")
//    @ApiOperation(value = "去结账......")
//    public HttpResponse settlement(@Valid @RequestBody SettlementInfo info,@Valid @RequestBody List<CartInfo> cartInfoList,
//    		 OrderQuery conditionInfo) {
//        
//    	LOGGER.info("去结账......");
//    	
//        return settlementService.settlement(info,cartInfoList,conditionInfo);
//    
//    }
    
//    @GetMapping("/TEST1")
//    @ApiOperation(value = "测试工具")
//    public void Xiadan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//	}
//    //PayResult
//    @GetMapping("/payresult")
//    @ApiOperation(value = "支付结果测试工具")
//    public void PayResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	String reqParams = StreamUtil.read(request.getInputStream());
//		System.out.println("-------支付结果:"+reqParams);
//		StringBuffer sb = new StringBuffer("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
//		response.getWriter().append(sb.toString());
//	}
}
