package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流单支付结果查询
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/25 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderLogisticsPayResultResponse {

    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_code")
    private String orderCode;
    @ApiModelProperty(value = "加盟商id")
    @JsonProperty("franchisee_id")
    private String franchiseeId;
    @ApiModelProperty(value = "加盟商编码")
    @JsonProperty("franchisee_code")
    private String franchiseeCode;
    @ApiModelProperty(value = "加盟商名称")
    @JsonProperty("franchisee_name")
    private String franchiseeName;

    @ApiModelProperty(value = "物流费用支付状态 0待支付 1已发起支付（支付中） 2支付成功 3支付失败")
    @JsonProperty("pay_status")
    private Integer payStatus;
    @JsonProperty("pay_status_desc")
    @ApiModelProperty(value = "物流费用支付状态描述")
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

    @ApiModelProperty(value = "支付流水号")
    @JsonProperty("pay_code")
    private String payCode;
    /***支付id*/
    @JsonProperty("pay_id")
    private String payId;
    @ApiModelProperty(value = "支付开始时间")
    @JsonProperty("pay_start_time")
    private Date payStartTime;
    @ApiModelProperty(value = "支付完成时间")
    @JsonProperty("pay_end_time")
    private Date payEndTime;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
