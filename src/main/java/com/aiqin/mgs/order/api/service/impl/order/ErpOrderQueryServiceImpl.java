package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErpOrderQueryServiceImpl implements ErpOrderQueryService {

    @Override
    public ErpOrderInfo getOrderByOrderId(String orderId) {
        return null;
    }

    @Override
    public ErpOrderInfo getOrderByOrderCode(String orderCode) {
        return null;
    }

    @Override
    public List<ErpOrderInfo> selectOrderBySelective(ErpOrderInfo po) {
        return null;
    }

}
