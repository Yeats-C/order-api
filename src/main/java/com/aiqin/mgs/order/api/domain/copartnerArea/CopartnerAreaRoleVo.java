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
@ApiModel("经营区域权限")
@Data
public class CopartnerAreaRoleVo extends PagesRequest{
	
	@ApiModelProperty(value = "经营区域ID")
    @JsonProperty("copartner_area_id")
    private String copartnerAreaId;
    
	@ApiModelProperty(value = "工号")
    @JsonProperty("person_id")
    private String personId;
	
	@ApiModelProperty(value = "姓名")
    @JsonProperty("person_name")
    private String personName;
	
	@ApiModelProperty(value = "权限编码")
    @JsonProperty("role_code")
    private String roleCode;
	
	@ApiModelProperty(value = "权限名称")
    @JsonProperty("role_name")
    private String roleName;
	
	@ApiModelProperty(value = "组织权限 1：本公司管理权限,2：下级公司管理权限")
    @JsonProperty("role_type")
    private Integer roleType;
	
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
