/*****************************************************************

* 模块名称：封装参数-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
* 开发人员: hzy
* 开发时间: 2018-12-12 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;


@Api("封装参数-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.")
public class DevelRequest {

	@ApiModelProperty("分销机构")
    @JsonProperty("distributor_id")
    private String distributorId;
	
	@ApiModelProperty("数量")
    @JsonProperty("member_list")
    private List<String> memberList;
	
	@ApiModelProperty("日期")
    @JsonProperty("trante")
    private String trante;
	
	@ApiModelProperty("数量")
    @JsonProperty("acount")
    private Integer acount;

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public List<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<String> memberList) {
		this.memberList = memberList;
	}

	public String getTrante() {
		return trante;
	}

	public void setTrante(String trante) {
		this.trante = trante;
	}

	public Integer getAcount() {
		return acount;
	}

	public void setAcount(Integer acount) {
		this.acount = acount;
	}
	
	
}
