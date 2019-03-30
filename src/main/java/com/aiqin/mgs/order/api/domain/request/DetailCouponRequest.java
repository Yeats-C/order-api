/*****************************************************************

* 模块名称：封装参数-同步订单优惠券关系.
* 开发人员: hzy
* 开发时间: 2018-12-13

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;


@Api("封装参数-同步订单优惠券关系.")
public class DetailCouponRequest {

	@ApiModelProperty("订单明细ID")
    @JsonProperty("detail_id")
    private String detailId;
	
	@ApiModelProperty("优惠券ID")
    @JsonProperty("coupon_id")
    private String couponId;

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	
}
