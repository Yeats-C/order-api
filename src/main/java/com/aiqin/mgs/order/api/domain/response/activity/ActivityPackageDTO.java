package com.aiqin.mgs.order.api.domain.response.activity;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author by wpp25
 * @Classname MarketDTO
 * @Description
 * @Date 2020/3/3 10:47
 */

@Data
@ApiModel("营销活动套餐信息")
public class ActivityPackageDTO extends PagesRequest {
    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "套餐id")
    @JsonProperty("package_id")
    private String packageId;

    @ApiModelProperty(value = "套餐code")
    @JsonProperty("package_code")
    private String packageCode;

    @ApiModelProperty(value = "套餐名称")
    @JsonProperty("package_name")
    private String packageName;

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "活动编码  活动编号")
    @JsonProperty("activity_num")
    private String activityNum;

    @ApiModelProperty(value = "预期毛利率")
    @JsonProperty("expected_gross_margin")
    private Integer expectedGrossMargin;

    @ApiModelProperty(value = "套餐价格")
    @JsonProperty("amount")
    private Long amount;

    @ApiModelProperty(value = "套餐主图")
    @JsonProperty("main_image")
    private String mainImage;

    @ApiModelProperty(value = "是否删除 0: 不删除  1 ：删除")
    @JsonProperty("is_delete")
    private Integer isDelete;

    @ApiModelProperty(value = "活动创建时间")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "活动修改时间")
    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "销量")
    @JsonProperty("product_sale")
    private Integer productSale;

    @ApiModelProperty(value = "库存")
    @JsonProperty("product_stock")
    private Long productStock;

    @ApiModelProperty(value = "上下架状态(1为上架,其余均为下架)")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "套餐对应的商品信息")
    @JsonProperty("product_list")
    private List<ActivityPackageProductDTO> productList;


}
