package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2020/9/1 19:33
 * Description:
 */
@Data
public class CostAndSalesResp {

    @ApiModelProperty("编码")
    @JsonProperty("code")
    private String code;

    @ApiModelProperty("名称")
    @JsonProperty("name")
    private String name;


    @ApiModelProperty(value = "销售商品数量")
    @JsonProperty("sale_product_num")
    private Long saleProductNum;

    @ApiModelProperty(value = "退货商品数量")
    @JsonProperty("return_product_num")
    private Long returnProductNum;

    @ApiModelProperty(value = "销售金额")
    @JsonProperty("total_amount")
    private Long totalAmount;


    @ApiModelProperty(value = "退货金额")
    @JsonProperty("return_price")
    private Long returnPrice;


    @ApiModelProperty(value = "退货数量")
    @JsonProperty("return_amount")
    private Long returnAmount;

    @ApiModelProperty(value = "实际销售额")
    @JsonProperty("amount")
    private Long amount;

    @ApiModelProperty("商品成本")
    @JsonProperty("product_cost")
    private Long productCost;

    @ApiModelProperty(value = "毛利额")
    @JsonProperty("product_maori")
    private Long productMaori;

    @JsonProperty("gross_profit_rate")
    @ApiModelProperty("毛利率")
    private Integer grossProfitRate;

    @JsonProperty("cost_points")
    @ApiModelProperty("积分")
    private Integer costPoints;
}
