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
import java.util.List;

@Data
@ApiModel("门店会员消费报表-实体类")
public class ReportMemberSaleVo extends PagesRequest {
	
	@ApiModelProperty(value="id")
	@JsonProperty("id")
	private Integer id;


	@ApiModelProperty(value="年月")
	@JsonProperty("stat_year_month")
	private String statYearMonth;


	@ApiModelProperty(value="门店id")
	@JsonProperty("store_id")
	private String storeId;


	@ApiModelProperty(value="门店编码")
	@JsonProperty("store_code")
	private String storeCode;


	@ApiModelProperty(value="门店名称")
	@JsonProperty("store_name")
	private String storeName;


	@ApiModelProperty(value="会员消费人数")
	@JsonProperty("order_member_num")
	private Integer orderMemberNum;


	@ApiModelProperty(value="会员消费总额")
	@JsonProperty("mem_total_amount")
	private Integer memTotalAmount;


	@ApiModelProperty(value="总消费额")
	@JsonProperty("total_amount")
	private Integer totalAmount;


	@ApiModelProperty(value="会员消费金额占比")
	@JsonProperty("pct_member_consume")
	private Integer pctMemberConsume;


	@ApiModelProperty(value="会员平均消费")
	@JsonProperty("avg_member_consume")
	private Integer avgMemberConsume;


	@ApiModelProperty(value="会员平均订单数")
	@JsonProperty("avg_member_order_num")
	private Integer avgMemberOrderNum;


	@ApiModelProperty(value="会员平均订单值")
	@JsonProperty("avg_order_price")
	private Integer avgOrderPrice;
	
	@ApiModelProperty(value="门店列表")
	@JsonProperty("store_ids")
	private List<String> storeIds;
	
	public ReportMemberSaleVo(List<String> storeIds, String statYearMonth, Integer pageNo, Integer pageSize) {
		this.storeIds = storeIds;
		this.statYearMonth = statYearMonth;
	    super.setPageNo(pageNo);
	    super.setPageSize(pageSize);
	}

	public ReportMemberSaleVo() {
		
	}

}