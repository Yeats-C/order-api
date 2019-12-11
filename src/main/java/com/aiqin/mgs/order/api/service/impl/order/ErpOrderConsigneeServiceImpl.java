package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.order.ErpOrderConsigneeDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderConsignee;
import com.aiqin.mgs.order.api.service.order.ErpOrderConsigneeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderConsigneeServiceImpl implements ErpOrderConsigneeService {

    @Resource
    private ErpOrderConsigneeDao erpOrderConsigneeDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderConsignee(ErpOrderConsignee po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderConsigneeDao.insert(po);
    }

    @Override
    public ErpOrderConsignee getOrderConsigneeByOrderId(String orderId) {
        ErpOrderConsignee orderConsignee = null;
        if (StringUtils.isNotEmpty(orderId)) {
            ErpOrderConsignee query = new ErpOrderConsignee();
            query.setOrderId(orderId);
            List<ErpOrderConsignee> select = erpOrderConsigneeDao.select(query);
            if (select != null && select.size() > 0) {
                orderConsignee = select.get(0);
            }
        }
        return orderConsignee;
    }
}
