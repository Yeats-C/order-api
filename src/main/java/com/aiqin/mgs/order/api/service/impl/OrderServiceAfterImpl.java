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
import com.aiqin.mgs.order.api.dao.OrderAfterDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderAfterService;
import com.aiqin.mgs.order.api.service.OrderService;

@SuppressWarnings("all")
@Service
public class OrderServiceAfterImpl implements OrderAfterService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceAfterImpl.class);
	
	
	@Resource
    private OrderAfterDao orderAfterDao;


	@Override
	public HttpResponse selectOrderAfter(@Valid OrderAfterSaleInfo orderAfterInfo) {
		
		return orderAfterDao.selectOrderAfter(orderAfterInfo);
	}


	@Override
	public HttpResponse selectOrderAfterDetail(String afterSaleId) {
		
		return orderAfterDao.selectOrderAfterDetail(afterSaleId);
	}




	

}
