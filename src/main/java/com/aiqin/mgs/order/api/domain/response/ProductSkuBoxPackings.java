package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * SKU整包装信息返回
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/6 14:15
 */
@Data
public class ProductSkuBoxPackings {

    /***毛重*/
    @JsonProperty("boxGrossWeight")
    private BigDecimal boxGrossWeight;

    /***箱子体积*/
    @JsonProperty("boxVolume")
    private BigDecimal boxVolume;

}
