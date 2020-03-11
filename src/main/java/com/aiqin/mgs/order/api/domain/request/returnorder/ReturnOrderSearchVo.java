package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("退货单列表搜索项")
public class ReturnOrderSearchVo extends PagesRequest {

    @ApiModelProperty("退货单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_id")
    private String storeId;

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

    @ApiModelProperty(value = "订单类型 0直送、1配送、2辅采")
    @JsonProperty("order_type")
    private Integer orderType;

    @ApiModelProperty("收货区域 :区/县")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty("收货区域 :省编码")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty("收货区域 :市编码")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty("收货区域 :区/县编码")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value = "用户id--数据权限使用")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "菜单编码--数据权限使用")
    @JsonProperty("resource_code")
    private String resourceCode;

    @ApiModelProperty("所有门店id")
    private List<String> storeIds;

}
