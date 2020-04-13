/*****************************************************************

* 模块名称：订单售后明细-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 
* 
* 2019-01-10 退货原因编码、名称数据源变更为运营中心统一管理

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单售后明细")
public class OrderAfterSaleDetailInfo extends PagesRequest {
    
	
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
	
	@ApiModelProperty(value="条形码")
	@JsonProperty("bar_code")
	private String barCode;
	
	@ApiModelProperty(value="单价")
	@JsonProperty("price")
	private Integer price;
	
	@ApiModelProperty(value="退货数量")
	@JsonProperty("return_amount")
	private Integer returnAmount;
	
	@ApiModelProperty(value="退货金额")
	@JsonProperty("return_price")
	private Integer returnPrice;
	

	
	@ApiModelProperty("退货原因名称")
	@JsonProperty("return_reason_name")
	private String returnReasonName;

	@ApiModelProperty("退货原因编码")
	@JsonProperty("return_reason_code")
    private String returnReasonCode;
	
	@ApiModelProperty(value="退款原因详情")
	@JsonProperty("return_reason_content")
	private String returnReasonContent;
	
	@ApiModelProperty(value="商品类别id")
	@JsonProperty("type_id")
	private Integer typeId ;
	
	@ApiModelProperty(value="商品类别名称")
	@JsonProperty("type_name")
	private String typeName ;
	
	@ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime ;
	
	@ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private Date updateTime ;
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy ;
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy ;
	
	@ApiModelProperty(value="操作员名称")
	@JsonProperty("create_by_name")
	private String createByName;
	
	
	@ApiModelProperty(value="修改员名称")
	@JsonProperty("update_by_name")
	private String updateByName;
	
	
	@ApiModelProperty(value="非入库字段-零售价格")
	@JsonProperty("retail_price")
    private Integer retailPrice;
	
    @ApiModelProperty("非入库字段-实际")
    @JsonProperty("actual_price")
    private Integer actualPrice;
    
    
    @ApiModelProperty("非入库字段-规格")
    @JsonProperty("spec")
    private String spec;
    
    @ApiModelProperty("非入库字段-单位")
    @JsonProperty("unit")
    private String unit;
    
    @ApiModelProperty("非入库字段-列表图")
    @JsonProperty("logo")
    private String logo;

	@ApiModelProperty(value="营销管理创建活动id")
	@JsonProperty("activity_id")
	private String activityId;


	@JsonProperty("package_id")
	@ApiModelProperty("套餐包id")
	private String packageId;
    
}



