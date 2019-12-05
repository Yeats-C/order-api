package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单发货物流信息表
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:39
 */
@Data
public class OrderStoreOrderSending {

    /***主键*/
    private Long id;
    /***发货id*/
    private String sendingId;
    /***物流公司编码*/
    private String logisticCentreCode;
    /***物流公司名称*/
    private String logisticCentreName;
    /***物流单号*/
    private String logisticCode;
    /***物流费用*/
    private BigDecimal logisticFee;
    /***发货仓库编码*/
    private String sendRepertoryCode;
    /***发货仓库名称*/
    private String sendRepertoryName;
    /***支付状态*/
    private Integer payStatus;
    /***开始支付物流费用时间*/
    private Date payStartTime;
    /***结束支付物流费用时间*/
    private Date payEndTime;
    /***物流券支付金额*/
    private BigDecimal couponPayFee;
    /***余额支付金额*/
    private BigDecimal balancePayFee;

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
