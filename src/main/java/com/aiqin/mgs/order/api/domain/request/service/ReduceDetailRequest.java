package com.aiqin.mgs.order.api.domain.request.service;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("查询订单明细")
public class ReduceDetailRequest extends PagesRequest {
    @ApiModelProperty(value = "门店信息")
    @JsonProperty("store_info")
    private String storeInfo;

    @ApiModelProperty(value = "会员手机号")
    @JsonProperty("customer_phone")
    private String customerPhone;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店编号")
    @JsonProperty("store_code")
    private String storeCode;


    @JsonProperty("list_distributor_id")
    @ApiModelProperty(value = "erp专用，门店列表，查全部传空")
    private List<String> listDistributorId;

    @ApiModelProperty(value = "服务项目类别id")
    @JsonProperty("type_id")
    private String typeId;

    @ApiModelProperty(value = "服务项目id")
    @JsonProperty("project_id")
    private String projectId;

    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    private String orderId;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "活动开始时间")
    @JsonProperty("begin_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty(value = "活动结束时间")
    @JsonProperty("finish_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date finishTime;

    @ApiModelProperty(value = "查询时间")
    @JsonProperty("query_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone="GMT+8")
    private Date queryTime;

    @ApiModelProperty(value = "订单类型,0为扣减,1为购买,2为退次")
    @JsonProperty("order_type")
    private Integer orderType;
}
