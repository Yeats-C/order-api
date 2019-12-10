package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

/**
 * 订单收货人信息表实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:10
 */
@Data
public class ErpOrderConsignee extends ErpOrderBase {

    /***订单id*/
    private String orderId;
    /***收货id*/
    private String receiveId;
    /***收货人*/
    private String receiveName;
    /***收货地址*/
    private String receiveAddress;
    /***收货电话*/
    private String receivePhone;
    /***是否需要发票 0：否 1：是*/
    private Integer billStatus;
}
