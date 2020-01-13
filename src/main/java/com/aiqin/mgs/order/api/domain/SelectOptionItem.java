package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 选项列表选项
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/23 11:30
 */
@Data
public class SelectOptionItem {

    @ApiModelProperty(value = "选项值")
    @JsonProperty("value")
    private String value;

    @ApiModelProperty(value = "选项文本")
    @JsonProperty("desc")
    private String desc;

    public SelectOptionItem(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
