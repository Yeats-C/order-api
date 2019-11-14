package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.OrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
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
    /***订单状态*/
    private Integer orderStatus;
    /***订单状态描述*/
    private String orderStatusDesc;
    /***是否发生退货 0:普通订单 1:退货单*/
    private Integer returnStatus;
    /***是否发生退货 描述*/
    private String returnStatusDesc;
    /***支付状态 */
    private Integer payStatus;
    /***支付状态描述 */
    private String payStatusDesc;
    /***订单类型*/
    private Integer orderType;
    /***订单类型描述*/
    private String orderTypeDesc;
    /***门店id*/
    private String storeId;
    /***门店名称*/
    private String storeName;
    /***订单总额*/
    private BigDecimal totalPrice;
    /***实付金额*/
    private BigDecimal actualPrice;

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

    public String getOrderStatusDesc() {
        return ErpOrderStatusEnum.getEnumDesc(orderStatus);
    }

    public String getReturnStatusDesc() {
        return returnStatusDesc;
    }

    public String getPayStatusDesc() {
        return PayStatusEnum.getEnumDesc(payStatus);
    }

    public String getOrderTypeDesc() {
        return OrderTypeEnum.getEnumDesc(orderType);
    }
}
