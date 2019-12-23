package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单物流信息表实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:03
 */
@Data
public class ErpOrderLogistics {

    /***主键*/
    private Long id;

    /***物流单id*/
    private String logisticsId;
    /***支付单id*/
    private String payId;
    /***支付状态 ErpPayStatusEnum*/
    @ApiModelProperty(value = "物流费用支付状态")
    private Integer payStatus;
    /***支付状态描述*/
    @ApiModelProperty(value = "物流费用支付状态描述")
    private String payStatusDesc;
    /***物流公司编码*/
    @ApiModelProperty(value = "物流公司编码")
    private String logisticsCentreCode;
    /***物流公司名称*/
    @ApiModelProperty(value = "物流公司名称")
    private String logisticsCentreName;
    /***物流单号*/
    @ApiModelProperty(value = "物流单号")
    private String logisticsCode;
    /***发货仓库编码*/
    @ApiModelProperty(value = "发货仓库编码")
    private String sendRepertoryCode;
    /***发货仓库名称*/
    @ApiModelProperty(value = "发货仓库名称")
    private String sendRepertoryName;
    /***物流费用*/
    @ApiModelProperty(value = "物流费用")
    private BigDecimal logisticsFee;
    /***物流券支付金额*/
    @ApiModelProperty(value = "物流券抵扣物流费用金额")
    private BigDecimal couponPayFee;
    /***余额支付金额*/
    @ApiModelProperty(value = "余额支付物流费用金额")
    private BigDecimal balancePayFee;
    /***物流券唯一标识，多个用逗号间隔*/
    private String couponIds;

    /***创建时间*/
    private Date createTime;
    /***创建人id*/
    private String createById;
    /***创建人姓名*/
    private String createByName;
    /***更新时间*/
    private Date updateTime;
    /***修改人id*/
    private String updateById;
    /***修改人姓名*/
    private String updateByName;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
