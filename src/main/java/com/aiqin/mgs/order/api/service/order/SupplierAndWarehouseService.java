package com.aiqin.mgs.order.api.service.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;

/**
 * description: SupplierAndWarehouseService
 * date: 2020/3/20 19:40
 * author: hantao
 * version: 1.0
 */
public interface SupplierAndWarehouseService {

    /**
     * 查询供应商信息
     * @param supplierCode
     * @return
     */
    HttpResponse getSupplierInfo(String supplierCode);

    /**
     * 查询仓库信息
     * @param transportCenterCode
     * @return
     */
    HttpResponse getWarehouseInfo(String transportCenterCode);

}
