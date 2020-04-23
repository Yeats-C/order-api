package com.aiqin.mgs.order.api.domain.response.gift;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("修改门店可用赠品额度")
public class StoreAvailableGiftQuotaResponse {

    @ApiModelProperty(value="门店Id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value="可用赠品额度")
    @JsonProperty("available_gift_quota")
    private BigDecimal availableGiftQuota;

}
