package com.aiqin.mgs.order.api.domain.request.statistical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
@Data
@ApiModel("营业数据统计请求实体")
public class BusinessStatisticalRequest {
    @ApiModelProperty("门店")
    @JsonProperty("distributor_id")
    private String distributorId;

    @ApiModelProperty("开始时间，yyyy-MM-dd HH:mm:ss")
    @JsonProperty("start_date")
    private Date startDate;

    @ApiModelProperty("结束时间，yyyy-MM-dd HH:mm:ss")
    @JsonProperty("end_date")
    private Date endDate;

    @ApiModelProperty("分组标识")
    @JsonProperty("group_by_flag")
    private Boolean groupByFlag;
}
