package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class FranchiseeInfo {
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value="加盟商id")
    @JsonProperty("franchisee_id")
    private String franchiseeId;

    @ApiModelProperty(value="加盟商编码")
    @JsonProperty("franchisee_code")
    private String franchiseeCode;

    @ApiModelProperty(value="加盟商名称")
    @JsonProperty("franchisee_name")
    private String franchiseeName;

    @ApiModelProperty(value = "属性 0直营1加盟")
    @JsonProperty("property")
    private Integer property;

    @ApiModelProperty(value="员工编码(管理该加盟商的运营人员)")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value="员工名称")
    @JsonProperty("person_name")
    private String personName;

    @ApiModelProperty(value="手机号")
    @JsonProperty("mobile")
    private String mobile;

    @ApiModelProperty(value="0启用1禁用")
    @JsonProperty("user_status")
    private Integer userStatus;

    @ApiModelProperty(value="身份证号")
    @JsonProperty("card_no")
    private String cardNo;

    @ApiModelProperty(value="客户类型 0:有钱  1一般有钱 2没钱 3很贫穷")
    @JsonProperty("person_type")
    private Integer personType;

    @ApiModelProperty(value="签约日期")
    @JsonProperty("begin_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty(value="合同到期")
    @JsonProperty("finish_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date finishTime;

    @ApiModelProperty(value="公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value="公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value="状态 0.未转正 1.转正  2.审核中 3.解约 4.合同到期")
    @JsonProperty("franchisee_status")
    private Integer franchiseeStatus;

    @ApiModelProperty(value="加盟商邮寄地址")
    @JsonProperty("address")
    private String address;

    @ApiModelProperty(value="微信号")
    @JsonProperty("weChat")
    private String weChat;

    @ApiModelProperty(value="QQ")
    @JsonProperty("qq")
    private String qq;

    @ApiModelProperty(value="备注")
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value="大区编码")
    @JsonProperty("area_id")
    private String areaId;

    @ApiModelProperty(value="大区名称")
    @JsonProperty("area_name")
    private String areaName;

    @ApiModelProperty(value="省id")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value="省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value="市id")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value="区县id")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value="区县名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value="创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value="创建者")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value="修改者")
    @JsonProperty("update_by")
    private String updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFranchiseeId() {
        return franchiseeId;
    }

    public void setFranchiseeId(String franchiseeId) {
        this.franchiseeId = franchiseeId;
    }

    public String getFranchiseeCode() {
        return franchiseeCode;
    }

    public String getFranchiseeName() {
        return franchiseeName;
    }

    public void setFranchiseeName(String franchiseeName) {
        this.franchiseeName = franchiseeName;
    }

    public void setFranchiseeCode(String franchiseeCode) {
        this.franchiseeCode = franchiseeCode;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getPersonType() {
        return personType;
    }

    public void setPersonType(Integer personType) {
        this.personType = personType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getFranchiseeStatus() {
        return franchiseeStatus;
    }

    public void setFranchiseeStatus(Integer franchiseeStatus) {
        this.franchiseeStatus = franchiseeStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }
}