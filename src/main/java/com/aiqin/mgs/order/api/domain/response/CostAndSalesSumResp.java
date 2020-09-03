package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2020/9/2 15:35
 * Description:
 */
@Data
public class CostAndSalesSumResp {

    @ApiModelProperty(value = "销售总数量")
    @JsonProperty("sale_all_num")
    private Integer saleAllNum=0;

    @ApiModelProperty(value = "销售总金额")
    @JsonProperty("total_amount")
    private Long totalAmount=0L;

    @ApiModelProperty("商品成本")
    @JsonProperty("product_all_cost")
    private Long productAllCost=0L;

    @ApiModelProperty(value = "毛利额")
    @JsonProperty("product_all_maori")
    private Long productAllMaori=0L;

    @JsonProperty("gross_profit_all_rate")
    @ApiModelProperty("毛利率")
    private Float grossProfitAllRate;
}
