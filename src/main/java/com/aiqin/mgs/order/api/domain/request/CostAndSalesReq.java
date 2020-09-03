package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2020/9/1 19:18
 * Description:
 */
@Data
public class CostAndSalesReq extends PagesRequest {
    @JsonProperty("distributor_id")
    @ApiModelProperty(value = "分销机构id/门店")
    private String distributorId;
    @JsonProperty("distributor_code")
    @ApiModelProperty(value = "分销机构code/门店")
    private String distributorCode;


    @ApiModelProperty(value = "开始时间")
    @JsonProperty("begin_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty(value = "开始时间")
    @JsonProperty("end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "收银员id")
    @JsonProperty("cashier_id")
    private String cashierId;

    @ApiModelProperty("商品属性code")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty("商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty("商品品类,0:全部品类，1：1级品类，2：2级品类，3：3级品类，4：4级品类")
    @JsonProperty("product_category")
    private Integer productCategory;



    @JsonProperty("brand_id")
    @ApiModelProperty("品牌类型id")
    private String brandId;

    @JsonProperty("brand_name")
    @ApiModelProperty("品牌类型名称")
    private String brandName;

    @ApiModelProperty(value = "商品sku码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("sku_name")
    private String skuName;
}
