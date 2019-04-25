package com.aiqin.mgs.order.api.domain.request.statistical;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Createed by sunx on 2019/4/8.<br/>
 */
@Data
public class SkuSalesRequest {
    //门店id
    private String distributorId;

    //开始时间
    private Date startDate;

    //结束时间
    private Date endDate;

    //skuCode集合
    private List<String> skuCodes;
}
