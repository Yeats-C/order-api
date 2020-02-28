package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品标签信息")
public class TagInfo {

    @ApiModelProperty(value = "标签编码")
    @JsonProperty("tag_code")
    private String tagCode;

    @ApiModelProperty(value = "标签名称")
    @JsonProperty("tag_name")
    private String tagName;

}
