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
@ApiModel("经营区域权限详情")
@Data
public class CopartnerAreaRoleDetail{
	
	@ApiModelProperty(value = "本公司管理权限")
    @JsonProperty("role_salf_list")
    private List<CopartnerAreaRoleDict> roleSalfList;
	
	@ApiModelProperty(value = "下级公司管理权限")
    @JsonProperty("role_down_list")
    private List<CopartnerAreaRoleDict> roleDownList;
}
