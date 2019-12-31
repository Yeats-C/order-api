package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单物流信息表实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:03
 */
@Data
public class ErpOrderLogistics {

    /***主键*/
    @JsonProperty("id")
    private Long id;

    /***物流单id*/
    @JsonProperty("logistics_id")
    private String logisticsId;
    /***支付单id*/
    @JsonProperty("pay_id")
    private String payId;
    @ApiModelProperty(value = "物流费用支付状态")
    @JsonProperty("pay_status")
    private Integer payStatus;
    @ApiModelProperty(value = "物流费用支付状态描述")
    @JsonProperty("pay_status_desc")
    private String payStatusDesc;
    @ApiModelProperty(value = "物流公司编码")
    @JsonProperty("logistics_centre_code")
    private String logisticsCentreCode;
    @ApiModelProperty(value = "物流公司名称")
    @JsonProperty("logistics_centre_name")
    private String logisticsCentreName;
    @ApiModelProperty(value = "物流单号")
    @JsonProperty("logistics_code")
    private String logisticsCode;
    @ApiModelProperty(value = "发货仓库编码")
    @JsonProperty("send_repertory_code")
    private String sendRepertoryCode;
    @ApiModelProperty(value = "发货仓库名称")
    @JsonProperty("send_repertory_name")
    private String sendRepertoryName;
    @ApiModelProperty(value = "物流费用")
    @JsonProperty("logistics_fee")
    private BigDecimal logisticsFee;
    @ApiModelProperty(value = "物流券抵扣物流费用金额")
    @JsonProperty("coupon_pay_fee")
    private BigDecimal couponPayFee;
    @ApiModelProperty(value = "余额支付物流费用金额")
    @JsonProperty("balance_pay_fee")
    private BigDecimal balancePayFee;
    @JsonProperty("coupon_ids")
    private String couponIds;

    /***创建时间*/
    @JsonProperty("create_time")
    private Date createTime;
    /***创建人id*/
    @JsonProperty("create_by_id")
    private String createById;
    /***创建人姓名*/
    @JsonProperty("create_by_name")
    private String createByName;
    /***更新时间*/
    @JsonProperty("update_time")
    private Date updateTime;
    /***修改人id*/
    @JsonProperty("update_by_id")
    private String updateById;
    /***修改人姓名*/
    @JsonProperty("update_by_name")
    private String updateByName;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
