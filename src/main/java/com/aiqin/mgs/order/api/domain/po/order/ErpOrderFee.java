package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单费用
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/11 20:18
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderFee {

    /***主键*/
    private Long id;
    /***订单费用id*/
    private String feeId;
    /***关联订单id*/
    private String orderId;
    /***支付单id*/
    private String payId;
    /***支付状态*/
    private Integer payStatus;
    /***支付状态描述*/
    private String payStatusDesc;
    /***订单总额*/
    private BigDecimal totalMoney;
    /***活动优惠金额*/
    private BigDecimal activityMoney;
    /***服纺券优惠金额*/
    private BigDecimal suitCouponMoney;
    /***A品券优惠金额*/
    private BigDecimal topCouponMoney;
    /***实付金额*/
    private BigDecimal payMoney;
    /***返还物流券金额*/
    private BigDecimal goodsCoupon;

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

    /***支付流水号*/
    private String payCode;
    /***订单支付*/
    private ErpOrderPay orderPay;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
