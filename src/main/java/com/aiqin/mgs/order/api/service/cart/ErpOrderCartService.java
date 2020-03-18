package com.aiqin.mgs.order.api.service.cart;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.request.cart.*;
import com.aiqin.mgs.order.api.domain.response.cart.*;

import java.util.List;

/**
 * 购物车操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/3/10 10:14c
 */
public interface ErpOrderCartService {

    /**
     * 插入行（原子操作）
     *
     * @param erpOrderCartInfo
     * @param authToken
     */
    void insertCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken);

    /**
     * 批量插入行
     *
     * @param list
     * @param authToken
     */
    void insertCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken);

    /**
     * 通过主键更新行（原子操作）
     *
     * @param erpOrderCartInfo
     * @param authToken
     */
    void updateCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken);

    /**
     * 批量根据主键更新行
     *
     * @param list
     * @param authToken
     */
    void updateCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken);

    /**
     * 根据cartId查询行
     *
     * @param cartId
     * @return
     */
    ErpOrderCartInfo getCartLineByCartId(String cartId);

    /**
     * 根据（部分）条件精确查询
     *
     * @param erpOrderCartInfo
     * @return
     */
    List<ErpOrderCartInfo> selectByProperty(ErpOrderCartInfo erpOrderCartInfo);

    /**
     * 根据cartId删除行
     *
     * @param cartId
     */
    void deleteCartLine(String cartId);

    /**
     * 清空购物车
     *
     * @param erpCartQueryRequest
     */
    void deleteAllCartLine(ErpCartQueryRequest erpCartQueryRequest);

    /**
     * 添加商品
     *
     * @param erpCartAddRequest
     * @param auth
     * @return
     */
    ErpOrderCartAddResponse addProduct(ErpCartAddRequest erpCartAddRequest, AuthToken auth);

    /**
     * 全选/全不选
     *
     * @param erpCartChangeGroupCheckStatusRequest
     * @param auth
     */
    void changeGroupCheckStatus(ErpCartChangeGroupCheckStatusRequest erpCartChangeGroupCheckStatusRequest, AuthToken auth);

    /**
     * 更新一行
     *
     * @param erpCartUpdateRequest
     * @param auth
     */
    void updateCartLineProduct(ErpCartUpdateRequest erpCartUpdateRequest, AuthToken auth);

    /**
     * erp端查询购物车
     *
     * @param erpCartQueryRequest
     * @return
     */
    ErpCartQueryResponse queryErpCartList(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth);

    /**
     * 查询爱掌柜购物车
     *
     * @param erpCartQueryRequest
     * @param auth
     * @return
     */
    ErpStoreCartQueryResponse queryStoreCartList(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth);

    /**
     * 创建订单结算数据，存储到redis
     *
     * @param erpCartQueryRequest
     * @param auth
     * @return
     */
    ErpGenerateCartGroupTempResponse generateCartGroupTemp(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth);

    /**
     * 查询订单结算缓存信息
     *
     * @param erpQueryCartGroupTempRequest
     * @param auth
     * @return
     */
    ErpStoreCartQueryResponse queryCartGroupTemp(ErpQueryCartGroupTempRequest erpQueryCartGroupTempRequest, AuthToken auth);

    /**
     * 返回购物车中的商品总数量
     *
     * @param erpCartNumQueryRequest
     * @param auth
     * @return
     */
    int getCartProductTotalNum(ErpCartNumQueryRequest erpCartNumQueryRequest, AuthToken auth);

    /**
     * 查询当前购物车活动条件满足情况
     *
     * @param storeActivityAchieveRequest
     * @return
     */
    StoreActivityAchieveResponse getStoreActivityAchieveDetail(StoreActivityAchieveRequest storeActivityAchieveRequest);

    /**
     * 删除购物车多行
     * @param erpCartDeleteMultipleRequest
     */
    void deleteMultipleCartLine(ErpCartDeleteMultipleRequest erpCartDeleteMultipleRequest);
}
