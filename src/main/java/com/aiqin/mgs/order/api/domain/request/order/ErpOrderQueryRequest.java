package com.aiqin.mgs.order.api.domain.request.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeCategoryQueryTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("order_store_code")
    private String orderStoreCode;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单支付状态")
    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @ApiModelProperty(value = "订单类型")
    @JsonProperty("order_type_code")
    private Integer orderTypeCode;

    @ApiModelProperty(value = "订单类型")
    @JsonProperty("order_category_code")
    private Integer orderCategoryCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "下单人")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "查询开始时间 yyyy-MM-dd")
    @JsonProperty("create_time_start")
    private String createTimeStart;

    @ApiModelProperty(value = "查询结束时间 yyyy-MM-dd")
    @JsonProperty("create_time_end")
    private String createTimeEnd;

    @ApiModelProperty(value = "每页条数")
    @JsonProperty("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    @JsonProperty("page_no")
    private Integer pageNo;

    /***订单级别*/
    @JsonProperty("order_level")
    private Integer orderLevel;

    @JsonIgnore
    private List<Integer> orderTypeQueryList;
    @JsonIgnore
    private List<Integer> orderCategoryQueryList;
    @JsonIgnore
    private List<String> primaryOrderCodeList;
    @JsonIgnore
    private ErpOrderTypeCategoryQueryTypeEnum queryTypeEnum;
}
