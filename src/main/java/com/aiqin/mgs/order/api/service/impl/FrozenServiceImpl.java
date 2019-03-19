/*****************************************************************

* 模块名称：挂单解挂后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.FrozenDao;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.FrozenService;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;

@SuppressWarnings("all")
@Service
public class FrozenServiceImpl implements FrozenService{

	private static final Logger LOGGER = LoggerFactory.getLogger(FrozenServiceImpl.class);
	
	
	@Resource
    private FrozenDao frozenDao;


	//挂单入库
	@Override
	@Transactional
	public HttpResponse addFrozenInfo(List<FrozenInfo> frozenInfolist) {

		try {
			//生成挂单ID
			String frozenId =DateUtil.sysDate()+OrderPublic.randomNumberF();
			
			if(frozenInfolist !=null && frozenInfolist.size()>0) {
			for(FrozenInfo info : frozenInfolist ) {
				LOGGER.info("挂单入库 {}", frozenInfolist);
				
				info.setFrozenId(frozenId);
				frozenDao.insert(info);
			}
			}
			return HttpResponse.success();
			
		} catch (Exception e) {
			LOGGER.error("挂单入库失败 {}", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
		
	}


	//查询挂单明细
	@Override
	public HttpResponse selectDetailByFrozenId(String frozenId) {
		
		try {
			LOGGER.info("查询挂单明细 {}", frozenId);
			return HttpResponse.success(frozenDao.selectDetailByFrozenId(frozenId));
		} catch (Exception e) {
			LOGGER.error("查询挂单明细失败:{}", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}


	//解挂
	@Override
	@Transactional
	public HttpResponse deleteByFrozenId(String frozenId) {
		
		try {
			LOGGER.info("解挂 {}", frozenId);
			frozenDao.deleteByFrozenId(frozenId);
			return HttpResponse.success();
		} catch (Exception e) {
			LOGGER.error("解挂失败 {}", e);
			return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
		}
	}


	//查询挂单汇总
	@Override
	public HttpResponse selectSumByFrozenId(String createBy,String distributorId) {
		
		try {
			LOGGER.info("查询挂单汇总 distributorId: {},createBy: {}", distributorId,createBy);
			return HttpResponse.success(frozenDao.selectSumByFrozenId(createBy,distributorId));
		} catch (Exception e) {
			LOGGER.error("查询挂单汇总 {}",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	@Override
	public HttpResponse selectDetail(String createBy, String distributorId) {
		 
		try {
			LOGGER.info("查询挂单明细 {}", createBy);
			return HttpResponse.success(frozenDao.selectDetail(createBy,distributorId));
		} catch (Exception e) {
			LOGGER.error("查询挂单明细失败 {}", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//查询挂单数量
	@Override
	public HttpResponse selectSumByParam(@Valid String createBy, String distributorId) {
		
		try {
			Integer frozenSum = null;
			frozenSum = frozenDao.selectSumByParam(createBy,distributorId);
			if(frozenSum == null) {
				frozenSum =0;
			}
			return HttpResponse.success(frozenSum);
		} catch (Exception e) {
			LOGGER.error("查询挂单数量失败 {}", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
	
}
