package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * @author csf
 */
@Data
@ApiModel("活动适于门店情况")
public class ActivityStore {

    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "市id")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value = "市")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "区id")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value = "区")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "省份id")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value = "省份")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    @JsonProperty("is_delete")
    private Integer isDelete;


}