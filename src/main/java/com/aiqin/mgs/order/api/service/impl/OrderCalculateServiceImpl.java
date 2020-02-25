/*****************************************************************

* 模块名称：订单统计-实现层
* 开发人员: 黄祉壹
* 开发时间: 2020-02-19 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
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
import com.aiqin.mgs.order.api.dao.OrderCalculateDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderAfteIListInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleQuery;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderMonthCalculateInfo;
import com.aiqin.mgs.order.api.domain.ReportCategoryVo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderAfterDetailService;
import com.aiqin.mgs.order.api.service.OrderAfterService;
import com.aiqin.mgs.order.api.service.OrderCalculateService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;

@SuppressWarnings("all")
@Service
public class OrderCalculateServiceImpl implements OrderCalculateService{

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderAfterDetailServiceImpl.class);
	
	@Resource
    private OrderCalculateDao orderCalculateDao;

	
	/**
	 * 当月 门店 销售额 汇总
	 */
	@Override
	public List<OrderMonthCalculateInfo> copartnerMonth() {
		
		//查询当月已完成订单所有的门店
		List<OrderMonthCalculateInfo> storeList = new ArrayList();
		storeList = orderCalculateDao.qryFinish(DateUtil.getFristOfMonthDay(new Date()),DateUtil.getLashOfMonthDay(new Date()));
		
		if(CollectionUtils.isNotEmpty(storeList)) {
			for(int i =0;i<storeList.size();i++) {
				OrderMonthCalculateInfo info = new OrderMonthCalculateInfo();
				info = storeList.get(i);
				String storeId = info.getStoreId();
				//总销售实际值
				int totalFinish= orderCalculateDao.qryTotalFinish(storeId,DateUtil.getFristOfMonthDay(new Date()),DateUtil.getLashOfMonthDay(new Date()));
				info.setTotalFinish(totalFinish);
				
				//18SA销售实际值
				String dictionaryContent = "18SA";
				int eighteenFinish= orderCalculateDao.qryEighteenFinish(storeId,dictionaryContent,DateUtil.getFristOfMonthDay(new Date()),DateUtil.getLashOfMonthDay(new Date()));
				info.setEighteenFinish(eighteenFinish);
				
				//服纺销售实际值
				String categoryCode = "11";
				int textileList= orderCalculateDao.qryTextileFinish(storeId,categoryCode,DateUtil.getFristOfMonthDay(new Date()),DateUtil.getLashOfMonthDay(new Date()));
				info.setTextileFinish(textileList);
				
				//汇总日期
				info.setCalculateMonth(DateUtil.afterMonth(0));
				
				storeList.set(i, info);
			}
		}
		
		return storeList;
	}


	@Override
	public List<ReportCategoryVo> storeCategoryCopartnerMonth() {
		//查询当月已完成订单所有的门店
		List<ReportCategoryVo> storeList = new ArrayList();
		storeList = orderCalculateDao.qryCategoryFinish(DateUtil.getFristOfMonthDay(new Date()),DateUtil.getLashOfMonthDay(new Date()));
		
		if(CollectionUtils.isNotEmpty(storeList)) {
			for(int i =0;i<storeList.size();i++) {
				ReportCategoryVo info = new ReportCategoryVo();
				info = storeList.get(i);
				String storeId = info.getStoreId();
				String childCategoryCode = info.getChildCategoryCode();
				
				//子级别销售数量
				int totalAmount= orderCalculateDao.qryCategoryAmountFinish(storeId,childCategoryCode,DateUtil.getFristOfMonthDay(new Date()),DateUtil.getLashOfMonthDay(new Date()));
				info.setTotalAmount(totalAmount);
				
				//子级别含税销售金额
				int totalPrice= orderCalculateDao.qryTextileFinish(storeId,childCategoryCode,DateUtil.getFristOfMonthDay(new Date()),DateUtil.getLashOfMonthDay(new Date()));
				info.setTotalPrice(totalPrice);
				
				//汇总日期
				info.setCalculateMonth(DateUtil.afterMonth(0));
				
				storeList.set(i, info);
			}
		}
		
		return storeList;
	}	
}
