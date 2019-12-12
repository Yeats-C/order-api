package com.aiqin.mgs.order.api.domain.response.conversionrate;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ch
 * Date: 2019/12/12 15:55
 * Description:
 */
@Data
@ApiModel("门店转化率年月resp")
public class StoreTransforRateYearMonth {

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

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("总客流数")
    @JsonProperty("total_customer_flow")
    private String totalCustomerFlow;

    @ApiModelProperty("总订单数")
    @JsonProperty("order_num")
    private String orderNum;

    @ApiModelProperty("目标转化率")
    @JsonProperty("goal_rate")
    private BigDecimal goalRate;

    @ApiModelProperty("实际转化率")
    @JsonProperty("actual_transfor_rate")
    private BigDecimal actualTransforRate;
}
