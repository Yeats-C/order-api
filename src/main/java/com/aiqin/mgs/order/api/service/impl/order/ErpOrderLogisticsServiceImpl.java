package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.order.ErpOrderLogisticsDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.service.order.ErpOrderLogisticsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderLogisticsServiceImpl implements ErpOrderLogisticsService {

    @Resource
    private ErpOrderLogisticsDao erpOrderLogisticsDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderLogistics(ErpOrderLogistics po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderLogisticsDao.insert(po);
    }

    @Override
    public ErpOrderLogistics getOrderLogisticsByPayId(String payId) {
        ErpOrderLogistics orderLogistics = null;
        if (StringUtils.isNotEmpty(payId)) {
            ErpOrderLogistics query = new ErpOrderLogistics();
            query.setPayId(payId);
            List<ErpOrderLogistics> select = erpOrderLogisticsDao.select(query);
            if (select != null && select.size() > 0) {
                orderLogistics = select.get(0);
            }
        }
        return orderLogistics;
    }

    @Override
    public ErpOrderLogistics getOrderLogisticsByLogisticsId(String logisticsId) {
        ErpOrderLogistics orderLogistics = null;
        if (StringUtils.isNotEmpty(logisticsId)) {
            ErpOrderLogistics query = new ErpOrderLogistics();
            query.setLogisticsId(logisticsId);
            List<ErpOrderLogistics> select = erpOrderLogisticsDao.select(query);
            if (select != null && select.size() > 0) {
                orderLogistics = select.get(0);
            }
        }
        return orderLogistics;
    }

    @Override
    public ErpOrderLogistics getOrderLogisticsByLogisticsCode(String logisticsCode) {
        ErpOrderLogistics orderLogistics = null;
        if (StringUtils.isNotEmpty(logisticsCode)) {
            ErpOrderLogistics query = new ErpOrderLogistics();
            query.setLogisticCode(logisticsCode);
            List<ErpOrderLogistics> select = erpOrderLogisticsDao.select(query);
            if (select != null && select.size() > 0) {
                orderLogistics = select.get(0);
            }
        }
        return orderLogistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderLogisticsSelective(ErpOrderLogistics po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderLogisticsDao.updateByPrimaryKeySelective(po);
    }

}
