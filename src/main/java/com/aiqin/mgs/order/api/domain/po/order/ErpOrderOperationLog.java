package com.aiqin.mgs.order.api.domain.po.order;

/**
 * 订单操作日志实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:58
 */
public class ErpOrderOperationLog extends ErpOrderBase {

    /***日志id*/
    private String logId;
    /***订单id*/
    private String orderId;
    /***订单状态*/
    private Integer orderStatus;
    /***操作内容*/
    private String operationContent;

}
