package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款单
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/12 20:31
 */
@Data
public class ErpOrderRefund extends ErpOrderBase {

    /***退款单id*/
    private String refundId;
    /***关联订单id*/
    private String orderId;
    /***退款金额*/
    private BigDecimal refundFee;
    /***退款状态*/
    private Integer refundStatus;
    /***退款开始时间*/
    private Date refundStartTime;
    /***退款结束时间*/
    private Date refundEndTime;
}
