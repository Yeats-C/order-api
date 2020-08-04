package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReturnOrderTypeResponse {

    @ApiModelProperty("枚举编码")
    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty("枚举名称")
    @JsonProperty("return_order_name")
    private String returnOrderName;
}
