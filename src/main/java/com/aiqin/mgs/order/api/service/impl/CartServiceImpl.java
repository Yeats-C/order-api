/*****************************************************************

* 模块名称：购物车后台-实现层
* 开发人员: 黄祉壹
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

@SuppressWarnings("all")
@Service
public class CartServiceImpl implements CartService{

	private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);
	
	
	@Resource
    private CartDao cartDao;
	
	
	@Override
	@Transactional
	public HttpResponse addCartInfo(CartInfo cartInfo) {
		
		try {
			
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
			
			
			return HttpResponse.success();
			
		}catch(Exception e) {
			
			LOGGER.error("添加购物车失败", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
			
		}
	}

	
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

	
	
	@Override
	public HttpResponse selectCartByMemberId(String memberId,String agioType) {
		
		try {
			
			LOGGER.info("根据会员ID查询购物车数据", memberId);
			if(memberId !=null && !memberId.equals("")) {
				
				//购物车数据
				List<CartInfo> cartInfoList=getCartInfoList(memberId,agioType);
				
				//避免二次查询数据库 总数据量-分页
				if(cartInfoList !=null && cartInfoList.size()>0) {
					CartInfo cartInfo = cartInfoList.get(0);
					int total = cartInfo.getRowno();  

				return HttpResponse.success(new PageResData(total,cartInfoList));
				}
				
				return HttpResponse.success();
			}else {
				LOGGER.error("根据会员ID查询购物车数据失败", memberId);
				return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
			}
			
		} catch (Exception e) {
			
			LOGGER.error("根据会员ID查询购物车数据失败", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}


	public List<CartInfo> getCartInfoList(String memberId, String agioType){

		
		//购物车数据
		List<CartInfo> cartInfoList=null;
		
		try {
			cartInfoList = cartDao.selectCartByMemberId(memberId);
			
		} catch (Exception e) {
			
			LOGGER.error("根据会员ID查询购物车数据失败", e);
		}
		CartInfo cartInfo = new CartInfo();
		if(cartInfoList !=null && cartInfoList.size()>0) {
			cartInfo = cartInfoList.get(0);
		}
		
	    //调用商品接口 返回商品信息
		//未完成cartInfoList = null; 
			
		if(agioType !=null && !agioType.equals("")) {
			
		}else {
           agioType =Global.AGIOTYPE_1;
		}	
		//调用优惠券接口  返回 优惠券信息   
	    //未完成cartInfoList = cartInfoList,agioType; 

		
		//得到总数量、总应付金额、总优惠、总实际金额
        getAcount(cartInfo,cartInfoList);
		
		return cartInfoList;
	}


	private void getAcount(CartInfo cartInfo,List<CartInfo> cartInfoList) {
		
		int acountAmount = 0 ; 
        int acountTotalprice =0 ;
        int activityDiscount = 0 ;
        int acountActualprice = 0;
        
        
        
        
		for(CartInfo cart : cartInfoList) {
			
			//测试字段  未完成
			cart.setRetailPrice(50);
			
			
			//总数量
			acountAmount += cart.getAmount();
			
			//总应付金额
			int productTotalprice =cart.getRetailPrice()*cart.getAmount(); 
			acountTotalprice +=productTotalprice;
			
			//总优惠   限时折扣 （商品*折扣）*数量  （商品实际金额*数量） -优惠金额
			activityDiscount= 0;
			
			//总实际金额
			acountActualprice = acountTotalprice - activityDiscount;
			
		}
		
		cartInfo.setAcountAmount(acountAmount);
		cartInfo.setAcountActualprice(acountActualprice);
		cartInfo.setActivityDiscount(activityDiscount);
		cartInfo.setAcountActualprice(acountActualprice);
		
		cartInfoList.set(0, cartInfo);
		
	}


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


	@Override
	@Transactional
	public HttpResponse deleteCartInfoById(CartInfo cartInfo) {
		
		try {
			LOGGER.error("删除购物清单",cartInfo);
			if(cartInfo !=null) {
				if(cartInfo.getMemberId() !=null && !cartInfo.equals("") && 
				   cartInfo.getSkuCode() !=null && !cartInfo.getSkuCode().equals("")) {
					cartDao.deleteCartInfoById(cartInfo);
				}else {
					return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
				}
			}
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.error("删除购物清单失败",cartInfo);
			return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
		}
		
	}
}
