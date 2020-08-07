package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel
public class ReturnRefundStatus {

    @ApiModelProperty(value = "订单号 ---传")
    private String orderStoreCode;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "(申请退货数量)实退数量 ----传")
    private Long actualReturnProductCount;

    @ApiModelProperty(value = "退款状态，0-未退款、1-已退款")
//    @JsonProperty("refund_status")
    private Integer refundStatus;

    @ApiModelProperty("退货单号")
//    @JsonProperty("return_order_code")
    private String returnOrderCode;

}
