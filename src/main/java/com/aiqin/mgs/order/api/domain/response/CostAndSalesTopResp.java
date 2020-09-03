package com.aiqin.mgs.order.api.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/9/2 15:32
 * Description:
 */
@Data
public class CostAndSalesTopResp {
    @ApiModelProperty("总记录数")
    private Integer totalCount = 0;

    @ApiModelProperty("分页数据集合")
    private List<CostAndSalesResp> dataList;

    @ApiModelProperty("数据总数")
    private CostAndSalesSumResp costAndSalesSumResp;
}
