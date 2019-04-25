package com.aiqin.mgs.order.api.domain.response.statistical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Createed by sunx on 2019/4/11.<br/>
 */
@Data
@ApiModel("近10天门店已完成订单情况统计")
public class Last10DaysOrderStatistical {
    
    @ApiModelProperty("日期标签2019-01-01")
    @JsonProperty("date_tag")
    private String dateTag;

    @ApiModelProperty("已完成订单数量")
    @JsonProperty("finish_order_num")
    private Integer finishOrderNum;
}
