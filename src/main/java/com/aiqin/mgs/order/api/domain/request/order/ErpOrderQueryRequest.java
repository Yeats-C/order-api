package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单查询请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/13 14:53
 */
@Data
public class ErpOrderQueryRequest {

    @ApiModelProperty(value = "订单编号")
    private String orderCode;
    @ApiModelProperty(value = "订单状态 枚举 ErpOrderStatusEnum")
    private Integer orderStatus;
    @ApiModelProperty(value = "订单支付状态 ErpPayStatusEnum")
    private Integer payStatus;
    @ApiModelProperty(value = "订单类型 枚举 ErpOrderCategoryEnum")
    private Integer orderType;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "查询开始时间 yyyy-MM-dd")
    private String createTimeStart;
    @ApiModelProperty(value = "查询结束时间 yyyy-MM-dd")
    private String createTimeEnd;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
    @ApiModelProperty(value = "当前页")
    private Integer pageNo;
    /***订单级别*/
    private Integer orderLevel;

    @JsonIgnore
    private List<Integer> orderTypeQueryList;
}
