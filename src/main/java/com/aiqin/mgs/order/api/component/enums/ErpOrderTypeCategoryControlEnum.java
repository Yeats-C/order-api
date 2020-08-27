package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.SelectOptionItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 订单类型节点差异控制枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/18 16:48
 */
@Getter
public enum ErpOrderTypeCategoryControlEnum {

    /***配送	- 普通首单*/
    PROCESS_1(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_2, true, true, false, false, true, false, false, false),
    /***配送	- 首单赠送*/
    PROCESS_2(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_4, true, true, false, false, true, false, false, false),
    /***配送	- 正常补货*/
    PROCESS_3(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_1, true, true, false, false, true, true, false, false),
    /***直送	- 正常补货*/
    PROCESS_4(ErpOrderTypeEnum.DIRECT_SEND, ErpOrderCategoryEnum.ORDER_TYPE_1, true, false, false, false, true, true, false, false),
    /***直送	- 普通首单*/
    PROCESS_5(ErpOrderTypeEnum.DIRECT_SEND, ErpOrderCategoryEnum.ORDER_TYPE_2, true, true, false, false, true, false, false, false),
    /***辅采直送	- 新店货架*/
    PROCESS_6(ErpOrderTypeEnum.ASSIST_PURCHASING, ErpOrderCategoryEnum.ORDER_TYPE_16, true, false, true, true, false, false, false, false),
    /***辅采直送	- 货架补货*/
    PROCESS_7(ErpOrderTypeEnum.ASSIST_PURCHASING, ErpOrderCategoryEnum.ORDER_TYPE_17, true, false, true, true, false, false, false, false),
    /***辅采直送	- 游泳游乐*/
    PROCESS_8(ErpOrderTypeEnum.ASSIST_PURCHASING, ErpOrderCategoryEnum.ORDER_TYPE_172, true, false, true, true, false, false, false, false),
    /***配送	- 批发订货*/
    PROCESS_9(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_51, false, false, false, false, false, false, true, true),
    /***采购直送	- 采购直送*/
    PROCESS_10(ErpOrderTypeEnum.DIRECT_PURCHASE, ErpOrderCategoryEnum.ORDER_TYPE_147, true, false, true, true, true, false, false, false),
    /***采购直送	- 首单赠送*/
    PROCESS_11(ErpOrderTypeEnum.DIRECT_PURCHASE, ErpOrderCategoryEnum.ORDER_TYPE_4, true, false, true, true, true, false, false, false),
    ;

    ErpOrderTypeCategoryControlEnum(ErpOrderTypeEnum orderTypeEnum, ErpOrderCategoryEnum orderCategoryEnum, boolean erpQuery, boolean erpCartCreate, boolean erpRackQuery, boolean erpRackCreate, boolean storeQuery, boolean storeCartCreate,boolean erpWholesaleQuery,boolean erpWholesaleCreate) {
        this.orderTypeEnum = orderTypeEnum;
        this.orderCategoryEnum = orderCategoryEnum;
        this.erpQuery = erpQuery;
        this.erpCartCreate = erpCartCreate;
        this.erpRackQuery = erpRackQuery;
        this.erpRackCreate = erpRackCreate;
        this.storeQuery = storeQuery;
        this.storeCartCreate = storeCartCreate;
        this.erpWholesaleQuery = erpWholesaleQuery;
        this.erpWholesaleCreate = erpWholesaleCreate;
    }

    /***订单类型枚举*/
    private ErpOrderTypeEnum orderTypeEnum;
    /***订单类别枚举*/
    private ErpOrderCategoryEnum orderCategoryEnum;
    /***erp查询销售单列表*/
    private boolean erpQuery;
    /***erp购物车创建*/
    private boolean erpCartCreate;
    /***erp查询货架订单*/
    private boolean erpRackQuery;
    /***erp创建货架订单*/
    private boolean erpRackCreate;
    /***门店查询列表*/
    private boolean storeQuery;
    /***门店购物车创建*/
    private boolean storeCartCreate;
    /***erp批发订单查询*/
    private boolean erpWholesaleQuery;
    /***erp批发订单创建*/
    private boolean erpWholesaleCreate;

    /**
     * 根据订单类型编码和订单类别编码获取订单类型节点差异控制枚举类
     *
     * @param orderTypeCode     订单类型编码
     * @param orderCategoryCode 订单类别编码
     * @return
     */
    public static ErpOrderTypeCategoryControlEnum getEnum(Object orderTypeCode, Object orderCategoryCode) {
        if (orderTypeCode != null && orderCategoryCode != null) {
            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(orderTypeCode.toString());
            ErpOrderCategoryEnum orderCategoryEnum = ErpOrderCategoryEnum.getEnum(orderCategoryCode.toString());
            if (orderTypeEnum != null && orderCategoryEnum != null) {

                for (ErpOrderTypeCategoryControlEnum item :
                        ErpOrderTypeCategoryControlEnum.values()) {
                    if (orderTypeEnum == item.getOrderTypeEnum() && orderCategoryEnum == item.getOrderCategoryEnum()) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 查询订单类型选项列表
     *
     * @param queryType 查询类型
     * @return
     */
    public static List<SelectOptionItem> getOrderTypeSelectOptionList(Integer queryType) {

        List<SelectOptionItem> list = new ArrayList<>();
        List<Integer> containsList = new ArrayList<>();


        for (ErpOrderTypeCategoryControlEnum item :
                ErpOrderTypeCategoryControlEnum.values()) {
            ErpOrderTypeEnum orderTypeEnum = item.getOrderTypeEnum();
            if (containsList.contains(orderTypeEnum.getCode())) {
                continue;
            }
            boolean flag=false;
            if(queryType!=null){
                flag= validateQueryType(queryType, item);
            }else{
                flag=true;
            }
            if (flag) {
                list.add(new SelectOptionItem(orderTypeEnum.getValue(), orderTypeEnum.getDesc()));
                containsList.add(orderTypeEnum.getCode());
            }
        }

        return list;
    }

    /**
     * 查询订单类别选项列表
     *
     * @param queryType 查询类型
     * @param orderType 订单类型
     * @return java.util.List<com.aiqin.mgs.order.api.domain.SelectOptionItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/23 21:24
     */
    public static List<SelectOptionItem> getOrderCategorySelectOptionList(Integer queryType, Integer orderType) {

        List<SelectOptionItem> list = new ArrayList<>();
        List<Integer> containsList = new ArrayList<>();
        ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(orderType);
        if (orderTypeEnum != null) {
            for (ErpOrderTypeCategoryControlEnum item :
                    ErpOrderTypeCategoryControlEnum.values()) {
                ErpOrderCategoryEnum orderCategoryEnum = item.getOrderCategoryEnum();
                if (containsList.contains(orderCategoryEnum.getCode())) {
                    continue;
                }
                if (item.getOrderTypeEnum() != orderTypeEnum) {
                    continue;
                }
                boolean flag = validateQueryType(queryType, item);
                if (flag) {
                    list.add(new SelectOptionItem(orderCategoryEnum.getValue(), orderCategoryEnum.getDesc()));
                    containsList.add(orderCategoryEnum.getCode());
                }
            }
        }else{
            Set<SelectOptionItem> optionItemSet=new HashSet<>();
            for (ErpOrderTypeCategoryControlEnum item :
                    ErpOrderTypeCategoryControlEnum.values()) {
                ErpOrderCategoryEnum orderCategoryEnum = item.getOrderCategoryEnum();
                optionItemSet.add(new SelectOptionItem(orderCategoryEnum.getValue(), orderCategoryEnum.getDesc()));
                containsList.add(orderCategoryEnum.getCode());
            }
            list.addAll(new ArrayList<>(optionItemSet));
        }


        return list;
    }

    /**
     * 根据查询类型返回该类型对应属性的boolean值
     *
     * @param queryType   查询类型
     * @param controlEnum 控制类型枚举
     * @return
     */
    private static boolean validateQueryType(Integer queryType, ErpOrderTypeCategoryControlEnum controlEnum) {
        ErpOrderTypeCategoryQueryTypeEnum queryTypeEnum = ErpOrderTypeCategoryQueryTypeEnum.getEnum(queryType);
        boolean flag = false;
        switch (queryTypeEnum) {
            case ERP_ORDER_LIST_QUERY:
                flag = controlEnum.isErpQuery();
                break;
            case ERP_RACK_ORDER_LIST_QUERY:
                flag = controlEnum.isErpRackQuery();
                break;
            case ERP_CART_ORDER_CREATE:
                flag = controlEnum.isErpCartCreate();
                break;
            case ERP_RACK_ORDER_CREATE:
                flag = controlEnum.isErpRackCreate();
                break;
            case STORE_ORDER_LIST_QUERY:
                flag = controlEnum.isStoreQuery();
                break;
            case STORE_CART_ORDER_CREATE:
                flag = controlEnum.isStoreCartCreate();
                break;
            default:
                ;
        }
        return flag;
    }

}
