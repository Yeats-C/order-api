package com.aiqin.mgs.order.api.domain.response.sales;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ch
 * Date: 2019/12/12 19:27
 * Description:
 */
@Data
@ApiModel("总销售额resp")
public class StoreSalesAchievedResp {

    @ApiModelProperty("客流日流量")
    @JsonProperty("store_sales_achieved_dailies")
    private List<StoreSalesAchievedDaily> storeSalesAchievedDailies;

    @ApiModelProperty("客流月流量")
    @JsonProperty("store_sales_achieved_year_months")
    private List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths;
}
