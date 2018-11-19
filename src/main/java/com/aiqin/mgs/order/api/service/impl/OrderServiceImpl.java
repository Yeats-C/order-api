/*****************************************************************

* 模块名称：购物车后台-实现层
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
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderService;

@SuppressWarnings("all")
@Service
public class OrderServiceImpl implements OrderService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	
	@Resource
    private OrderDao orderDao;
	
	

	@Override
	public HttpResponse selectOrder(OrderInfo orderInfo) {
		
			LOGGER.info("模糊查询订单列表", orderInfo);

			try {
				return HttpResponse.success(orderDao.selectOrder(orderInfo));
			} catch (Exception e) {
				LOGGER.info("模糊查询订单列表報錯", e);
				return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
			}
			
	}



	//查询订单详细列表-带结算、退货信息......
	@Override
	public HttpResponse selectorderDetail(@Valid OrderInfo orderInfo) {
		LOGGER.info("查询订单详细列表-带结算、退货信息......", orderInfo);
		orderDao.selectorderDetail(orderInfo);
		return null;
	}
}
