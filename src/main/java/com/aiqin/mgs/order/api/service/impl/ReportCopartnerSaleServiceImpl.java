/*****************************************************************
 * 
 * 模块名称：合伙人销售报表-实现层
 * 开发人员: huangzy
 * 开发时间: 2020-02-19
 * 
 * ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;


import javax.annotation.Resource;
import javax.validation.Valid;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.mgs.order.api.component.*;
import com.aiqin.mgs.order.api.component.PayTypeEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaDetail;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaListReq;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleDetail;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaSave;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.NewStoreTreeResponse;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
import com.aiqin.mgs.order.api.domain.copartnerArea.SystemResource;
import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.domain.request.*;
import com.aiqin.mgs.order.api.domain.response.*;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.util.DayUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.mgs.order.api.domain.request.DevelRequest;
import com.aiqin.mgs.order.api.domain.request.DistributorMonthRequest;
import com.aiqin.mgs.order.api.domain.request.MemberByDistributorRequest;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.OrderIdAndAmountRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.request.returnorder.AreaReq;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.SelectByMemberPayCountResponse;
import com.aiqin.mgs.order.api.domain.response.WscSaleResponse;
import com.aiqin.mgs.order.api.domain.response.WscWorkViewResponse;
import com.aiqin.mgs.order.api.domain.response.DistributorMonthResponse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.domain.response.LatelyResponse;
import com.aiqin.mgs.order.api.domain.response.MevBuyResponse;
import com.aiqin.mgs.order.api.domain.response.OradskuResponse;

@SuppressWarnings("all")
@Slf4j
@Service
public class ReportCopartnerSaleServiceImpl implements ReportCopartnerSaleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportCopartnerSaleServiceImpl.class);

    @Resource
    private ReportCopartnerSaleDao reportCopartnerSaleDao;
    
    @Autowired
    private CopartnerAreaService copartnerAreaService;

	@Override
	public HttpResponse qryReportPageList(String reportYear, String reportMonth, Integer pageNo, Integer pageSize) {
		
		ReportCopartnerSaleVo vo = new ReportCopartnerSaleVo();
		try {
			
			List<ReportCopartnerSaleInfo> dataList = new ArrayList();
					
			//区域权限控制
			String resourceCode = "ERP012006";
			List<String> storeIds = getStoreIds(resourceCode);
			if(CollectionUtils.isEmpty(storeIds)) {
				return HttpResponse.success(new PageResData(0,dataList));
			}
			//门店所属区域
			List<String> areaIds = copartnerAreaService.qryAreaByStores(storeIds);
			if(CollectionUtils.isNotEmpty(areaIds)) {
				vo.setAreaIds(areaIds);
			}
			vo.setReportYear(reportYear);
			vo.setReportMonth(reportMonth);
			vo.setPageNo(pageNo);
			vo.setPageSize(pageSize);
			vo.setStoreIds(storeIds);
			dataList = reportCopartnerSaleDao.selectMainPageList(vo);
			
			if(CollectionUtils.isNotEmpty(dataList)) {
				for(int i=0;i<dataList.size();i++) {
					ReportCopartnerSaleInfo reportCopartnerSaleInfo = new ReportCopartnerSaleInfo();
					reportCopartnerSaleInfo = dataList.get(i);
					    //月报区域小计
					if(reportCopartnerSaleInfo.getReportSubtotalType().equals(2)) {//HUANGZYTODO
						ReportCopartnerSaleVo reportCopartnerSaleVo = reportCopartnerSaleDao.qryAreaTotal(reportCopartnerSaleInfo.getCopartnerAreaId(),storeIds,reportYear, reportMonth);
						if(reportCopartnerSaleVo !=null) {
							reportCopartnerSaleInfo.setEighteenFinish(reportCopartnerSaleVo.getEighteenFinish());
							reportCopartnerSaleInfo.setEighteenTarget(reportCopartnerSaleVo.getEighteenTarget());
							reportCopartnerSaleInfo.setKeyFinish(reportCopartnerSaleVo.getKeyFinish());
							reportCopartnerSaleInfo.setKeyTarget(reportCopartnerSaleVo.getKeyTarget());
							reportCopartnerSaleInfo.setTextileFinish(reportCopartnerSaleVo.getTextileFinish());
							reportCopartnerSaleInfo.setTextileTarget(reportCopartnerSaleVo.getTextileTarget());
							reportCopartnerSaleInfo.setTotalFinish(reportCopartnerSaleVo.getTotalFinish());
							reportCopartnerSaleInfo.setTotalTarget(reportCopartnerSaleVo.getTotalTarget());
						}
					    dataList.set(i, reportCopartnerSaleInfo);
					}
					if(reportCopartnerSaleInfo.getReportSubtotalType().equals(3)){
						//年报月份区域小计
						ReportCopartnerSaleVo reportCopartnerSaleVo = reportCopartnerSaleDao.qryMonthTotal(storeIds,reportYear,reportCopartnerSaleInfo.getReportMonth());
						if(reportCopartnerSaleVo !=null) {
							reportCopartnerSaleInfo.setEighteenFinish(reportCopartnerSaleVo.getEighteenFinish());
							reportCopartnerSaleInfo.setEighteenTarget(reportCopartnerSaleVo.getEighteenTarget());
							reportCopartnerSaleInfo.setKeyFinish(reportCopartnerSaleVo.getKeyFinish());
							reportCopartnerSaleInfo.setKeyTarget(reportCopartnerSaleVo.getKeyTarget());
							reportCopartnerSaleInfo.setTextileFinish(reportCopartnerSaleVo.getTextileFinish());
							reportCopartnerSaleInfo.setTextileTarget(reportCopartnerSaleVo.getTextileTarget());
							reportCopartnerSaleInfo.setTotalFinish(reportCopartnerSaleVo.getTotalFinish());
							reportCopartnerSaleInfo.setTotalTarget(reportCopartnerSaleVo.getTotalTarget());
							reportCopartnerSaleInfo.setReportMonth(reportCopartnerSaleInfo.getReportMonth()+"-小计");
						}
					    dataList.set(i, reportCopartnerSaleInfo);
					}
				}
			}
			
		    int totalCount = reportCopartnerSaleDao.countMainPage(vo);

		    return HttpResponse.success(new PageResData(totalCount,dataList));
		}catch(Exception e) {
		    log.error("查询异常:区域销售列表-请求参数{},{}",vo,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	
	/**
	 * 获取权限
	 * @param resourceCode
	 * @return
	 */
	private List<String> getStoreIds(String resourceCode) {

		List<String> storeIds = new ArrayList();
		
		//获取登陆人信息
		AuthToken authToken = AuthUtil.getCurrentAuth();
//		HttpResponse response = copartnerAreaService.selectStoreByPerson(authToken.getTicketPersonId(),resourceCode);
		HttpResponse response = copartnerAreaService.selectStoreByPerson("12213",resourceCode);
		List<PublicAreaStore> list = JsonUtil.fromJson(JsonUtil.toJson(response.getData()),new TypeReference<List<PublicAreaStore>>() {});  
		if(CollectionUtils.isNotEmpty(list)) {
			for(PublicAreaStore publicAreaStore : list) {
				storeIds.add(publicAreaStore.getStoreId());
			}
			return storeIds;
		}else {
			return null;
		}
	}

	@Override
	public void save(ReportCopartnerSaleVo vo) {
		reportCopartnerSaleDao.save(vo);
	}

	@Override
	public void delete(String reportYear, String reportMonth) {
		reportCopartnerSaleDao.delete(reportYear,reportMonth);
		
	}

//	@Override
//	public List<ReportCopartnerSaleVo> qryAreaTotal(String year, String month) {
//		return reportCopartnerSaleDao.qryAreaTotal(year,month);
//	}

	@Override
	public void deleteByArea(String copartnerAreaId, String year, String month) {
		reportCopartnerSaleDao.deleteByArea(copartnerAreaId,year,month);
		
	}

//	@Override
//	public ReportCopartnerSaleVo qryMonthTotal(String year, String month) {
//		return reportCopartnerSaleDao.qryMonthTotal(year,month);
//	}

	@Override
	public void deleteByMonth(String year, String month) {
		reportCopartnerSaleDao.deleteByMonth(year,month);
	}


	@Override
	public List<ReportCopartnerSaleVo> qryAreaInit(String year, String month) {
		return reportCopartnerSaleDao.qryAreaInit(year,month);
	}
    
}


