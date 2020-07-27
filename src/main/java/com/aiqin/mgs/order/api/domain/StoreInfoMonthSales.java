package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 门店信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 15:57
 */
@Data
@ApiModel("门店上月销量入参信息")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class StoreInfoMonthSales {

//    @ApiModelProperty(value="门店数据集合")
//    @JsonProperty("store_info_list")
//    private List<StoreInfo> storeInfoList;

    @ApiModelProperty(value="门店数据集合")
    @JsonProperty("store_code_list")
    private List<String> storeCodeList;

    @ApiModelProperty("年月")
    @JsonProperty(value = "stat_year_month")
    private String statYearMonth;

}
