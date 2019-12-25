package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取门店畅滞销sku")
public class ProductSeachSkuReq extends PagesRequest{

    @ApiModelProperty(value = "分销机构编码")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "状态：0畅销  1滞销")
    @JsonProperty("status")
    private Integer status;
}
