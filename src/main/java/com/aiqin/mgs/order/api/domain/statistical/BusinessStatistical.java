package com.aiqin.mgs.order.api.domain.statistical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
@Data
@ApiModel("营业统计数据")
public class BusinessStatistical {

    @JsonProperty("total_paid_price")
    @ApiModelProperty("实收金额/分")
    private Long totalPaidPrice;

    @ApiModelProperty("订单数量")
    private Integer amount;

    @ApiModelProperty("平均消费金额/分")
    @JsonProperty("avg_paid_price")
    private Long avgPaidPrice;

    @ApiModelProperty("日期/年-月")
    @JsonProperty("date_tag")
    private String dateTag;
}
