package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("物流减免规则类型信息")
public class LogisticsRuleType {


    @ApiModelProperty(value = "规则类型：0单品购买数量 1单品购买金额 2累计购买数量 3累计购买金额")
    @JsonProperty("rult_type")
    private Integer rultType;

    @ApiModelProperty(value = "规则类型名称")
    @JsonProperty("rult_type_name")
    private String rultTypeName;

    @ApiModelProperty(value = "物流减免规则唯一编码---前端不用传")
    @JsonProperty("rult_code")
    private String rultCode;

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
