package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付信息实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:56
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderPay {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "支付id")
    @JsonProperty("pay_id")
    private String payId;

    @ApiModelProperty(value = "业务外键")
    @JsonProperty("business_key")
    private String businessKey;

    @ApiModelProperty(value = "支付流水号")
    @JsonProperty("pay_code")
    private String payCode;

    @ApiModelProperty(value = "费用类型")
    @JsonProperty("fee_type")
    private Integer feeType;

    @ApiModelProperty(value = "支付状态")
    @JsonProperty("pay_status")
    private Integer payStatus;

    @ApiModelProperty(value = "支付方式")
    @JsonProperty("pay_way")
    private Integer payWay;

    @ApiModelProperty(value = "支付费用（元）")
    @JsonProperty("pay_fee")
    private BigDecimal payFee;

    @ApiModelProperty(value = "开始支付时间")
    @JsonProperty("pay_start_time")
    private Date payStartTime;

    @ApiModelProperty(value = "结束支付时间")
    @JsonProperty("pay_end_time")
    private Date payEndTime;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人姓名")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "修改人id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "修改人姓名")
    @JsonProperty("update_by_name")
    private String updateByName;

}
