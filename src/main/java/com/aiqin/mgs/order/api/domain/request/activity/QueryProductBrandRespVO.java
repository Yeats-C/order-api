package com.aiqin.mgs.order.api.domain.request.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 商品品牌类型列表返回vo
 * @author zth
 * @date 2018/12/13
 */
@ApiModel("商品品牌类型列表返回vo")
@Data
public class QueryProductBrandRespVO {
    @ApiModelProperty(value = "自增主键")
    private Long id;

    @ApiModelProperty(value = "品牌类型名称")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value = "状态，0为启用，1为禁用")
    @JsonProperty("brand_status")
    private Integer brandStatus;

    @ApiModelProperty(value = "品牌类型id")
    @JsonProperty("brand_id")
    private String brandId;

    @ApiModelProperty(value = "'品牌logo(图片url地址)")
    @JsonProperty("brand_logo")
    private String brandLogo;

    @ApiModelProperty(value = "品牌文件名称")
    @JsonProperty("brand_logo_name")
    private String brandLogoName;

    @ApiModelProperty(value = "品牌首字母")
    @JsonProperty("brand_initials")
    private String brandInitials;

//    @ApiModelProperty(value = "品牌介绍")
//    private String brandIntroduction;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryProductBrandRespVO that = (QueryProductBrandRespVO) o;
        return Objects.equals(brandId, that.brandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId);
    }
}