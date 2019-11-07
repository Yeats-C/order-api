/*****************************************************************

* 模块名称：购物车后台-实现层
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.AddProduct2CartRequest;
import com.aiqin.mgs.order.api.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


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
		    		LOGGER.info("判断SKU商品是否已存在购物车中:{}", cartInfo);
					String OldAount = cartDao.isYesCart(cartInfo);
					
					if(OldAount !=null && !OldAount.equals("")) {
						
						//已存在购物车的、新添加+已存在购物车的数量=真实数量
						LOGGER.info("更新购物车:{}", cartInfo);
						int newAount = Integer.valueOf(OldAount)+cartInfo.getAmount();
						cartInfo.setAmount(newAount);
						
						//更新购物车
						cartDao.updateCartById(cartInfo);
						
					}else {
						
						//添加购物车
						LOGGER.info("添加购物车:{}", cartInfo);
						cartDao.insertCart(cartInfo);
					}
					return HttpResponse.success();
		    	}
		    }else {
		    	
				LOGGER.warn("购物车信息为空!");
				return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
			}
		}catch(Exception e) {
			
			LOGGER.error("添加购物车异常：{}", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
	}

	@Override
	public HttpResponse addProduct(AddProduct2CartRequest addProduct2CartRequest) {
		//检查库存

		//判断库存是否满足

		return null;
	}


	//更新购物车清单
	@Override
	@Transactional
	public HttpResponse updateCartByMemberId(CartInfo cartInfo) {
		
		LOGGER.info("更新购物车清单参数：{}",cartInfo);
		
		try {
			cartDao.updateCartById(cartInfo);
			return HttpResponse.success();
		} catch (Exception e) {
			
			LOGGER.error("更新购物车异常：{}", cartInfo);
			return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
		}
	
		
	}
	
	
	//根据会员ID查询购物车
	@Override
	public HttpResponse selectCartByMemberId(String memberId,String distributorId,Integer pageNo,Integer pageSize) {
		
		try {
			CartInfo cartInfo = new CartInfo();
			cartInfo.setMemberId(memberId);
			cartInfo.setPageNo(pageNo);
			cartInfo.setPageSize(pageSize);
			cartInfo.setDistributorId(distributorId);
				
			//购物车数据
			List<CartInfo> cartInfoList= cartDao.selectCartByMemberId(cartInfo);
				
			//计算总数据量
			Integer totalCount = 0;
			Integer icount =null;
			cartInfo.setIcount(icount);
			List<CartInfo> Icount_list= cartDao.selectCartByMemberId(cartInfo);
			if(Icount_list !=null && Icount_list.size()>0) {
				totalCount = Icount_list.size();
			}
				
			return HttpResponse.success(new PageResData(totalCount,cartInfoList));
			
		} catch (Exception e) {
			
			LOGGER.error("根据会员ID查询购物车数据异常：{}", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}
	

	//清空购物车
	@Override
	@Transactional
	public HttpResponse deleteCartInfo(String memberId) {
		
		try {

			if(memberId !=null && !memberId.equals("")) {
				cartDao.deleteCartInfo(memberId);
				return HttpResponse.success();
			}else {
				LOGGER.warn("清空购物车-找不到会员ID");
				return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
			}
			
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
			LOGGER.info("删除购物清单会员: {}",memberId);
			
			CartInfo cartInfo = new CartInfo();
			cartInfo.setMemberId(memberId);
            if(skuCodes !=null && skuCodes.size()>0) {
            	cartInfo.setSkuCodes(skuCodes);
            }
			cartInfo.setDistributorId(distributorId);
			cartDao.deleteCartInfoById(cartInfo);
			
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.error("删除购物清单失败 ： {}",e);
			return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
		}
		
	}
}
