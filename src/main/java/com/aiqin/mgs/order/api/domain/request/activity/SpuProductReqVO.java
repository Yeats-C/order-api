package com.aiqin.mgs.order.api.domain.request.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.aiqin.mgs.order.api.base.PagesRequest;
import java.util.List;

/**
 * @Auther: mamingze
 * @Date: 2019-12-05 15:51
 * @Description:
 */
@Data
@ApiModel("查询spu商品信息信息")
public class SpuProductReqVO extends PagesRequest {

    @ApiModelProperty("供货渠道类别code")
    @JsonProperty("categories_supply_channels_code")
    private String categoriesSupplyChannelsCode;

    @ApiModelProperty("供货渠道类别名称")
    @JsonProperty("categories_supply_channels_name")
    private String categoriesSupplyChannelsName;

    @ApiModelProperty("商品品类code")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty("商品属性code")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty("商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty("商品品牌code")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty("商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty("spu编码")
    @JsonProperty("product_code")
    private String productCode;

    @ApiModelProperty("spu名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty("sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty("是否需要棉品类：0:不需要 1:需要")
    @JsonProperty("is_cotton")
    private String isCotton;


    @ApiModelProperty("标签编码")
    @JsonProperty("tag_code")
    private String tagCode;

    @ApiModelProperty("标签名称")
    @JsonProperty("tag_name")
    private String tagName;

    @ApiModelProperty("条形码")
    @JsonProperty("bar_code")
    private String barCode;

    @ApiModelProperty("省 Code")
    @JsonProperty("province_code")
    private String provinceCode;

    @ApiModelProperty("城市 Code")
    @JsonProperty("city_code")
    private String cityCode;

    @ApiModelProperty("公司编号")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty("排序方式:0:库存;1:价格;2:订货 3:销量")
    @JsonProperty("order_by_type")
    private String orderByType;

    @ApiModelProperty(value = "排序 降序 desc,升序 asc")
    @JsonProperty("sort")
    private String sort;

    @ApiModelProperty("0:有库存 1:没有库存 2:全部;")
    @JsonProperty("exit_stock")
    private String exitStock;

    @ApiModelProperty("可用的spuCode集合")
    @JsonProperty("spu_codes")
    private String spuCodes ;

    @ApiModelProperty("库存量少的skuCodes")
    @JsonProperty("stock_less_codes")
    private String  stockLessCodes;

    @ApiModelProperty(" skucode，spuCode，条形码  查询")
    @JsonProperty("key_code")
    private String  keyCode;


    @ApiModelProperty("库房类型编码：1:销售库，2：特卖库")
    @JsonProperty("warehouse_type_code")
    private String  warehouseTypeCode;


    @ApiModelProperty("对应运输中心")
    @JsonProperty("transport_center_code")
    private List<String>  transportCenterCodes;

    @ApiModelProperty("排除的Sku编码List")
    @JsonProperty("exclude_sku_codes")
    private List<String>  excludeSkuCodes;

    @ApiModelProperty("包含Sku编码List")
    @JsonProperty("include_sku_codes")
    private List<String>  includeSkuCodes;

    /**活动id*/
    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";
}
