package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.SequenceGeneratorDao;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.service.SequenceGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    @Resource
    private SequenceGeneratorDao sequenceGeneratorDao;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String generateOrderCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String curDay = sdf.format(new Date());
        Long sequenceNextVal = sequenceGeneratorDao.getSequenceNextVal(OrderConstant.SEQUENCE_NAME_ORDER_STORE_CODE);
        return curDay + String.format("%06d", sequenceNextVal);
    }
}
