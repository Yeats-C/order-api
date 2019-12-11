package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum;
import lombok.Data;

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

    /***订单编号*/
    private String orderCode;
    /***物流id*/
    private String logisticsId;
    /***物流单号*/
    private String logisticsCode;
    /***支付状态*/
    private Integer payStatus;
    /***支付状态描述*/
    private String payStatusDesc;
    /***支付流水号*/
    private String payCode;
    /***支付开始时间*/
    private Date payStartTime;
    /***支付完成时间*/
    private Date payEndTime;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
