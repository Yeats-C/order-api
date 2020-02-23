package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.component.enums.activity.ActivityTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("购物车商品楼层")
@Data
public class CartGroupInfo {

    @ApiModelProperty(value = "是否是有活动的楼层  0:不是，作为普通商品楼层解析;  1:是，作为有活动的楼层解析")
    @JsonProperty("has_activity")
    private Integer hasActivity;

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "活动名称")
    @JsonProperty("activity_name")
    private String activityName;

    @ApiModelProperty(value = "活动类型")
    @JsonProperty("activity_type")
    private String activityType;

    @ApiModelProperty(value = "活动类型描述")
    @JsonProperty("activity_type_desc")
    private String activityTypeDesc;

    @ApiModelProperty(value = "楼层的商品（本品）列表")
    @JsonProperty("cart_order_list")
    private List<CartOrderInfo> cartOrderList;


    public String getActivityTypeDesc() {
        return ActivityTypeEnum.getEnumDesc(activityType);
    }

}
