package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.config.properties.UrlPath;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.BridgeOmsService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author jinghaibo
 * Date: 2019/12/6 14:16
 * Description:
 */
@Service
public class BridgeOmsServiceImpl implements BridgeOmsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProjectServiceImpl.class);
    @Resource
    private UrlProperties urlProperties;


    @Override
    public Integer countUseStoreProjectByStoreId(String storeId) {
        try {
            LOGGER.info("通过门店id查询门店在用服务项目个数,请求参数storeId为{}", storeId);
            Map<String, Object> result;
            StringBuilder sb = new StringBuilder();
            sb.append(urlProperties.getOmsApi()).append("/service/store/count/").append(storeId);
            LOGGER.info("通过门店id查询门店在用服务项目个数,请求url为{}", sb);
            HttpClient httpClient = HttpClient.get(sb.toString());
           // HttpClientHelper.getCurrentClient(httpClient);
            result = httpClient.action().result(new TypeReference<Map<String, Object>>() {
            });
            if ("0".equals(result.get(Global.RESULT_CODE))) {
                LOGGER.info("通过门店id查询门店在用服务项目个数成功，storeId为{}", storeId);
                return JsonUtil.fromJson(JsonUtil.toJson(result.get("data")), Integer.class);
            } else {
                LOGGER.info("通过门店id查询门店在用服务项目个数失败，storeId为{}", storeId);
                throw new RuntimeException("统计某个门店的各个服务商品的销量失败");
            }
        } catch (Exception e) {
            LOGGER.error("通过门店id查询门店在用服务项目个数异常", e);
            throw new RuntimeException("统计某个门店的各个服务商品的销量异常");
        }
    }


}
