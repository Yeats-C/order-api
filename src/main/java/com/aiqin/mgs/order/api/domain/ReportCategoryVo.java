/*****************************************************************
* 模块名称：品类报表-数据库文件 
* 开发人员: huangzy
* 开发时间: Mon Feb 24 10:04:13 CST 2020 
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
@ApiModel("品类报表-实体类")
public class ReportCategoryVo extends PagesRequest {
	
	@ApiModelProperty(value="主键")
	@JsonProperty("id")
	private Integer id;


	@ApiModelProperty(value="经营区域ID")
	@JsonProperty("copartner_area_id")
	private String copartnerAreaId;


	@ApiModelProperty(value="经营区域名称")
	@JsonProperty("copartner_area_name")
	private String copartnerAreaName;


	@ApiModelProperty(value="门店ID")
	@JsonProperty("store_id")
	private String storeId;


	@ApiModelProperty(value="门店编码")
	@JsonProperty("store_code")
	private String storeCode;


	@ApiModelProperty(value="门店名称")
	@JsonProperty("store_name")
	private String storeName;


	@ApiModelProperty(value="一级品类编码")
	@JsonProperty("category_code")
	private String categoryCode;


	@ApiModelProperty(value="一级品类名称")
	@JsonProperty("category_name")
	private String categoryName;
	
	@ApiModelProperty(value="子类编码")
	@JsonProperty("child_category_code")
	private String childCategoryCode;


	@ApiModelProperty(value="子类名称")
	@JsonProperty("child_category_name")
	private String childCategoryName;


	@ApiModelProperty(value="销售目标")
	@JsonProperty("total_target")
	private Integer totalTarget;


	@ApiModelProperty(value="销售目标达成率")
	@JsonProperty("total_rate")
	private Integer totalRate;


	@ApiModelProperty(value="销售数量")
	@JsonProperty("total_amount")
	private Integer totalAmount;


	@ApiModelProperty(value="含税销售金额")
	@JsonProperty("total_price")
	private Integer totalPrice;


	@ApiModelProperty(value="销售金额占比")
	@JsonProperty("price_rate")
	private Integer priceRate;


	@ApiModelProperty(value="上期-销售额")
	@JsonProperty("last_rate")
	private Integer lastRate;


	@ApiModelProperty(value="同比")
	@JsonProperty("same_rate")
	private Integer sameRate;


	@ApiModelProperty(value="环比")
	@JsonProperty("qoq_rate")
	private Integer qoqRate;


	@ApiModelProperty(value="报表年份")
	@JsonProperty("report_year")
	private String reportYear;


	@ApiModelProperty(value="报表月份")
	@JsonProperty("report_month")
	private String reportMonth;


	@ApiModelProperty(value="小计类型 1:门店 2:经营区域 3:月份")
	@JsonProperty("report_subtotal_type")
	private Integer reportSubtotalType;
	
	@ApiModelProperty(value="报表月份")
	@JsonProperty("calculate_month")
	private String calculateMonth;
	
	@ApiModelProperty(value="可见门店列表")
	private List<String> storeIds;
	
	@ApiModelProperty(value="可见区域列表")
	private List<String> areaIds;
	
	@ApiModelProperty(value="订单id")
	private String orderStoreId;
	

}