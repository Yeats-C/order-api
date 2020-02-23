/*****************************************************************
* 模块名称：门店会员报表-实现层 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import javax.annotation.Resource;
import javax.validation.Valid;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.ReportCopartnerSaleVo;
import com.aiqin.mgs.order.api.domain.ReportMemberSaleVo;
import com.aiqin.mgs.order.api.domain.ReportMemberVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
import com.aiqin.mgs.order.api.domain.response.ReportCopartnerSaleInfo;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@Service
public class ReportMemberServiceImpl implements ReportMemberService {

    private static final Logger log = LoggerFactory.getLogger(ReportMemberServiceImpl.class);

    
    @Value("${member.host}")
    private String memberUrl;
    
    @Resource
    private CopartnerAreaService copartnerAreaService;

	@Override
	public HttpResponse qryReportMonthPageList(Integer statYear, Integer statMonth, Integer pageNo,
			Integer pageSize) {
		
		try {
			//区域权限控制
			String resourceCode = "ERP012012";  //HUANGZYTODO
			List<String> storeIds = getStoreIds(resourceCode);
			if(CollectionUtils.isEmpty(storeIds)) {
				return HttpResponse.success(new PageResData(0,null));
			}
			StringBuffer sb = new StringBuffer();
			sb.append(memberUrl+"/member/alculate/qry/month/list?stat_year="+statYear+"&stat_month="+statMonth+"&page_no="+pageNo+"&page_size="+pageSize+"");
			HttpClient httpClient = HttpClient.post(sb.toString()).json(storeIds);
			log.info("请求地址="+sb.toString());
		    return HttpResponse.success(httpClient.action().result(HttpResponse.class));
		}catch(Exception e) {
		    log.error("查询异常:请求参数{},{}",e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	@Override
	public HttpResponse qryReportYearList(Integer statYear, Integer pageNo, Integer pageSize) {
		try {
			//区域权限控制
			String resourceCode = "ERP012012";  //HUANGZYTODO
			List<String> storeIds = getStoreIds(resourceCode);
			if(CollectionUtils.isEmpty(storeIds)) {
				return HttpResponse.success(new PageResData(0,null));
			}
			StringBuffer sb = new StringBuffer();
			sb.append(memberUrl+"/member/alculate/qry/year/list?stat_year="+statYear+"&page_no="+pageNo+"&page_size="+pageSize+"");
			HttpClient httpClient = HttpClient.post(sb.toString()).json(storeIds);
		    return HttpResponse.success(httpClient.action().result(HttpResponse.class));
		}catch(Exception e) {
		    log.error("查询异常:请求参数{},{}",e);
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
//		HttpResponse response = copartnerAreaService.selectStoreByPerson(authToken.getTicketPersonId(),resourceCode); HUANGZYTODO
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
	public HttpResponse<ReportMemberSaleVo> qrySaleMonthList(String statYearMonth, Integer pageNo, Integer pageSize) {
		try {
			//区域权限控制
			String resourceCode = "ERP012013";  //HUANGZYTODO
			List<String> storeIds = getStoreIds(resourceCode);
			if(CollectionUtils.isEmpty(storeIds)) {
				return HttpResponse.success(new PageResData(0,null));
			}
			StringBuffer sb = new StringBuffer();
			sb.append(memberUrl+"/member/alculate/qry/sale/month/list?stat_year_month="+statYearMonth+"&page_no="+pageNo+"&page_size="+pageSize+"");
			HttpClient httpClient = HttpClient.post(sb.toString()).json(storeIds);
		    return HttpResponse.success(httpClient.action().result(HttpResponse.class));
		}catch(Exception e) {
		    log.error("查询异常:请求参数{},{}",e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	
	
	@Override
	public HttpResponse<ReportMemberSaleVo> qrySaleYearList(String statYear, Integer pageNo, Integer pageSize) {
		try {
			//区域权限控制
			String resourceCode = "ERP012013";  //HUANGZYTODO
			List<String> storeIds = getStoreIds(resourceCode);
			if(CollectionUtils.isEmpty(storeIds)) {
				return HttpResponse.success(new PageResData(0,null));
			}
			StringBuffer sb = new StringBuffer();
			sb.append(memberUrl+"/member/alculate/qry/sale/year/list?stat_year="+statYear+"&page_no="+pageNo+"&page_size="+pageSize+"");
			HttpClient httpClient = HttpClient.post(sb.toString()).json(storeIds);
		    return HttpResponse.success(httpClient.action().result(HttpResponse.class));
		}catch(Exception e) {
		    log.error("查询异常:请求参数{},{}",e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}




}