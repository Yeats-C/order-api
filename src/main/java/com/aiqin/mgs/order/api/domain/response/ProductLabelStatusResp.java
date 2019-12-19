package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("爱掌柜首页商品畅销滞销数量返回")
public class ProductLabelStatusResp {

    @ApiModelProperty("畅销数量")
    @JsonProperty("best_member")
    private Integer bestMember;

    @ApiModelProperty("滞销数量")
    @JsonProperty("unsalable_member")
    private Integer unsalableMember;
}
