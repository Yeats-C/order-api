package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OrderIogisticsVo implements Serializable {
        //订单
        @ApiModelProperty(value = "发货时间",example = "2001-01-01 01:01:01")
        @JsonProperty("delivery_time")
        private Date deliveryTime;

        @ApiModelProperty(value = "订单号")
        @JsonProperty("order_store_code")
        @NotEmpty(message = "订单号不能为空")
        private String orderStoreCode;

        @ApiModelProperty(value = "实际销售数量")
        @JsonProperty("actual_total_count")
        private Long actualTotalCount;

        @ApiModelProperty(value = "发货人")
        @JsonProperty("delivery_person")
        private String deliveryPersonId;

        @ApiModelProperty(value = "操作人id")
        @JsonProperty("person_id")
        private String personId;

        @ApiModelProperty(value = "操作人姓名")
        @JsonProperty("person_name")
        private String personName;

        //订单明细
        @ApiModelProperty(value = "订单商品明细行")
        @JsonProperty("orders_store_detail")
        private List<OrderStoreDetail> orderStoreDetail;

    //订单批次明细（仓卡）
    @ApiModelProperty(value = "订单批次明细")
    @JsonProperty("orders_batch_store_detail")
    private List<OrderBatchStoreDetail> orderBatchStoreDetail;
}
