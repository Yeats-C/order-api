package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

/**
 * 订单操作日志实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:58
 */
@Data
public class ErpOrderOperationLog extends ErpOrderBase {

    /***订单id*/
    private String orderId;
    /***订单状态*/
    private Integer orderStatus;
    /***操作内容*/
    private String operationContent;

}
