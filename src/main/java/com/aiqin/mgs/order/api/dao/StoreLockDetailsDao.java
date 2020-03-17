package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.StoreLockDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreLockDetailsDao {

    int deleteByPrimaryKey(Long id);

    int insert(StoreLockDetails record);

    int insertSelective(StoreLockDetails record);

    StoreLockDetails selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StoreLockDetails record);

    int updateByPrimaryKey(StoreLockDetails record);

    int insertBatch(@Param("records") List<StoreLockDetails> records);

    StoreLockDetails selectByLineCodeAndSkuCodeAndLockCount(StoreLockDetails record);

}