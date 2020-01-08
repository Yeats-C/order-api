package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@ApiModel("查询的退货单列表")
public class ReturnOrderDetailBySearchResponse {
    @ApiModelProperty(value = "退货单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;


    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_store_code")
    private String orderStoreCode;

    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货、3冲减单")
    @JsonProperty("return_order_type")
    private Integer returnOrderType;


    @ApiModelProperty(value = "客户编码")
    @JsonProperty("customer_code")
    private String customerCode;

    @ApiModelProperty(value = "仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value = "库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value = "供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value = "1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，12-退款完成，97-退货终止，98-审核不通过，99-已取消")
    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_count")
    private Long productCount;


    @ApiModelProperty(value = "退货金额")
    @JsonProperty("return_order_amount")
    private BigDecimal returnOrderAmount;

    @ApiModelProperty(value = "实退商品数量")
    @JsonProperty("actual_product_count")
    private Long actualProductCount;

    @ApiModelProperty(value = "实退商品总金额")
    @JsonProperty("actual_return_order_amount")
    private BigDecimal actualReturnOrderAmount;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;


    @ApiModelProperty(value = "修改人名称")
    @JsonProperty("update_by_name")
    private String updateByName;



}
