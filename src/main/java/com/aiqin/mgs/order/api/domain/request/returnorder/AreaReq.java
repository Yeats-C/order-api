package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: AreaReq
 * date: 2019/12/26 15:44
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("退货列表区域查询门店使用")
public class AreaReq implements Serializable {

    @ApiModelProperty(value="省编码")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value="市编码")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="区编码")
    @JsonProperty("district_id")
    private String districtId;

}
