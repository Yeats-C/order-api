/*****************************************************************

* 模块名称：挂单解挂后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

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
import com.aiqin.mgs.order.api.util.OrderPublic;

@SuppressWarnings("all")
@Service
public class FrozenServiceImpl implements FrozenService{

	private static final Logger LOGGER = LoggerFactory.getLogger(FrozenServiceImpl.class);
	
	
	@Resource
    private FrozenDao frozenDao;


	@Override
	@Transactional
	public HttpResponse addFrozenInfo(List<FrozenInfo> frozenInfolist) {
		
		
		
		try {
			
			//生成挂单ID
			String frozenId =OrderPublic.sysDate()+OrderPublic.randomNumberF();
			
			for(FrozenInfo info : frozenInfolist ) {
				LOGGER.info("挂单入库...", frozenInfolist);
				
				info.setFrozenId(frozenId);
				frozenDao.insert(info);
			}
			
			
			return HttpResponse.success();
		} catch (Exception e) {
			
			
			LOGGER.info("挂单入库失败", e);
			return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
		}
		
	}


	@Override
	public HttpResponse selectDetailByFrozenId(String frozenId) {
		
		try {
			
			LOGGER.info("查询挂单明细...", frozenId);
			return HttpResponse.success(frozenDao.selectDetailByFrozenId(frozenId));
		} catch (Exception e) {
			
			LOGGER.info("查询挂单明细失败", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
	}


	@Override
	@Transactional
	public HttpResponse deleteByFrozenId(String frozenId) {
		
		try {
			
			LOGGER.info("解挂...", frozenId);
			frozenDao.deleteByFrozenId(frozenId);
			return HttpResponse.success();
		} catch (Exception e) {
			
			LOGGER.info("解挂失败", e);
			return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
		}
	}


	@Override
	public HttpResponse selectSumByFrozenId(String saleById,String distributorId) {
		
		try {
			
			LOGGER.info("查询挂单汇总...", distributorId+"."+saleById);
			return HttpResponse.success(frozenDao.selectSumByFrozenId(saleById,distributorId));
			
			
		} catch (Exception e) {
			
			LOGGER.info("查询挂单汇总",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
	
}
