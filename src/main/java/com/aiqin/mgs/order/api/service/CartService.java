/*****************************************************************

* 模块名称：购物车后台-接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;

@SuppressWarnings("all")
public interface CartService {

	
    HttpResponse addCartInfo(CartInfo cartInfo);  //添加购物车

    HttpResponse selectCartByMemberId(String memberId,String agioType);   //根据会员ID查询购物车
    
    List<CartInfo> getCartInfoList(String memberId,String agioType);   //根据会员ID查询购物车
    
	HttpResponse deleteCartInfo(String memberId);  //清空购物车
	
	HttpResponse deleteCartInfoById(String memberId,String skuCode);  //删除购物车购物清单

	HttpResponse updateCartByMemberId(@Valid CartInfo cartInfo);  //更新购物车清单
}
