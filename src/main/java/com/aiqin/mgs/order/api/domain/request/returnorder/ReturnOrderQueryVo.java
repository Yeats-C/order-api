package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: AfterReturnOrderSearchVo
 * date: 2019/12/26 17:22
 * author: hantao
 * version: 1.0
 */

@Data
@ApiModel("售后管理--退货单列表搜索项")
public class ReturnOrderQueryVo extends AfterReturnOrderSearchVo implements Serializable {

    @ApiModelProperty(value = "配送中心")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货、3冲减单")
    @JsonProperty("return_order_type")
    private Integer returnOrderType;

    @ApiModelProperty(value="退款状态 0退款中，1已完成")
    @JsonProperty("refund_status")
    private Integer refundStatus;

}
