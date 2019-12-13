package com.aiqin.mgs.order.api.domain.response.conversionrate;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ch
 * Date: 2019/12/12 15:37
 * Description:
 */
@Data
@ApiModel("门店转化率resp")
public class StoreTransforRateResp {

    @ApiModelProperty("门店转化率日")
    @JsonProperty("store_transfor_rate_dailies")
    private List<StoreTransforRateDaily> storeTransforRateDailies;

    @ApiModelProperty("门店转化率年月")
    @JsonProperty("store_transfor_rate_yearMonths")
    private List<StoreTransforRateYearMonth> storeTransforRateYearMonths;

}
