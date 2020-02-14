package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author csf
 */
@ApiModel("促销活动优惠方式（规则）bean")
@Data
public class ActivityRule {

    /**活动id*/
    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";

    /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单*/
    @ApiModelProperty(value = "活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单")
    @JsonProperty("activity_type")
    private Integer activityType;

    /**优惠单位：1.按数量（件）2.按金额（元）*/
    @ApiModelProperty(value = "优惠单位：1.按数量（件）2.按金额（元")
    @JsonProperty("rule_unit")
    private Integer rule_unit;

    /**满足条件*/
    @ApiModelProperty(value = "满足条件(满多少参加活动)")
    @JsonProperty("meeting_conditions")
    private BigDecimal meetingConditions;

    /**优惠金额*/
    @ApiModelProperty(value = "优惠金额")
    @JsonProperty("preferential_amount")
    private BigDecimal preferentialAmount;

    /**规则序号*/
    @ApiModelProperty(value = "规则序号")
    @JsonProperty("rule_num")
    private Integer ruleNum;

    /**sku编码(满赠规则使用)*/
    @ApiModelProperty(value = "sku编码(满赠规则使用)")
    @JsonProperty("sku_code")
    private String skuCode="";

    /**商品编码(满赠规则使用)*/
    @ApiModelProperty(value = "商品编码(满赠规则使用)")
    @JsonProperty("product_code")
    private String productCode="";

    /**商品名称(满赠规则使用)*/
    @ApiModelProperty(value = "商品名称(满赠规则使用)")
    @JsonProperty("product_name")
    private String productName="";

    /**商品数量(满赠规则使用)*/
    @ApiModelProperty(value = "商品数量(满赠规则使用)")
    @JsonProperty("numbers")
    private Integer numbers;


    /***创建人*/
    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    /***修改人*/
    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;

    /***创建时间*/
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    /***更新时间*/
    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;
}
