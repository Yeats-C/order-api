package com.aiqin.mgs.order.api.domain.wholesale;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 批发客户信息bean
 */
@Data
@ApiModel("批发客户信息bean")
public class WholesaleCustomers extends PagesRequest implements Serializable {

    @ApiModelProperty(value = "批发客户编码（唯一标识）")
    @JsonProperty("customer_code")
    private String customerCode;

    @ApiModelProperty(value = "批发客户名称")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "联系人")
    @JsonProperty("contact_person")
    private String contactPerson;

    @ApiModelProperty(value = "手机号码")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @ApiModelProperty(value = "身份证号")
    @JsonProperty("identity_number")
    private String identityNumber;

    @ApiModelProperty(value = "省编码")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value = "省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "市编码")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value = "市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "区县编码")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value = "区县名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("street_address")
    private String streetAddress;

    @ApiModelProperty(value = "所属公司")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "状态，0. 启用   1.禁用")
    @JsonProperty("use_status")
    private Integer useStatus;

    @ApiModelProperty(value = "所属合伙公司")
    @JsonProperty("affiliated_company")
    private String affiliatedCompany;

    @ApiModelProperty(value = "拓客人员编码")
    @JsonProperty("invite_people_code")
    private String invitePeopleCode;

    @ApiModelProperty(value = "拓客人员名称")
    @JsonProperty("invite_people_name")
    private String invitePeopleName;

    @ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;


    @ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;


    @ApiModelProperty(value="创建人")
    @JsonProperty("create_by")
    private String createBy;


    @ApiModelProperty(value="修改人")
    @JsonProperty("update_by")
    private String updateBy;

    @ApiModelProperty(value="仓库List")
    @JsonProperty("warehouse_list")
    private List<WholesaleRule> warehouseList;

    @ApiModelProperty(value="品牌List")
    @JsonProperty("brand_list")
    private List<WholesaleRule> brandList;

    @ApiModelProperty(value="品类List")
    @JsonProperty("category_list")
    private List<WholesaleRule> categoryList;

    @ApiModelProperty(value="单品List")
    @JsonProperty("product_list")
    private List<WholesaleRule> productList;
}
