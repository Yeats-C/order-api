package com.aiqin.mgs.order.api.domain.logisticsRule;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "减免类型信息")
public class NewReduceInfo {

    @ApiModelProperty(value = "物流减免规则唯一编码---不用传")
    @JsonProperty("rult_code")
    private String rultCode;

    @ApiModelProperty(value = "品牌编码 ---范围是全部，传“All”")
    @JsonProperty("brand")
    private String brand;

    @ApiModelProperty(value = "品牌名称 ---范围是全部，传“全部”")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value = "品类编码 ---范围是全部，传“All”" )
    @JsonProperty("category")
    private String category;

    @ApiModelProperty(value = "品类名称 ---范围是全部，传“全部”")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty(value = "商品属性")
    @JsonProperty("product_type")
    private String productType;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "修改人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "生效状态 0未开 1开启  ---不用传")
    @JsonProperty("effective_status")
    private Integer effectiveStatus;

    @ApiModelProperty(value = "减免范围 1全部  2部分 ---不用传")
    @JsonProperty("reduce_scope")
    private Integer reduceScope;

    @ApiModelProperty(value = "spu编码 ---不用传")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "spu名称 ---不用传")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty(value = "物流减免商品唯一id---不用传")
    @JsonProperty("rult_id")
    private String rultId;

    @ApiModelProperty(value = "是否删除 1是 2否 --不用传")
    @JsonProperty("is_delete")
    private Integer isDelete;


}
