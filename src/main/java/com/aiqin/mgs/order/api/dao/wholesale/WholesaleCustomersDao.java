package com.aiqin.mgs.order.api.dao.wholesale;

import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;

import java.util.List;

public interface WholesaleCustomersDao {

    /**
     * 批发客户列表
     * @param wholesaleCustomers
     * @return
     */
    List<WholesaleCustomers> list(WholesaleCustomers  wholesaleCustomers);

    /**
     * 批发客户列表总数
     * @param wholesaleCustomers
     * @return
     */
    int totalCount(WholesaleCustomers  wholesaleCustomers);


}








