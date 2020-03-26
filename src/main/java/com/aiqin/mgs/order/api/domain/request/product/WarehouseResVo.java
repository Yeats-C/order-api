package com.aiqin.mgs.order.api.domain.request.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:库房查看返回实体
 *
 * @Author: Kt.w
 * @Date: 2018/12/25
 * @Version 1.0
 * @since 1.0
 */
@Data
@ApiModel("库房查看返回实体")
public class WarehouseResVo {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("库房编码")
    private String warehouseCode;

    @ApiModelProperty("库房名称")
    private String warehouseName;

    @ApiModelProperty("物流中心编码")
    private String logisticsCenterCode;

    @ApiModelProperty("物流中心名称")
    private String logisticsCenterName;

    @ApiModelProperty("仓库类型名称")
    private String warehouseTypeName;

    @ApiModelProperty("仓库类型编码")
    private Byte warehouseTypeCode;

    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("省编码")
    private String provinceName;

    @ApiModelProperty("市编码")
    private String cityCode;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区编码")
    private String countyCode;

    @ApiModelProperty("区名称")
    private String countyName;

    @ApiModelProperty("详细地址")
    private String detailedAddress;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("启用禁用状态")
    private Byte enable;

    @ApiModelProperty("排序")
    private Integer warehouseOrder;
}
