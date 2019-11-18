package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("购物车商品项")
@Data
public class CartOrderInfo extends PagesRequest {

    /**商品ID*/
    private String productId="";

    /***商品名称*/
    private String productName="";

    /***商品数量*/
    private int amount;

    /***商品logo图片*/
    private String logo="";

    /***商品原价*/
    private BigDecimal price;

    /**实付金额总和*/
    private BigDecimal acountActualprice;

    /**应付金额总和*/
    private BigDecimal acountTotalprice;

    /***商品颜色*/
    private String color="";

    /***商品规格*/
    private String productSize="";

    /***活动id*/
    private String activityId="";

    /***活动名称*/
    private String activityName="";

    /***购物车id*/
    private String cartId;

    /***加盟商id*/
    private String franchiseeId;

    /***门店Id*/
    private String storeId;

    /***sku码*/
    private String skuId;

    /***spu码*/
    private String spuId;

    /***创建时间*/
    private Date createTime;

    /***更新时间*/
    private Date updateTime;

    /***配送方式 1:配送 2:直送 3:货架*/
    private Integer productType;

    /***skuId集合*/
    private List<String> skuIds;

    /***门店地址*/
    private String storeAddress;

    /***门店联系人*/
    private String storeContacts;

    /***门店联系人电话*/
    private String storeContactsPhone;

    /***商品添加来源 1:门店 2:erp*/
    private Integer createSource;

    /***本品或者赠品 1:本品 2:赠品*/
    private Integer productGift;

    /***赠品关联本品行cart_id*/
    private String giftParentCartId;

    /***行是否选中 1:true 0:false*/
    private Integer lineCheckStatus;





}
