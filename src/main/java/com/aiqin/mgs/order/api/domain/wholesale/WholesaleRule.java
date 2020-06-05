package com.aiqin.mgs.order.api.domain.wholesale;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 批发客户商品权限bean
 */
@Data
@ApiModel("批发客户商品权限bean")
public class WholesaleRule implements Serializable {

    @ApiModelProperty(value = "批发客户编码（唯一标识）")
    @JsonProperty("customer_code")
    private String customerCode;

    @ApiModelProperty(value = "仓库编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty("sku编号")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    @ApiModelProperty(value = "规则类型：1.仓库  2.品牌  3品类  4 单品")
    @JsonProperty("type")
    private String type;
}
