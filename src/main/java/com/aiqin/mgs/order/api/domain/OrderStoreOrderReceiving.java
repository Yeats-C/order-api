package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.util.Date;

/**
 * 订单收货信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:26
 */
@Data
public class OrderStoreOrderReceiving {

    /***主键*/
    private String id;
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

    /***创建时间*/
    private Date createTime;
    /***创建人id*/
    private String createById;
    /***创建人姓名*/
    private String createByName;
    /***更新时间*/
    private Date updateTime;
    /***修改人id*/
    private String updateById;
    /***修改人姓名*/
    private String updateByName;
}
