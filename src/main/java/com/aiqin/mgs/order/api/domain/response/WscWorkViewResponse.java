/*****************************************************************

* 模块名称：封装-微商城事务总览
* 开发人员: 黄祉壹
* 开发时间: 2019-01-03 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-微商城事务总览")
public class WscWorkViewResponse{
    
		@ApiModelProperty("待付款订单")
	    @JsonProperty("un_paid")
	    private Integer unPaid;	
		
		@ApiModelProperty("待提货订单")
		@JsonProperty("not_pick")
		private Integer notPick;
		
		@ApiModelProperty("待发货订单")
		@JsonProperty("not_delivery")
		private Integer notDelivery;
			
		@ApiModelProperty("待确认售后单")
		@JsonProperty("not_sure")
		private Integer notSure;
		
		@ApiModelProperty("待处理退款申请")
		@JsonProperty("not_refund")
		private Integer notRefund;

		public Integer getUnPaid() {
			return unPaid;
		}

		public void setUnPaid(Integer unPaid) {
			this.unPaid = unPaid;
		}

		public Integer getNotPick() {
			return notPick;
		}

		public void setNotPick(Integer notPick) {
			this.notPick = notPick;
		}

		public Integer getNotDelivery() {
			return notDelivery;
		}

		public void setNotDelivery(Integer notDelivery) {
			this.notDelivery = notDelivery;
		}

		public Integer getNotSure() {
			return notSure;
		}

		public void setNotSure(Integer notSure) {
			this.notSure = notSure;
		}

		public Integer getNotRefund() {
			return notRefund;
		}

		public void setNotRefund(Integer notRefund) {
			this.notRefund = notRefund;
		}
		
	
}



