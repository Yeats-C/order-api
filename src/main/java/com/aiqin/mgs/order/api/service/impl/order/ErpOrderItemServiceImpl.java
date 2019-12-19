package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.service.order.ErpOrderItemService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErpOrderItemServiceImpl implements ErpOrderItemService {

    @Resource
    private ErpOrderItemDao erpOrderItemDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderItem(ErpOrderItem po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderItemDao.insert(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderItemList(List<ErpOrderItem> list, AuthToken auth) {
        if (list != null && list.size() > 0) {
            for (ErpOrderItem item :
                    list) {
                this.saveOrderItem(item, auth);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderItem(ErpOrderItem po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderItemDao.updateByPrimaryKeySelective(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderItemList(List<ErpOrderItem> list, AuthToken auth) {
        if (list != null && list.size() > 0) {
            for (ErpOrderItem item :
                    list) {
                this.updateOrderItem(item, auth);
            }
        }
    }

    @Override
    public List<ErpOrderItem> selectOrderItemListByOrderId(String orderId) {
        List<ErpOrderItem> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(orderId)) {
            ErpOrderItem query = new ErpOrderItem();
            query.setOrderStoreId(orderId);
            list = erpOrderItemDao.select(query);
        }
        return list;
    }

}
