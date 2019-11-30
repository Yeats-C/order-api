package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderStoreOrderProductItemDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem;
import com.aiqin.mgs.order.api.service.ErpOrderProductItemService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErpOrderProductItemServiceImpl implements ErpOrderProductItemService {

    @Resource
    private OrderStoreOrderProductItemDao orderStoreOrderProductItemDao;


    @Override
    public List<OrderStoreOrderProductItem> selectOrderProductListByOrderId(String orderId) {
        List<OrderStoreOrderProductItem> productItemList = new ArrayList<>();
        if (StringUtils.isNotEmpty(orderId)) {
            OrderStoreOrderProductItem productItemQuery = new OrderStoreOrderProductItem();
            productItemQuery.setOrderId(orderId);
            List<OrderStoreOrderProductItem> select = orderStoreOrderProductItemDao.select(productItemQuery);
            productItemList.addAll(select);
        }
        return productItemList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderProductItem(OrderStoreOrderProductItem orderStoreOrderProductItem, AuthToken auth) {
        orderStoreOrderProductItem.setCreateById(auth.getPersonId());
        orderStoreOrderProductItem.setCreateByName(auth.getPersonName());
        orderStoreOrderProductItem.setUpdateById(auth.getPersonId());
        orderStoreOrderProductItem.setUpdateByName(auth.getPersonName());
        Integer insert = orderStoreOrderProductItemDao.insert(orderStoreOrderProductItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderProductItemList(List<OrderStoreOrderProductItem> list, AuthToken auth) {
        if (list != null && list.size() > 0) {
            for (OrderStoreOrderProductItem item :
                    list) {
                item.setCreateById(auth.getPersonId());
                item.setCreateByName(auth.getPersonName());
                item.setUpdateById(auth.getPersonId());
                item.setUpdateByName(auth.getPersonName());
                Integer insert = orderStoreOrderProductItemDao.insert(item);
            }
        }
    }

    @Override
    public void updateOrderProductItem(OrderStoreOrderProductItem orderStoreOrderProductItem, AuthToken auth) {
        orderStoreOrderProductItem.setUpdateById(auth.getPersonId());
        orderStoreOrderProductItem.setUpdateByName(auth.getPersonName());
        orderStoreOrderProductItemDao.updateByPrimaryKeySelective(orderStoreOrderProductItem);
    }

    @Override
    public void updateOrderProductItemList(List<OrderStoreOrderProductItem> list, AuthToken auth) {
        if (list != null && list.size() > 0) {
            for (OrderStoreOrderProductItem item :
                    list) {
                item.setUpdateById(auth.getPersonId());
                item.setUpdateByName(auth.getPersonName());
                Integer insert = orderStoreOrderProductItemDao.updateByPrimaryKeySelective(item);
            }
        }
    }

}
