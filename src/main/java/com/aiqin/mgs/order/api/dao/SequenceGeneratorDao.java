package com.aiqin.mgs.order.api.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 存储过程模拟序列工具
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/9 11:43
 */
public interface SequenceGeneratorDao {

    /**
     * 调用存储过程生成新的序列值
     *
     * @param sequenceName
     * @return java.lang.Long
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/9 11:43
     */
    Long getSequenceNextVal(@Param("sequenceName") String sequenceName);

}
