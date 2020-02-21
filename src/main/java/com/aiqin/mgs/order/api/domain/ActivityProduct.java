package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author csf
 */
@ApiModel("促销活动商品范围bean")
@Data
public class ActivityProduct {
    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    /**活动id*/
    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";

    @ApiModelProperty(value = "商品编码")
    @JsonProperty("sku_code")
    private String skuCode="";

    @ApiModelProperty(value = "商品编码")
    @JsonProperty("product_code")
    private String productCode="";

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName="";

    /***商品品牌名称*/
    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    /***商品品牌编码*/
    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    /***商品品类名称*/
    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    /***商品品类编码*/
    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    /***原价(爱亲分销价)*/
    @ApiModelProperty(value = "原价(爱亲分销价)")
    @JsonProperty("price_tax")
    private BigDecimal priceTax;

    /***折扣*/
    @ApiModelProperty(value = "折扣")
    @JsonProperty("discount")
    private BigDecimal discount;

    /***减价金额*/
    @ApiModelProperty(value = "减价金额")
    @JsonProperty("reduce")
    private BigDecimal reduce;

    /***实际价格*/
    @ApiModelProperty(value = "实际价格")
    @JsonProperty("actual_price")
    private BigDecimal actualPrice;

    /***状态，0为启用，1为禁用*/
    @ApiModelProperty(value = "状态，0为启用，1为禁用")
    @JsonProperty("status")
    private Integer status;

    /***是否删除0否1是*/
    @ApiModelProperty(value = "是否删除0否1是")
    @JsonProperty("is_delete")
    private Integer isDelete;

   /***活动范围：1.按单品设置2.按品类设置3.按品牌设置4.按单品排除*/
    @ApiModelProperty(value = "活动范围：1.按单品设置2.按品类设置3.按品牌设置4.按单品排除")
    @JsonProperty("activity_scope")
    private Integer activityScope;

    /***创建人*/
    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    /***修改人*/
    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;

    /***创建时间*/
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    /***更新时间*/
    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    /***子节点集合*/
    @ApiModelProperty(value = "子节点集合")
    @JsonProperty("activity_product_list")
    private List<ActivityProduct> activityProductList;
}
