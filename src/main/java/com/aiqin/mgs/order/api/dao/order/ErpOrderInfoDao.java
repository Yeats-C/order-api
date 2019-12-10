package com.aiqin.mgs.order.api.dao.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;

import java.util.List;

/**
 * 订单信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 14:10
 */
public interface ErpOrderInfoDao {

    /**
     * 根据字段精确查询符合的数据列表
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    List<ErpOrderInfo> select(ErpOrderInfo po);

    /**
     * 根据主键更新非空字段（部分字段除外）
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    Integer updateByPrimaryKeySelective(ErpOrderInfo po);

    /**
     * 插入数据
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:04
     */
    Integer insert(ErpOrderInfo po);

}
