package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "列表实体信息")
@JsonInclude(value = JsonInclude.Include.ALWAYS)
public class LogisticsAllResponse {

    @ApiModelProperty(value = "物流减免规则唯一编码")
    @JsonProperty("rult_code")
    private String rultCode;

    @ApiModelProperty(value = "规则类型：0单品购买数量 1单品购买金额 2累计购买数量 3累计购买金额")
    @JsonProperty("rult_type")
    private Integer rultType;

    @ApiModelProperty(value = "商品编码")
    @JsonProperty("product_code")
    private String productCode;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty(value = "品牌")
    @JsonProperty("brand")
    private String brand;

    @ApiModelProperty(value = "品类")
    @JsonProperty("category")
    private String category;

    @ApiModelProperty(value = "类型 0件 1元")
    @JsonProperty("types")
    private Integer types;

    @ApiModelProperty(value = "免运费门槛")
    @JsonProperty("fare_sill")
    private BigDecimal fareSill;

    @ApiModelProperty(value = "生效状态 0未开 1开启")
    @JsonProperty("effective_status")
    private Integer effectiveStatus;

    @ApiModelProperty(value = "操作时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "操作人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "spu编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "spu名称")
    @JsonProperty("spu_name")
    private String spuName;


}
