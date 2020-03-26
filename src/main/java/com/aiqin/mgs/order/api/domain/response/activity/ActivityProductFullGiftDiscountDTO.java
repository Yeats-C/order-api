package com.aiqin.mgs.order.api.domain.response.activity;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author by wpp25
 * @Classname MarketDTO
 * @Description
 * @Date 2020/3/3 10:47
 */

@Data
@ApiModel("营销活动 满赠折扣dto")
public class ActivityProductFullGiftDiscountDTO extends PagesRequest {
    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "活动编号 也是 活动编码")
    @JsonProperty("activity_num")
    private String activityNum;

    @ApiModelProperty(value = "折扣id")
    @JsonProperty("discount_id")
    private String discountId;

    @ApiModelProperty(value = "商品折扣规则类型 1： 按金额 2 按数量（件）")
    @JsonProperty("rule_type")
    private Integer ruleType;

    @ApiModelProperty(value = "折扣条件 满多少元 或 满 多少件")
    @JsonProperty("discount_condition")
    private Long discountCondition;

    @ApiModelProperty(value = "活动创建时间")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "活动修改时间")
    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;




}
