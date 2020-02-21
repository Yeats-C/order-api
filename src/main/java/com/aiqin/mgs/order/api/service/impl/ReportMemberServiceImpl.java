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
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.ReportMemberDao;
import com.aiqin.mgs.order.api.domain.ReportCopartnerSaleVo;
import com.aiqin.mgs.order.api.domain.ReportMemberSaleVo;
import com.aiqin.mgs.order.api.domain.ReportMemberVo;
import com.aiqin.mgs.order.api.domain.response.ReportCopartnerSaleInfo;
import com.aiqin.mgs.order.api.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@SuppressWarnings("all")
@Service
public class ReportMemberServiceImpl implements ReportMemberService {

    private static final Logger log = LoggerFactory.getLogger(ReportMemberServiceImpl.class);

    @Resource
    private ReportMemberDao dao;

	@Override
	public HttpResponse qryReportPageList(String reportYear, String reportMonth, Integer pageNo, Integer pageSize) {
		ReportMemberVo vo = new ReportMemberVo();
		try {
			vo.setReportYear(reportYear);
			vo.setReportMonth(reportMonth);
			vo.setPageNo(pageNo);
			vo.setPageSize(pageSize);
		    List<ReportCopartnerSaleInfo> dataList = dao.selectMainPageList(vo);
		    int totalCount = dao.countMainPage(vo);

		    return HttpResponse.success(new PageResData(totalCount,dataList));
		}catch(Exception e) {
		    log.error("查询异常:门店会员报表-请求参数{},{}",vo,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	@Override
	public HttpResponse qryReportSalePageList(String reportYear, String reportMonth, Integer pageNo, Integer pageSize) {
		ReportMemberSaleVo vo = new ReportMemberSaleVo();
		try {
			vo.setReportYear(reportYear);
			vo.setReportMonth(reportMonth);
			vo.setPageNo(pageNo);
			vo.setPageSize(pageSize);
		    List<ReportMemberSaleVo> dataList = dao.selectSalePageList(vo);
		    int totalCount = dao.countSaleMainPage(vo);

		    return HttpResponse.success(new PageResData(totalCount,dataList));
		}catch(Exception e) {
		    log.error("查询异常:门店会员报表-请求参数{},{}",vo,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	@Override
	public HttpResponse qryReportSaleCatepPageList(String reportYear, String reportMonth, Integer pageNo,
			Integer pageSize) {
		ReportMemberSaleVo vo = new ReportMemberSaleVo();
		try {
			vo.setReportYear(reportYear);
			vo.setReportMonth(reportMonth);
			vo.setPageNo(pageNo);
			vo.setPageSize(pageSize);
		    List<ReportMemberSaleVo> dataList = dao.selectSaleCatepPageList(vo);
		    int totalCount = dao.countSaleCatepPage(vo);

		    return HttpResponse.success(new PageResData(totalCount,dataList));
		}catch(Exception e) {
		    log.error("查询异常:门店会员品类消费报表(人数)-请求参数{},{}",vo,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}

	@Override
	public HttpResponse qryReportSaleCatemPageList(String reportYear, String reportMonth, Integer pageNo,
			Integer pageSize) {
		ReportMemberSaleVo vo = new ReportMemberSaleVo();
		try {
			vo.setReportYear(reportYear);
			vo.setReportMonth(reportMonth);
			vo.setPageNo(pageNo);
			vo.setPageSize(pageSize);
		    List<ReportMemberSaleVo> dataList = dao.selectCatemPageList(vo);
		    int totalCount = dao.countCatemPage(vo);

		    return HttpResponse.success(new PageResData(totalCount,dataList));
		}catch(Exception e) {
		    log.error("查询异常:门店会员品类消费报表(金额)-请求参数{},{}",vo,e);
		    return HttpResponse.failure(MessageId.create(Project.ZERO, 01,"查询出现未知异常,请联系系统管理员."));
		}
	}



}