/*****************************************************************

* 模块名称：订单售后后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;
import java.util.List;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.component.PayOriginTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单售后后台")
public class OrderAfterSaleInfo extends PagesRequest {
  
	
	@ApiModelProperty(value="售后id")
	@JsonProperty("after_sale_id")
	private String afterSaleId ;

	@ApiModelProperty(value="售后编号")
	@JsonProperty("after_sale_code")
	private String afterSaleCode ;
	
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	private String orderCode ;
	
	@ApiModelProperty(value="会员id")
	@JsonProperty("member_id")
	private String memberId;
	
	@ApiModelProperty(value="会员名称")
	@JsonProperty("member_name")
	private String memberName;
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId;
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	private String distributorCode;
	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	private String distributorName;

	
	@ApiModelProperty(value="退款金额")
	@JsonProperty("return_price")
	private Integer returnPrice ;
	
	
	@ApiModelProperty(value="退货状态")
	@JsonProperty("after_sale_status")
	private Integer afterSaleStatus ;

	@ApiModelProperty(value="退款状态 0退款中，1已完成")
	@JsonProperty("refund_status")
	private Integer refundStatus ;

	
	@ApiModelProperty(value="售后备注信息")
	@JsonProperty("after_sale_content")
	private String afterSaleContent;
	
	
	@ApiModelProperty(value="售后类型")
	@JsonProperty("after_sale_type")
	private Integer afterSaleType ;
	
	
	@ApiModelProperty(value="处理方式")
	@JsonProperty("process_type")
	private Integer processType ;
	
	
	@ApiModelProperty(value="收货人姓名")
	@JsonProperty("receive_name")
	private String receiveName ;
	
	
	@ApiModelProperty(value="收货人手机号")
	@JsonProperty("receive_phone")
	private String receivePhone;
	
	@ApiModelProperty(value="订单支付方式")
	@JsonProperty("pay_type")
	private Integer payType=3;
	
	@ApiModelProperty(value="来源类型:2||null-全部;0&&3-门店;1-微商城;0-pos;3:web")
	@JsonProperty("origin_type")
	private Integer originType;
	
	//20190214
	@ApiModelProperty(value="订单类型 1：TOC订单 2: TOB订单 3：服务商品")
	@JsonProperty("order_type")
	private Integer orderType;


	@ApiModelProperty(value="申请开始时间",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_time")
	private Date beginTime;
	
	@ApiModelProperty(value="申请结束时间",example = "2001-01-01 01:01:01")
	@JsonProperty("end_time")
	private Date endTime;
	
	@ApiModelProperty(value="申请时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
	
	
	@ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private Date updateTime;
	
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy;
	
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy;
	
	@ApiModelProperty(value="操作员名称")
	@JsonProperty("create_by_name")
	private String createByName;
	
	
	@ApiModelProperty(value="修改员名称")
	@JsonProperty("update_by_name")
	private String updateByName;
	
	@ApiModelProperty(value="售后明细数据")
	@JsonProperty("detail_list")
	private List<OrderAfterSaleDetailInfo> detailList;
	
	
	@ApiModelProperty(value="退款方式")
	@JsonProperty("return_money_type")
	private Integer returnMoneyType;

	@ApiModelProperty("下单时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("order_time")
	private Date orderTime;

	@ApiModelProperty("加盟商id")
	@JsonProperty("franchisee_id")
	private String franchiseeId;

	@ApiModelProperty(value = "支付来源")
	@JsonProperty("pay_origin_type")
	private Integer payOriginType= PayOriginTypeEnum.TOC_POS.getCode();
}



