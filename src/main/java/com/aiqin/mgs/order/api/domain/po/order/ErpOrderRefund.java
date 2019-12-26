package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款单
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/12 20:31
 */
@Data
public class ErpOrderRefund {

    /***主键*/
    private Long id;
    /***退款单id*/
    private String refundId;
    /***关联订单id*/
    private String orderId;
    /***退款金额*/
    private BigDecimal refundFee;
    /***退款状态*/
    private Integer refundStatus;
    /***退款开始时间*/
    private Date refundStartTime;
    /***退款结束时间*/
    private Date refundEndTime;
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
    /***数据状态 1有效 0删除*/
    private Integer status;
}
