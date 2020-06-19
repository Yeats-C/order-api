package com.aiqin.mgs.order.api.domain.wholesale;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新建加盟商参数")
public class JoinMerchant {

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

    @ApiModelProperty(value = "身份证号")
    @JsonProperty("card_type")
    private String cardType;

    @ApiModelProperty(value = "公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "详细地址")
    @JsonProperty("address")
    private String address;

    @ApiModelProperty(value = "属性 0:直营 1:加盟")
    @JsonProperty("property")
    private Integer property;

    @ApiModelProperty(value="创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value="")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value="岗位id")
    @JsonProperty("position_code")
    private String positionCode;

    @ApiModelProperty(value="岗位名称")
    @JsonProperty("position_name")
    private String positionName;

    /**
     * 部门编码
     */
    @ApiModelProperty("部门编码")
    @JsonProperty(value = "department_code")
    private String departmentCode;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @JsonProperty(value = "department_name")
    private String departmentName;

    @ApiModelProperty("部门级别")
    @JsonProperty(value = "department_level")
    private Integer departmentLevel;

    @ApiModelProperty("加盟商角色")
    @JsonProperty(value = "role_id")
    private String[] roleId;

    @ApiModelProperty("账号")
    @JsonProperty(value = "user_name")
    private String userName;
}
