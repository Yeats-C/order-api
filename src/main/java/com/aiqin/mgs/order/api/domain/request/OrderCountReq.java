package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2020/1/3 17:16
 * Description:
 */
@Data
public class OrderCountReq {
    @ApiModelProperty("门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty("开始时间")
    @JsonProperty("start_day")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    @ApiModelProperty("结束时间")
    @JsonProperty("end_day")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;
}
