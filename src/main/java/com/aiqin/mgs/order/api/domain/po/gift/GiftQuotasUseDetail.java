package com.aiqin.mgs.order.api.domain.po.gift;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 兑换赠品积分账户使用明细
 *
 * @author: csf
 * @version: v1.0.0
 * @date 2020/04/03 17:18
 */
@Data
@ApiModel("兑换赠品积分账户使用明细")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GiftQuotasUseDetail {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "赠品额度变化")
    @JsonProperty("change_in_gift_quota")
    private String changeInGiftQuota;

    @ApiModelProperty(value = "相关单据code")
    @JsonProperty("bill_code")
    private String billCode;

    @ApiModelProperty(value = "相关单据链接")
    @JsonProperty("bill_link")
    private String billLink;

    @ApiModelProperty(value = "类型 1 赠品划单 2订单使用 3订单赠送 4过期")
    @JsonProperty("type")
    private Integer type;

    @ApiModelProperty(value = "开始时间【预留字段】")
    @JsonProperty("begin_time")
    private String beginTime;

    @ApiModelProperty(value = "终止时间【预留字段】")
    @JsonProperty("finish_time")
    private String finishTime;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

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

}
