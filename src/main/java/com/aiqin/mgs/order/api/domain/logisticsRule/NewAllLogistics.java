package com.aiqin.mgs.order.api.domain.logisticsRule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "物流减免列表信息")
@JsonInclude(value = JsonInclude.Include.ALWAYS)
public class NewAllLogistics {

    @ApiModelProperty(value = "物流减免说明")
    @JsonProperty("logistics_explain")
    private String logisticsExplain;

    @ApiModelProperty(value = "规则类型：10新规则")
    @JsonProperty("rult_type")
    private Integer rultType;

    @ApiModelProperty(value = "物流减免商品唯一id")
    @JsonProperty("rult_id")
    private String rultId;

    @ApiModelProperty(value = "物流减免规则唯一编码")
    @JsonProperty("rult_code")
    private String rultCode;

    @ApiModelProperty(value = "金额要求")
    @JsonProperty("amount_required")
    private BigDecimal amountRequired;

    @ApiModelProperty(value = "物流费比例")
    @JsonProperty("logistics_proportion")
    private BigDecimal logisticsProportion;

    @ApiModelProperty(value = "生效状态 0未开 1开启")
    @JsonProperty("effective_status")
    private Integer effectiveStatus;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

}
