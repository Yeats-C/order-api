package com.aiqin.mgs.order.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Createed by sunx on 2019/4/8.<br/>
 */
@Data
@ApiModel("库存低于预警库存的商品信息-畅缺商品使用")
public class ProductDistributorOrderDTO {
    @ApiModelProperty(value = "sku_code")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "分销机构id")
    @JsonProperty("distributor_id")
    private String distributorId;

    @ApiModelProperty(value = "陈列仓位库存")
    @JsonProperty("display_stock")
    private Integer displayStock;

    @ApiModelProperty(value = "退货仓位库存")
    @JsonProperty("return_stock")
    private Integer returnStock;

    @ApiModelProperty(value = "存储仓位库存")
    @JsonProperty("storage_stock")
    private Integer storageStock;

    @ApiModelProperty(value = "锁库库存")
    @JsonProperty("lock_stock")
    private Integer lockStock;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty(value = "列表图")
    @JsonProperty("logo")
    private String logo;
}
