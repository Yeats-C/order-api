package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jinghaibo
 * Date: 2019/11/7 09:52
 * Description:
 */
@AllArgsConstructor
@Getter
@ApiModel("库存释放类型")
public enum PrestorageOrderEnum {
    UNEXTRACTED(0,"未提取"),
    EXTRACTED(1,"已提取");
    private Integer code;

    private String desc;

}
