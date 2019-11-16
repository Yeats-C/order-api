package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表数据
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 15:45
 */
@Data
public class OrderStoreOrderInfo extends PagesRequest {

    /***主键*/
    private Long id;
    /***订单id*/
    private String orderId;
    /***订单编号*/
    private String orderCode;
    /***订单状态 枚举 ErpOrderStatusEnum*/
    private Integer orderStatus;
    /***是否发生退货 0否 1是*/
    private Integer returnStatus;
    /***支付状态 */
    private Integer payStatus;
    /***订单类型 枚举 OrderTypeEnum*/
    private Integer orderType;
    /***门店id*/
    private String storeId;
    /***门店名称*/
    private String storeName;
    /***订单总额*/
    private BigDecimal totalPrice;
    /***实付金额*/
    private BigDecimal actualPrice;
    /***订单级别 枚举 OrderLevelEnum*/
    private Integer orderLevel;
    /***关联主订单编码*/
    private String primaryCode;

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
