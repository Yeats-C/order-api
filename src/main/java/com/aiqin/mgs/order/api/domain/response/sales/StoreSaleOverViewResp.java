package com.aiqin.mgs.order.api.domain.response.sales;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ch
 * Date: 2019/12/12 14:14
 * Description:
 */
@Data
@ApiModel("爱掌柜首页订单概览resp")
public class StoreSaleOverViewResp {

    @ApiModelProperty("主键id")
    @JsonProperty("id")
    private Integer id;

    @ApiModelProperty("年月")
    @JsonProperty("stat_year_month")
    private String statYearMonth;

    @ApiModelProperty("年")
    @JsonProperty("stat_year")
    private String statYear;

    @ApiModelProperty("月")
    @JsonProperty("stat_month")
    private String statMonth;

    @ApiModelProperty("门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty("门店code")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("订单数")
    @JsonProperty("order_num")
    private Integer orderNum;

    @ApiModelProperty("销售额(实销金额)")
    @JsonProperty("sale_amt")
    private Integer saleAmt;

    @ApiModelProperty("上月销售额")
    @JsonProperty("last_month_sale_amt")
    private Integer lastMonthSaleAmt;

    @ApiModelProperty("上年销售额")
    @JsonProperty("last_year_sale_amt")
    private Integer lastYearSaleAmt;

    @ApiModelProperty("环比增长率")
    @JsonProperty("chain_growth")
    private Integer chainGrowth;

    @ApiModelProperty("同比增长率")
    @JsonProperty("year_on_year_growth")
    private Integer yearOnYearGrowth;


}
