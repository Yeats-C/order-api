package com.aiqin.mgs.order.api.domain.request.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @功能说明:品类返回VO
 * @author wangxu
 * @date 2018/12/12 0012 17:15
 */
@ApiModel("品类列表返回属性")
@Data
public class ProductCategoryRespVO {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "品类名称")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty("品类code")
    @JsonProperty("category_code")
    private String categoryId;

    @ApiModelProperty(value = "状态，0为启用，1为禁用")
    @JsonProperty("category_status")
    private Byte categoryStatus;

    @ApiModelProperty(value = "父级品类code")
    @JsonProperty("parent_code")
    private String parentId;

    @ApiModelProperty(value = "级别，1、2、3、4级")
    @JsonProperty("category_level")
    private Integer categoryLevel;

    @ApiModelProperty(value = "图片路径")
    @JsonProperty("picture_path")
    private String picturePath;

    @ApiModelProperty(value = "排序")
    private Integer weight;

    @ApiModelProperty(value = "图片名称")
    @JsonProperty("picture_name")
    private String pictureName;

    @ApiModelProperty(value = "子节点集合")
    @JsonProperty("product_category_resp")
    private List<ProductCategoryRespVO> productCategoryRespVOS;

}
