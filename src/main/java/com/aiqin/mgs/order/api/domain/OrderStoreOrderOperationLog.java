package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.util.Date;

/**
 * 订单操作日志
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:45
 */
@Data
public class OrderStoreOrderOperationLog {

    /***主键*/
    private Long id;
    /***日志id*/
    private String logId;
    /***订单id*/
    private String orderId;
    /***订单状态*/
    private Integer orderStatus;
    /***操作内容*/
    private String operationContent;
    /***创建时间*/
    private Date createTime;
    /***创建人id*/
    private String createById;
    /***创建人姓名*/
    private String createByName;

}
