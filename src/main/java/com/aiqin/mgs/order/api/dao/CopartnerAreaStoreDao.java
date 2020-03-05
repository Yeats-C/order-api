/*****************************************************************

 * 模块名称：经营区域门店-DAO接口层
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
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
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


public interface CopartnerAreaStoreDao {

	int countStoreByArea(@Valid @Param("copartnerAreaId") String copartnerAreaId);
	
	void saveCopartnerAreaStore(CopartnerAreaStoreVo vo);
	
	List<CopartnerAreaStoreList> selectStoreMainPageList(CopartnerAreaStoreVo vo);
	
	int countStoreMainPage(CopartnerAreaStoreVo vo);
	
	void deleteById(@Valid @Param("copartnerAreaId") String copartnerAreaId);
	
	List<PublicAreaStore> selectPublicAreaStoreList(@Valid @Param("copartnerAreaId") String copartnerAreaId);
	
	int countStoreByStoreCode(@Valid @Param("storeCode") String storeCode);
	
	CopartnerAreaUp qryInfo(@Valid @Param("storeCode") String storeCode);

	List<String> qryAreaByStores(@Valid @Param("storeIds")List<String> storeIds);

	void deleteByAreaStore(@Valid @Param("storeId") String storeId);
}