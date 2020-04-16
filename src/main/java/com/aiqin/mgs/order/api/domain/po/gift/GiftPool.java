package com.aiqin.mgs.order.api.domain.po.gift;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.domain.request.product.StockBatchRespVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 兑换赠品池明细
 *
 * @author: csf
 * @version: v1.0.0
 * @date 2020/04/04 17:18
 */
@Data
@ApiModel("兑换赠品池明细")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GiftPool extends PagesRequest {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "spu编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "spu名称")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty(value = "爱亲分销价")
    @JsonProperty("price_tax")
    private BigDecimal priceTax;

    @ApiModelProperty(value = "厂商指导价")
    @JsonProperty("manufacturer_guide_price")
    private BigDecimal manufacturerGuidePrice;

    @ApiModelProperty(value = "0. 启用 1.禁用")
    @JsonProperty("use_status")
    private String useStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;

    @ApiModelProperty("库存列表")
    @JsonProperty("stock_resp_vos")
    private List<StockBatchRespVO> stockRespVOS;

    @ApiModelProperty("门店id，爱掌柜使用字段")
    @JsonProperty("store_id")
    private String storeId;

    /***订单类型*/
    @ApiModelProperty(value = "订单类型 1直送 2配送 3货架，爱掌柜使用字段")
    @JsonProperty("product_type")
    private Integer productType;


    @ApiModelProperty("仓库权限IdList【爱掌柜门店权限使用，前端请忽视此字段】")
    @JsonProperty("warehouse_code_list")
    private List<String> warehouseCodeList;

    @ApiModelProperty(value = "交易倍数【爱掌柜使用】")
    @JsonProperty("zero_removal_coefficient")
    private Integer zeroRemovalCoefficient;

    @ApiModelProperty(value = "库存数量【爱掌柜使用】")
    @JsonProperty("stock_num")
    private Integer stockNum;

    @ApiModelProperty(value = "最大订货数【爱掌柜使用】")
    @JsonProperty("max_order_num")
    private Integer maxOrderNum;

    @ApiModelProperty(value = "主图片路径【爱掌柜使用】")
    @JsonProperty("product_picture_path")
    private String productPicturePath;

    @ApiModelProperty(value = "已加入兑换赠品购物车数量【爱掌柜使用】")
    @JsonProperty("cart_num")
    private Integer cartNum;

    @ApiModelProperty(value = "颜色名称【爱掌柜使用】")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty(value = "型号【爱掌柜使用】")
    @JsonProperty("model_number")
    private String modelNumber;

    @ApiModelProperty(value = "规格【爱掌柜使用】")
    @JsonProperty("spec")
    private String spec;
}
