package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 18:00
 */
@Data
public class OrderStoreCart {

    /***主键id*/
    private Long id;
    /***购物车id*/
    private String cartId;
    /***商品id*/
    private String productId;
    /***商品名称*/
    private String productName;
    /***加盟商id*/
    private String franchiseeId;
    /***门店id*/
    private String storeId;
    /***活动id*/
    private String activityId;
    /***活动名称*/
    private String activityName;
    /***logo图片*/
    private String logo;
    /***商品数量*/
    private Integer amount;
    /***商品价格*/
    private BigDecimal price;
    /***商品颜色*/
    private String color;
    /***商品规格*/
    private String productSize;
    /***sku码*/
    private String skuId;
    /***spu码*/
    private String spuId;
    /***商品类型 枚举 CartProductTypeEnum*/
    private Integer productType;
    /***创建时间*/
    private Date createTime;
    /***创建者id*/
    private String createById;
    /***创建者名称*/
    private String createByName;
    /***修改时间*/
    private Date updateTime;
    /***修改者id*/
    private String updateById;
    /***修改者名称*/
    private String updateByName;

    /***创建来源 爱掌柜/ERP 枚举 CartProductCreateSourceEnum */
    private Integer createSource;
    /***本品和赠品 TODO 如果购物车不存赠品行可以从数据库删除该字段*/
    private Integer productGift;
    /***赠品行关联本品行cartId TODO 如果购物车不存赠品行可以从数据库删除该字段*/
    private String giftParentCartId;
    /***行勾选状态*/
    private Integer lineCheckStatus;
}
