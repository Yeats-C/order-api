package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单支付结果查询返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/25 14:49
 */
@Data
public class OrderPayResultResponse {

    /***订单id*/
    private String orderId;
    /***订单编码*/
    private String orderCode;
    /***订单状态*/
    private Integer orderStatus;
    /***订单状态描述*/
    private String orderStatusDesc;
    /***支付状态*/
    private Integer payStatus;
    /***支付状态描述*/
    private String payStatusDesc;
    /***物流券*/
    private BigDecimal goodsCoupon;

    public String getOrderStatusDesc() {
        return ErpOrderStatusEnum.getEnumDesc(orderStatus);
    }

    public String getPayStatusDesc() {
        return PayStatusEnum.getEnumDesc(payStatus);
    }
}
