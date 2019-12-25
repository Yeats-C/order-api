package com.aiqin.mgs.order.api.domain.response.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付成功请求物流券返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/23 16:36
 */
@Data
public class ErpOrderGoodsCouponResponse {

    @ApiModelProperty(value = "面值为100的物流券数量")
    private Integer hundredNum;
    @ApiModelProperty(value = "面值为50的物流券数量")
    private Integer fiftyNum;
    @ApiModelProperty(value = "总的物流券额度")
    private BigDecimal money;
}
