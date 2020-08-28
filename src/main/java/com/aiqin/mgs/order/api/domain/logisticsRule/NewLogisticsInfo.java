package com.aiqin.mgs.order.api.domain.logisticsRule;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "新规则-物流减免规则")
public class NewLogisticsInfo {

    @ApiModelProperty(value = "规则类型：10新规则 ---不用传")
    @JsonProperty("rult_type")
    private Integer rultType;

    @ApiModelProperty(value = "物流减免说明")
    @JsonProperty("logistics_explain")
    private String logisticsExplain;

    @ApiModelProperty(value = "金额要求")
    @JsonProperty("amount_required")
    private BigDecimal amountRequired;

    @ApiModelProperty(value = "物流费比例")
    @JsonProperty("logistics_proportion")
    private BigDecimal logisticsProportion;

    @ApiModelProperty(value = "物流减免规则唯一编码---前端不用传")
    @JsonProperty("rult_code")
    private String rultCode;

    @ApiModelProperty(value = "物流订单类型 1按品类 2按品牌 3按商品属性")
    @JsonProperty("logistics_order_type")
    private Integer logisticsOrderType;

    @ApiModelProperty(value = "减免范围 1全部  2部分")
    @JsonProperty("reduce_scope")
    private Integer reduceScope;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;


}
