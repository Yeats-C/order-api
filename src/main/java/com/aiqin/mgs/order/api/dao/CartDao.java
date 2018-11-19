/*****************************************************************

* 模块名称：购物车后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import com.aiqin.mgs.order.api.domain.CartInfo;

import io.lettuce.core.dynamic.annotation.Param;


public interface CartDao {
	

	void insertCart(CartInfo cartInfo) throws Exception;

	List<CartInfo> selectCartByMemberId(@Param("memberId")String memberId) throws Exception;
	
	int selectCartBySkuCode(CartInfo cartInfoe) throws Exception;

	void deleteCartInfo(String memberId) throws Exception;
	
	void deleteCartInfoById(CartInfo cartInfo) throws Exception;
	
	void updateCartById(CartInfo cartInfo) throws Exception;

	String isYesCart(CartInfo cartInfo) throws Exception;
	
}
