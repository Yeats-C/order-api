/*****************************************************************

* 模块名称：订单支付-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单操作日志")
public class OrderAfterSaleDetail extends PagesRequest {
    
	
	@ApiModelProperty(value="退货明细id")
	@JsonProperty("after_sale_detail_id")
	private String afterSaleDetailId;
	@ApiModelProperty(value="退货id")
	@JsonProperty("after_sale_id")
	private String afterSaleId;
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	private String orderCode;
	@ApiModelProperty(value="订单明细id")
	@JsonProperty("order_detail_id")
	private String orderDetailId;
	@ApiModelProperty(value="商品名称")
	@JsonProperty("product_name")
	private String productName;
	@ApiModelProperty(value="sku码")
	@JsonProperty("sku_code")
	private String skuCode;
	@ApiModelProperty(value="spu码")
	@JsonProperty("spu_code")
	private String spuCode;
	@ApiModelProperty(value="单价")
	@JsonProperty("price")
	private String price;
	@ApiModelProperty(value="退货数量")
	@JsonProperty("return_amount")
	private String returnAmount;
	@ApiModelProperty(value="退货金额")
	@JsonProperty("return_price")
	private String returnPrice;
	@ApiModelProperty(value="退款原因类型")
	@JsonProperty("return_reason_type")
	private String returnReasonType ;
	@ApiModelProperty(value="退款原因详情")
	@JsonProperty("return_reason_content")
	private String returnReasonContent;
	@ApiModelProperty(value="商品类别id")
	@JsonProperty("type_id")
	private String typeId ;
	@ApiModelProperty(value="商品类别名称")
	@JsonProperty("type_name")
	private String typeName ;
	@ApiModelProperty(value="创建时间")
	@JsonProperty("create_time")
	private String createTime ;
	@ApiModelProperty(value="修改时间")
	@JsonProperty("update_time")
	private String updateTime ;
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy ;
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy ;
	
    
}



