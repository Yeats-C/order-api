/*****************************************************************
* 模块名称：品类报表-DAO接口层 
* 开发人员: huangzy
* 开发时间: Mon Feb 24 10:04:13 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import javax.validation.Valid;
import org.apache.ibatis.annotations.Param;
import com.aiqin.mgs.order.api.domain.*;
import java.util.List;

public interface ReportCategoryDao {

	void save(ReportCategoryVo vo);

	void delete(@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	List<ReportCategoryVo> qryAreaInit(@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	List<ReportCategoryVo> selectMainPageList(ReportCategoryVo vo);
	
	int countMainPage(ReportCategoryVo vo);

	//品类汇总
	ReportCategoryVo qryCategoryTotal(@Valid @Param("copartnerAreaId")String copartnerAreaId,@Valid @Param("categoryCode")String categoryCode,@Valid @Param("storeIds")List<String> storeIds, @Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);
	
	//区域汇总
	ReportCategoryVo qryAreaTotal(@Valid @Param("copartnerAreaId")String copartnerAreaId,@Valid @Param("storeIds")List<String> storeIds, @Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	//月份汇总
	ReportCategoryVo qryMonthTotal(@Valid @Param("storeIds")List<String> storeIds,@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);
}