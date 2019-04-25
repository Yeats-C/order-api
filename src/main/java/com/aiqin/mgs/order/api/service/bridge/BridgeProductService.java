package com.aiqin.mgs.order.api.service.bridge;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.dto.ProductDistributorOrderDTO;
import com.aiqin.mgs.order.api.domain.request.statistical.ProductDistributorOrderRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * Createed by sunx on 2019/4/8.<br/>
 */
@Service
@Slf4j
public class BridgeProductService {

    @Resource
    private UrlProperties urlProperties;

    /**
     * 获取低库存畅缺商品明细信息
     *
     * @param request
     * @return
     */
    public List<ProductDistributorOrderDTO> getProductDistributorOrderDTO(ProductDistributorOrderRequest request) {
        HttpClient httpClient = HttpClient.post(urlProperties.getProductApi() + "/inventory/sold/out/stock").json(request);
        HttpResponse<List<ProductDistributorOrderDTO>> response = httpClient.action().result(new TypeReference<HttpResponse<List<ProductDistributorOrderDTO>>>() {
        });

        if (Objects.nonNull(response) && Objects.equals("0", response.getCode())) {
            return response.getData();
        }
        return Lists.newArrayList();
    }
}
