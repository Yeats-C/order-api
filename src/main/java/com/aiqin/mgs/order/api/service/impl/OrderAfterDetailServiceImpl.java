/*****************************************************************

* 模块名称：订单售后明细后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.util.ArrayList;
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
import com.aiqin.mgs.order.api.domain.OrderAfteIListInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderAfterDetailService;
import com.aiqin.mgs.order.api.service.OrderAfterService;
//import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.util.OrderPublic;

@SuppressWarnings("all")
@Service
public class OrderAfterDetailServiceImpl implements OrderAfterDetailService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderAfterDetailServiceImpl.class);
	
	@Resource
    private OrderAfterDao orderAfterDao;
	
	@Resource
    private OrderAfterDetailDao orderAfterDetailDao;
	
//	@Resource
//    private OrderLogService orderLogService;

	//添加新的订单售后明细数据
	@Override
	public void addAfterOrderDetail(@Valid List<OrderAfterSaleDetailInfo> afterDetailList,String afterSaleId) throws Exception {
		
		 if(afterDetailList !=null && afterDetailList.size()>0) {
			for(OrderAfterSaleDetailInfo info : afterDetailList) {
				
				//订单售后ID、订单售后明细ID
				info.setAfterSaleId(afterSaleId);
				info.setAfterSaleDetailId(OrderPublic.getUUID());
				
				//保存售后数据
				orderAfterDetailDao.addAfterOrderDetail(info);
			}
		 }
	}


    //售后id、门店查询售后维权明细列表
	@Override
	public HttpResponse OrderAfteIList(@Valid OrderAfterSaleQuery orderAfterSaleQuery) {
		
		//返回对象
		OrderAfteIListInfo list = new OrderAfteIListInfo();

		try {
			
			//组装订单售后主表
			OrderAfterSaleInfo afterinfo = new OrderAfterSaleInfo();
			if(orderAfterSaleQuery.getAfterSaleId()!=null && !orderAfterSaleQuery.getAfterSaleId().equals("")) {
				afterinfo = orderAfterDao.selectOrderAfterById(orderAfterSaleQuery);
				list.setOrderaftersaleinfo(afterinfo);
				
				//组装订单售后明细表
				List<OrderAfterSaleDetailInfo> orderAfterSaleDetailInfolist = new ArrayList();
				orderAfterSaleDetailInfolist = orderAfterDetailDao.selectOrderAfterDetail(orderAfterSaleQuery);
				list.setOrderAfterDetailList(orderAfterSaleDetailInfolist);
			}
			
			return HttpResponse.success(list);
			
		} catch (Exception e) {
			
			LOGGER.info("售后id、门店查询售后维权明细列表報錯", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
}
