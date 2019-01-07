/*****************************************************************

* 模块名称：购物车后台-入口
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
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
import java.util.List;

@RestController
@RequestMapping("/cart")
@Api("购物车相关操作接口")
@SuppressWarnings("all")
public class CartController {

    
	private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    @Resource
    private CartService cartService;
    
    
    /**
    * 微商城-添加/修改购物车信息
    * @param cartInfo
    * @return
    */
    @PostMapping("")
    @ApiOperation(value = "微商城-添加/修改购物车信息0:添加1:修改....")
    public HttpResponse addNewCart(@Valid @RequestBody CartInfo cartInfo) {
 	
 	
        LOGGER.info("微商城-添加/修改购物车信息0:添加1:修改......",cartInfo);
        return cartService.addCartInfo(cartInfo);
 
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
    public HttpResponse deleteCartInfoById(@Valid @RequestParam(name = "member_id", required = true) String memberId,
    		@Valid @RequestBody List<String> skuCodes,
    		@RequestParam(name = "distributor_id", required = true) String distributorId) {
        
        LOGGER.info("删除购物车购物清单......");
        return cartService.deleteCartInfoById(memberId,skuCodes,distributorId);
    }

    
   /**
   * 购物车展示列表
   * @param memberId
   * @return
   */
    @GetMapping("/selectcartbymemberid")
    @ApiOperation(value = "购物车展示列表")
    public HttpResponse selectCartByMemberId(@Valid @RequestParam(name = "member_id", required = true) String memberId,
    	@RequestParam(name = "distributor_id", required = true) String distributorId,
 		@Valid @RequestParam(name = "page_no", required = true) Integer pageNo,
 		@Valid @RequestParam(name = "page_size", required = true) Integer pageSize
 		) {
     
 	    LOGGER.info("购物车展示列表......");    	
        return cartService.selectCartByMemberId(memberId,distributorId,pageNo,pageSize);//购物车展示列表
    }
    
}
