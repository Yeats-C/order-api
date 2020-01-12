package com.aiqin.mgs.order.api.domain.pay;

import com.aiqin.mgs.order.api.component.OperationTypeEnum;
import com.aiqin.mgs.order.api.component.PayOriginTypeEnum;
import com.aiqin.mgs.order.api.component.ServiceSceneEnum;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 被扫reqvo
 */
@Data
public class PayReq implements Serializable {


    private static final long serialVersionUID = -2797140355522762575L;

    @ApiModelProperty(value = "通联会员UUID--不传", hidden = true)
    @JsonProperty("allinpay_member_id")
    private String allinpayMemberId;
    @ApiModelProperty(value = "通联会员名称--不传", hidden = true)
    @JsonProperty("allinpay_member_name")
    private String allinpayMemberName;

    @ApiModelProperty(value = "门店id",hidden = true)
    @JsonProperty("aiqin_merchant_id")
    private String aiqinMerchantId;

    @ApiModelProperty(value = "门店id",hidden = true)
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty("订单号")
    @JsonProperty("order_no")
    private String orderNo;

    @ApiModelProperty(value = "订单金额")
    @JsonProperty("order_amount")
    private Long orderAmount;

    @ApiModelProperty("订单手续费--不传")
    @JsonProperty("fee")
    private Long fee=0L;
    @ApiModelProperty("下单时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("order_time")
    private Date orderTime;
    @ApiModelProperty(value = "(支付类型1在线支付-支付宝 2在线支付-银联 3到店支付-现金支付 4到店支付-微信 5到店支付-支付宝 6到店支付-银联 7到店支付-储值卡支付")
    @JsonProperty("pay_type")
    private Integer payType;
    @ApiModelProperty(value = "微信公众号标识--不传", hidden = true)
    @JsonProperty("openid")
    private String openid;
    @ApiModelProperty("订单来源（0:pos 1：微商城  2：全部  3：web ）")
    @JsonProperty("order_source")
    private Integer orderSource=3;
    @ApiModelProperty("支付授权码---扫码枪扫码，主扫不传")
    @JsonProperty("auth_code")
    private String authCode;
    @ApiModelProperty(value = "收款会员编号--不传", hidden = true)
    @JsonProperty("user_id")
    private String userId;
    @ApiModelProperty(value = "实际收款会员编号--不传", hidden = true)
    @JsonProperty("parent_user_id")
    private String parentUserId;
    @ApiModelProperty("访问终端类型1：Mobile；2pc ")
    @JsonProperty("source")
    private Integer source;
    @ApiModelProperty(value = "创建人编号 ", hidden = true)
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建人 ", hidden = true)
    @JsonProperty("create_name")
    private String createName;

    @ApiModelProperty(value = "创建人-退款使用 ", hidden = true)
    @JsonProperty("update_by")
    private String updateBy;

    @ApiModelProperty(value = "回调地址", hidden = true)
    @JsonProperty("back_url")
    private String backUrl;

    @ApiModelProperty(value = "业务场景: 0 TO C,1 TO B")
    @JsonProperty("service_scene")
    private Integer serviceScene= ServiceSceneEnum.TO_C.getCode();

    @ApiModelProperty(value = "支付来源1toc-pos消费 3toc-web消费 18 toc-pos储值 19toc-web储值 20toc-web仅退款 21 toc-web退款退货 22toc-pos仅退款 23 toc-pos退货退款")
    @JsonProperty("pay_origin_type")
    private Integer payOriginType= PayOriginTypeEnum.TOC_POS.getCode();

    @ApiModelProperty("1:充值；2：消费")
    @JsonProperty("type")
    private  Integer type= OperationTypeEnum.CONSUME.getCode();

    @ApiModelProperty(value = "公司编码--type 为充值时传递",hidden = true)
    @JsonProperty("company_code")
    private  String companyCode;

    @ApiModelProperty(value = "公司",hidden = true)
    @JsonProperty("company_name")
    private  String companyName;

    @ApiModelProperty(value="操作门店名称")
    @JsonProperty("operation_store_name")
    private String operationStoreName;
    /*******************新增*******************/
    @ApiModelProperty(value = "加盟商id",hidden = true)
    @JsonProperty("franchisee_id")
    private  String franchiseeId;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("会员id")
    @JsonProperty("member_id")
    private String memberId;

    @ApiModelProperty("会员手机号")
    @JsonProperty("member_phone")
    private String memberPhone;

    @ApiModelProperty("会员姓名")
    @JsonProperty("member_name")
    private String memberName;
    /**
     * 环境  test  测试  release  正式
     */
    private String environment;

    @ApiModelProperty("订单类型 2 toc ")
    @JsonProperty("pay_order_type")
    private Integer payOrderType=2;
    @ApiModelProperty("申请退款金额")
    @NotNull(message = "退款金额不能为空")
    @JsonProperty("refund_amount")
    private Long refundAmount;

    @ApiModelProperty("退款支付方式(0.现金 1.储值卡)")
    @JsonProperty("refund_type")
    @NotNull(message = "退款支付方式不能为空")
    private Integer refundType;

    @ApiModelProperty("业务类型(0.正常销售订单 1.预存订单)")
    @JsonProperty("business_type")
    @NotNull(message = "业务类型不能为空")
    private Integer businessType= Global.PAY_ORDER_TYPE_0;

}
