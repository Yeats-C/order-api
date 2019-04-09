package com.aiqin.mgs.order.api.domain.statistical;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Createed by sunx on 2019/4/8.<br/>
 */
@Data
@ApiModel("销量数据信息")
public class SkuSales {

    private String skuCode;

    private Integer sales;
}
