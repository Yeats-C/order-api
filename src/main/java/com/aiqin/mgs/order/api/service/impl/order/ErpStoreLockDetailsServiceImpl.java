package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.StoreLockDetailsDao;
import com.aiqin.mgs.order.api.domain.StoreLockDetails;
import com.aiqin.mgs.order.api.service.order.ErpStoreLockDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * description: ErpStoreLockDetailsServiceImpl
 * date: 2020/3/17 15:27
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class ErpStoreLockDetailsServiceImpl implements ErpStoreLockDetailsService {

    @Autowired
    private StoreLockDetailsDao storeLockDetailsDao;

    @Override
    @Transactional
    public void insertStoreLockDetails(List<StoreLockDetails> list) {
        storeLockDetailsDao.insertBatch(list);
    }

}
