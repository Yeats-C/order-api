package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("门店和加盟商信息")
public class StoreFranchiseeInfoResponse {

    @ApiModelProperty(value="门店信息")
    @JsonProperty("store_info")
    private StoreInfo storeInfo;
}
