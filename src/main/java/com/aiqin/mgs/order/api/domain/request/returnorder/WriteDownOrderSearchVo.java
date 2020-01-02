package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description: AfterReturnOrderSearchVo
 * date: 2019/12/26 17:22
 * author: hantao
 * version: 1.0
 */

@Data
@ApiModel("售后管理--冲减单列表搜索项")
public class WriteDownOrderSearchVo implements Serializable {

    @ApiModelProperty("冲减单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("申请开始时间")
    @JsonProperty("apply_start_time")
    private String applyStartTime;

    @ApiModelProperty("申请结束时间")
    @JsonProperty("apply_end_time")
    private String applyEndTime;

    @ApiModelProperty("退货单状态 ")
    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;

    @ApiModelProperty(value = "配送中心")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

}
