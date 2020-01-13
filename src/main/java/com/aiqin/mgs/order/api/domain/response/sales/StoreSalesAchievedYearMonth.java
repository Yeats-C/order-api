package com.aiqin.mgs.order.api.domain.response.sales;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ch
 * Date: 2019/12/12 19:29
 * Description:
 */
@Data
@ApiModel("总销售额年月resp(18A销售）")
public class StoreSalesAchievedYearMonth {

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

    @ApiModelProperty("销售目标额")
    @JsonProperty("goal_sale_amt")
    private BigDecimal goalSaleAmt;

    @ApiModelProperty("销售额")
    @JsonProperty("sale_amt")
    private BigDecimal saleAmt;

    @ApiModelProperty("销售成本")
    @JsonProperty("sale_cost")
    private BigDecimal saleCost;

    @ApiModelProperty("销售数量")
    @JsonProperty("sale_num")
    private Integer saleNum;

    @ApiModelProperty("毛利额")
    @JsonProperty("sale_margin")
    private BigDecimal saleMargin;

    @ApiModelProperty("销售额差值")
    @JsonProperty("different_amt")
    private BigDecimal differentAmt;

    @ApiModelProperty("是否达标状态（0不达标，1达标）")
    @JsonProperty("is_satisfy_code")
    private Integer isSatisfyCode;

    @ApiModelProperty("是否达标")
    @JsonProperty("is_satisfy")
    private String isSatisfy;
}
