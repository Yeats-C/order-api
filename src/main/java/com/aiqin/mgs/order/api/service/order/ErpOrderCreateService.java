package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSaveRequest;

/**
 * 创建订单service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 13:57
 */
public interface ErpOrderCreateService {

    /**
     * erp从购物车创建订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.vo.order.ErpOrderInfoVO
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 13:59
     */
    ErpOrderInfo erpSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth);

    /**
     * 爱掌柜从购物车创建订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.vo.order.ErpOrderInfoVO
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 13:59
     */
    ErpOrderInfo storeSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth);

    /**
     * 创建货架订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.vo.order.ErpOrderInfoVO
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:00
     */
    ErpOrderInfo saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth);
}
