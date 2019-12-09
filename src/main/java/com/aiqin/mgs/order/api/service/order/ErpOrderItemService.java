package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;

import java.util.List;

public interface ErpOrderItemService {

    void saveOrderItemList(List<ErpOrderItem> list, AuthToken auth);
}
