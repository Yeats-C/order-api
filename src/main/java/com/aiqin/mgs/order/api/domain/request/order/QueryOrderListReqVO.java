package com.aiqin.mgs.order.api.domain.request.order;

import com.aiqin.mgs.order.api.base.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel("订单列表查询请求vo")
public class QueryOrderListReqVO extends PageReq {

    @ApiModelProperty(value = "创建时间从")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDateStart;

    @ApiModelProperty(value = "创建时间到")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDateEnd;

    @ApiModelProperty("订单状态(状态有点多，后面补)")
    private Integer orderStatus;

    @ApiModelProperty("订单类型")
    //@NotNull(message = "订单类型不能为空！(1直送，2配送，3辅采)")
    private Integer orderType;

    @ApiModelProperty("物流中心编码")
    private String transportCenterCode;

    @ApiModelProperty("仓库编码")
    private String warehouseCode;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("是否锁定(0否1是）")
    private Integer beLock;

    @ApiModelProperty("是否是异常订单(0否1是)")
    private Integer beException;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("订单编码(订单号)")
    private String orderCode;

    @ApiModelProperty("公司编码")
    private String companyCode;

}
