package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 日志类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/19 20:36
 */
@Getter
public enum ErpLogOperationTypeEnum {

    /***新增*/
    ADD(0, "0", "新增"),
    /***修改*/
    UPDATE(1, "1", "修改"),
    /***删除*/
    DELETE(2, "2", "删除"),
    /***下载*/
    DOWNLOAD(3, "3", "下载"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpLogOperationTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据值获取枚举类
     *
     * @param object
     * @return
     */
    public static ErpLogOperationTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpLogOperationTypeEnum item :
                    ErpLogOperationTypeEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 根据枚举类的值获取枚举的描述
     *
     * @param object
     * @return
     */
    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpLogOperationTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
