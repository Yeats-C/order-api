package com.aiqin.mgs.order.api.domain.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("门店城市信息")
public class NewStoreCategory {

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "行政区域-市id")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value = "行政区域-省id")
    @JsonProperty("province_id")
    private String provinceId;


}
