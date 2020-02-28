package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;

/**
 * 订单物流信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 19:19
 */
public interface ErpOrderLogisticsService {

    /**
     * 保存订单物流信息
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:41
     */
    void saveOrderLogistics(ErpOrderLogistics po, AuthToken auth);

    /**
     * 根据支付id获取订单物流信息
     *
     * @param payId 支付id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:41
     */
    ErpOrderLogistics getOrderLogisticsByPayId(String payId);

    /**
     * 根据物流id获取物流信息
     *
     * @param logisticsId 物流id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:42
     */
    ErpOrderLogistics getOrderLogisticsByLogisticsId(String logisticsId);

    /**
     * 根据物流单号获取物流信息
     *
     * @param logisticsCode 物流单号
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:42
     */
    ErpOrderLogistics getOrderLogisticsByLogisticsCode(String logisticsCode);

    /**
     * 根据主键更新物流信息（非空字段）
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:43
     */
    void updateOrderLogisticsSelective(ErpOrderLogistics po, AuthToken auth);

    /**
     * 根据主键更新物流信息（全字段）
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 10:43
     */
    void updateOrderLogistics(ErpOrderLogistics po, AuthToken auth);

    /**
     * 发起支付物流费用
     *
     * @param erpOrderPayRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 15:41
     */
    void orderLogisticsPay(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 完成物流单支付状态
     *
     * @param logisticsCode 物流单号
     * @param payCode       支付流水号
     * @param payStatusEnum 支付结果
     * @param auth          操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 11:19
     */
    void endOrderLogisticsPay(String logisticsCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth);


}
