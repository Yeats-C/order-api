package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付结果查询返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/25 14:49
 */
@Data
public class ErpOrderPayResultResponse {

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
    /***支付流水号*/
    private String payCode;
    /***支付id*/
    private String payId;
    /***支付开始时间*/
    private Date payStartTime;
    /***支付完成时间*/
    private Date payEndTime;

    public String getOrderStatusDesc() {
        return ErpOrderStatusEnum.getEnumDesc(orderStatus);
    }

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
