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

    /**
     * 新增批发客户
     * @param wholesaleCustomers
     * @return
     */
    HttpResponse insert(WholesaleCustomers wholesaleCustomers);

    /**
     * 通过customerCode查询批发客户
     * @param customerCode
     * @return
     */
    HttpResponse<WholesaleCustomers> getCustomerByCode(String customerCode);

    /**
     * 修改批发客户
     * @param wholesaleCustomers
     * @return
     */
    HttpResponse update(WholesaleCustomers wholesaleCustomers);

    /**
     * 通过名称或者账户查询批发客户
     * @param parameter
     * @return
     */
    HttpResponse<WholesaleCustomers> getCustomerByNameOrAccount(String parameter);
}
