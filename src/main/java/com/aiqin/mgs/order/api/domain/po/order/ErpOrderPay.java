package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付信息实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:56
 */
@Data
public class ErpOrderPay extends ErpOrderBase {

    /***支付id*/
    private String payId;
    /***业务外键*/
    private String businessId;
    /***支付流水号*/
    private String payCode;
    /***费用类型*/
    private Integer feeType;
    /***支付状态*/
    private Integer payStatus;
    /***支付方式*/
    private Integer payWay;
    /***费用*/
    private BigDecimal fee;
    /***开始支付时间*/
    private Date payStartTime;
    /***结束支付时间*/
    private Date payEndTime;

}
