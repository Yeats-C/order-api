package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/9/4 10:58
 * Description:
 */
@Data
public class CalculateReqVo {
    @ApiModelProperty("门店id")
    @JsonProperty("distributor_id")
    private String distributorId;


    @ApiModelProperty("订单商品sku_code集合")
    @JsonProperty("sku_code_list")
    private List<String> skuCodeList;

}
