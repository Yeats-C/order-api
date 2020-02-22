package com.aiqin.mgs.order.api.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel("首单报表的查询销售金额信息")
public class FirstReportStoreAndOrderRerequest {

    @ApiModelProperty(value = "订单类型编码")
    private String order_type_code;

    @ApiModelProperty(value = "订单类别编码")
    private String order_category_code;

    @ApiModelProperty(value = "门店id集合")
    private List<String> records;

    @ApiModelProperty(value = "统计时间")
    private String countTime;

}
