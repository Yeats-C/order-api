package com.aiqin.mgs.order.api.dao.order;

import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单明细行dao
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 14:10
 */
public interface ErpOrderItemDao {

    /**
     * 根据字段精确查询符合的数据列表
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    List<ErpOrderItem> select(ErpOrderItem po);

    /**
     * 根据主键更新非空字段（部分字段除外）
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    Integer updateByPrimaryKeySelective(ErpOrderItem po);

    /**
     * 插入数据
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:04
     */
    Integer insert(ErpOrderItem po);

    /**
     * 获取活动商品销售额（订单中的商品命中了这个活动的促销规则，那么把这些命中促销规则的商品的实收纳入统计）
     * @return
     * @param activity
     */
    BigDecimal getProductSales(Activity activity);

    /**
     * 根据字段精确查询符合的数据列表总数
     * @param erpOrderItem
     * @return
     */
    Integer totalCount(ErpOrderItem erpOrderItem);

    /**
     * 根据字段精确查询符合的数据列表(关联主表)
     * @param erpOrderItem
     * @return
     */
    List<ErpOrderItem> getActivityItem(ErpOrderItem erpOrderItem);
}
