package com.aiqin.mgs.order.api.dao.gift;

import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;

import java.util.List;

/**
 * 兑换赠品购物车操作类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/3/10 10:05
 */
public interface ErpOrderGiftPoolCartDao {

    /**
     * 根据字段精确查询符合的数据列表
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/3/10 10:06
     */
    List<ErpOrderCartInfo> select(ErpOrderCartInfo po);

    /**
     * 根据主键更新字段（仅部分字段）
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/3/10 10:07
     */
    int updateByPrimaryKey(ErpOrderCartInfo po);

    /**
     * 插入数据
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/3/10 10:07
     */
    int insert(ErpOrderCartInfo po);

    /**
     * 删除数据
     *
     * @param id
     * @return int
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/3/10 10:11
     */
    int deleteByPrimaryKey(Long id);

    /**
     *返回兑换赠品购物车中的sku商品的数量
     * @param shoppingCartRequest
     * @return
     */
    Integer getSkuNum(ShoppingCartRequest shoppingCartRequest);

}
