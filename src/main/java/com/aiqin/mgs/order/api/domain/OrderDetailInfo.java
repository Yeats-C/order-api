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

@ApiModel("订单明细")
public class OrderDetailInfo extends PagesRequest {
    
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	
	@ApiModelProperty(value="订单明细id")
	@JsonProperty("order_detail_id")
	private String orderDetailId ;
	
	
	@ApiModelProperty(value="商品id")
	@JsonProperty("product_id")
	private String productId ;
	
	
	@ApiModelProperty(value="商品编码")
	@JsonProperty("product_code")
	private String productCode ;
	
	
	@ApiModelProperty(value="商品名称")
	@JsonProperty("product_name")
	private String productName ;
	
	
	@ApiModelProperty(value="列表名称")
	@JsonProperty("list_name")
	private String listName;
	
	
	@ApiModelProperty(value="sku_code")
	@JsonProperty("sku_code")
	private String skuCode ;
	
	
	@ApiModelProperty(value="spu_code")
	@JsonProperty("spu_code")
	private String spuCode ;
	
	
	@ApiModelProperty(value="条形码")
	@JsonProperty("bar_code")
	private String barCode ;
	
	
	@ApiModelProperty(value="规格")
	@JsonProperty("spec")
	private String spec;
	
	
	@ApiModelProperty(value="单位")
	@JsonProperty("unit")
	private String unit;
	
	
	@ApiModelProperty(value="零售价格")
	@JsonProperty("retail_price")
	private String retailPrice ;
	
	
	@ApiModelProperty(value="实际价格")
	@JsonProperty("actual_price")
	private String actualPrice ;
	
	
	@ApiModelProperty(value="购买数量")
	@JsonProperty("amount")
	private String amount;
	
	
	@ApiModelProperty(value="商品状态")
	@JsonProperty("product_status")
	private String productStatus ;
	
	
	@ApiModelProperty(value="列表图")
	@JsonProperty("logo")
	private String logo;
	
	
	@ApiModelProperty(value="分类类型id")
	@JsonProperty("type_id")
	private String typeId;
	
	
	@ApiModelProperty(value="分类类型名称")
	@JsonProperty("type_name")
	private String typeName;
	
	
	@ApiModelProperty(value="是否为赠品，0-是，1-不是")
	@JsonProperty("gift_status")
	private String giftStatus;
	
	
	@ApiModelProperty(value="退货状态")
	@JsonProperty("return_status")
	private String returnStatus;
	
	
	@ApiModelProperty(value="已退货数量")
	@JsonProperty("return_amount")
	private String returnAmount;
	
	
	@ApiModelProperty(value="营销管理创建活动id")
	@JsonProperty("activity_id")
	private String activityId;
	
	
	@ApiModelProperty(value="营销管理创建活动名称")
	@JsonProperty("activity_name")
	private String activityName;
	
	
	@ApiModelProperty(value="优惠券优惠金额")
	@JsonProperty("coupon_discount")
	private String couponDiscount;
	
	
	@ApiModelProperty(value="创建时间")
	@JsonProperty("create_time")
	private String createTime;
	
	
	@ApiModelProperty(value="修改时间")
	@JsonProperty("update_time")
	private String updateTime;
	
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy;
	
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy;
	
	
    
}



