package com.aiqin.mgs.order.api.domain.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("首单报表返回区域信息")
public class FirstReportResponse {

    @ApiModelProperty(value = "经营区域id")
    private String copartner_area_id;

    @ApiModelProperty(value = "经营区域名称")
    private String copartner_area_name;
}
