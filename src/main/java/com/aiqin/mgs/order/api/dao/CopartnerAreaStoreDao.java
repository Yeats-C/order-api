/*****************************************************************

 * 模块名称：经营区域门店-DAO接口层
 * 开发人员: huangzy
 * 开发时间: 2020-02-13

 * ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.copartnerArea.*;
import org.apache.ibatis.annotations.Param;


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

	List<CopartnerAreaDetail> slecetTwoCompanyByName(@Param("copartnerAreaCompany") String copartnerAreaCompany);
}
