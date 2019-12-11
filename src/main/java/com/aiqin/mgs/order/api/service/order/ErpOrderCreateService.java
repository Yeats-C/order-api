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
     * 创建订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.vo.order.ErpOrderInfoVO
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 13:59
     */
    ErpOrderInfo saveOrder(ErpOrderSaveRequest erpOrderSaveRequest);

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
}
