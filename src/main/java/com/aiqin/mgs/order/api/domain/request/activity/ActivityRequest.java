package com.aiqin.mgs.order.api.domain.request.activity;

import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityProduct;
import com.aiqin.mgs.order.api.domain.ActivityRule;
import com.aiqin.mgs.order.api.domain.ActivityStore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author csf
 */
@Data
@ApiModel("活动新建、修改请求参数")
public class ActivityRequest implements Serializable {

    /**活动信息*/
    @ApiModelProperty(value = "活动信息")
    @JsonProperty("activity")
    private Activity activity;

    /**参与门店集合*/
    @ApiModelProperty(value = "参与门店列表集合")
    @JsonProperty("activityStores")
    private List<ActivityStore> activityStores;

    /**参与活动商品范围列表集合*/
    @ApiModelProperty(value = "参与活动商品范围列表集合")
    @JsonProperty("activityProducts")
    private List<ActivityProduct> activityProducts;

    /**参与活动梯度规则列表集合*/
    @ApiModelProperty(value = "参与活动梯度规则列表集合")
    @JsonProperty("activityRules")
    private List<ActivityRule> activityRules;

}
