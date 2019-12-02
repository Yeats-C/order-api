package com.aiqin.mgs.order.api.domain;

import lombok.Data;

/**
 * 门店信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 15:57
 */
@Data
public class StoreInfo {

    /***门店id*/
    private String storeId;
    /***门店code*/
    private String storeCode;
    /***门店名称*/
    private String storeName;
    /***加盟商id*/
    private String franchiseeId;
    /***加盟商编码*/
    private String franchiseeCode;
    /***加盟商名称*/
    private String franchiseeName;
    /***门店联系人*/
    private String contacts;
    /***门店联系人电话*/
    private String contactsPhone;
    /***地址*/
    private String address;

    //TODO 字段待补充

}
