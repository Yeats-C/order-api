package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.dao.OrderPayDao;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderPayInfo;
import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.service.BridgePayService;
import com.aiqin.mgs.order.api.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author jinghaibo
 * Date: 2019/11/12 13:55
 * Description:
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderPayDao orderPayDao;
    @Resource
    private BridgePayService bridgePayService;
    @Override
    public HttpResponse doPay(PayReq vo) {
        checkPayMoney(vo);
        vo.setStoreId(vo.getAiqinMerchantId());
        return bridgePayService.mainSwept(vo);
    }

    private void checkPayMoney(PayReq vo) {
        OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
        orderDetailQuery.setOrderCode(vo.getOrderNo());
        OrderInfo orderInfo = null;
        try {
            orderInfo = orderDao.selecOrderById(orderDetailQuery);

        Assert.notNull(orderInfo,"没有查到对应的订单信息");
        OrderPayInfo info = new OrderPayInfo();
        info.setOrderId(orderInfo.getOrderId());
        List<OrderPayInfo> list = orderPayDao.pay(info);

        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(input -> {
                if ((Objects.isNull(vo.getPayType()) && Objects.isNull(input.getPayWay()))
                        || (Objects.nonNull(input.getPayWay()) && vo.getPayType().equals(input.getPayWay() - 1))) {
                    //判断金额
                    vo.setOrderAmount(Long.valueOf(Optional.ofNullable(input.getPayPrice()).orElse(0)));
                }
            });
        }
        } catch (Exception e) {
            log.error("查询BYorderid-返回订单主数据 {}",e);
        }
    }
}
