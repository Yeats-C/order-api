package com.aiqin.mgs.order.api.domain.response.activity;


import com.aiqin.mgs.order.api.domain.response.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@ApiModel("活动")
@Data
public class ActivityDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = -3249241994881611312L;

    @ApiModelProperty(value = "活动id，业务id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "活动编号，用来显示")
    @JsonProperty("activity_num")
    private String activityNum;

    @ApiModelProperty(value = "活动名称")
    @JsonProperty("activity_name")
    private String activityName;

    @ApiModelProperty(value = "活动类型，分为领券活动和促销活动 0:领券活动 1:促销活动 2：限时折扣活动 3:定金活动 4:妈妈班 5:抽奖活动 6:满减 7：满赠 8：套餐包 9 ：第N件特价")
    @JsonProperty("activity_type")
    private Integer activityType;

    @ApiModelProperty(value = "活动状态，分为未开始、进行中和已过期")
    @JsonProperty("activity_status")
    private Integer activityStatus;

    @ApiModelProperty(value = "活动创建方，0为总部，1为门店自主创建")
    @JsonProperty("activity_permission")
    private Integer activityPermission;

    @ApiModelProperty(value = "0为不限购，1为每人每种商品限购，2为每人每种前几件商品限购")
    @JsonProperty("purchase_limit")
    private Integer purchaseLimit;

    @ApiModelProperty(value = "限购数量")
    @JsonProperty("limit_count")
    private Integer limitCount;

    @ApiModelProperty(value = "活动开始时间")
    @JsonProperty("begin_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @NotBlank
    private Date beginTime;

    @ApiModelProperty(value = "活动结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("finish_time")
    private Date finishTime;

    @ApiModelProperty(value = "活动对应门店类型，分为全国、部分省市区和部分门店")
    @JsonProperty("activity_store_type")
    private Integer activityStoreType;

    @ApiModelProperty(value = "本活动中每人最多领取优惠券的数量")
    @JsonProperty("everyone_num")
    private Integer everyoneNum;

    @ApiModelProperty(value = "本活动中每人每天最多领取优惠券的数量")
    @JsonProperty("everyone_day_num")
    private Integer everyoneDayNum;

    @ApiModelProperty(value = "销售目标")
    @JsonProperty("sale_target")
    private Long saleTarget;

    @ApiModelProperty(value = "预计客户数")
    @JsonProperty("estimate_customer_num")
    private Long estimateCustomerNum;

    @ApiModelProperty(value = "预计费用")
    @JsonProperty("estimate_cost")
    private Long estimateCost;

    @ApiModelProperty(value = "预计费比")
    @JsonProperty("estimate_cost_percentage")
    private Long estimateCostPercentage;

    @ApiModelProperty(value = "活动创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "活动修改人")
    @JsonProperty("update_by")
    private String updateBy;

    @ApiModelProperty(value = "是否删除")
    @JsonProperty("is_delete")
    private Integer isDelete;


    @ApiModelProperty(value = "预期毛利润 单位 分")
    @JsonProperty("expected_gross_profit_amount")
    private String expectedGrossProfitAmount;

    @ApiModelProperty(value = "预期销售额 单位分")
    @JsonProperty("expected_sale_amount")
    private String expectedSaleAmount;

    @ApiModelProperty(value = "活动商品范围  1 ： 全部 门店商品 2：部分门店商品")
    @JsonProperty("product_range")
    private Integer productRange;

    @ApiModelProperty(value = "适用客户范围  1：所有客户  2： 仅会员")
    @JsonProperty("applicable_customers_range")
    private String applicableCustomersRange;
}