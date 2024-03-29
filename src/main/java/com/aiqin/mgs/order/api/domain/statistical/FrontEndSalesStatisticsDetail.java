package com.aiqin.mgs.order.api.domain.statistical;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatistics
 * @Description 前台销售统计明细实体类 精确到天
 * @Date 2020/2/15 14:07
 */
@Data
@ApiModel("前台销售统计数据")
public class FrontEndSalesStatisticsDetail {

    @JsonProperty("id")
    @ApiModelProperty("id")
    private Long id;

    @JsonProperty("sale_statistics_id")
    @ApiModelProperty("前台销售统计id")
    private String saleStatisticsId;

    @JsonProperty("sale_statistics_detail_id")
    @ApiModelProperty("前台销售统计明细id")
    private String saleStatisticsDetailId;

    @JsonProperty("store_id")
    @ApiModelProperty("门店id")
    private String storeId;

    @JsonProperty("order_detail_id")
    @ApiModelProperty("订单明细id")
    private String orderDetailId;

    @JsonProperty("sku_code")
    @ApiModelProperty("sku码")
    private String skuCode;

    @JsonProperty("sku_name")
    @ApiModelProperty("sku名")
    private String skuName;

    @JsonProperty("month")
    @ApiModelProperty("统计时间 精确到日 eg: 20200101")
    private Integer day;

    @JsonProperty("category_id")
    @ApiModelProperty("分类id")
    private String categoryId;

    @JsonProperty("category_name")
    @ApiModelProperty("分类名称")
    private String categoryName;

    @JsonProperty("sale_total_count")
    @ApiModelProperty("销售总数")
    private String saleTotalCount;

    @JsonProperty("sale_total_amount")
    @ApiModelProperty("销售总额")
    private String saleTotalAmount;

    @JsonProperty("price_unit")
    @ApiModelProperty("价格单位 0：分 1： 元 默认为分")
    private Integer priceUnit;

    @JsonProperty("sku_unit")
    @ApiModelProperty("sku单位 eg：包  听等等")
    private String skuUnit;

    @ApiModelProperty("创建时间")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建者")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty("修改者")
    @JsonProperty("update_by")
    private String updateBy;


}
