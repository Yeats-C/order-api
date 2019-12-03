package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StoreBackInfoResponse {

    @ApiModelProperty("门店编码")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty("门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty("公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty("门店类型编码")
    @JsonProperty("nature_code")
    private String natureCode;

    @ApiModelProperty("门店类型名称")
    @JsonProperty("nature_name")
    private String natureName;

    @ApiModelProperty("门店类别名称")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty("门店类别编码")
    @JsonProperty("category_code")
    private String categoryCode;

    @ApiModelProperty("城市级别")
    @JsonProperty("city_level")
    private String cityLevel;

    @ApiModelProperty("城市级别名称")
    @JsonProperty("city_level_name")
    private String cityLevelName;

    @ApiModelProperty("门店级别编码")
    @JsonProperty("sales_level_code")
    private String salesLevelCode;

    @ApiModelProperty("门店级别名称")
    @JsonProperty("sales_level_name")
    private String salesLevelName;

    @ApiModelProperty("商圈类别code")
    @JsonProperty("business_category_code")
    private String businessCategoryCode;

    @ApiModelProperty("商圈类别名称")
    @JsonProperty("business_category_name")
    private String businessCategoryName;

    @ApiModelProperty("合同面积")
    @JsonProperty("contract_area")
    private String contractArea;

    @ApiModelProperty("零售面积")
    @JsonProperty("retail_area")
    private String retailArea;

    @ApiModelProperty("仓储面积")
    @JsonProperty("stock_area")
    private String stockArea;

    @ApiModelProperty("开店日期")
    @JsonProperty("open_date")
    private String openDate;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("门店联系人")
    private String contacts;

    @ApiModelProperty("联系人电话")
//    @JsonProperty("contacts_phone")
    private String contactsPhone;

    @ApiModelProperty("经营开始时间")
    @JsonProperty("manage_begin_time")
    private String manageBeginTime;

    @ApiModelProperty("经营结束时间")
    @JsonProperty("manage_finish_time")
    private String manageFinishTime;

    @ApiModelProperty("大区编码")
    @JsonProperty("area_id")
    private String areaId;

    @ApiModelProperty("大区名称")
    @JsonProperty("area_name")
    private String areaName;

    @ApiModelProperty("门店地址")
    private String address;

    @ApiModelProperty("门店城市编码")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty("门店城市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty("门店省份编码")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty("门店省份名称")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty("门店状态")
    @JsonProperty("user_status")
    private Integer userStatus;

    @ApiModelProperty("经营状态")
    @JsonProperty("manage_status")
    private Integer manageStatus;

    @ApiModelProperty("经营时间状态  0.全天  1.自定义")
    @JsonProperty("all_day")
    private Integer allDay;

    @ApiModelProperty("开店月龄")
    @JsonProperty("month_count")
    private Integer monthCount;

    @ApiModelProperty("门店区县编码")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty("门店区县名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty("门店是否已被绑定 0.是 1.否")
    @JsonProperty(value = "status")
    private Integer status;

}
