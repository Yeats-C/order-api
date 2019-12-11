package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import lombok.Data;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2019/12/5 11:55
 * Description: 预存订单售后
 */
@Data
public class PrestoragProductAfter {
    private OrderAfterSaleInfo afterSaleSaveVo;
    private List<PrestorageOrderSupplyDetailVo> prestorageOrderSupplyDetailVos;
}
