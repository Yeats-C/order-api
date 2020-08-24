package com.aiqin.mgs.order.api.domain.response.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @author wanghao
 */
@Data
@ApiModel("日结报表响应实体")
public class ReportForDayResponse {

    @ApiModelProperty("现金收款")
    private Long xianJinGet = 0L;

    @ApiModelProperty("微信收款")
    private Long weiXinGet = 0L;

    @ApiModelProperty("支付宝收款")
    private Long zhiFuBaoGet = 0L;

    @ApiModelProperty("积分收款")
    private Long jiFenGet = 0L;

    @ApiModelProperty("现金退款")
    private Long xianJinReturn = 0L;

    @ApiModelProperty("微信退款")
    private Long weiXinReturn = 0L;

    @ApiModelProperty("支付宝退款")
    private Long zhiFuBaoReturn = 0L;

    @ApiModelProperty("积分退款")
    private Long jiFenReturn = 0L;

    @ApiModelProperty("现金实际收款")
    private Long xianJinActualGet = 0L;

    @ApiModelProperty("微信实际收款")
    private Long weiXinActualGet = 0L;

    @ApiModelProperty("支付宝实际收款")
    private Long zhiFuBaoActualGet = 0L;

    @ApiModelProperty("积分实际收款")
    private Long jiFenActualGet = 0L;

    @ApiModelProperty("收银员id")
    private String cashierId;

    @ApiModelProperty("收银员名字")
    private String cashierName;

    @ApiModelProperty("销售单数")
    private Long saleCount = 0L;

    @ApiModelProperty("退货单数")
    private Long returnCount = 0L;

    @ApiModelProperty("充值单数")
    private Long rechargeSale = 0L;

    @ApiModelProperty("收款合计")
    private Long collectionTotal = 0L;

    @ApiModelProperty("退款合计")
    private Long returnTotal = 0L;

    @ApiModelProperty("应交总金额")
    private Long total = 0L;

    @Transient
    private Integer payType;

    @Transient
    private Long price;

}
