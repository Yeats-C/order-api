package com.aiqin.mgs.order.api.domain.request.order;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 保存订单请求参数类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/22 16:31
 */
@Data
public class ErpOrderSaveRequest {

    /***门店id*/
    @ApiModelProperty(value = "门店id")
    private String storeId;

    /***订单类型 OrderTypeEnum*/
    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    /***订单来源 OrderOriginTypeEnum*/
    @ApiModelProperty(value = "订单来源")
    private Integer orderOriginType;

    /***订单销售渠道标识 OrderChannelEnum*/
    @ApiModelProperty(value = "销售渠道")
    private Integer orderChannel;

    /***是否需要发票*/
    @ApiModelProperty(value = "是否需要发票")
    private Integer billStatus;

    /***货架商品列表*/
    @ApiModelProperty(value = "货架商品列表")
    private List<OrderStoreOrderProductItem> productList;
}
