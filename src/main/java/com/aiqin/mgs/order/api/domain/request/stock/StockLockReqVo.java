package com.aiqin.mgs.order.api.domain.request.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * StockLockReqVo
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Data
public class StockLockReqVo {
    @ApiModelProperty("市ID")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty("公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty("订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty("省ID")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty("sku列表")
    @JsonProperty("sku_list")
    private List<StockLockSkuReqVo> skuList;
}
