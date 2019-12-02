package com.aiqin.mgs.order.api.domain;

import lombok.Data;

/**
 * 枚举类对象
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/27 11:00
 */
@Data
public class EnumItemInfo {

    private Integer code;
    private String value;
    private String desc;

    public EnumItemInfo(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
