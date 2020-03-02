package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.ReportAreaReturnSituation;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;

import java.util.List;

/**
 * description: ReportAreaReturnSituationService
 * date: 2020/3/2 18:02
 * author: hantao
 * version: 1.0
 */
public interface ReportAreaReturnSituationService {

    List<ReportAreaReturnSituation> getList(ReportAreaReturnSituationVo vo);

    Boolean insertBatch(List<ReportAreaReturnSituation> entity);

}
