package com.aiqin.mgs.order.api.domain.response.sales;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ch
 * Date: 2019/12/13 10:24
 * Description:
 */
@Data
@ApiModel("销售毛利resp")
public class StoreSaleMarginResp {

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

    @ApiModelProperty("毛利额")
    @JsonProperty("sale_margin")
    private BigDecimal saleMargin;

    @ApiModelProperty("上月毛利额")
    @JsonProperty("last_month_margin")
    private BigDecimal lastMonthMargin;

    @ApiModelProperty("上年毛利额")
    @JsonProperty("last_year_margin")
    private BigDecimal lastYearMargin;

    @ApiModelProperty("环比增长率")
    @JsonProperty("chain_growth")
    private BigDecimal chainGrowth;

    @ApiModelProperty("同比增长率")
    @JsonProperty("year_on_year_growth")
    private BigDecimal yearOnYearGrowth;

    @ApiModelProperty("当月18a销售毛利")
    @JsonProperty("eighteen_sale_margin")
    private BigDecimal eighteenSaleMargin;
}
