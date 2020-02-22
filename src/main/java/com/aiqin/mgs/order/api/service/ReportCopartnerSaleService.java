package com.aiqin.mgs.order.api.service;

import java.util.List;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.ReportCopartnerSaleVo;

public interface ReportCopartnerSaleService {

	HttpResponse qryReportPageList(String reportYear, String reportMonth, Integer pageNo, Integer pageSize);

	public void save(ReportCopartnerSaleVo vo);

	void delete(String year, String month);
	
	List<ReportCopartnerSaleVo> qryAreaInit(String year, String month);
	
//	List<ReportCopartnerSaleVo> qryAreaTotal(String year, String month);

	void deleteByArea(String copartnerAreaId, String year, String month);

//	ReportCopartnerSaleVo qryMonthTotal(String year, String month);

	void deleteByMonth(String year, String month);
}
