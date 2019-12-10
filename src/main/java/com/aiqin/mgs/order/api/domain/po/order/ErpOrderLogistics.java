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
    /***物流费用是否支付成功 YesOrNoEnum*/
    private Integer paid;
    /***物流公司编码*/
    private String logisticCentreCode;
    /***物流公司名称*/
    private String logisticCentreName;
    /***物流单号*/
    private String logisticCode;
    /***发货仓库编码*/
    private String sendRepertoryCode;
    /***发货仓库名称*/
    private String sendRepertoryName;
    /***物流费用*/
    private BigDecimal logisticFee;
    /***物流券支付金额*/
    private BigDecimal couponPayFee;
    /***余额支付金额*/
    private BigDecimal balancePayFee;

}
