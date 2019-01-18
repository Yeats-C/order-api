package com.aiqin.mgs.order.api.domain.response.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * StockLockRespVo
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Data
public class StockLockRespVo implements Serializable {
    private static final long serialVersionUID = -34661368734853365L;

    @JsonProperty("lock_num")
    private Integer lockNum;

    @JsonProperty("sku_code")
    private String skuCode;

    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @JsonProperty("warehouse_name")
    private String warehouseName;
}
