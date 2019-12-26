package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * description: AreaReq
 * date: 2019/12/26 15:44
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("退货列表区域查询门店使用")
public class AreaReq implements Serializable {

    private String proCode;

    private String cityCode;



}
