/*****************************************************************

* 模块名称：返回订单明细数据、订单数据、收货信息-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("根据orderID返回订单明细数据、订单主数据、收货信息、结算信息")
public class OrderodrInfo {
    
	@ApiModelProperty(value = "订单主数据")
	@JsonProperty("order_info")
	private OrderInfo orderInfo;
	
	@ApiModelProperty(value = "订单明细数据")
	@JsonProperty("detail_list")
	private List<OrderDetailInfo> detailList;
	
	@ApiModelProperty(value = "收货信息")
	@JsonProperty("receiving_info")
	private OrderReceivingInfo receivingInfo;

	@ApiModelProperty(value = "结算信息")
	@JsonProperty("settlement_info")
	private SettlementInfo settlementInfo;
	
	
	@ApiModelProperty(value = "支付信息")
	@JsonProperty("order_pay_detail")
	private List<OrderPayInfo> payList;

	@ApiModelProperty(value = "订单物流信息")
	@JsonProperty("order_list_logistics")
	private List<OrderListLogistics> orderListLogisticsList;


	@ApiModelProperty(value="前端控制退货按钮使用字段:1:订单已全数退完")
	@JsonProperty("turn_return_view")
	private Integer turnReturnView;
}



