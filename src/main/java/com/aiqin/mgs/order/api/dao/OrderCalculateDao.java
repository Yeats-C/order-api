/*****************************************************************

* 模块名称：订单统计-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2020-02-19 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;

import com.aiqin.mgs.order.api.domain.*;



public interface OrderCalculateDao {

	List<OrderMonthCalculateInfo> qryFinish(@Valid @Param("beginTime")Date beginTime,@Valid @Param("endTime")Date endTime);
	
	int qryTotalFinish(@Valid @Param("storeId")String storeId,@Valid @Param("beginTime")Date beginTime,@Valid @Param("endTime")Date endTime);

	int qryEighteenFinish(@Valid @Param("storeId")String storeId,@Valid @Param("dictionaryContent") String dictionaryContent,@Valid @Param("beginTime")Date beginTime,@Valid @Param("endTime")Date endTime);

	int qryTextileFinish(@Valid @Param("storeId")String storeId,@Valid @Param("categoryCode") String categoryCode,@Valid @Param("beginTime")Date beginTime,@Valid @Param("endTime")Date endTime);
}
