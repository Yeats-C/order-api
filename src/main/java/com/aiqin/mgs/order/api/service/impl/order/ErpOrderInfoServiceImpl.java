package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.service.order.ErpOrderInfoService;
import org.springframework.stereotype.Service;

@Service
public class ErpOrderInfoServiceImpl implements ErpOrderInfoService {

    @Override
    public void saveOrder(ErpOrderInfo po, AuthToken auth) {

    }

    @Override
    public void saveOrderNoLog(ErpOrderInfo po, AuthToken auth) {

    }

    @Override
    public void updateOrderByPrimaryKeySelective(ErpOrderInfo po, AuthToken auth) {

    }
}
