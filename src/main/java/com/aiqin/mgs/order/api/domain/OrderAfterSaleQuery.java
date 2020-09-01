/*****************************************************************

* 模块名称：订单售后-查询条件
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

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
@ApiModel("订单售后查询条件")
public class OrderAfterSaleQuery extends PagesRequest {
    
	@ApiModelProperty(value="不传参不返回 0:全部")
	@JsonProperty("icount")
	private Integer icount = 0;

	
	@ApiModelProperty(value="售后编号")
	@JsonProperty("after_sale_code")
	private String afterSaleCode ;
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone ;
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	private String orderCode ;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId;


	@JsonProperty("list_distributor_id")
	@ApiModelProperty(value = "erp专用，门店列表，查全部传空")
	private List<String> listDistributorId;

	@ApiModelProperty(value="分销机构name")
	@JsonProperty("distributor_name")
	private String distributorName;
	
	@ApiModelProperty(value="售后id")
	@JsonProperty("after_sale_id")
	private String afterSaleId ;

	
	@ApiModelProperty(value="来源类型:2||null-全部;0&&3-门店;1-微商城;0-pos;3:web")
	@JsonProperty("origin_type_list")
	private List<Integer> originTypeList;
	
	@ApiModelProperty(value="订单支付方式")
	@JsonProperty("pay_type")
	private Integer payType;
	
	@ApiModelProperty(value="退货状态")
	@JsonProperty("after_sale_status")
	private Integer afterSaleStatus;
	
	
	@ApiModelProperty(value="开始时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_time")
	private Date beginTime;
	
	@ApiModelProperty(value="结束时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("end_time")
	private Date endTime;
	
	@ApiModelProperty(value="申请时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
	
	
	@ApiModelProperty(value="开始时间类型:String,格式:yyyy-mm-dd")
	@JsonProperty("begin_date")
	private String beginDate ;
	
	@ApiModelProperty(value="结束时间类型:String,格式:yyyy-mm-dd")
	@JsonProperty("end_date")
	private String endDate ;
	
	@ApiModelProperty(value="订单号/手机号")
	@JsonProperty("code_and_phone")
	private String codeAndPhone;

	@ApiModelProperty(value="退款方式(0.现金 1.储值卡)")
	@JsonProperty("return_money_type")
	private Integer returnMoneyType;

	@JsonProperty("update_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("更新时间")
	public Date updateTime;
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	private String distributorCode="";
	/**
	 * 渠道来源
	 */
	@JsonProperty("origin_type")
	@ApiModelProperty("来源类型，0:pos 1：微商城  2：全部  3：web")
	private Integer originType;

	@ApiModelProperty(value="条形码/sku码")
	@JsonProperty("bar_code_and_sku")
	private String barCodeAndSku;

	@ApiModelProperty(value="商品名称")
	@JsonProperty("sku_name")
	private String skuName;

}



