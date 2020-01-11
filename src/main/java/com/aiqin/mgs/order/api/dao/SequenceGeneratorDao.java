package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.SequenceGenerator;

import java.util.List;

/**
 * 模拟序列Dao
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/9 11:43
 */
public interface SequenceGeneratorDao {

    /**
     * 条件查询
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.SequenceGenerator>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 14:23
     */
    List<SequenceGenerator> select(SequenceGenerator po);

    /**
     * 更新非空字段
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 14:23
     */
    Integer updateByPrimaryKeySelective(SequenceGenerator po);

    /**
     * 新增
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 14:24
     */
    Integer insert(SequenceGenerator po);

}
