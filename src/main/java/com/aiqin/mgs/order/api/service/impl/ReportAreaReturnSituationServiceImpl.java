package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.ReportAreaReturnSituationDao;
import com.aiqin.mgs.order.api.domain.ReportAreaReturnSituation;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import com.aiqin.mgs.order.api.service.ReportAreaReturnSituationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: ReportAreaReturnSituationServiceImpl
 * date: 2020/3/2 18:07
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class ReportAreaReturnSituationServiceImpl implements ReportAreaReturnSituationService {

    @Autowired
    private ReportAreaReturnSituationDao reportAreaReturnSituationDao;

    @Override
    public List<ReportAreaReturnSituation> getList(ReportAreaReturnSituationVo vo) {
        return reportAreaReturnSituationDao.selectList(vo);
    }

    @Override
    public Boolean insertBatch(List<ReportAreaReturnSituation> entity) {
        return reportAreaReturnSituationDao.insertBatch(entity)>0;
    }

}
