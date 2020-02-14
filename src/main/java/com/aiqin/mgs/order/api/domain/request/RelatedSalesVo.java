package com.aiqin.mgs.order.api.domain.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * description: RelatedSalesVo
 * date: 2020/2/14 11:43
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("关联销售查询")
public class RelatedSalesVo {

    private String salseCategoryId;

    private Integer status;

}
