package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.FrontEndSalesStatisticsDao;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatisticsServiceImpl
 * @Description TODO
 * @Date 2020/2/15 14:31
 */
@Service
@Slf4j
public class FrontEndSalesStatisticsServiceImpl implements FrontEndSalesStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndSalesStatisticsServiceImpl.class);

    @Resource
    private FrontEndSalesStatisticsDao dao;

    @Override
    public List<FrontEndSalesStatistics> selectByCondition(FrontEndSalesStatistics frontEndSalesStatistics) {
        return dao.selectByCondition(frontEndSalesStatistics);
    }

    @Override
    public List<FrontEndSalesStatistics> selectCurrentMonthSalesStatistics() {
        return dao.selectCurrentMonthSalesStatistics();
    }

    @Override
    public Integer deleteSalesStatisticsByMonth(Integer month) {
        if (month == null || month < 0) {
            return -1;
        }
        return dao.deleteSalesStatisticsByMonth(month);
    }

    @Override
    public Integer insertSalesStatisticsList(List<FrontEndSalesStatistics> list) {
        if (list.isEmpty()) {
            return -1;
        }
        return dao.insertSalesStatisticsList(list);
    }

    @Override
    public Integer updateBatchByMonthAndStoreId(List<FrontEndSalesStatistics> list) {
        if (list.isEmpty()) {
            return -1;
        }
        return dao.updateBatchByMonthAndStoreId(list);
    }
}
