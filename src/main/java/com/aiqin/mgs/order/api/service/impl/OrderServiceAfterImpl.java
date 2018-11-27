/*****************************************************************

* 模块名称：订单后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.util.List;

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
import com.aiqin.mgs.order.api.dao.OrderAfterDao;
import com.aiqin.mgs.order.api.dao.OrderAfterDetailDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderAfterService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.util.OrderPublic;

@SuppressWarnings("all")
@Service
public class OrderServiceAfterImpl implements OrderAfterService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceAfterImpl.class);
	
	
	@Resource
    private OrderAfterDao orderAfterDao;
	
	@Resource
    private OrderAfterDetailDao orderAfterDetailDao;


	//支持-条件查询售后维权列表 /条件查询退货信息
	@Override
	public HttpResponse selectOrderAfter(@Valid OrderAfterSaleQuery orderAfterSaleQuery) {
		
		List<OrderAfterSaleInfo> OrderAfterSaleInfolist;
		try {
			
			OrderAfterSaleInfolist = orderAfterDao.selectOrderAfter(orderAfterSaleQuery);
			//避免二次查询数据库 总数据量-分页
			int total =0;
			if(OrderAfterSaleInfolist !=null && OrderAfterSaleInfolist.size()>0) {
				OrderAfterSaleInfo orderAfterSaleInfo = OrderAfterSaleInfolist.get(0);
				total = orderAfterSaleInfo.getRowno();  
			}
			return HttpResponse.success(new PageResData(total,OrderAfterSaleInfolist));
		} catch (Exception e) {
			
			LOGGER.info("条件查询售后维权列表 /条件查询退货信息報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}


//	//条件查询售后维权明细列表
//	@Override
//	public HttpResponse selectOrderAfterDetail(@Valid OrderAfterSaleQuery orderAfterSaleQuery) {
//		
//		List<OrderAfterSaleDetailInfo>  orderAfterSaleDetailInfolist = orderAfterDetailDao.selectOrderAfterDetail(orderAfterSaleQuery);
//
//		//避免二次查询数据库 总数据量-分页
//		if(orderAfterSaleDetailInfolist !=null && orderAfterSaleDetailInfolist.size()>0) {
//			OrderAfterSaleDetailInfo orderAfterSaleDetailInfo = orderAfterSaleDetailInfolist.get(0);
//			int total = orderAfterSaleDetailInfo.getRowno();  
//
//		return HttpResponse.success(new PageResData(total,orderAfterSaleDetailInfolist));
//		}
//		return null;
//	}


	//添加新的订单售后数据
	@Override
	public HttpResponse addAfterOrder(@Valid OrderAfterSaleInfo orderAfterSaleInfo) {
		
		String after_sale_id = "";
		String after_sale_code = "";
		try {
			//生成订单售后ID
			after_sale_id = OrderPublic.getUUID();
			orderAfterSaleInfo.setAfterSaleId(after_sale_id);
			
			//生成订单售后编号
			after_sale_code = OrderPublic.currentDate()+String.valueOf(orderAfterSaleInfo.getOriginType())+String.valueOf(Global.ORDERID_channel_4)+OrderPublic.randomNumberF();
			orderAfterSaleInfo.setAfterSaleCode(after_sale_code);
			
			orderAfterDao.addAfterOrder(orderAfterSaleInfo);
			
			return HttpResponse.success(after_sale_id);
		
		} catch (Exception e) {
			LOGGER.info("添加新的订单售后数据報錯", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
	}




	

}
