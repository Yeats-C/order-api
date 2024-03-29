package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author huangzy
 * @create 2020-02-13
 */
@ApiModel("经营区域信息")
@Data
public class CopartnerAreaVo extends PagesRequest{
	
	private String id;
	
	@ApiModelProperty(value = "经营区域ID")
    @JsonProperty("copartner_area_id")
    private String copartnerAreaId;
    
	@ApiModelProperty(value = "经营区域名称")
    @JsonProperty("copartner_area_name")
    private String copartnerAreaName;
	
	@ApiModelProperty(value = "管理层级 1:一级 2：二级")
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
	
	@ApiModelProperty(value = "上级经营区域ID")
    @JsonProperty("copartner_area_id_up")
    private String copartnerAreaIdUp;

	@ApiModelProperty(value = "上级经营区域名称")
    @JsonProperty("copartner_area_name_up")
    private String copartnerAreaNameUp;
	
	@ApiModelProperty(value = "备注")
    @JsonProperty("memo")
    private String memo;
	
	@ApiModelProperty("创建人编码")
    @JsonProperty("create_by")
    private String createBy;
	
	@ApiModelProperty("修改人编码")
	@JsonProperty("update_by")
	private String updateBy;
		
    @ApiModelProperty("创建日期")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("create_time")
	private Date createTime;
	  
	@ApiModelProperty("修改日期")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("update_time")
	private Date updateTime;
	
}
