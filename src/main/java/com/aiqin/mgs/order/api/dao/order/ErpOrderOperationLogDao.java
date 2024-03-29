package com.aiqin.mgs.order.api.dao.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;

import java.util.List;

/**
 * 订单操作日志dao
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 14:10
 */
public interface ErpOrderOperationLogDao {

    /**
     * 根据字段精确查询符合的数据列表
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    List<ErpOrderOperationLog> select(ErpOrderOperationLog po);

    /**
     * 插入数据
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:04
     */
    Integer insert(ErpOrderOperationLog po);

}
