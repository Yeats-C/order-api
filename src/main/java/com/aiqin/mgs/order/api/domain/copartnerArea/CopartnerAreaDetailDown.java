package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@ApiModel("下辖公司")
@Data
public class CopartnerAreaDetailDown {

    @ApiModelProperty(value = "经营区域ID-新增保存自动生成&修改保存必传")
    @JsonProperty("copartner_area_id")
    private String copartnerAreaId;

    @ApiModelProperty(value = "经营区域名称")
    @JsonProperty("copartner_area_name")
    private String copartnerAreaName;

    @ApiModelProperty(value = "管理层级 0:总部 1:一级 2：二级")
    @JsonProperty("copartner_area_level")
    private Integer copartnerAreaLevel;

    @ApiModelProperty(value = "合伙人公司")
    @JsonProperty("copartner_area_company")
    private String copartnerAreaCompany;

    @ApiModelProperty(value = "公司负责人ID")
    @JsonProperty("company_person_id")
    private String companyPersonId;

    @ApiModelProperty(value = "公司负责人名称")
    @JsonProperty("company_person_name")
    private String companyPersonName;

    @ApiModelProperty(value = "上级合伙人ID")
    @JsonProperty("copartner_area_id_up")
    private String copartnerAreaIdUp;

    @ApiModelProperty(value = "上级合伙人名称")
    @JsonProperty("copartner_area_name_up")
    private String copartnerAreaNameUp;

    @ApiModelProperty("创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty("修改日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty("创建人编码")
    @JsonProperty("create_by")
    private String createBy;


}
