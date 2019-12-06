package com.aiqin.mgs.order.api.domain.response.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 物流单支付结果返回
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/6 11:25
 */
@Data
public class OrderSendingPayResultResponse {

    /***订单号*/
    private String orderCode;
    /***门店名称*/
    private String storeName;
    /***物流公司编码*/
    private String logisticCentreCode;
    /***物流公司名称*/
    private String logisticCentreName;
    /***物流单号*/
    private String logisticCode;
    /***物流费用*/
    private BigDecimal logisticFee;
    /***支付流水号*/
    private String payCode;

}
