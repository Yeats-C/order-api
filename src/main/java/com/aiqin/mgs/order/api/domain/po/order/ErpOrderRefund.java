package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款单
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/12 20:31
 */
@Data
public class ErpOrderRefund {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "退款单id")
    @JsonProperty("refund_id")
    private String refundId;

    @ApiModelProperty(value = "关联订单id")
    @JsonProperty("order_id")
    private String orderId;

    @ApiModelProperty(value = "关联支付单id")
    @JsonProperty("pay_id")
    private String payId;

    @ApiModelProperty(value = "退款金额")
    @JsonProperty("refund_fee")
    private BigDecimal refundFee;

    @ApiModelProperty(value = "退款状态")
    @JsonProperty("refund_status")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款类型")
    @JsonProperty("refund_type")
    private Integer refundType;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人姓名")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "修改人id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "修改人姓名")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "据状态 1有效 0删除")
    @JsonProperty("status")
    private Integer status;
}
