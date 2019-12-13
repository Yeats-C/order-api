package com.aiqin.mgs.order.api.domain.response.sales;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ch
 * Date: 2019/12/12 20:03
 * Description:
 */
@Data
@ApiModel("18A销售额resp")
public class StoreSalesEighteenAchievedResp {

    @ApiModelProperty("18A每月品牌销售额")
    @JsonProperty("store_sales_eighteen_achieved_brand")
    private List<StoreSalesEighteenAchievedBrand> storeSalesEighteenAchievedBrands;

    @ApiModelProperty("18A年销售额")
    @JsonProperty("store_sales_achieved_year_months")
    private List<StoreSalesAchievedYearMonth> storeSalesAchievedYearMonths;
}
