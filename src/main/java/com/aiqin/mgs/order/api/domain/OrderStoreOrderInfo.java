package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单主表数据
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 15:45
 */
@Data
public class OrderStoreOrderInfo {

    /***主键*/
    private Long id;
    /***订单id*/
    private String orderId;
    /***订单编号*/
    @ApiModelProperty(value = "订单号")
    private String orderCode;
    /***订单状态 枚举 ErpOrderStatusEnum*/
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;
    /***是否发生退货 0否 1是*/
    private Integer returnStatus;
    /***支付状态 */
    @ApiModelProperty(value = "支付状态")
    private Integer payStatus;
    /***订单类型 枚举 OrderTypeEnum*/
    @ApiModelProperty(value = "订单类型")
    private Integer orderType;
    /***加盟商id*/
    private String franchiseeId;
    /***门店id*/
    private String storeId;
    /***门店名称*/
    @ApiModelProperty(value = "门店名称")
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
    /***创建时间开始 yyyy-MM-dd*/
    @ApiModelProperty(value = "订单日期 开始")
    private String createTimeBegin;
    /***创建时间结束 yyyy-MM-dd*/
    @ApiModelProperty(value = "订单日期 结束")
    private String createTimeEnd;
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

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
    @ApiModelProperty(value = "当前页")
    private Integer pageNo;

    /***关联子订单*/
    private List<OrderStoreOrderInfo> secondaryOrderList;

}
