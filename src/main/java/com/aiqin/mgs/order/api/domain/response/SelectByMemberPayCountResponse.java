/*****************************************************************

* 模块名称：封装-会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日周月.
* 开发人员: 黄祉壹
* 开发时间: 2018-01-21 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日周月.")
public class SelectByMemberPayCountResponse{
    
	@ApiModelProperty("会员数量")
    @JsonProperty("count_member")
    private Integer countMember;

    @ApiModelProperty("统计日期")
    @JsonProperty("count_date")
    private String countDate;

	public Integer getCountMember() {
		return countMember;
	}

	public void setCountMember(Integer countMember) {
		this.countMember = countMember;
	}

	public String getCountDate() {
		return countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
    
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}
    
	
}



