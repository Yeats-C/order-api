/*****************************************************************

* 模块名称：订单查询条件-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单查询条件")
public class OrderQuery extends PagesRequest {
    
	@ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    private String orderId="";
	
	@ApiModelProperty(value = "订单code")
    @JsonProperty("order_code")
    private String orderCode="";
	
    @ApiModelProperty(value = "会员id")
    @JsonProperty("member_id")
    private String memberId="";
    
    @ApiModelProperty(value="会员IDlist")
	@JsonProperty("memberid_list")
	private List<String>  memberidList;
    
    @ApiModelProperty(value = "会员名称")
    @JsonProperty("member_name")
    private String memberName="";
    
    @ApiModelProperty(value = "会员手机号")
    @JsonProperty("member_phone")
    private String memberPhone;
    
    @ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId="";

	@JsonProperty("list_distributor_id")
	@ApiModelProperty(value = "erp专用，门店列表，查全部传空")
	private List<String> listDistributorId;
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	private String distributorCode="";


	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	private String distributorName="";
	


//	@ApiModelProperty(value="来源类型0-pos，1-微商城, 2-全部 ,3:web")
//	@JsonProperty("origin_type")
//	private Integer orderOriginType;

	@ApiModelProperty(value="来源类型:2||null-全部;0&&3-门店;1-微商城;0-pos;3:web")
	@JsonProperty("origin_type_list")
	private List<Integer> originTypeList;
	
	@ApiModelProperty(value="收银员id")
	@JsonProperty("cashier_id")
	private String cashierId;
	
	
	@ApiModelProperty(value="收银员名称")
	@JsonProperty("cashier_name")
	private String cashierName;
	
	
	@ApiModelProperty(value="导购id")
	@JsonProperty("guide_id")
	private String guideId;
	
	
	@ApiModelProperty(value="导购名称")
	@JsonProperty("guide_name")
	private String guideName;
	
	@ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date create_time;
	
	@ApiModelProperty(value="创建开始时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_time")
	private Date beginTime;
	
	
	@ApiModelProperty(value="创建结束时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("end_time")
	private Date endTime;
	
	@ApiModelProperty(value="开始时间String类型、格式YYYY-MM-DD")
	@JsonProperty("begin_date")
	private String beginDate;
	
	
	@ApiModelProperty(value="结束时间String类型、格式YYYY-MM-DD")
	@JsonProperty("end_date")
	private String endDate;
	
	
	@ApiModelProperty(value="订单状态")
	@JsonProperty("order_status")
	private Integer orderStatus;
	
	@ApiModelProperty(value="订单支付方式")
	@JsonProperty("pay_type")
	private String payType;
	
	
	@ApiModelProperty("收货方式，0-到店自提，1-快递; 2:全部  扩展字段")
    @JsonProperty("receive_type")
    private Integer receiveType;	
	
	@ApiModelProperty(value="8位提货码")
	@JsonProperty("receive_code")
	private String receiveCode;
	
	@ApiModelProperty(value="扩展查询条件")
	@JsonProperty("any")
	private Integer any;
	
	@ApiModelProperty(value="不传参不返回 0:全部")
	@JsonProperty("icount")
	private Integer icount = 0;
	
	@ApiModelProperty(value="订单号/手机号")
	@JsonProperty("code_and_phone")
	private String codeAndPhone;

	@ApiModelProperty(value="条形码/sku码")
	@JsonProperty("bar_code_and_sku")
	private String barCodeAndSku;

	@ApiModelProperty(value="查询条件:订单状态集合")
	@JsonProperty("order_status_list")
	private List<Integer> orderStatusList;
	
	@ApiModelProperty(value="查询条件:订单状态集合")
	@JsonProperty("order_id_list")
	private List<String> orderIdList;
	
	@ApiModelProperty(value="控制SQL:订单状态集合排除")
	@JsonProperty("no_exist_order_code_list")
	private List<String> noExistOrderCodeList;

	@ApiModelProperty(value="订单类型 1：TOC订单 2: TOB订单 3：服务商品，4 预存订单")
	@JsonProperty("order_type")
	private String orderType;



}



