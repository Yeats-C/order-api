package com.aiqin.mgs.order.api.domain.request.returnorder;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("虚拟资产")
public class FranchiseeAsset implements Serializable {

    private Long id;

    @ApiModelProperty(value="优惠券类型，0-物流券 1-服纺券 2-A品券")
    @JsonProperty("coupon_type")
    private Integer couponType;

    @ApiModelProperty(value = "优惠券名称")
    @JsonProperty("coupon_name")
    private String couponName;

    @ApiModelProperty(value = "面值")
    @JsonProperty("nominal_Value")
    private double nominalValue;

    @ApiModelProperty(value = "有效期开始时间")
    @JsonProperty("validity_start_time")
    private Date validityStartTime;

    @ApiModelProperty(value = "使用状态")
    @JsonProperty("active_condition")
    private String activeCondition;

    @ApiModelProperty(value = "备注")
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value = "获得时间")
    @JsonProperty("acquire_time")
    private Date acquireTime;

    @ApiModelProperty(value = "加盟商id")
    @JsonProperty("franchisee_Id")
    private String franchiseeId;

    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    private String orderId;

    @ApiModelProperty(value = "使用来源")
    @JsonProperty("employ_source")
    private String employSource;

    @ApiModelProperty(value = "有效期结束时间")
    @JsonProperty("validity_end_time")
    private Date validityEndTime;

    @ApiModelProperty(value = "优惠券编码")
    @JsonProperty("coupon_code")
    private String couponCode;

    @ApiModelProperty(value = "获得时间")
    private Date createTime;

    @ApiModelProperty(value = "优惠券单号")
    @JsonProperty("Logistics_number")
    private String logisticsNumber;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "支付流水号")
    @JsonProperty("pay_serial_number")
    private String paySerialNumber;

    @ApiModelProperty(value = "余额支付")
    @JsonProperty("balance_pay")
    private double balancePay;

    /**
     * 物流券优惠总额度
     */
    private String  money;
    /**
     * 添加虚拟资产使用
     */
    private List<FranchiseeAsset> list;
}
