/*****************************************************************

* 模块名称：挂单解挂后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import com.aiqin.mgs.order.api.domain.FrozenInfo;
import org.apache.ibatis.annotations.Param;

public interface FrozenDao {
	

	void insert(FrozenInfo frozenInfo) throws Exception; //挂单入库
	
	void deleteByFrozenId(String frozenId) throws Exception; //解卦

	List<FrozenInfo> selectDetailByFrozenId(String frozenId) throws Exception; //查询挂单明细

	List<FrozenInfo> selectSumByFrozenId(@Param("createBy")String createBy,@Param("distributorId")String distributorId) throws Exception; //查询挂单汇总
	
	List<FrozenInfo> selectDetail(@Param("createBy")String createBy,@Param("distributorId")String distributorId) throws Exception;
}
