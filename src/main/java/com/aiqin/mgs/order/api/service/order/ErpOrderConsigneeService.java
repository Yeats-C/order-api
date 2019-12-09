package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderConsignee;

public interface ErpOrderConsigneeService {

    void saveOrderConsignee(ErpOrderConsignee po, AuthToken auth);
}
