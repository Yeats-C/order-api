/*****************************************************************

 * 模块名称：合伙人销售报表-DAO接口层
 * 开发人员: huangzy
 * 开发时间: 2020-02-19

 * ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.response.*;
import org.apache.ibatis.annotations.Param;

import com.aiqin.mgs.order.api.domain.*;


public interface ReportCopartnerSaleDao {

	List<ReportCopartnerSaleInfo> selectMainPageList(ReportCopartnerSaleVo vo);

	int countMainPage(ReportCopartnerSaleVo vo);
	
	int save(ReportCopartnerSaleVo vo);

	void delete(@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	ReportCopartnerSaleVo qryAreaTotal(@Valid @Param("copartnerAreaId") String copartnerAreaId,@Valid @Param("storeIds")List<String> storeIds,@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	void deleteByArea(@Valid @Param("copartnerAreaId")String copartnerAreaId,@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	ReportCopartnerSaleVo qryMonthTotal(@Valid @Param("storeIds")List<String> storeIds,@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	void deleteByMonth(@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

	List<ReportCopartnerSaleVo> qryAreaInit(@Valid @Param("reportYear") String reportYear,@Valid @Param("reportMonth") String reportMonth);

}
