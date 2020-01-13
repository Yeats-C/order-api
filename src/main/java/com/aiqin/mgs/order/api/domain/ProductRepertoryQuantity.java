package com.aiqin.mgs.order.api.domain;

import lombok.Data;

/**
 * 商品仓库库存信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/30 15:27
 */
@Data
public class ProductRepertoryQuantity {

    /***仓库编码*/
    private String repertoryCode;
    /***仓库名称*/
    private String repertoryName;
    /***库存数量*/
    private Integer quantity;
}
