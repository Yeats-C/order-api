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
import com.aiqin.mgs.order.api.domain.copartnerArea.SystemMethod;
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
    
    @Value("${bridge.url.slcs_api}")
    private String slcsMainUrl;
    
    
    /**
     * 列表分页
     */
	@Override
	public HttpResponse copartnerAreaList(@Valid CopartnerAreaVo param) {
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
					//查询权限编码、权限名称
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
	 * 权限树
	 */
	@Override
	public HttpResponse roledetail(@Valid String copartnerAreaId, @Valid String personId) {
		
		//权限详情
		CopartnerAreaRoleDetail detail = new CopartnerAreaRoleDetail();

		
		//菜单字典项
		List<SystemResource> dictList = new ArrayList();
		try {
			dictList = getMenu();
		}catch(Exception e) {
			LOGGER.error("查询主控菜单异常{}",e);
			return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"连接主控查询出现未知异常,请联系系统管理员."));
		}
		
		
		if(StringUtils.isBlank(copartnerAreaId)) {
			detail.setRoleSalfList(dictList);
			detail.setRoleDownList(dictList);
			return HttpResponse.success(detail);
		}
		
		//本公司已授权的菜单	
		List<SystemResource> roleSalfList = dictList;
		roleSalfList = getCheckFlag(copartnerAreaId,personId,1,roleSalfList); //HUANGZYTODO
		detail.setRoleSalfList(roleSalfList);
		
		
		//下级公司已授权的菜单
		List<SystemResource> roleDownList = dictList;
		roleDownList = getCheckFlag(copartnerAreaId,personId,2,roleDownList); //HUANGZYTODO
		detail.setRoleDownList(roleDownList);
		
		
		return HttpResponse.success(detail);
		
	}

	
	/**
	 * 添加勾选标识
	 * @param copartnerAreaId
	 * @param personId
	 * @param roleType
	 * @param dictList
	 * @return
	 */
	private List<SystemResource> getCheckFlag(String copartnerAreaId,String personId,Integer roleType,List<SystemResource> dictList) {
		
		CopartnerAreaRoleVo param = new CopartnerAreaRoleVo();
		param.setCopartnerAreaId(copartnerAreaId);
		param.setPersonId(personId);
		param.setRoleType(roleType);
		CopartnerAreaRoleVo vo = copartnerAreaRoleDao.getRoleByPky(param);
		if(vo !=null) {
			for(int i=0;i< dictList.size(); i++) {
				SystemResource systemResource = new SystemResource();
				systemResource = dictList.get(i);
				//每一个菜单对应的权限
				String[] roleCodes = vo.getRoleCode().split("、");
				for(String roleCode : roleCodes) {
					String[] elements= roleCode.split("-");
					if(elements[1].equals(String.valueOf(roleType))) {
						if(systemResource.getResourceCode().equals(elements[0])) {
							systemResource.setCheckFlag(1); //HUANGZYTODO 1:已勾选
							dictList.set(i, systemResource);
						}
					}
				}
			}
		}
		
		return dictList;
	}


	/**
	 * 菜单字典项
	 * @return
	 */
	private List<SystemResource> getMenu() throws Exception{
		
		//查询所有菜单
		String systemCode = "erp-system";
//		AuthToken auth = AuthUtil.getCurrentAuth();
//		StringBuffer sb = new StringBuffer();
//		sb.append(centerMainUrl);
//		sb.append("/resource/system/erp-system?ticket_person_id="+auth.getTicketPersonId()+"&ticket="+auth.getTicket()+"");
//    	HttpClient httpClient = HttpClient.get(sb.toString());
//    	HttpResponse response = httpClient.action().result(HttpResponse.class);
    	List<SystemResource> systemResources = copartnerAreaDao.selectSystemResource(systemCode);
    	this.getList(systemResources);
    	return systemResources;
	}
	
	
    private void getList(List<SystemResource> list){
        List<SystemMethod> systemMethods;
        for (SystemResource systemResource:list) {
//            systemMethods = systemMethodManager.selectListResourceCode(systemResource.getResourceCode());//systemMethodDao.selectList(new EntityWrapper<SystemMethod>().eq("resource_code", systemResource.getResourceCode()));
//            if(CollectionUtils.isNotEmpty(systemMethods)){
//                systemResource.setSystemMethods(systemMethods);
//            }
//            systemResource.setResourceMark(new ArrayList<>());
            systemResource.setResources(getSystemChild(systemResource.getResourceCode()));
        }
    }
    
    private List<SystemResource> getSystemChild(String parentCode){
        List<SystemResource> list = copartnerAreaDao.selectByParent(parentCode);
        this.getList(list);
        return list;
    }
	
	
	/**
	 * 保存区域
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public HttpResponse saveCopartnerArea(@Valid CopartnerAreaSave param) {
		
		LOGGER.info("保存区域请求参数{}",param);
		
		try {

			String copartnerAreaId = "";
			//删除新增
			if(StringUtils.isNotBlank(param.getCopartnerAreaDetail().getCopartnerAreaId())) {
				copartnerAreaId = param.getCopartnerAreaDetail().getCopartnerAreaId();
				copartnerAreaDao.deleteById(copartnerAreaId);
				copartnerAreaStoreDao.deleteById(copartnerAreaId);
				copartnerAreaRoleDao.deleteById(copartnerAreaId);
			}
			if(StringUtils.isNotBlank(param.getCopartnerAreaDetail().getCopartnerAreaId()) && param.getCopartnerAreaDetail().getCopartnerAreaId().equals("0")) {
				copartnerAreaId = "0";
			}else {
				copartnerAreaId = IdUtil.uuid();
			}
			
			//基本信息
			if(param.getCopartnerAreaDetail() !=null ) {
				CopartnerAreaVo vo = new CopartnerAreaVo();
				BeanUtils.copyProperties(param.getCopartnerAreaDetail(), vo);
				vo.setCopartnerAreaId(copartnerAreaId);
				AuthToken auth = AuthUtil.getCurrentAuth();
				vo.setCreateBy(auth.getTicketPersonId());
				
				//原型不足,兼容处理
				if(copartnerAreaId.equals("0")) {
					vo.setCopartnerAreaIdUp("");
					vo.setCopartnerAreaNameUp("");
					vo.setCopartnerAreaLevel(0);
				}
				if(vo.getCopartnerAreaLevel() !=null && vo.getCopartnerAreaLevel().equals(1)) {
					vo.setCopartnerAreaIdUp("0");
					vo.setCopartnerAreaNameUp("总部");
				}
				
				copartnerAreaDao.saveCopartnerArea(vo);
			}
			
			//门店信息
			if(CollectionUtils.isNotEmpty(param.getStoreList())) {
				for(CopartnerAreaStoreList copartnerAreaStore : param.getStoreList()) {
					CopartnerAreaStoreVo vo = new CopartnerAreaStoreVo();
					BeanUtils.copyProperties(copartnerAreaStore, vo);
					vo.setCopartnerAreaId(copartnerAreaId);
					copartnerAreaStoreDao.saveCopartnerAreaStore(vo);
				}
			}
			
			//权限信息
			if(CollectionUtils.isNotEmpty(param.getRoleList())) {
				for(CopartnerAreaRoleList copartnerAreaRole : param.getRoleList()) {
					CopartnerAreaRoleVo vo = new CopartnerAreaRoleVo();
					BeanUtils.copyProperties(copartnerAreaRole, vo);
					vo.setCopartnerAreaId(copartnerAreaId);
					copartnerAreaRoleDao.saveCopartnerAreaRole(vo);
				}
			}
			
			return HttpResponse.success(true);
		}catch(Exception e) {
		    log.error("保存异常:保存区域-请求参数{},{}",param,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"保存出现未知异常,请联系系统管理员."));
		}
	}


	/**
	 * 查询详情基本信息
	 */
	@Override
	public HttpResponse getCopartnerAreaDetail(@Valid String copartnerAreaId) {
		try {

			//返回实体
			CopartnerAreaDetail info = new CopartnerAreaDetail();
			//数据实体
			CopartnerAreaVo vo = copartnerAreaDao.selectCopartnerAreaInfo(copartnerAreaId);
		    if(vo !=null ) {
		    	//->
		    	BeanUtils.copyProperties(vo, info);
		    }

		    return HttpResponse.success(info);
		}catch(Exception e) {
		    log.error("查询异常:查询详情基本信息-请求参数{},{}",copartnerAreaId,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	
	/**
	 * 查询门店列表
	 */
	@Override
	public HttpResponse getCopartnerAreaStore(String copartnerAreaId) {
		
		List<CopartnerAreaStoreList> dataList = new ArrayList();
		try {
			CopartnerAreaStoreVo vo = new CopartnerAreaStoreVo();
			vo.setCopartnerAreaId(copartnerAreaId);
		    dataList = copartnerAreaStoreDao.selectStoreMainPageList(vo);
//		    int totalCount = copartnerAreaStoreDao.countStoreMainPage(vo);

		    return HttpResponse.success(dataList);
		}catch(Exception e) {
		    log.error("查询异常:查询门店列表-请求参数{},{}",dataList,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	
	/**
	 * 查询权限列表
	 */
	@Override
	public HttpResponse getCopartnerAreaRole(String copartnerAreaId) {
		
		List<CopartnerAreaRoleList> dataList = new ArrayList();
		try {
			CopartnerAreaRoleVo vo = new CopartnerAreaRoleVo();
			vo.setCopartnerAreaId(copartnerAreaId);
			dataList = copartnerAreaRoleDao.selectRoleMainPageList(vo);

		    return HttpResponse.success(dataList);
		}catch(Exception e) {
		    log.error("查询异常:查询权限列表分页-请求参数{},{}",dataList,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}


	/**
	 * 删除区域设置
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public HttpResponse deleteMainById(String copartnerAreaId) {
		try {

			copartnerAreaDao.deleteById(copartnerAreaId);
			copartnerAreaStoreDao.deleteById(copartnerAreaId);
			copartnerAreaRoleDao.deleteById(copartnerAreaId);

		    return HttpResponse.success(true);
		}catch(Exception e) {
		    log.error("删除异常:删除区域设置-请求参数{},{}",copartnerAreaId,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"删除出现未知异常,请联系系统管理员."));
		}
	}


	/**
	 * 合伙人数据权限控制公共接口
	 */
	@Override
	public HttpResponse selectStoreByPerson(String personId,String resourceCode) {
		List<PublicAreaStore> dataList = new ArrayList();
		
		if(StringUtils.isBlank(personId) || StringUtils.isBlank(resourceCode)) {
			return HttpResponse.success(dataList);
		}
		try {
			//查询人员+菜单编码查询总部本公司权限
			CopartnerAreaRoleVo roleVo = new CopartnerAreaRoleVo();
			roleVo.setPersonId(personId);
			roleVo.setRoleCode(resourceCode+"-1"); //本级公司
			List<CopartnerAreaRoleVo> roleList = copartnerAreaRoleDao.selectRoleMainList(roleVo);
			//根据区域查询所辖门店
			if(CollectionUtils.isNotEmpty(roleList)) {
				for(CopartnerAreaRoleVo vo : roleList) {
					List<PublicAreaStore> storeList = copartnerAreaStoreDao.selectPublicAreaStoreList(vo.getCopartnerAreaId());
				    if(CollectionUtils.isNotEmpty(storeList)) {
				    	dataList.addAll(storeList);
				    }
				}
			}
			
			//查询人员+菜单编码查询下级公司的权限
			CopartnerAreaRoleVo downRoleVo = new CopartnerAreaRoleVo();
			downRoleVo.setPersonId(personId);
			downRoleVo.setRoleCode(resourceCode+"-2"); //下级公司
			List<CopartnerAreaRoleVo> downRoleList = copartnerAreaRoleDao.selectRoleMainList(downRoleVo);
			if(CollectionUtils.isNotEmpty(downRoleList)) {
				for(CopartnerAreaRoleVo vo : downRoleList) {
					//获取第一层下级列表
					CopartnerAreaVo copartnerAreaVo = new CopartnerAreaVo();
					copartnerAreaVo.setCopartnerAreaIdUp(vo.getCopartnerAreaId());
					List<CopartnerAreaVo> areaList = copartnerAreaDao.copartnerAreaVoList(copartnerAreaVo);
					if(CollectionUtils.isNotEmpty(areaList)) {
						//获取所有下级公司的权限
						for(CopartnerAreaVo downAreaVo : areaList) {
							List<PublicAreaStore> downStoreList = copartnerAreaStoreDao.selectPublicAreaStoreList(downAreaVo.getCopartnerAreaId());
						    if(CollectionUtils.isNotEmpty(downStoreList)) {
						    	dataList.addAll(downStoreList);
						    }
						    
						  //获取第二层下级列表
						    CopartnerAreaVo scodCopartnerAreaVo = new CopartnerAreaVo();
						    scodCopartnerAreaVo.setCopartnerAreaIdUp(vo.getCopartnerAreaId());
							List<CopartnerAreaVo> scodAreaList = copartnerAreaDao.copartnerAreaVoList(scodCopartnerAreaVo);
						    if(CollectionUtils.isNotEmpty(scodAreaList)) {
						    	for(CopartnerAreaVo scodAreaVo : areaList) {
									List<PublicAreaStore> scodStoreList = copartnerAreaStoreDao.selectPublicAreaStoreList(scodAreaVo.getCopartnerAreaId());
								    if(CollectionUtils.isNotEmpty(scodStoreList)) {
								    	dataList.addAll(scodStoreList);
								    }
						    	}
						    }
						}
					}
				}
			}
			
		    return HttpResponse.success(dataList);
		}catch(Exception e) {
		    log.error("查询异常:查询门店列表-请求参数{},{}",dataList,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}


	/**
	 * 根据类型查询区域
	 */
	@Override
	public HttpResponse areaTypeInfo(Integer areaType) {
		StringBuffer sb = new StringBuffer();
		sb.append(centerMainUrl+"/area/info/no-authority/type/"+areaType+"");
		HttpClient httpClient = HttpClient.get(sb.toString());
	    return httpClient.action().result(HttpResponse.class);
	}


	/**
	 * 根据上级编码查询所有的子集
	 */
	@Override
	public HttpResponse childrenInfo(String parentId) {
		StringBuffer sb = new StringBuffer();
		sb.append(centerMainUrl+"/area/info/no-authority/"+parentId+"");
		HttpClient httpClient = HttpClient.get(sb.toString());
	    return httpClient.action().result(HttpResponse.class);
	}


	@Override
	public HttpResponse<List<NewStoreTreeResponse>> getStoresByAreaCode(AreaReq areaReq) {
		
		List<NewStoreTreeResponse> dataList = new ArrayList();
		HttpClient httpClient = HttpClient.post(slcsMainUrl + "/store/getStoresByAreaCode").json(areaReq);
		HttpResponse response = httpClient.action().result(HttpResponse.class);
		List<NewStoreTreeResponse> list = JsonUtil.fromJson(JsonUtil.toJson(response.getData()),new TypeReference<List<NewStoreTreeResponse>>() {}); 
		if(CollectionUtils.isNotEmpty(list)) {
			for(NewStoreTreeResponse newStoreTreeResponse : list) {
				int amount = copartnerAreaStoreDao.countStoreByStoreCode(newStoreTreeResponse.getStoreCode());
				if(amount <=0) {
					dataList.add(newStoreTreeResponse);
				}
			}
		}
		return HttpResponse.success(dataList);
	}


	@Override
	public HttpResponse<List<NewStoreTreeResponse>> getStoresByCodeOrName(String parm) {
		
		List<NewStoreTreeResponse> dataList = new ArrayList();
		
		StringBuffer sb = new StringBuffer();
		sb.append(slcsMainUrl+"/store/getStoresByCodeOrName?");
		if(StringUtils.isNotBlank(parm)) {
		    sb.append("parm="+parm+"");
		}
		HttpClient httpClient = HttpClient.get(sb.toString());
	    HttpResponse response = httpClient.action().result(HttpResponse.class);
		List<NewStoreTreeResponse> list = JsonUtil.fromJson(JsonUtil.toJson(response.getData()),new TypeReference<List<NewStoreTreeResponse>>() {}); 
		if(CollectionUtils.isNotEmpty(list)) {
			for(NewStoreTreeResponse newStoreTreeResponse : list) {
				int amount = copartnerAreaStoreDao.countStoreByStoreCode(newStoreTreeResponse.getStoreCode());
				if(amount <=0) {
					dataList.add(newStoreTreeResponse);
				}
			}
		}
		return HttpResponse.success(dataList);
	}


	@Override
	public CopartnerAreaUp qryInfo(String storeCode) {
		return copartnerAreaStoreDao.qryInfo(storeCode);
	}


	@Override
	public List<String> qryAreaByStores(List<String> storeIds) {
		return copartnerAreaStoreDao.qryAreaByStores(storeIds);
	}
}


