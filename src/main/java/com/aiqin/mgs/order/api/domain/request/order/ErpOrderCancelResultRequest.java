package com.aiqin.mgs.order.api.domain.request.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 供应链返回订单是否可以取消参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/4 16:38
 */
@Data
public class ErpOrderCancelResultRequest {

    @ApiModelProperty(value = "订单编号")
    private String orderCode;
    @ApiModelProperty(value = "结果 1可以取消 0不可取消")
    private String status;
    @ApiModelProperty(value = "不可取消原因（选填）")
    private String reason;
}
