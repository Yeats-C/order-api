package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("区域门店树形结构实体类")
public class NewStoreTreeResponse implements Serializable {

    @ApiModelProperty("门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty("门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value="省份(直辖市)")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value="城市(直辖市区)")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="区(县级市)")
    @JsonProperty("district_id")
    private String districtId;

}
