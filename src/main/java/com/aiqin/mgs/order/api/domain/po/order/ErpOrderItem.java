package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品明细行实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:11
 */
@Data
public class ErpOrderItem {

    /***主键*/
    private Long id;
    /***业务id*/
    private String orderInfoDetailId;
    /***订单id*/
    private String orderStoreId;
    /***订单号*/
    private String orderStoreCode;
    /***spu编码*/
    private String spuCode;
    /***spu名称*/
    private String spuName;
    /***sku编码*/
    private String skuCode;
    /***sku名称*/
    private String skuName;
    /***图片地址*/
    private String pictureUrl;
    /***规格*/
    private String productSpec;
    /***颜色编码*/
    private String colorCode;
    /***颜色名称*/
    private String colorName;
    /***型号*/
    private String modelCode;
    /***单位编码*/
    private String unitCode;
    /***单位名称*/
    private String unitName;
    /***折零系数*/
    private Long zeroDisassemblyCoefficient;
    /***商品类型  0商品 1赠品 2实物返回*/
    private Integer productType;
    /***商品数量*/
    private Long productCount;
    /***商品单价*/
    private BigDecimal productAmount;
    /***含税采购价*/
    private BigDecimal taxPurchaseAmount;
    /***商品总价*/
    private BigDecimal totalProductAmount;
    /***实际商品总价*/
    private BigDecimal actualTotalProductAmount;
    /***优惠分摊总金额*/
    private BigDecimal totalPreferentialAmount;
    /***活动优惠总金额*/
    private BigDecimal totalAcivityAmount;
    /***实收数量（门店）*/
    private Long actualInboundCount;
    /***实际商品数量*/
    private Long actualProductCount;
    /***退货数量*/
    private Long returnProductCount;
    /***税率*/
    private BigDecimal taxRate;
    /***活动编码(多个，隔开）*/
    private String activityCode;
    /***行号*/
    private Long lineCode;
    /***赠品行号*/
    private Long giftLineCode;
    /***公司编码*/
    private String companyCode;
    /***公司名称*/
    private String companyName;
    /***签收数量差异原因*/
    private String signDifferenceReason;

    /***0. 启用   1.禁用*/
    private String useStatus;
    /***创建人编码*/
    private String createById;
    /***创建人名称*/
    private String createByName;
    /***更新人编码*/
    private String updateById;
    /***更新人名称*/
    private String updateByName;
    /***创建时间*/
    private Date createTime;
    /***更新时间*/
    private Date updateTime;
}
