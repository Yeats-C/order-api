package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: ReportAreaReturnSituationVo
 * date: 2020/3/2 19:10
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("各地区退货情况统计表请求类")
public class ReportAreaReturnSituationVo implements Serializable {

    @ApiModelProperty("类型")
    @JsonProperty("type")
    private Integer type;

    @ApiModelProperty("退货理由")
    @JsonProperty("reason_code")
    private String reasonCode;

}
