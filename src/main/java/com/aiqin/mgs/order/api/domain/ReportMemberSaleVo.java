/*****************************************************************
* 模块名称：门店会员销售报表-数据库文件 
* 开发人员: huangzy
* 开发时间: Fri Feb 21 09:26:42 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("门店会员销售报表-实体类")
public class ReportMemberSaleVo extends PagesRequest {
	
	@ApiModelProperty(value="报表年份")
	@JsonProperty("report_year")
	private String reportYear;
	
	@ApiModelProperty(value="报表月份")
	@JsonProperty("report_month")
	private String reportMonth;

}