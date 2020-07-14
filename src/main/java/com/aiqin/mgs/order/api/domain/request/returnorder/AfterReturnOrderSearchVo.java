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
@ApiModel("售后管理--退货单列表搜索项")
public class AfterReturnOrderSearchVo implements Serializable {

    @ApiModelProperty("退货单号")
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

    @ApiModelProperty("批发客户名称 ")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty("地区层级编码对象")
    @JsonProperty("area_req")
    private AreaReq areaReq;

    @ApiModelProperty("所有门店id")
    private List<String> storeIds;

    //用来区分是批发业务还是门店业务
    @ApiModelProperty("区分业务---0门店业务  1批发业务 ")
    @JsonProperty("business_form")
    private Integer businessForm;

//    @ApiModelProperty(value = "订单类型 0直送、1配送、2辅采")
    @ApiModelProperty(value = "订单类型 1直送 2配送 3货架")
    @JsonProperty("order_type")
    private Integer orderType;

    @ApiModelProperty(value = "退货原因编码 14:质量问题退货 15:一般退货")
    @JsonProperty("return_reason_code")
    private String returnReasonCode;

    @ApiModelProperty(value = "用户id--数据权限使用")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "菜单编码--数据权限使用")
    @JsonProperty("resource_code")
    private String resourceCode;

}
