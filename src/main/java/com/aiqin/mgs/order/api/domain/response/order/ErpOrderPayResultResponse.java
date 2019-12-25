package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付结果查询返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/25 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderPayResultResponse {

    @ApiModelProperty(value = "订单id")
    private String orderId;
    @ApiModelProperty(value = "订单编码")
    private String orderCode;
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;
    @ApiModelProperty(value = "订单状态描述")
    private String orderStatusDesc;
    @ApiModelProperty(value = "支付状态 0待支付 1已发起支付（支付中） 2支付成功 3支付失败")
    private Integer payStatus;
    @ApiModelProperty(value = "支付状态描述")
    private String payStatusDesc;
    @ApiModelProperty(value = "物流券")
    private BigDecimal goodsCoupon;
    @ApiModelProperty(value = "支付流水号")
    private String payCode;
    @ApiModelProperty(value = "支付id")
    private String payId;
    @ApiModelProperty(value = "支付开始时间")
    private Date payStartTime;
    @ApiModelProperty(value = "支付完成时间")
    private Date payEndTime;

    public String getOrderStatusDesc() {
        return ErpOrderStatusEnum.getEnumDesc(orderStatus);
    }

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
