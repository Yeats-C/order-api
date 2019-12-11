package com.aiqin.mgs.order.api.domain.response.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流费用支付凭证打印数据
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/10 16:40
 */
@Data
public class ErpOrderLogisticsPrintQueryResponse {

    /***门店名称*/
    private String storeName;
    /***订单编号*/
    private String orderCode;
    /***物流公司编码*/
    private String logisticCentreCode;
    /***物流公司名称*/
    private String logisticCentreName;
    /***物流单号*/
    private String logisticCode;
    /***物流费用*/
    private BigDecimal logisticFee;
    /***物流券支付金额*/
    private BigDecimal couponPayFee;
    /***余额支付金额*/
    private BigDecimal balancePayFee;
    /***支付流水号*/
    private String payCode;
    /***支付完成时间*/
    private Date payEndTime;
    /***支付人姓名*/
    private String payUser;

}
