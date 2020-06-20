package com.aiqin.mgs.order.api.domain.response.returnorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("批发退货列表信息")
public class WholesaleReturnList {

    @ApiModelProperty(value = "退货单号")
//    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty(value = "订单号")
//    @JsonProperty("order_store_code")
    private String orderStoreCode;

    @ApiModelProperty(value = "门店名称")
//    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货、3冲减单 ")
//    @JsonProperty("return_order_type")
    private Integer returnOrderType;

    @ApiModelProperty(value = "退货金额")
//    @JsonProperty("return_order_amount")
    private BigDecimal returnOrderAmount;

    @ApiModelProperty("退货单状态 ")
//    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建人名称")
//    @JsonProperty("create_by_name")
    private String createByName;


}
