/*****************************************************************

* 模块名称：订单明细-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel("订单明细查询条件")
public class OrderDetailQuery extends PagesRequest {

	@ApiModelProperty(value = "门店id")
	@JsonProperty("store_id")
	private String storeId;
	
	@ApiModelProperty(value="不传参不返回 0:全部")
	@JsonProperty("icount")
	private Integer icount = 0;
	
	@ApiModelProperty(value="订单code")
	@JsonProperty("order_code")
	private String orderCode ;
	
	//订单ID查询明细
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	@ApiModelProperty(value="查询条件:订单状态集合")
	@JsonProperty("order_id_list")
	private List<String> orderIdList;
	
	@ApiModelProperty(value="会员名称")
	@JsonProperty("member_name")
	private String memberName ;
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone ;
	
	//接口-优惠活动查询订单明细
	@ApiModelProperty(value="营销管理创建活动id")
	@JsonProperty("activity_id")
	private String activityId ;
	
	@ApiModelProperty(value="营销管理创建活动idList")
	@JsonProperty("activity_id_list")
	private List<String> activityIdList ;	
	
	//接口-会员管理-会员消费记录
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId="";
	
	@ApiModelProperty(value="起始时间,date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date beginDate;
	
	@ApiModelProperty(value="结束时间,date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("end_date")
	private Date endDate;
	
	@ApiModelProperty(value="会员IDlist")
	@JsonProperty("memberid_list")
	private List<String>  memberidList;
	
	@ApiModelProperty(value="订单状态")
	@JsonProperty("order_status")
	private Integer orderStatus;
	
	
	@ApiModelProperty(value="sku集合")
	@JsonProperty("suk_list")
	private List<String>  sukList;

	@ApiModelProperty(value="来源类型:2||null-全部;0&&3-门店;1-微商城;0-pos;3:web")
	@JsonProperty("origin_type_list")
	private List<Integer> originTypeList;
	
	@ApiModelProperty(value="订单类型 1：TOC订单 2: TOB订单 3：服务商品")
	@JsonProperty("order_type")
	private Integer orderType;

	@ApiModelProperty(value = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@JsonProperty("update_time")
	private Date updateTime;
}



