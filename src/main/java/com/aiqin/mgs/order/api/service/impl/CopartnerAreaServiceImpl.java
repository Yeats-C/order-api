/*****************************************************************
 * 
 * 模块名称：经营区域设置-实现层
 * 开发人员: huangzy
 * 开发时间: 2020-02-13
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
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaListReq;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleDetail;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleDict;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class CopartnerAreaServiceImpl implements CopartnerAreaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CopartnerAreaServiceImpl.class);

    @Resource
    private CopartnerAreaDao copartnerAreaDao;
    @Resource
    private CopartnerAreaStoreDao copartnerAreaStoreDao;
    @Resource
    private CopartnerAreaRoleDao copartnerAreaRoleDao;
    
    @Value("${center.main.url}")
    private String centerMainUrl;
    
    /**
     * 列表分页
     */
	@Override
	public HttpResponse copartnerAreaList(@Valid CopartnerAreaListReq param) {
		try {
			LOGGER.info("列表分页请求参数={}",param);
			List<CopartnerAreaList> list = new ArrayList();
			list = copartnerAreaDao.copartnerAreaList(param);
			int totalCount = copartnerAreaDao.countCopartnerAreaList(param);
			if(CollectionUtils.isNotEmpty(list)) {
				for(int i=0;i<list.size();i++) {
					CopartnerAreaList copartnerArea = new CopartnerAreaList();
					copartnerArea = list.get(i);
					//统计合伙人公司下的门店个数
					int storeAmount = copartnerAreaStoreDao.countStoreByArea(copartnerArea.getCopartnerAreaId());
					copartnerArea.setStoreAmount(storeAmount);
					list.set(i, copartnerArea);
				}
			}
			return HttpResponse.success(new PageResData(totalCount,list));
		}catch(Exception e) {
			LOGGER.error("列表分页异常:请求参数{},{}",param,e);
			return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}
	
	
	/**
	 * 查询上级合伙人
	 */
	@Override
	public HttpResponse<CopartnerAreaUp> copartnerAreaUp() {
		try {
			List<CopartnerAreaUp> list = new ArrayList();
			list = copartnerAreaDao.copartnerAreaUp();
			return HttpResponse.success(list);
		}catch(Exception e) {
			LOGGER.error("查询上级合伙人异常{}",e);
			return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}
	
	
	/**
	 * 查找公司人员
	 */
	@Override
	public HttpResponse getPersonList(@Valid String personTeam) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(centerMainUrl);
			sb.append("/person/list?company_code=01&page_no=0&page_size=250&person_team="+personTeam+"");
	    	HttpClient httpClient = HttpClient.get(sb.toString());
	    	HttpResponse response = httpClient.action().result(HttpResponse.class);
	    	PageResData resData = JsonUtil.fromJson(JsonUtil.toJson(response.getData()), PageResData.class);
	    	List<CopartnerAreaRoleList> roleList = JsonUtil.fromJson(JsonUtil.toJson(resData.getDataList()), new TypeReference<List<CopartnerAreaRoleList>>() {
	        });
	    	return HttpResponse.success(roleList);
		}catch(Exception e) {
			LOGGER.error("查找公司人员异常{}",e);
			return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"连接主控查询出现未知异常,请联系系统管理员."));
		}
		
	}
	
	
	/**
	 * 公司人员查询权限列表
	 */
	@Override
	public HttpResponse getRoleList(@Valid List<CopartnerAreaRoleList> param) {
		try {
			LOGGER.info("公司人员查询权限列表请求参数{}",param);
			if(param !=null) {
				for(int i=0;i<param.size();i++) {
					CopartnerAreaRoleList copartnerAreaRole = new CopartnerAreaRoleList();
					copartnerAreaRole = param.get(i);
					//查询权限名称
					CopartnerAreaRoleList roleName = copartnerAreaRoleDao.getRoleByUnion(copartnerAreaRole);
					if(roleName != null ) {
						param.set(i, roleName);
					}
				}
			}
			return HttpResponse.success(param);
		}catch(Exception e) {
			LOGGER.error("公司人员查询权限列表异常{}",e);
			return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}
	
	
	/**
	 * 查询权限详情
	 */
	@Override
	public HttpResponse roledetail(@Valid String copartnerAreaId, @Valid String personId) {
		
		//权限详情
		CopartnerAreaRoleDetail detail = new CopartnerAreaRoleDetail();

		
		//菜单字典项
		List<CopartnerAreaRoleDict> dictList = new ArrayList();
		try {
			dictList = getMenu(dictList);
		}catch(Exception e) {
			LOGGER.error("查询主控菜单异常{}",e);
			return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"连接主控查询出现未知异常,请联系系统管理员."));
		}
		
		
		//本公司已授权的菜单
		List<CopartnerAreaRoleDict> roleSalfList = getCheckFlag(copartnerAreaId,personId,1,dictList); //HUANGZYTODO
		detail.setRoleSalfList(roleSalfList);
		
		
		//下级公司已授权的菜单
		List<CopartnerAreaRoleDict> roleDownList = getCheckFlag(copartnerAreaId,personId,2,dictList); //HUANGZYTODO
		detail.setRoleDownList(roleDownList);
		
		
		return HttpResponse.success(detail);
		
	}

	
	private List<CopartnerAreaRoleDict> getCheckFlag(String copartnerAreaId,String personId,Integer checkFlag,List<CopartnerAreaRoleDict> dictList) {
		
		List<CopartnerAreaRoleDict> list = new ArrayList();
		list = dictList;
		
		//单个权限查询
		CopartnerAreaRoleVo copartnerAreaRoleVo = new CopartnerAreaRoleVo();
		copartnerAreaRoleVo.setCopartnerAreaId(copartnerAreaId);
		copartnerAreaRoleVo.setPersonId(personId);
		copartnerAreaRoleVo.setRoleType(checkFlag);
		copartnerAreaRoleVo = copartnerAreaRoleDao.getRoleByPky(copartnerAreaRoleVo);
		if(copartnerAreaRoleVo !=null ) {
			String[] roleCodes = copartnerAreaRoleVo.getRoleCode().split("、");
			for(String roleCode : roleCodes) {
				for(int i=0;i<list.size();i++) {
					CopartnerAreaRoleDict dict = new CopartnerAreaRoleDict();
					dict = list.get(i);
					if(roleCode.equals(dict.getRoleCode())) {
						dict.setCheckFlag(1); //HUANGZYTODO
						list.set(i, dict);
						continue;
					}
				}
			}
		}
		
		return list;
	}


	/**
	 * 菜单字典项
	 * @return
	 */
	private List<CopartnerAreaRoleDict> getMenu(List<CopartnerAreaRoleDict> dictList) throws Exception{
		
		//查询所有菜单
		AuthToken auth = AuthUtil.getCurrentAuth();
		StringBuffer sb = new StringBuffer();
		sb.append(centerMainUrl);
		sb.append("/resource/system/erp-system?ticket_person_id="+auth.getTicketPersonId()+"&ticket="+auth.getTicket()+"");
    	HttpClient httpClient = HttpClient.get(sb.toString());
    	HttpResponse response = httpClient.action().result(HttpResponse.class);
    	List<SystemResource> systemResources = JsonUtil.fromJson(JsonUtil.toJson(response.getData()),new TypeReference<List<SystemResource>>() {
        });  
    	//一级菜单
    	if(CollectionUtils.isNotEmpty(systemResources)) {
    		for(SystemResource systemResource : systemResources) {
    			CopartnerAreaRoleDict dict = new CopartnerAreaRoleDict();
    			dict.setRoleCode(systemResource.getResourceCode());
    			dict.setRoleName(systemResource.getResourceShowName());
    			dictList.add(dict);
    			//+二级菜单
    			if(CollectionUtils.isNotEmpty(systemResource.getResources())) {
    				for(SystemResource downResource : systemResource.getResources()) {
    					CopartnerAreaRoleDict downDict = new CopartnerAreaRoleDict();
    					downDict.setRoleCode(downResource.getResourceCode());
    					downDict.setRoleName(downResource.getResourceShowName());
    	    			dictList.add(downDict);
    				}
    			}
    		}
    	}
    	
    	return dictList;
	}


	@Override
	public HttpResponse getCopartnerAreaDetail(@Valid String copartnerAreaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse getCopartnerAreaStore(CopartnerAreaStoreVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse getCopartnerAreaRole(CopartnerAreaRoleVo vo) {
		// TODO Auto-generated method stub
		return null;
	}
}


