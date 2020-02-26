/*****************************************************************
* 模块名称：品类报表-接口层 
* 开发人员: huangzy
* 开发时间: Mon Feb 24 10:04:13 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.ReportCategoryVo;
import com.aiqin.mgs.order.api.domain.response.ReportCopartnerSaleInfo;

import java.util.List;

@SuppressWarnings("all")
public interface ReportCategoryService {

	void delete(String year, String month);

	void save(ReportCategoryVo vo);

	List<ReportCategoryVo> qryAreaInit(String year, String month);

	HttpResponse<ReportCategoryVo> qryReportPageList(String reportYear, String reportMonth, Integer pageNo,
			Integer pageSize);

}