package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description: AfterReturnOrderSearchVo
 * date: 2019/12/26 17:22
 * author: hantao
 * version: 1.0
 */

@Data
@ApiModel("批发退货单列表搜索项")
public class wholesaleReturnOrderSearchVo implements Serializable {

    @ApiModelProperty("退货单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty("申请开始时间")
    @JsonProperty("apply_start_time")
    private String applyStartTime;

    @ApiModelProperty("申请结束时间")
    @JsonProperty("apply_end_time")
    private String applyEndTime;

    @ApiModelProperty("退货单状态 ")
    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;

//    @ApiModelProperty("地区层级编码对象")
//    @JsonProperty("area_req")
//    private AreaReq areaReq;

    @ApiModelProperty("批发客户名称 ")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty("业务形式 0门店业务 1批发业务 ")
    @JsonProperty("business_form")
    private Integer businessForm;

    @ApiModelProperty(value="省编码")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value="市编码")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="区编码")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value = "每页条数")
    @JsonProperty("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    @JsonProperty("page_no")
    private Integer pageNo;




}
