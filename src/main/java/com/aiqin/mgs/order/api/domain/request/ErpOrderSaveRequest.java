package com.aiqin.mgs.order.api.domain.request;

import lombok.Data;

@Data
public class ErpOrderSaveRequest {

    /***门店id*/
    private String storeId;

    /***订单类型 OrderTypeEnum*/
    private Integer orderType;

    /***订单来源 OrderOriginTypeEnum*/
    private Integer orderOriginType;

    /***订单销售渠道标识 OrderChannelEnum*/
    private Integer orderChannel;

    /***是否需要发票*/
    private Integer billStatus;
}
