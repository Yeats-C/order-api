package com.aiqin.mgs.order.api.domain.request.order;

import lombok.Data;

/**
 * 订单签收请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/14 11:02
 */
@Data
public class ErpOrderSignRequest {

    /***订单编号*/
    private String orderCode;
    /***门店id*/
    private String storeId;
    /***门店编号*/
    private String storeCode;

}
