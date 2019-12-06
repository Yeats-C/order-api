package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2019/11/5 19:26
 * Description:
 */
@ApiModel("预存订单")
@Data
public class PrestorageOrderSupply {

    @ApiModelProperty(value="预存订单提货id")
    @JsonProperty("prestorage_order_supply_id")
    private String prestorageOrderSupplyId="";

    @ApiModelProperty(value="预存订单提货编号")
    @JsonProperty("prestorage_order_supply_code")
    private String prestorageOrderSupplyCode="";


    @ApiModelProperty(value="订单id")
    @JsonProperty("order_id")
    private String orderId="";

    @ApiModelProperty(value="订单编号")
    @JsonProperty("order_code")
    private String orderCode="";

    @ApiModelProperty(value="会员id")
    @JsonProperty("member_id")
    private String memberId="";

    @ApiModelProperty(value="会员名称")
    @JsonProperty("member_name")
    private String memberName="";

    @ApiModelProperty(value="会员手机号")
    @JsonProperty("member_phone")
    private String memberPhone="";

    @ApiModelProperty(value="分销机构id")
    @JsonProperty("distributor_id")
    private String distributorId="";


    @ApiModelProperty(value="分销机构编码")
    @JsonProperty("distributor_code")
    private String distributorCode="";


    @ApiModelProperty(value="分销机构名称")
    @JsonProperty("distributor_name")
    private String distributorName="";


    @ApiModelProperty(value="提货状态")
    @JsonProperty("prestorage_order_supply_status")
    private Integer prestorageOrderSupplyStatus;

    @ApiModelProperty(value="支付方式，1.现金、2微信  3.支付宝、4银联 5储值卡")
    @JsonProperty("pay_type")
    private Integer payType;
    @ApiModelProperty(value="下单时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;


    @ApiModelProperty(value="更改时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;


    @ApiModelProperty(value="操作员")
    @JsonProperty("create_by")
    private String createBy="";


    @ApiModelProperty(value="修改员")
    @JsonProperty("update_by")
    private String updateBy="";


    @ApiModelProperty(value="操作员名称")
    @JsonProperty("`create_by_name`")
    private String createByName="";


    @ApiModelProperty(value="修改员名称")
    @JsonProperty("`update_by_name`")
    private String updateByName="";


}
