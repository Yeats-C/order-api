package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Createed by ch on 2019/12/24.<br/>
 */
@Data
@ApiModel("分销机构商品")
public class ProductOverViewReq {

    private static final long serialVersionUID = -2067479393862211291L;
    @ApiModelProperty(value = "分销机构编码")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty(value = "模糊查询SKU或者销售码")
    private String text;

    @ApiModelProperty(value = "商品品牌id")
    @JsonProperty("brand_id")
    private String brandId;

    @ApiModelProperty(value = "商品品牌")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value = "品类id")
    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("product_source_type")
    @ApiModelProperty("商品来源类型(0:平台  1:地采)")
    private Integer productSourceType;

    @JsonProperty("month")
    @ApiModelProperty("年月 ---前端不传")
    private String month;

    @ApiModelProperty(value = "升降序标识，asc  升序（低）  desc 降序（高）")
    @JsonProperty("asc_or_desc")
    private String ascOrDesc;

    @ApiModelProperty(value = "商品状态(0畅销，1滞销，2缺货/毛利（互用），3库存预警)")
    @JsonProperty("product_status")
    private Integer productStatus;
}
