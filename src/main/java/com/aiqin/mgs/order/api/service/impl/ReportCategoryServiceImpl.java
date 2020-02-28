/*****************************************************************
* 模块名称：品类报表-实现层 
* 开发人员: huangzy
* 开发时间: Mon Feb 24 10:04:13 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import javax.annotation.Resource;
import javax.validation.Valid;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.ReportCategoryDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.ReportCategoryVo;
import com.aiqin.mgs.order.api.domain.ReportCopartnerSaleVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
import com.aiqin.mgs.order.api.domain.response.ReportCopartnerSaleInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@Service
public class ReportCategoryServiceImpl implements ReportCategoryService {

    private static final Logger log = LoggerFactory.getLogger(ReportCategoryServiceImpl.class);

    @Resource
    private ReportCategoryDao dao;
    @Resource
    private CopartnerAreaService copartnerAreaService;
    
    
    @Override
	public void save(ReportCategoryVo vo) {
    	dao.save(vo);
	}

	@Override
	public void delete(String reportYear, String reportMonth) {
		dao.delete(reportYear,reportMonth);
		
	}


	@Override
	public List<ReportCategoryVo> qryAreaInit(String year, String month) {
		return dao.qryAreaInit(year,month);
	}

	@Override
	public HttpResponse<ReportCategoryVo> qryReportPageList(String reportYear, String reportMonth, Integer pageNo,
			Integer pageSize) {

		
		ReportCategoryVo vo = new ReportCategoryVo();
		try {
			
			List<ReportCategoryVo> dataList = new ArrayList();
					
			//区域权限控制
			String resourceCode = "ERP012002"; //菜单编码 HUANGZYTODO
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
			//一级品类的销售数据
			dataList = dao.selectMainPageList(vo);
			
			if(CollectionUtils.isNotEmpty(dataList)) {
				for(int i=0;i<dataList.size();i++) {
					ReportCategoryVo reportCategoryVo = new ReportCategoryVo();
					reportCategoryVo = dataList.get(i);
					
					//计算一级品类其余字段
					if(reportCategoryVo.getReportSubtotalType().equals(1) && StringUtils.isNotBlank(reportMonth)) {//HUANGZYTODO
						//一级品类在当前区域中的销售金额占比.
						ReportCategoryVo monthVo = dao.qryAreaTotal(reportCategoryVo.getCopartnerAreaId(),storeIds,reportYear, reportMonth);
						if(StringUtils.isNotBlank(monthVo.getCopartnerAreaId())) {
							reportCategoryVo.setTotalRate(reportCategoryVo.getTotalPrice()*100/monthVo.getTotalPrice());
						}

						//一级品类在同一区域内. 上个月
						String lastMonth = DateUtil.dateAfterMonth(reportYear+"-"+reportMonth+"-15",-1);
						ReportCategoryVo lastMonthVo = dao.qryCategoryTotal(reportCategoryVo.getCopartnerAreaId(),reportCategoryVo.getCategoryCode(),storeIds,lastMonth.substring(0, 4),lastMonth.substring(5, 7));
						if(lastMonthVo !=null && lastMonthVo.getTotalPrice() !=null) {
							//上期销售额
							reportCategoryVo.setLastRate(lastMonthVo.getTotalPrice());
							//环比
							reportCategoryVo.setQoqRate((reportCategoryVo.getTotalPrice()-lastMonthVo.getTotalPrice())*100/lastMonthVo.getTotalPrice());
						}
						
						//同比 上年同月
						String lastYearMonth = DateUtil.dateAfterMonth(reportYear+"-"+reportMonth+"-15",-12);
						ReportCategoryVo lastYearMonthVo = dao.qryCategoryTotal(reportCategoryVo.getCopartnerAreaId(),reportCategoryVo.getCategoryCode(),storeIds,lastYearMonth.substring(0, 4),lastYearMonth.substring(5, 7));
						if(lastYearMonthVo !=null && lastYearMonthVo.getTotalPrice() !=null) {  
							reportCategoryVo.setSameRate((reportCategoryVo.getTotalPrice()-lastYearMonthVo.getTotalPrice())*100/lastYearMonthVo.getTotalPrice());
						}
						dataList.set(i, reportCategoryVo);
					}
					//计算月报区域小计
					if(reportCategoryVo.getReportSubtotalType().equals(2)) {//HUANGZYTODO
						//区域总金额
						reportCategoryVo = dao.qryAreaTotal(reportCategoryVo.getCopartnerAreaId(),storeIds,reportYear, reportMonth);
						//上期
						String lastMonth = DateUtil.dateAfterMonth(reportYear+"-"+reportMonth+"-15",-1);
						ReportCategoryVo lastMonthVo = dao.qryAreaTotal(reportCategoryVo.getCopartnerAreaId(),storeIds,lastMonth.substring(0, 4),lastMonth.substring(5, 7));
						if(lastMonthVo !=null && lastMonthVo.getTotalPrice() !=null) {  
							//上期销售额
							reportCategoryVo.setLastRate(lastMonthVo.getTotalPrice());
							//环比
							reportCategoryVo.setQoqRate((reportCategoryVo.getTotalPrice()-lastMonthVo.getTotalPrice())*100/lastMonthVo.getTotalPrice());
						}
						
						//同比 上年同月
						String lastYearMonth = DateUtil.dateAfterMonth(reportYear+"-"+reportMonth+"-15",-12);
						ReportCategoryVo lastYearMonthVo = dao.qryAreaTotal(reportCategoryVo.getCopartnerAreaId(),storeIds,lastYearMonth.substring(0, 4),lastYearMonth.substring(5, 7));
						if(lastYearMonthVo !=null && lastYearMonthVo.getTotalPrice() !=null) {
							reportCategoryVo.setSameRate((reportCategoryVo.getTotalPrice()-lastYearMonthVo.getTotalPrice())*100/lastYearMonthVo.getTotalPrice());
						}
						
						dataList.set(i, reportCategoryVo);
					}
					//年报月份区域小计
					if(reportCategoryVo.getReportSubtotalType().equals(3)){
						//区域总金额
						reportCategoryVo = dao.qryMonthTotal(storeIds,reportYear,reportCategoryVo.getReportMonth());
						//上期
						String lastMonth = DateUtil.dateAfterMonth(reportYear+"-"+reportCategoryVo.getReportMonth()+"-15",-1);
						ReportCategoryVo lastMonthVo = dao.qryMonthTotal(storeIds,lastMonth.substring(0, 4),lastMonth.substring(5, 7));
						if(lastMonthVo !=null && lastMonthVo.getTotalPrice() !=null) {
							//上期销售额
							reportCategoryVo.setLastRate(lastMonthVo.getTotalPrice());
							//环比
							reportCategoryVo.setQoqRate((reportCategoryVo.getTotalPrice()-lastMonthVo.getTotalPrice())*100/lastMonthVo.getTotalPrice());
						}
						
						//同比 上年同月
						String lastYearMonth = DateUtil.dateAfterMonth(reportYear+"-"+reportCategoryVo.getReportMonth()+"-15",-12);
						ReportCategoryVo lastYearMonthVo = dao.qryMonthTotal(storeIds,lastYearMonth.substring(0, 4),lastYearMonth.substring(5, 7));
						if(lastYearMonthVo !=null && lastYearMonthVo.getTotalPrice() !=null) {
							reportCategoryVo.setSameRate((reportCategoryVo.getTotalPrice()-lastYearMonthVo.getTotalPrice())*100/lastYearMonthVo.getTotalPrice());
						}
					    dataList.set(i, reportCategoryVo);
					}
				}
			}
			
		    int totalCount = dao.countMainPage(vo);

		    return HttpResponse.success(new PageResData(totalCount,dataList));
		}catch(Exception e) {
		    log.error("查询异常:品类销售列表-请求参数{},{}",vo,e);
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
}