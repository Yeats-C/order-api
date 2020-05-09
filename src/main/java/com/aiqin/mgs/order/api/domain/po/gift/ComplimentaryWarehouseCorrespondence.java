package com.aiqin.mgs.order.api.domain.po.gift;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 兑换赠品与仓库对应关系
 *
 * @author: csf
 * @version: v1.0.0
 * @date 2020/04/04 17:18
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ComplimentaryWarehouseCorrespondence {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "仓库code")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

}
