package com.aiqin.mgs.order.api.domain.response.orderlistre;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-11 11:48
 */
@ApiModel("订单查询一段时间进货量返回vo")
@Data
public class OrderStockReVo {
    @ApiModelProperty(value = "门店销量")
    @JsonProperty("stock_value")
    private Integer stockValue;

    @ApiModelProperty(value = "门店code")
    @JsonProperty("store_code")
    private String storeCode;


}
