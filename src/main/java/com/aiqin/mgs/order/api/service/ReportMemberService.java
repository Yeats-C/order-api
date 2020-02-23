/*****************************************************************
* 模块名称：门店会员报表-接口层 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.ReportMemberSaleCateVo;
import com.aiqin.mgs.order.api.domain.ReportMemberSaleVo;
import com.aiqin.mgs.order.api.domain.ReportMemberVo;

import java.util.List;

@SuppressWarnings("all")
public interface ReportMemberService {

	HttpResponse<ReportMemberVo> qryReportMonthPageList(Integer statYear, Integer statMonth, Integer pageNo,
			Integer pageSize);

	HttpResponse<ReportMemberVo> qryReportYearList(Integer statYear, Integer pageNo, Integer pageSize);

	HttpResponse<ReportMemberSaleVo> qrySaleMonthList(String statYearMonth, Integer pageNo, Integer pageSize);

	HttpResponse<ReportMemberSaleVo> qrySaleYearList(String statYear, Integer pageNo, Integer pageSize);

}