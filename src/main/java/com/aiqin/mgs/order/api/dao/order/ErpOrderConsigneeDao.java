package com.aiqin.mgs.order.api.dao.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderConsignee;

import java.util.List;

/**
 * 订单签收人信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 14:09
 */
public interface ErpOrderConsigneeDao {
    
    /**
     * 根据字段精确查询符合的数据列表
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderConsignee>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    List<ErpOrderConsignee> select(ErpOrderConsignee po);

    /**
     * 插入数据
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:04
     */
    Integer insert(ErpOrderConsignee po);
}
