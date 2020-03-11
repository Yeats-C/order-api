package com.aiqin.mgs.order.api.service.cart;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartAddRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartChangeGroupCheckStatusRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartQueryRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartUpdateRequest;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartQueryResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpOrderCartAddResponse;

import java.util.List;

/**
 * 购物车操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/3/10 10:14c
 */
public interface ErpOrderCartService {

    void insertCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken);

    void insertCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken);

    void updateCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken);

    void updateCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken);

    ErpOrderCartInfo getCartLineByCartId(String cartId);

    List<ErpOrderCartInfo> selectByProperty(ErpOrderCartInfo erpOrderCartInfo);

    void deleteCartLine(String cartId);

    ErpOrderCartAddResponse addProduct(ErpCartAddRequest erpCartAddRequest, AuthToken auth);

    void changeGroupCheckStatus(ErpCartChangeGroupCheckStatusRequest erpCartChangeGroupCheckStatusRequest, AuthToken auth);

    void updateCartLineProduct(ErpCartUpdateRequest erpCartUpdateRequest,AuthToken auth);

    ErpCartQueryResponse queryErpCartList(ErpCartQueryRequest erpCartQueryRequest);
}
