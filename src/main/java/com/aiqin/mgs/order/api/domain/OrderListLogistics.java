package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("订单物流从表")
@Data
public class OrderListLogistics {
    private String id;

    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_list_id")
    private Long orderListId;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "发货单号")
    @JsonProperty("invoice_code")
    private String invoiceCode;

    @ApiModelProperty(value = "物流中心标示")
    @JsonProperty("logistics_centre_code")
    private String logisticsCentreCode;

    @ApiModelProperty(value = "物流中心名称")
    @JsonProperty("logistics_centre_name")
    private String logisticsCentreName;

//    @ApiModelProperty(value = "发货时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @JsonProperty("delivery_time")
//    private Date deliveryTime;

    @ApiModelProperty(value = "执行人")
    @JsonProperty("implement_by")
    private String implementBy;

    @ApiModelProperty(value = "执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("implement_time")
    private Date implementTime;

    @ApiModelProperty(value = "执行内容")
    @JsonProperty("implement_content")
    private String implementContent;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;
}