package com.aiqin.mgs.order.api.domain.request.orderList;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 描述:
 *
 * @author yuanhaole
 * @create 2020年1月3日16:15:26
 */
@ApiModel("退货查询vo")
@Data
public class OrderListVo3 extends PagesRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String finishTime;
    @ApiModelProperty(value = "退货单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;
    @ApiModelProperty(value = "客户名称")
    @JsonProperty("customer_name")
    private String customerName;
    @ApiModelProperty(value = "仓库名称")
    @JsonProperty("transport_center_name")
    private String transportCenterName;
    @ApiModelProperty(value = "库房名称")
    @JsonProperty("warehouse_name")
    private String warehouseName;
    @ApiModelProperty(value = "1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，12-退款完成，97-退货终止，98-审核不通过，99-已取消")
    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;
    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_store_code")
    private String orderStoreCode;
    @ApiModelProperty(value = "公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    public OrderListVo3(String beginTime, String finishTime, String returnOrderCode, String customerName, String transportCenterName, String warehouseName, Integer returnOrderStatus, String orderStoreCode,String companyCode) {
        this.beginTime = beginTime;
        this.finishTime = finishTime;
        this.returnOrderCode = returnOrderCode;
        this.customerName = customerName;
        this.transportCenterName = transportCenterName;
        this.warehouseName = warehouseName;
        this.returnOrderStatus = returnOrderStatus;
        this.orderStoreCode = orderStoreCode;
        this.companyCode = companyCode;
    }
}
