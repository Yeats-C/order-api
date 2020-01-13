package com.aiqin.mgs.order.api.dao.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;

import java.util.List;

/**
 * 订单费用信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/11 20:27
 */
public interface ErpOrderFeeDao {

    /**
     * 根据字段精确查询符合的数据列表
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:45
     */
    List<ErpOrderFee> select(ErpOrderFee po);

    /**
     * 根据主键更新非空字段（部分字段除外）
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:46
     */
    Integer updateByPrimaryKeySelective(ErpOrderFee po);

    /**
     * 插入数据
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:46
     */
    Integer insert(ErpOrderFee po);
}
