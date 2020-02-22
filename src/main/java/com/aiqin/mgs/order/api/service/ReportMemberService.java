/*****************************************************************
* 模块名称：门店会员报表-接口层 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.ReportMemberSaleCateVo;
import com.aiqin.mgs.order.api.domain.ReportMemberSaleVo;

import java.util.List;

@SuppressWarnings("all")
public interface ReportMemberService {

	HttpResponse qryReportPageList(String reportYear, String reportMonth, Integer pageNo, Integer pageSize);

	HttpResponse qryReportSalePageList(String reportYear, String reportMonth, Integer pageNo,Integer pageSize);

	HttpResponse qryReportSaleCatepPageList(String reportYear, String reportMonth,Integer pageNo, Integer pageSize);

	HttpResponse qryReportSaleCatemPageList(String reportYear, String reportMonth, Integer pageNo,Integer pageSize);

}