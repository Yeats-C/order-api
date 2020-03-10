package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退货商品分类统计")
public class ReportCategoryGoods {

    @ApiModelProperty(value="id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value="一级品类编码")
    @JsonProperty("category_code")
    private String categoryCode;

    @ApiModelProperty(value="一级品类名称")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty(value="金额")
    @JsonProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty(value="比例")
    @JsonProperty("proportion")
    private BigDecimal proportion;

    @ApiModelProperty(value="创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty("类型 1:直送退货 2:质量退货 3:一般退货")
    @JsonProperty("type")
    private Integer type;

}