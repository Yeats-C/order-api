package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author csf
 */
@ApiModel("促销活动赠品bean")
@Data
public class ActivityGift {

    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    /**赠品规则id唯一标识*/
    @ApiModelProperty(value = "赠品规则id唯一标识")
    @JsonProperty("rule_id")
    private String ruleId="";


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

}
