package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author jinghaibo
 * Date: 2019/11/25 19:36
 * Description:
 */
@Data
public class UnPayVo extends PagesRequest {

    @ApiModelProperty("门店id")
    @JsonProperty("distributor_id")
    private String distributorId;

    @ApiModelProperty(value="创建开始时间Date类型",example = "2001-01-01 01:01:01")
    @JsonProperty("begin_time")
    private Date beginTime;


    @ApiModelProperty(value="创建结束时间Date类型",example = "2001-01-01 01:01:01")
    @JsonProperty("end_time")
    private Date endTime;

    @ApiModelProperty(value="订单状态")
    @JsonProperty("order_status")
    private Integer orderStatus;
    @JsonProperty("order_status_list")
    private List orderStatusList;
}
