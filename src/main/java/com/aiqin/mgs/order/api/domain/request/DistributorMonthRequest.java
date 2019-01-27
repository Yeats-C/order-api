/*****************************************************************

* 模块名称：封装参数-销售目标管理-分销机构-月销售额.
* 开发人员: hzy
* 开发时间: 2018-12-13

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;


@Api("封装参数-销售目标管理-分销机构-月销售额.")
public class DistributorMonthRequest {

	@ApiModelProperty("门店CODE")
    @JsonProperty("distributor_code_list")
	@NotNull
    private List<String> distributorCodeList;
	
	@ApiModelProperty("开始日期")
    @JsonProperty("begin_time")
	@NotBlank
    private String beginTime;
	
	@ApiModelProperty("结束日期")
    @JsonProperty("end_time")
	@NotBlank
    private String endTime;

	public List<String> getDistributorCodeList() {
		return distributorCodeList;
	}

	public void setDistributorCodeList(List<String> distributorCodeList) {
		this.distributorCodeList = distributorCodeList;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
	
	
}
