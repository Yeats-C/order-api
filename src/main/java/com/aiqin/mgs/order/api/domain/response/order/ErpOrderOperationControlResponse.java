package com.aiqin.mgs.order.api.domain.response.order;

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

    @ApiModelProperty(value = "确认收款按钮 0有 1无")
    private int repay = 1;
    @ApiModelProperty(value = "编辑订单添加赠品按钮 0有 1无")
    private int addGift = 1;
    @ApiModelProperty(value = "支付订单费用 0有 1无")
    private int payOrder = 1;
    @ApiModelProperty(value = "订单签收 0有 1无")
    private int sign = 1;
    @ApiModelProperty(value = "支付物流费用 0有 1无")
    private int payLogistics = 1;
    @ApiModelProperty(value = "取消 0有 1无")
    private int cancel = 1;
    @ApiModelProperty(value = "查看 0有 1无")
    private int detail = 0;

}
