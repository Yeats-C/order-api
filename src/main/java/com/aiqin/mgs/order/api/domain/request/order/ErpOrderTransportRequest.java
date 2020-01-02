package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单发运请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/1 17:01
 */
@Data
public class ErpOrderTransportRequest {

    @ApiModelProperty(value = "物流信息 不需要物流单的订单不需要传")
    @JsonProperty("logistics")
    private ErpOrderTransportLogisticsRequest logistics;

    @ApiModelProperty(value = "发运时间")
    @JsonProperty("transport_time")
    private Date transportTime;

    @ApiModelProperty(value = "发运状态")
    @JsonProperty("transport_status")
    private Integer transportStatus;

    @ApiModelProperty(value = "配送方式编码")
    @JsonProperty("distribution_mode_code")
    private String distributionModeCode;

    @ApiModelProperty(value = "配送方式名称")
    @JsonProperty("distribution_mode_name")
    private String distributionModeName;

    @ApiModelProperty(value = "操作人id")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "操作人姓名")
    @JsonProperty("person_name")
    private String personName;

    @ApiModelProperty(value = "该物流单关联的订单，必须是同一个加盟商，同一个类型的订单")
    @JsonProperty("order_code_list")
    private List<String> orderCodeList;

}
