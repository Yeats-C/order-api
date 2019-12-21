package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流单支付结果查询
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/25 14:49
 */
@Data
public class ErpOrderLogisticsPayResultResponse {

    @ApiModelProperty(value = "订单号")
    private String orderCode;
    @ApiModelProperty(value = "加盟商id")
    private String franchiseeId;
    @ApiModelProperty(value = "加盟商编码")
    private String franchiseeCode;
    @ApiModelProperty(value = "加盟商名称")
    private String franchiseeName;

    @ApiModelProperty(value = "物流费用支付状态")
    private Integer payStatus;
    @ApiModelProperty(value = "物流费用支付状态描述")
    private String payStatusDesc;
    @ApiModelProperty(value = "物流公司编码")
    private String logisticsCentreCode;
    @ApiModelProperty(value = "物流公司名称")
    private String logisticsCentreName;
    @ApiModelProperty(value = "物流单号")
    private String logisticsCode;
    @ApiModelProperty(value = "发货仓库编码")
    private String sendRepertoryCode;
    @ApiModelProperty(value = "发货仓库名称")
    private String sendRepertoryName;
    @ApiModelProperty(value = "物流费用")
    private BigDecimal logisticsFee;

    @ApiModelProperty(value = "支付流水号")
    private String payCode;
    /***支付id*/
    private String payId;
    @ApiModelProperty(value = "支付开始时间")
    private Date payStartTime;
    @ApiModelProperty(value = "支付完成时间")
    private Date payEndTime;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
