/*****************************************************************

* 模块名称：挂单解挂后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.FrozenInfo;
import org.apache.ibatis.annotations.Param;

public interface FrozenDao {
    

    void insert(@Valid FrozenInfo frozenInfo) throws Exception; //挂单入库
    
    void deleteByFrozenId(@Valid @Param("frozenId")String frozenId) throws Exception; //解卦

    List<FrozenInfo> selectDetailByFrozenId(@Valid @Param("frozenId")String frozenId) throws Exception; //查询挂单明细

    List<FrozenInfo> selectSumByFrozenId(@Valid @Param("createBy")String createBy,@Param("distributorId")String distributorId) throws Exception; //查询挂单汇总
    
    List<FrozenInfo> selectDetail(@Valid @Param("createBy")String createBy,@Param("distributorId")String distributorId) throws Exception;

    //查询挂单数量
	Integer selectSumByParam(@Valid @Param("createBy")String createBy,@Param("distributorId")String distributorId)throws Exception;
}
