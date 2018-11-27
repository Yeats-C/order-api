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
@RequestMapping("/cart")
@Api("购物车相关操作接口")
@SuppressWarnings("all")
public class CartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    
    @Resource
    private CartService cartService;
    
    
    
    /**
     * 微商城-添加购物车信息
     * @param cartInfo
     * @return
     */
    @PostMapping("")
    @ApiOperation(value = "添加购物车")
    public HttpResponse addNewCart(@Valid @RequestBody CartInfo cartInfo) {
    	
    	
        LOGGER.info("添加购物车......",cartInfo);
        return cartService.addCartInfo(cartInfo);
    
    }
    
    
    /**
     * 微商城-更新购物车
     * @param cartInfo
     * @return
     */
    @PostMapping("/updatecartbymemberid")
    @ApiOperation(value = "更新购物车")
    public HttpResponse updateCartByMemberId(@Valid @RequestBody CartInfo cartInfo) {
        
        LOGGER.info("更新购物车......");
    	
    	
        return cartService.updateCartByMemberId(cartInfo);
    
    }
    
    
    /**
     * 微商城-清空购物车
     * @param memberId
     * @return
     */
    @DeleteMapping("/deletecartinfo/{member_id}")
    @ApiOperation(value = "清空购物车")
    public HttpResponse deleteCartInfo(@Valid @PathVariable(name = "member_id") String memberId) {
        
        LOGGER.info("清空购物车......");
        return cartService.deleteCartInfo(memberId);
    }
    
    
    /**
     * 微商城-删除购物车购物清单
     * @param cartInfo
     * @return
     */
    @DeleteMapping("/deletecartinfobyid")
    @ApiOperation(value = "根据商品SKU删除购物车购物清单") 
    public HttpResponse deleteCartInfoById(@Valid @RequestParam(name = "member_id", required = true) String memberId,@RequestParam(name = "sku_code", required = true) String skuCode) {
        
        LOGGER.info("删除购物车购物清单......");
        return cartService.deleteCartInfoById(memberId,skuCode);
    }
    
    
    /**
     * 购物车展示列表 默认 agioType:折扣类型
     * @param memberId
     * @return
     */
    @GetMapping("/selectcartbymemberid")
    @ApiOperation(value = "购物车展示列表")
    public HttpResponse selectCartByMemberId(@Valid @RequestParam(name = "member_id", required = true) String memberId,@RequestParam(name = "agio_type", required = true) String agioType,
    		@Valid @RequestParam(name = "page_no", required = true) String pageNo,
    		@Valid @RequestParam(name = "page_size", required = true) String pageSize
    		) {
        
    	
    	LOGGER.info("购物车展示列表......");    	
        return cartService.selectCartByMemberId(memberId,agioType);//购物车展示列表
    }
    

}
