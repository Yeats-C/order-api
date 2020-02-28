/*****************************************************************
* 模块名称：门店会员报表-数据库文件 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("门店会员情况报表-实体类")
public class ReportMemberVo extends PagesRequest {
	
	@ApiModelProperty(value="id")
	@JsonProperty("id")
	private Integer id;

	@ApiModelProperty(value="年")
	@JsonProperty("stat_year")
	private Integer statYear;


	@ApiModelProperty(value="月")
	@JsonProperty("stat_month")
	private Integer statMonth;


	@ApiModelProperty(value="门店id")
	@JsonProperty("store_id")
	private String storeId;
	
	@ApiModelProperty(value="门店编码")
	@JsonProperty("store_code")
	private String storeCode;


	@ApiModelProperty(value="门店名称")
	@JsonProperty("store_name")
	private String storeName;


	@ApiModelProperty(value="总会员数")
	@JsonProperty("total_member_num")
	private Integer totalMemberNum;


	@ApiModelProperty(value="新增会员数")
	@JsonProperty("new_member_num")
	private Integer newMemberNum;


	@ApiModelProperty(value="复购会员数")
	@JsonProperty("reactive_member")
	private Integer reactiveMember;


	@ApiModelProperty(value="沉默会员数")
	@JsonProperty("sale_low_num")
	private Integer saleLowNum;


	@ApiModelProperty(value="用户价值")
	@JsonProperty("user_value")
	private Integer userValue;
	
	@ApiModelProperty(value="上个月用户价值")
	@JsonProperty("last_month_user_value")
	private Integer lastMonthUserValue;
	
	@ApiModelProperty(value="环比")
	@JsonProperty("chain_growth")
	private Integer chainGrowth;
	
	@ApiModelProperty(value="统计时间")
	@JsonProperty("create_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@ApiModelProperty(value="门店列表")
	@JsonProperty("store_ids")
	private List<String> storeIds;


	public ReportMemberVo(List<String> storeIds, Integer statYear, Integer statMonth, Integer pageNo,
			Integer pageSize) {
		this.storeIds = storeIds;
		this.statYear = statYear;
		this.statMonth = statMonth;
		super.setPageNo(pageNo);
	    super.setPageSize(pageSize);
	}

	public ReportMemberVo(List<String> storeIds, Integer statYear, Integer pageNo, Integer pageSize) {
		this.storeIds = storeIds;
		this.statYear = statYear;
	    super.setPageNo(pageNo);
	    super.setPageSize(pageSize);
	}

	public ReportMemberVo() {
		
	}

}