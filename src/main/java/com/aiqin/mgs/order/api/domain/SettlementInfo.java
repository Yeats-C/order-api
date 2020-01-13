/*****************************************************************

* 模块名称：结算后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("结算")
public class SettlementInfo extends PagesRequest {
    
	
	@ApiModelProperty(value = "结算id")
    @JsonProperty("settlement_id")
    private String settlementId;
	
	@ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    @NotBlank
    private String orderId;
    

    
    @ApiModelProperty(value = "商品合计")
    @JsonProperty("product_sum")
    private Integer productSum;
    
    
    @ApiModelProperty(value = "运费")
    @JsonProperty("freight")
    private Integer freight;
    
    @ApiModelProperty(value = "优惠券总优惠金额")
    @JsonProperty("total_coupons_discount")
    private Integer totalCouponsDiscount=0;
    
    
    @ApiModelProperty(value = "积分折扣")
    @JsonProperty("point_percentage")
    private Integer pointPercentage=0;
    
    
    @ApiModelProperty(value = "活动优惠")
    @JsonProperty("activity_discount")
    private Integer activityDiscount;
    
    
    @ApiModelProperty(value = "订单合计")
    @JsonProperty("order_sum")
    private Integer orderSum;
    
    
    @ApiModelProperty(value = "订单应收")
    @JsonProperty("order_receivable")
    private Integer orderReceivable;
    
    
    @ApiModelProperty(value = "订单实收")
    @JsonProperty("order_actual")
    private Integer orderActual;
    
    
    @ApiModelProperty(value = "订单找零")
    @JsonProperty("order_change")
    private Integer orderChange;
    
    
    @ApiModelProperty(value = "订单舍零")
    @JsonProperty("order_abandon")
    private Integer orderAbandon;
    
    
    @ApiModelProperty(value = "使用积分数")
    @JsonProperty("points")
    private Integer points;
    
    
    @ApiModelProperty(value = "积分抵扣金额")
    @JsonProperty("point_discount")
    private Integer pointDiscount=0;
    
    
    @ApiModelProperty(value = "订单创建时间")
    @JsonProperty("create_time")
    private String createTime;
    
    
    @ApiModelProperty(value = "订单操作员")
    @JsonProperty("create_by")
    private String createBy;
    
    @ApiModelProperty(value = "订单修改时间")
    @JsonProperty("update_time")
    private String updateTime;
    
    
    @ApiModelProperty(value = "订单修改员")
    @JsonProperty("update_by")
    private String updateBy;
    

}



