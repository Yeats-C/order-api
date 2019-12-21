package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.ErpPayPollingBackStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
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

    /***请求是否成功*/
    private boolean requestSuccess = true;
    /***订单号*/
    private String orderCode;
    /***支付结果*/
    private ErpPayStatusEnum payStatusEnum;
    /***支付单状态*/
    private ErpPayPollingBackStatusEnum payPollingBackStatusEnum;
    /***支付流水号*/
    private String payCode;
    /***支付id*/
    private String payId;

}
