package com.aiqin.mgs.order.api.domain.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单操作按钮控制类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/24 15:41
 */
@Data
public class ErpOrderOperationControlResponse {

    @ApiModelProperty(value = "异常的订单，显示确认订单按钮 0是 1否")
    @JsonProperty("abnormal")
    private int abnormal = 1;

    @ApiModelProperty(value = "erp编辑订单添加赠品按钮 0有 1无")
    @JsonProperty("add_gift")
    private int addGift = 1;

    @ApiModelProperty(value = "支付订单费用 0有 1无")
    @JsonProperty("pay_order")
    private int payOrder = 1;

    @ApiModelProperty(value = "订单签收 0有 1无")
    @JsonProperty("sign")
    private int sign = 1;

    @ApiModelProperty(value = "支付物流费用 0有 1无")
    @JsonProperty("pay_logistics")
    private int payLogistics = 1;

    @ApiModelProperty(value = "取消 0有 1无")
    @JsonProperty("cancel")
    private int cancel = 1;

    @ApiModelProperty(value = "爱掌柜查看按钮 0有 1无")
    @JsonProperty("detail")
    private int detail = 0;

    @ApiModelProperty(value = "erp查看按钮 0有 1无")
    @JsonProperty("erp_detail")
    private int erpDetail = 0;

    @ApiModelProperty(value = "详情是否显示物流信息 0有 1无")
    @JsonProperty("logistics_area")
    private int logisticsArea = 1;

    @ApiModelProperty(value = "重新加入购物车 0有 1无")
    @JsonProperty("rejoin_cart")
    private int rejoinCart = 1;

    @ApiModelProperty(value = "退货 0有 1无")
    @JsonProperty("order_return")
    private int orderReturn = 1;

    @ApiModelProperty(value = "退款 0有 1无")
    @JsonProperty("refund")
    private int refund = 1;

}
