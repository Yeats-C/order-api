/*****************************************************************

* 模块名称：封装参数-销售目标管理-分销机构-月销售额.
* 开发人员: hzy
* 开发时间: 2019-02-13

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;


@Api("封装参数-判断会员是否在当前门店时候有过消费记录.")
public class MemberByDistributorRequest {

	@ApiModelProperty("会员ID集合")
    @JsonProperty("member_list")
	@NotNull
    private List<String> memberList;
	
	@ApiModelProperty("门店ID")
    @JsonProperty("distributor_id")
	@NotBlank
    private String distributorId;

	public List<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<String> memberList) {
		this.memberList = memberList;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	
	
}
