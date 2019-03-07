package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.domain.request.stock.StockLockReqVo;
import com.aiqin.mgs.order.api.domain.response.stock.StockLockRespVo;
import com.aiqin.mgs.order.api.service.BridgeStockService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BridgeStockServiceImpl
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Service
public class BridgeStockServiceImpl implements BridgeStockService {
    @Resource
    private UrlProperties urlProperties;

    @Override
    public List<StockLockRespVo> lock(StockLockReqVo reqVo) {
//        HttpClient client = HttpClient.post(urlProperties.getProductApi() + "/stock/lock/merchant").json(JSON.toJSONString(reqVo));
//        HttpResponse<List<StockLockRespVo>> response = client.action().result(new TypeReference<HttpResponse<List<StockLockRespVo>>>() {
//        });
//        Assert.isTrue(Objects.nonNull(response) && Objects.equals("0", response.getCode()), "锁定库存失败");
//        return response.getData();
        List<StockLockRespVo> lockRespVos = Lists.newLinkedList();
        reqVo.getSkuList().forEach(skuReqVo -> {
            StockLockRespVo respVo = new StockLockRespVo();
            respVo.setLockNum(skuReqVo.getNum());
            respVo.setSkuCode(skuReqVo.getSku_code());
            respVo.setTransportCenterCode("TransportCenter1");
            respVo.setTransportCenterName("物流中心1");
            respVo.setWarehouseCode("1022");
            respVo.setWarehouseName("测试301");
            lockRespVos.add(respVo);
        });
        return lockRespVos.stream().filter(vo -> vo.getLockNum() > 0).collect(Collectors.toList());
    }
}
