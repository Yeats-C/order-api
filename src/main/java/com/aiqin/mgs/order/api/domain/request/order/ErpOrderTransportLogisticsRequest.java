package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单发运物流信息请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/1 17:01
 */
@Data
public class ErpOrderTransportLogisticsRequest {

    @ApiModelProperty(value = "物流单号")
    @JsonProperty("logistics_code")
    private String logisticsCode;

    @ApiModelProperty(value = "物流公司编码")
    @JsonProperty("logistics_centre_code")
    private String logisticsCentreCode;

    @ApiModelProperty(value = "物流公司名称")
    @JsonProperty("logistics_centre_name")
    private String logisticsCentreName;

    @ApiModelProperty(value = "发货仓库编码")
    @JsonProperty("send_repertory_code")
    private String sendRepertoryCode;

    @ApiModelProperty(value = "发货仓库名称")
    @JsonProperty("send_repertory_name")
    private String sendRepertoryName;

    @ApiModelProperty(value = "物流费用")
    @JsonProperty("logistics_fee")
    private BigDecimal logisticsFee;

}
