package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2020/1/14 14:12
 * Description:
 */
@Data
public class MemberSaleRequest {

    @ApiModelProperty("消费金额")
    @JsonProperty("last_sale_amount")
    private Integer lastSaleAmount ;

    @ApiModelProperty("会员ID")
    @JsonProperty("member_id")
    private String memberId;
}
