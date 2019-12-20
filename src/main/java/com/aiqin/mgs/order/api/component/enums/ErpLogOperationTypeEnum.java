package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpLogOperationTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpLogOperationTypeEnum item :
                ErpLogOperationTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpLogOperationTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpLogOperationTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
