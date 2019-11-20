package com.aiqin.mgs.order.api.domain.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderStoreOrderInfoRequest {

    /***订单id*/
    private String orderId;
    /***订单编号*/
    private String orderCode;
    /***订单状态 枚举 ErpOrderStatusEnum*/
    private Integer orderStatus;
    /***是否发生退货 0否 1是*/
    private Integer returnStatus;
    /***支付状态 */
    private Integer payStatus;
    /***订单类型 枚举 OrderTypeEnum*/
    private Integer orderType;
    /***门店id*/
    private String storeId;
    /***门店名称*/
    private String storeName;
    /***订单总额*/
    private BigDecimal totalPrice;
    /***实付金额*/
    private BigDecimal actualPrice;
    /***订单级别 枚举 OrderLevelEnum*/
    private Integer orderLevel;
    /***关联主订单编码*/
    private String primaryCode;

    private Integer pageSize;
    private Integer pageNo;
}
