package com.aiqin.mgs.order.api.domain.copartnerArea;

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
@ApiModel("经营区域权限字典")
@Data
public class CopartnerAreaRoleDict{
	
	@ApiModelProperty(value = "权限编码")
    @JsonProperty("role_code")
    private String roleCode;
	
	@ApiModelProperty(value = "权限名称")
    @JsonProperty("role_name")
    private String roleName;
	
	@ApiModelProperty(value = "勾选标识: 1:已勾选")
    @JsonProperty("check_flag")
    private Integer checkFlag;
}
