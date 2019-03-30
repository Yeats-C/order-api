package com.aiqin.mgs.order.api.domain.response.orderlistre;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * FirstOrderTimeRespVo
 *
 * @author zhangtao
 * @createTime 2019-02-21
 * @description
 */
@Data
public class FirstOrderTimeRespVo {

    @JsonProperty("store_id")
    private String storeId;

    @JsonProperty("first_order_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date firstOrderTime;
}
