package com.aiqin.mgs.order.api.domain;

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

    @ApiModelProperty("值")
    private String value;
    @ApiModelProperty("文本")
    private String desc;

    public SelectOptionItem(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
