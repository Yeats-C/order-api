package com.aiqin.mgs.order.api.domain.request.product;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: mamingze
 * @Date: 2019-12-05 15:51
 * @Description:
 */
@Data
@ApiModel("查询spu商品信息信息")
public class SkuProductReqVO extends PagesRequest {

    @ApiModelProperty("商品品类code")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty("商品属性code")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty("sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;


    @ApiModelProperty("库房类型编码：1:销售库，2：特卖库")
    @JsonProperty("warehouse_type_code")
    private String  warehouseTypeCode;

    @ApiModelProperty("省市code集合")
    @JsonProperty("area_codes")
    private List<AreaReq>  areaReqs;

    @ApiModelProperty("对应运输中心")
    @JsonProperty("transport_center_code")
    private List<String>  transportCenterCodes;



    @ApiModelProperty("是否可售：0为不可售，1为可售")
    @JsonProperty("is_sale")
    private Byte isSale;

    @ApiModelProperty("公司编号")
    @JsonProperty("company_code")
    private String companyCode;
}
