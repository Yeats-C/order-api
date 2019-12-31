package com.aiqin.mgs.order.api.service.order;

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
    ErpOrderInfo erpSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest);

    /**
     * 爱掌柜从购物车创建订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.vo.order.ErpOrderInfoVO
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 13:59
     */
    ErpOrderInfo storeSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest);

    /**
     * 创建货架订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.vo.order.ErpOrderInfoVO
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:00
     */
    ErpOrderInfo saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest);

    /**
     * 生成订单号
     *
     * @param
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/28 15:18
     */
    String getOrderCode();
}
