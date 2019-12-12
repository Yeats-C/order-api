package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.order.ErpOrderFeeDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.service.order.ErpOrderFeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderFeeServiceImpl implements ErpOrderFeeService {

    @Resource
    private ErpOrderFeeDao erpOrderFeeDao;

    @Override
    public void saveOrderFee(ErpOrderFee po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderFeeDao.insert(po);
    }

    @Override
    public void updateOrderFeeByPrimaryKeySelective(ErpOrderFee po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderFeeDao.updateByPrimaryKeySelective(po);
    }

    @Override
    public ErpOrderFee getOrderFeeByFeeId(String feeId) {
        ErpOrderFee result = null;
        if (StringUtils.isNotEmpty(feeId)) {
            ErpOrderFee query = new ErpOrderFee();
            query.setFeeId(feeId);
            List<ErpOrderFee> select = erpOrderFeeDao.select(query);
            if (select != null && select.size() > 0) {
                result = select.get(0);
            }
        }
        return result;
    }

    @Override
    public ErpOrderFee getOrderFeeByPayId(String payId) {
        ErpOrderFee result = null;
        if (StringUtils.isNotEmpty(payId)) {
            ErpOrderFee query = new ErpOrderFee();
            query.setPayId(payId);
            List<ErpOrderFee> select = erpOrderFeeDao.select(query);
            if (select != null && select.size() > 0) {
                result = select.get(0);
            }
        }
        return result;
    }

    @Override
    public ErpOrderFee getOrderFeeByOrderId(String orderId) {
        ErpOrderFee result = null;
        if (StringUtils.isNotEmpty(orderId)) {
            ErpOrderFee query = new ErpOrderFee();
            query.setOrderId(orderId);
            List<ErpOrderFee> select = erpOrderFeeDao.select(query);
            if (select != null && select.size() > 0) {
                result = select.get(0);
            }
        }
        return result;
    }
}
