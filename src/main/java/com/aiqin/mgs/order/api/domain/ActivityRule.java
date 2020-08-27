package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author csf
 */
@ApiModel("促销活动优惠方式（规则）bean")
@Data
public class ActivityRule {

    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    /**活动id*/
    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";

    /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单7.买赠*/
    @ApiModelProperty(value = "活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单7.买赠")
    @JsonProperty("activity_type")
    private Integer activityType;

    /**优惠单位：0.无条件 1.按数量（件）2.按金额（元）*/
    @ApiModelProperty(value = "优惠单位：0.无条件 1.按数量（件）2.按金额（元")
    @JsonProperty("rule_unit")
    private Integer ruleUnit;

    /**满足条件*/
    @ApiModelProperty(value = "满足条件(满多少参加活动)")
    @JsonProperty("meeting_conditions")
    private BigDecimal meetingConditions;

    /**优惠金额、优惠件数、折扣点数（百分比）*/
    @ApiModelProperty(value = "优惠金额、优惠件数、折扣点数（百分比）")
    @JsonProperty("preferential_amount")
    private BigDecimal preferentialAmount;

    /**规则序号*/
    @ApiModelProperty(value = "规则序号")
    @JsonProperty("rule_num")
    private Integer ruleNum;

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

    /**规则id唯一标识*/
    @ApiModelProperty(value = "规则id唯一标识")
    @JsonProperty("rule_id")
    private String ruleId="";

    /***赠品List*/
    @ApiModelProperty(value = "赠品List")
    @JsonProperty("gift_list")
    private List<ActivityGift> giftList;

    /***附加字段 赠品数量*/
    @ApiModelProperty(value = "赠品数量")
    @JsonProperty("gift_number")
    private Integer giftNumber;

    @ApiModelProperty(value = "商品编码")
    @JsonProperty("sku_code")
    private String skuCode="";

    public Integer getGiftNumber() {
        int number = 0;
        if (giftList != null && giftList.size() > 0) {
            for (ActivityGift item :
                    giftList) {
                number += item.getNumbers() != null ? item.getNumbers() : 0;
            }
        }
        return number;
    }

        /**买赠活动sku门槛*/
    @ApiModelProperty(value = "买赠活动sku门槛")
    @JsonProperty("threshold")
    private BigDecimal threshold;
}
