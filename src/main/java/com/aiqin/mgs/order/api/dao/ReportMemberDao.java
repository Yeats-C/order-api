/*****************************************************************
* 模块名称：门店会员报表-DAO接口层 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import javax.validation.Valid;
import org.apache.ibatis.annotations.Param;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.response.ReportCopartnerSaleInfo;

import java.util.List;

public interface ReportMemberDao {

	List<ReportCopartnerSaleInfo> selectMainPageList(ReportMemberVo vo);

	int countMainPage(ReportMemberVo vo);

	List<ReportMemberSaleVo> selectSalePageList(ReportMemberSaleVo vo);

	int countSaleMainPage(ReportMemberSaleVo vo);

	List<ReportMemberSaleVo> selectSaleCatepPageList(ReportMemberSaleVo vo);

	int countSaleCatepPage(ReportMemberSaleVo vo);

	List<ReportMemberSaleVo> selectCatemPageList(ReportMemberSaleVo vo);

	int countCatemPage(ReportMemberSaleVo vo);

}