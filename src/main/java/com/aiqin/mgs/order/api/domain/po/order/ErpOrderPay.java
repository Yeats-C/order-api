package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付信息实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:56
 */
@Data
public class ErpOrderPay {

    /***主键*/
    private Long id;
    /***支付id*/
    private String payId;
    /***业务外键*/
    private String businessKey;
    /***支付流水号*/
    private String payCode;
    /***费用类型*/
    private Integer feeType;
    /***支付状态*/
    private Integer payStatus;
    /***支付方式*/
    private Integer payWay;
    /***费用*/
    private BigDecimal payFee;
    /***开始支付时间*/
    private Date payStartTime;
    /***结束支付时间*/
    private Date payEndTime;
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
