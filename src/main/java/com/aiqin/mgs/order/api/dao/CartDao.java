/*****************************************************************

* 模块名称：购物车后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import com.aiqin.mgs.order.api.domain.CartInfo;



public interface CartDao {
    
	//微商城-添加/修改购物车信息0:添加 1:修改....
    void insertCart(CartInfo cartInfo) throws Exception;

    //根据会员ID查询购物车
    List<CartInfo> selectCartByMemberId(CartInfo cartInfoe) throws Exception;

    //清空购物车
    void deleteCartInfo(String memberId) throws Exception;
    
    //删除购物车购物清单
    void deleteCartInfoById(CartInfo cartInfo) throws Exception;
    
    //调用购物车修改逻辑
    void updateCartById(CartInfo cartInfo) throws Exception;

    //判断SKU商品是否已存在购物车中
    String isYesCart(CartInfo cartInfo) throws Exception;
    
}
