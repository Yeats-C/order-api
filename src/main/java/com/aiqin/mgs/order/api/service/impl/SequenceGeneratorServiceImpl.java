package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.component.enums.SequenceGeneratorTypeEnum;
import com.aiqin.mgs.order.api.dao.SequenceGeneratorDao;
import com.aiqin.mgs.order.api.domain.SequenceGenerator;
import com.aiqin.mgs.order.api.service.SequenceGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    @Resource
    private SequenceGeneratorDao sequenceGeneratorDao;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String generateOrderCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String curDay = sdf.format(new Date());
        Long sequenceVal = 1L;
        SequenceGenerator query = new SequenceGenerator();
        query.setSequenceName(SequenceGeneratorTypeEnum.ORDER_STORE_CODE.getValue());
        List<SequenceGenerator> select = sequenceGeneratorDao.select(query);
        if (select != null && select.size() > 0) {
            SequenceGenerator sequenceGenerator = select.get(0);
            if (curDay.equals(sequenceGenerator.getCurrentDay())) {
                sequenceVal = sequenceGenerator.getSequenceVal();
            } else {
                sequenceGenerator.setCurrentDay(curDay);
            }
            sequenceGenerator.setSequenceVal(sequenceVal + (sequenceGenerator.getSequenceStep() == null ? 1L : sequenceGenerator.getSequenceStep()));
            sequenceGeneratorDao.updateByPrimaryKeySelective(sequenceGenerator);
        }
        return curDay + String.format("%07d", sequenceVal);
    }
}
