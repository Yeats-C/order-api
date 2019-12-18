package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单状态枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 16:53
 */
@Getter
public enum ErpOrderStatusEnum {

    /***待支付*/
    ORDER_STATUS_1(1, "1", "待支付",""),
    /***已支付*/
    ORDER_STATUS_2(2, "2", "已支付",""),
    /***订单拆分中*/
    ORDER_STATUS_3(3, "3", "订单拆分中",""),
    /***订单同步中*/
    ORDER_STATUS_4(4, "4", "订单同步中",""),
    /***等待拣货*/
    ORDER_STATUS_6(6, "6", "等待拣货",""),
    /***正在拣货*/
    ORDER_STATUS_7(7, "7", "正在拣货",""),
    /***扫描完成*/
    ORDER_STATUS_8(8, "8", "扫描完成",""),
    /***已发货*/
    ORDER_STATUS_11(11, "11", "已发货",""),
    /***已支付物流费用*/
    ORDER_STATUS_12(12, "12", "已支付物流费用",""),
    /***已签收*/
    ORDER_STATUS_13(13, "13", "已签收",""),
    /***待支付人工确认支付*/
    ORDER_STATUS_92(92, "92", "待支付人工确认支付",""),
    /***已取消人工确认支付*/
    ORDER_STATUS_93(93, "93", "已取消人工确认支付",""),
    /***终止交易失败*/
    ORDER_STATUS_94(94, "94", "终止交易失败",""),
    /***延期收货*/
    ORDER_STATUS_95(95, "95", "延期收货",""),
    /***拒收终止交易*/
    ORDER_STATUS_96(96, "96", "拒收终止交易",""),
    /***缺货终止交易*/
    ORDER_STATUS_97(97, "97", "缺货终止交易",""),
    /***交易异常终止*/
    ORDER_STATUS_98(98, "98", "交易异常终止",""),
    /***取消订单*/
    ORDER_STATUS_99(99, "99", "取消订单","超时未支付成功"),
    /***申请交易终止，已经支付后客户取消*/
    ORDER_STATUS_102(102, "102", "申请交易终止","已经支付后客户取消"),
    /***申请终止交易，等待拣货时客户取消*/
    ORDER_STATUS_106(106, "106", "申请终止交易","等待拣货时客户取消"),
    /***申请终止交易，正在拣货时客户取消*/
    ORDER_STATUS_107(107, "107", "申请终止交易","正在拣货时客户取消"),
    /***申请终止交易，扫描完成后客户取消");*/
    ORDER_STATUS_108(108, "108", "申请终止交易","扫描完成后客户取消");

    private Integer code;
    private String value;
    private String desc;
    private String remark;

    ErpOrderStatusEnum(Integer code, String value, String desc,String remark) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.remark = remark;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpOrderStatusEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpOrderStatusEnum item :
                ErpOrderStatusEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpOrderStatusEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpOrderStatusEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }
    
}
