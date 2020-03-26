package com.aiqin.mgs.order.api.domain.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: mamingze
 * @Date: 2020-03-17 16:19
 * @Description:
 */
@Data
@ApiModel("省市code封装实体")
public class AreaReq {
    @ApiModelProperty("省 Code")
    @JsonProperty("province_code")
    private String provinceCode;

    @ApiModelProperty("城市 Code")
    @JsonProperty("city_code")
    private String cityCode;
}
