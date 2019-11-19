package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2019/11/7 14:19
 * Description:
 */
@Data
@ApiModel("预存订单")
public class PrestorageOrderInfo {
    /**
     * 订单id
     */
    @JsonProperty("order_id")
    @ApiModelProperty("订单id")
    private String orderId;
    /**
     * 订单编号
     */
    @JsonProperty("order_code")
    @ApiModelProperty("订单编号")
    private String orderCode;
    /**
     * 订单状态，0.未提货、1.已提货
     */
    @JsonProperty("order_status")
    @ApiModelProperty("订单状态，0.未提货、1.已提货")
    private Integer orderStatus;


    @ApiModelProperty(value = "前端控制退货按钮使用字段:1:订单已全数退完,0 未退完")
    @JsonProperty("turn_return_view")
    private Integer turnReturnView=0;
    /**
     * 会员名称
     */
    @JsonProperty("member_name")
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 支付方式，微信、支付宝、现金、银联，若为多种方式，用“—”连接
     */
    @JsonProperty("pay_type")
    @ApiModelProperty("支付方式，微信、支付宝、现金、银联，若为多种方式，用“—”连接")
    private String payType;
    /**
     * 来源类型，0为pos，1为微商城
     */
    @JsonProperty("origin_type")
    @ApiModelProperty("来源类型，0为pos，1为微商城，2全部")
    private Integer originType;
    /**
     * 实际金额
     */
    @JsonProperty("actual_price")
    @ApiModelProperty("实际金额")
    private Long actualPrice;
    /**
     * 总金额
     */
    @JsonProperty("total_price")
    @ApiModelProperty("总金额")
    private Long totalPrice;
    /**
     * 会员手机号
     */
    @JsonProperty("member_phone")
    @ApiModelProperty("会员手机号")
    private String memberPhone;
    /**
     * 收银员id
     */
    @JsonProperty("cashier_id")
    @ApiModelProperty("收银员id")
    private String cashierId;
    /**
     * 收银员名称
     */
    @JsonProperty("cashier_name")
    @ApiModelProperty("收银员名称")
    private String cashierName;
    /**
     * 导购id
     */
    @JsonProperty("guide_id")
    @ApiModelProperty("导购id")
    private String guideId;
    /**
     * 导购名称
     */
    @JsonProperty("guide_name")
    @ApiModelProperty("导购名称")
    private String guideName;


    @JsonProperty("pay_time")
    @ApiModelProperty(value = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;


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
