package com.aiqin.mgs.order.api.domain.response.order;

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

    private Integer hundredNum;
    private Integer fiftyNum;
    private BigDecimal money;
}
