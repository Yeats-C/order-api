package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderRefundTypeEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderRefund;
import com.aiqin.mgs.order.api.service.order.ErpOrderRefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ErpOrderRefundServiceImpl implements ErpOrderRefundService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderRefundServiceImpl.class);


    @Override
    public ErpOrderRefund getOrderRefundByRefundId(String payId) {
        return null;
    }

    @Override
    public ErpOrderRefund getOrderRefundByOrderIdAndRefundType(String orderId, ErpOrderRefundTypeEnum refundTypeEnum) {

        return null;
    }

    @Override
    public void saveOrderRefund(ErpOrderRefund po, AuthToken auth) {

    }

    @Override
    public void updateOrderRefundSelective(ErpOrderRefund po, AuthToken auth) {

    }

}
