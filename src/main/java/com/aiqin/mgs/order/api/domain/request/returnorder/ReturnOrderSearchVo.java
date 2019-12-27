package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("退货单列表搜索项")
public class ReturnOrderSearchVo {

    @ApiModelProperty("退货单号")
    @JsonProperty("return_order_code")
    private String ReturnOrderCode;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty("市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty("申请开始时间")
    @JsonProperty("apply_start_time")
    private String applyStartTime;

    @ApiModelProperty("申请结束时间")
    @JsonProperty("apply_end_time")
    private String applyEndTime;

    @ApiModelProperty("退货单状态 ")
    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;

    @ApiModelProperty("订单类别：1：收单配送 2：首单赠送 3：配送补货 4：首单直送 5：直送补货 ")
    @JsonProperty("order_category")
    private Integer orderCategory;

    @ApiModelProperty("处理办法 0退货退款  1仅退款")
    @JsonProperty("treatment_method")
    private Integer treatmentMethod;

    @ApiModelProperty("退货类型  0客户退货、1缺货退货、2售后退货")
    @JsonProperty("return_order_type")
    private Integer returnOrderType;
}
