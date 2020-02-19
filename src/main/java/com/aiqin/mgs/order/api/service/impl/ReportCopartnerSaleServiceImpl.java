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

	@Override
	public HttpResponse qryReportPageList(String reportYear, String reportMonth, Integer pageNo, Integer pageSize) {
		
		ReportCopartnerSaleVo vo = new ReportCopartnerSaleVo();
		try {
			vo.setReportYear(reportYear);
			vo.setReportMonth(reportMonth);
			vo.setPageNo(pageNo);
			vo.setPageSize(pageSize);
		    List<ReportCopartnerSaleInfo> dataList = reportCopartnerSaleDao.selectMainPageList(vo);
		    int totalCount = reportCopartnerSaleDao.countMainPage(vo);

		    return HttpResponse.success(new PageResData(totalCount,dataList));
		}catch(Exception e) {
		    log.error("查询异常:?-请求参数{},{}",vo,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
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

	@Override
	public List<ReportCopartnerSaleVo> qryAreaTotal(String year, String month) {
		return reportCopartnerSaleDao.qryAreaTotal(year,month);
	}

	@Override
	public void deleteByArea(String copartnerAreaId, String year, String month) {
		reportCopartnerSaleDao.deleteByArea(copartnerAreaId,year,month);
		
	}

	@Override
	public ReportCopartnerSaleVo qryMonthTotal(String year, String month) {
		return reportCopartnerSaleDao.qryMonthTotal(year,month);
	}

	@Override
	public void deleteByMonth(String year, String month) {
		reportCopartnerSaleDao.deleteByMonth(year,month);
	}
    
}


