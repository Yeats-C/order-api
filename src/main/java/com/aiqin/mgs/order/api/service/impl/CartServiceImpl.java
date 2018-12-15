/*****************************************************************

* 模块名称：购物车后台-实现层
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.util.OrderPublic;

@SuppressWarnings("all")
@Service
public class CartServiceImpl implements CartService{

	private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);
	
	
	@Resource
    private CartDao cartDao;
	
	
	//微商城-添加/修改购物车信息0:添加 1:修改....
	@Override
	@Transactional
	public HttpResponse addCartInfo(CartInfo cartInfo) {
		
	    try {
		    if(cartInfo !=null) {
		    	if(cartInfo.getModityType()!=null && cartInfo.getModityType().equals(Global.MODIFY_TYPE_1)) {
		    		
		    		//调用购物车修改逻辑
		    		cartDao.updateCartById(cartInfo);
					return HttpResponse.success();
		    	}else {
		    		
		    		//调用添加购物车逻辑
		    		LOGGER.info("判断SKU商品是否已存在购物车中", cartInfo);
					String OldAount = cartDao.isYesCart(cartInfo);
					
					if(OldAount !=null && !OldAount.equals("")) {
						
						//已存在购物车的、新添加+已存在购物车的数量=真实数量
						LOGGER.info("更新购物车", cartInfo);
						int newAount = Integer.valueOf(OldAount)+cartInfo.getAmount();
						cartInfo.setAmount(newAount);
						
						//更新购物车
						cartDao.updateCartById(cartInfo);
						
					}else {
						
						//添加购物车
						LOGGER.info("添加购物车", cartInfo);
						cartDao.insertCart(cartInfo);
					}
		    	}
			
		    	return HttpResponse.success();
			
		    }else {
				LOGGER.error("购物车信息为空!");
				return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
			}
			
		}catch(Exception e) {
			
			LOGGER.error("添加购物车失败", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
			
		}
	}

	
	//更新购物车清单
	@Override
	@Transactional
	public HttpResponse updateCartByMemberId(CartInfo cartInfo) {
		LOGGER.info("更新购物车", cartInfo);
		
		try {
			
			cartDao.updateCartById(cartInfo);
			return HttpResponse.success();
			
		} catch (Exception e) {
			
			LOGGER.info("更新购物车失败", cartInfo);
			return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
		}
	
		
	}
	
	
	//根据会员ID查询购物车
	@Override
	public HttpResponse selectCartByMemberId(String memberId,String distributorId,Integer pageNo,Integer pageSize) {
		
		try {
			
			LOGGER.info("根据会员ID查询购物车数据", memberId);
			
			CartInfo cartInfo = new CartInfo();
			
			cartInfo.setMemberId(memberId);
			cartInfo.setPageNo(pageNo);
			cartInfo.setPageSize(pageSize);
			cartInfo.setDistributorId(distributorId);
				
				//购物车数据
				List<CartInfo> cartInfoList= cartDao.selectCartByMemberId(cartInfo);
				
				return HttpResponse.success(OrderPublic.getData(cartInfoList));
			
		} catch (Exception e) {
			
			LOGGER.error("根据会员ID查询购物车数据失败", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}
	

	//清空购物车
	@Override
	@Transactional
	public HttpResponse deleteCartInfo(String memberId) {
		
		try {
			LOGGER.error("清空购物车",memberId);
			if(memberId !=null && !memberId.equals("")) {
				cartDao.deleteCartInfo(memberId);
			}else {
				LOGGER.error("清空购物车-找不到会员ID",memberId);
				return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
			}
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.error("清空购物车失败",e);
			return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
		}

	}

	
	//删除购物车购物清单
	@Override
	@Transactional
	public HttpResponse deleteCartInfoById(String memberId,List<String> skuCodes,String distributorId) {
		
		try {
			LOGGER.error("删除购物清单",memberId);
			
					CartInfo cartInfo = new CartInfo();
					cartInfo.setMemberId(memberId);
					cartInfo.setSkuCodes(skuCodes);
					cartInfo.setDistributorId(distributorId);
					cartDao.deleteCartInfoById(cartInfo);
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.error("删除购物清单失败",e);
			return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
		}
		
	}
}
