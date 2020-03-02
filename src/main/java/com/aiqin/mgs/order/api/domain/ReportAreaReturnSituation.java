package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("售后管理--各地区退货情况统计表")
public class ReportAreaReturnSituation implements Serializable {

    @ApiModelProperty("ID")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty("省编码")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty("省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty("退货单数")
    @JsonProperty("return_count")
    private Long returnCount;

    @ApiModelProperty("退货金额")
    @JsonProperty("return_amount")
    private BigDecimal returnAmount;

    @ApiModelProperty("类型")
    @JsonProperty("type")
    private Integer type;

    @ApiModelProperty("创建时间")
    @JsonProperty("create_time")
    private Date createTime;

}