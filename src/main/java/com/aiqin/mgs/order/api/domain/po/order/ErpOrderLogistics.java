package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单物流信息表实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:03
 */
@Data
public class ErpOrderLogistics extends ErpOrderBase {

    /***发货id*/
    private String logisticsId;
    /***支付单id*/
    private String payId;
    /***支付状态 ErpPayStatusEnum*/
    private Integer payStatus;
    /***物流公司编码*/
    private String logisticsCentreCode;
    /***物流公司名称*/
    private String logisticsCentreName;
    /***物流单号*/
    private String logisticsCode;
    /***发货仓库编码*/
    private String sendRepertoryCode;
    /***发货仓库名称*/
    private String sendRepertoryName;
    /***物流费用*/
    private BigDecimal logisticsFee;
    /***物流券支付金额*/
    private BigDecimal couponPayFee;
    /***余额支付金额*/
    private BigDecimal balancePayFee;
    /***物流券ids，多个用逗号间隔*/
    private String couponIds;

    /***物流费用支付*/
    private ErpOrderPay logisticsPay;
}
