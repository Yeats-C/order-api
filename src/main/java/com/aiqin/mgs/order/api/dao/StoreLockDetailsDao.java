package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.StoreLockDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreLockDetailsDao {

    int deleteByPrimaryKey(Long id);

    /**
     * 根据skuCode、仓库编码、库房编码删除数据（本地解锁库存）
     * @param record
     * @return
     */
    int deleteBySkuCodeAndLockCount(StoreLockDetails record);

    int insert(StoreLockDetails record);

    int insertSelective(StoreLockDetails record);

    StoreLockDetails selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreLockDetails record);

    int updateByPrimaryKey(StoreLockDetails record);

    int insertBatch(@Param("records") List<StoreLockDetails> records);

    StoreLockDetails selectByLineCodeAndSkuCodeAndLockCount(StoreLockDetails record);

}