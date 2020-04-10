/*****************************************************************

 * 模块名称：经营区域设置-DAO接口层
 * 开发人员: huangzy
 * 开发时间: 2020-02-13

 * ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.request.UnPayVo;
import com.aiqin.mgs.order.api.domain.request.statistical.BusinessStatisticalRequest;
import com.aiqin.mgs.order.api.domain.request.statistical.SkuSalesRequest;
import com.aiqin.mgs.order.api.domain.response.*;
import com.aiqin.mgs.order.api.domain.response.statistical.Last10DaysOrderStatistical;
import com.aiqin.mgs.order.api.domain.statistical.BusinessStatistical;
import com.aiqin.mgs.order.api.domain.statistical.SkuSales;
import org.apache.ibatis.annotations.Param;

import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaListReq;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.SystemResource;
import com.aiqin.mgs.order.api.domain.request.DevelRequest;
import com.aiqin.mgs.order.api.domain.request.MemberByDistributorRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.SelectByMemberPayCountResponse;
import com.aiqin.mgs.order.api.domain.response.SkuSaleResponse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.domain.response.LatelyResponse;
import com.aiqin.mgs.order.api.domain.response.MevBuyResponse;
import com.aiqin.mgs.order.api.domain.response.OradskuResponse;
import org.apache.ibatis.annotations.Select;


public interface CopartnerAreaDao {

	List<CopartnerAreaList> copartnerAreaList(@Valid CopartnerAreaVo param);

	int countCopartnerAreaList(@Valid CopartnerAreaVo param);

	List<CopartnerAreaUp> copartnerAreaUp();

	void saveCopartnerArea(CopartnerAreaVo vo);

	CopartnerAreaVo selectCopartnerAreaInfo(@Valid @Param("copartnerAreaId") String copartnerAreaId);

	void deleteById(@Valid @Param("copartnerAreaId") String copartnerAreaId);
	
	void deleteByUpId(@Valid @Param("copartnerAreaId") String copartnerAreaId);
	
	List<CopartnerAreaVo> copartnerAreaVoList(@Valid CopartnerAreaVo param);

	@Select("select resource_code as resourceCode, resource_link as resourceLink, resource_name as resourceName,resource_show_name as resourceShowName, resource_level AS resourceLevel, resource_order AS resourceOrder, resource_icon as resourceIcon, resource_status as resourceStatus, parent_code as parentCode, system_code as systemCode, plan_mark as planMark from system_resource where system_code = #{systemCode} and parent_code = '0'")
    public List<SystemResource> selectSystemResource(@Param("systemCode") String systemCode);

	@Select("select resource_code as resourceCode, resource_link as resourceLink, resource_name as resourceName ,resource_show_name as resourceShowName, resource_level AS resourceLevel, resource_order AS resourceOrder, resource_icon as resourceIcon, resource_status as resourceStatus, parent_code as parentCode, system_code as systemCode, plan_mark as planMark from system_resource where parent_code = #{parentCode} ")
    public List<SystemResource> selectByParent(String parentCode);

	List<CopartnerAreaUp> qryCopartnerAreaList();

}
