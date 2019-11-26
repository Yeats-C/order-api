package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:16
 */
@Data
public class OrderStoreOrderPay {

    /***主键*/
    private Long id;
    /***订单id*/
    private String orderId;
    /***支付id*/
    private String payId;
    /***支付中心支付单编号*/
    private String payCode;
    /***支付状态*/
    private Integer payStatus;
    /***订货含税总金额*/
    private BigDecimal orderTotal;
    /***活动金额*/
    private BigDecimal activityMoney;
    /***A品券抵减金额*/
    private BigDecimal activityCouponMoney;
    /***服纺券抵减金额*/
    private BigDecimal clothCouponMoney;
    /***实付金额*/
    private BigDecimal actualMoney;
    /***轮询次数*/
    private Integer pollingTimes;
    /***开始支付时间*/
    private Date payStartTime;
    /***结束支付时间*/
    private Date payEndTime;

    /***创建时间*/
    private Date createTime;
    /***创建人id*/
    private String createById;
    /***创建人姓名*/
    private String createByName;
    /***更新时间*/
    private Date updateTime;
    /***修改人id*/
    private String updateById;
    /***修改人姓名*/
    private String updateByName;

}
