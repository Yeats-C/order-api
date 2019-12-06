package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import lombok.Data;

/**
 * 订单支付状态查询返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/2 15:55
 */
@Data
public class ErpOrderPayStatusResponse {
    private boolean requestSuccess = true;
    private String orderCode;
    private PayStatusEnum payStatusEnum;
    private String payCode;

}
