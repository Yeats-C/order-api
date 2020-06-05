package com.aiqin.mgs.order.api.service.wholesale;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;

public interface WholesaleCustomersService {

    /**
     * 批发客户列表
     * @param wholesaleCustomers
     * @return
     */
    HttpResponse<PageResData<WholesaleCustomers>> list(WholesaleCustomers wholesaleCustomers);


}
