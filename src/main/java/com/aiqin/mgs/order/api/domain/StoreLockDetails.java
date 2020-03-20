package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("锁库明细表")
public class StoreLockDetails {

    @ApiModelProperty(value="id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value="仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value="仓库名称")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value="库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value="库房名称")
    @JsonProperty("warehouse_name")
    private String warehouseName;

    @ApiModelProperty(value="sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value="sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value="锁定库存数")
    @JsonProperty("lock_count")
    private Long lockCount;

    @ApiModelProperty(value="行号")
    @JsonProperty("line_code")
    private Long lineCode;

    @ApiModelProperty(value="订单编码")
    @JsonProperty("order_code")
    private Long orderCode;

}