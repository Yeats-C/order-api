package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 门店信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 15:57
 */
@Data
@ApiModel("门店信息实体类")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class StoreInfo {

    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value="门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value="门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value="门店联系人")
    @JsonProperty("contacts")
    private String contacts;

    @ApiModelProperty(value="门店联系电话")
    @JsonProperty("contacts_phone")
    private String contactsPhone;

    @ApiModelProperty(value="门店类型")
    @JsonProperty("nature_code")
    private String natureCode;

    @ApiModelProperty(value="门店类型名称")
    @JsonProperty("nature_name")
    private String natureName;

    @ApiModelProperty(value="公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value="公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value="加盟商id")
    @JsonProperty("franchisee_id")
    private String franchiseeId;

    @ApiModelProperty(value="加盟商编码")
    @JsonProperty("franchisee_code")
    private String franchiseeCode;

    @ApiModelProperty(value="加盟商名称")
    @JsonProperty("franchisee_name")
    private String franchiseeName;

    @ApiModelProperty(value="大区")
    @JsonProperty("area_id")
    private String areaId;

    @ApiModelProperty(value="大区名称")
    @JsonProperty("area_name")
    private String areaName;

    @ApiModelProperty(value="城市(直辖市区)")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value="省份(直辖市)")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value="省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value="区(县级市)")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value="区(县级市)名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value="地址")
    @JsonProperty("address")
    private String address;

    @ApiModelProperty("首单赠送市值金额")
    @JsonProperty(value = "market_value")
    private Double marketValue;

    @ApiModelProperty("首单赠送费用")
    @JsonProperty(value = "free_cost")
    private Double freeCost;

    @ApiModelProperty("首单赠送市值余额")
    @JsonProperty(value = "market_value_balance")
    private Double marketValueBalance;

    @ApiModelProperty("首单赠送费用余额")
    @JsonProperty(value = "free_cost_balance")
    private Double freeCostBalance;

}
