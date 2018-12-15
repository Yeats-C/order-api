/*****************************************************************

* 模块名称：购物车后台-接口层
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartInfo;

@SuppressWarnings("all")
public interface CartService {
	
	//微商城-添加/修改购物车信息0:添加1:修改....
	HttpResponse addCartInfo(CartInfo cartInfo);    
    
	//根据会员ID查询购物车
    HttpResponse selectCartByMemberId(String memberId,String distributorId,Integer pageNo,Integer pageSize);   

    //清空购物车
	HttpResponse deleteCartInfo(String memberId);  
	
	//删除购物车购物清单
	HttpResponse deleteCartInfoById(String memberId,List<String> skuCodes,String distributorId);  

	//更新购物车清单
	HttpResponse updateCartByMemberId(@Valid CartInfo cartInfo);  
}
