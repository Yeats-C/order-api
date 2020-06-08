package com.aiqin.mgs.order.api.dao.wholesale;

import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleRule;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 新增批发客户
     * @param wholesaleCustomers
     * @return
     */
    int insert(WholesaleCustomers wholesaleCustomers);

    /**
     * 批量插入批发客户商品权限
     * @param wholesaleRuleList
     * @return
     */
    int bulkInsertionRules(List<WholesaleRule> wholesaleRuleList);

    /**
     * 通过customerCode查询批发客户商品权限
     * @param customerCode
     * @return
     */
    List<WholesaleRule> getWholesaleRuleList(@Param("customerCode") String customerCode);

    /**
     * 修改批发客户
     * @param wholesaleCustomers
     * @return
     */
    int update(WholesaleCustomers wholesaleCustomers);

    /**
     * 清空批发空户的商品权限
     * @param customerCode
     * @return
     */
    int clearRules(String customerCode);
}








