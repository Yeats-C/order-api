package com.aiqin.mgs.order.api.domain.response.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询支付中心支付状态返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/27 14:39
 */
@Data
public class ErpPayPollingResponse {

    @ApiModelProperty(value = "支付状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "支付流水号")
    private String payNum;

}
