package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2019/11/22 14:46
 * Description:
 */
@Data
@ApiModel("返回加盟商信息")
public class NewFranchiseeResponse {

    @ApiModelProperty(value = "加盟商编码")
    @JsonProperty("franchisee_code")
    private String franchiseeCode;

    @ApiModelProperty(value = "加盟商姓名")
    @JsonProperty("franchisee_name")
    private String franchiseeName;

    @ApiModelProperty(value = "手机号码")
    @JsonProperty("mobile")
    private String mobile;

    @ApiModelProperty(value = "身份证号")
    @JsonProperty("card_no")
    private String cardNo;

    @ApiModelProperty(value = "加盟邮寄地址：省id")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value="省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "加盟邮寄地址：市id")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "加盟邮寄地址：区县id")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value="区县名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "微信")
    @JsonProperty("weChat")
    private String weChat;

    @ApiModelProperty(value = "QQ")
    @JsonProperty("qq")
    private String qq;

    @ApiModelProperty(value = "客户类型 0:有钱  1一般有钱 2没钱 3很贫穷")
    @JsonProperty("person_type")
    private Integer personType;

    @ApiModelProperty(value = "公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "详细地址")
    @JsonProperty("address")
    private String address;

    @ApiModelProperty(value = "加盟商ID")
    @JsonProperty("franchisee_id")
    private String franchiseeId;

    @ApiModelProperty(value = "属性 0:直营 1：加盟")
    @JsonProperty("property")
    private Integer property;


    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private Integer createBy;

}
