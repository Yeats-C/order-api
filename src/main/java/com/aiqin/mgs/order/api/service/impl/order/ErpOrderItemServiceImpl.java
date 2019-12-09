package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.service.order.ErpOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErpOrderItemServiceImpl implements ErpOrderItemService {
    @Override
    public void saveOrderItemList(List<ErpOrderItem> list, AuthToken auth) {

    }
}
