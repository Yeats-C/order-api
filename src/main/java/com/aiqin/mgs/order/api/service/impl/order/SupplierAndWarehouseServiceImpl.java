package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.service.order.SupplierAndWarehouseService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * description: SupplierAndWarehouseServiceImpl
 * date: 2020/3/20 19:42
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class SupplierAndWarehouseServiceImpl implements SupplierAndWarehouseService {

    @Value("${bridge.url.product-api}")
    private String productHost;

    @Override
    public HttpResponse getSupplierInfo(String supplierCode) {
        log.info("查询供应商信息,入参supplierCode={}",supplierCode);
        //TODO 修改为正确的请求地址
        StringBuilder sb=new StringBuilder(productHost+"/store/getAllStoreCode");
        sb.append("?supplierCode="+supplierCode);
        log.info("调用供应链系统,查询供应商信息,请求url={}", sb.toString());
        HttpClient httpClient = HttpClient.get(sb.toString());
        Map<String, Object> res = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
        log.info("调用供应链系统,查询供应商信息返回结果，result={}", JsonUtil.toJson(res));
        if (res!=null&&StringUtils.isNotBlank(res.get("code").toString()) && "0".equals(String.valueOf(res.get("code")))) {
//            proList = JSONArray.parseArray(JSON.toJSONString(res.get("data")), ProvinceAreaResponse.class);
            log.info("调用供应链系统,查询供应商信息失败");
            return HttpResponse.success();
        } else {
            log.info("调用供应链系统,查询供应商信息失败");
            return HttpResponse.success();
        }
    }

    @Override
    public HttpResponse getWarehouseInfo(String transportCenterCode) {
        log.info("查询仓库信息,入参supplierCode={}",transportCenterCode);
        //TODO 修改为正确的请求地址
        StringBuilder sb=new StringBuilder(productHost+"/store/getAllStoreCode");
        sb.append("?supplierCode="+transportCenterCode);
        log.info("调用供应链系统,查询仓库信息,请求url={}", sb.toString());
        HttpClient httpClient = HttpClient.get(sb.toString());
        Map<String, Object> res = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
        log.info("调用供应链系统,查询仓库信息返回结果，result={}", JsonUtil.toJson(res));
        if (res!=null&&StringUtils.isNotBlank(res.get("code").toString()) && "0".equals(String.valueOf(res.get("code")))) {
//            proList = JSONArray.parseArray(JSON.toJSONString(res.get("data")), ProvinceAreaResponse.class);
            log.info("调用供应链系统,查询仓库信息失败");
            return HttpResponse.success();
        } else {
            log.info("调用供应链系统,查询仓库信息失败");
            return HttpResponse.success();
        }
    }

}
