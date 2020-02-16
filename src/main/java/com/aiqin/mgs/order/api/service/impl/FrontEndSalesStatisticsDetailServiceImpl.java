package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.FrontEndSalesStatisticsDao;
import com.aiqin.mgs.order.api.dao.FrontEndSalesStatisticsDetailDao;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatisticsDetail;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsDetailService;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsService;
import com.aiqin.mgs.order.api.util.DateUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatisticsServiceImpl
 * @Description
 * @Date 2020/2/15 14:31
 */
@Service
@Slf4j
public class FrontEndSalesStatisticsDetailServiceImpl implements FrontEndSalesStatisticsDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndSalesStatisticsDetailServiceImpl.class);

    @Resource
    private FrontEndSalesStatisticsDetailDao dao;

    @Override
    public List<FrontEndSalesStatisticsDetail> selectYesterdaySalesStatistics() {

        return dao.selectYesterdaySalesStatistics();
    }

    @Override
    public Integer insertSalesStatisticsDetailList(List<FrontEndSalesStatisticsDetail> list) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        return dao.insertSalesStatisticsDetailList(list);
    }

    @Override
    public HttpResponse selectStoreMonthSaleStatisticsByMonthAndStoreId(String month, String storeId) {
        if (StringUtils.isBlank(storeId) || StringUtils.isBlank(month)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        Date specifiedMonth = null;
        try {
            specifiedMonth = sdf1.parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer formatMonthFirst = Integer.parseInt(sdf0.format(specifiedMonth)+"01");
        Integer formatMonthEnd = Integer.parseInt(sdf0.format(specifiedMonth)+"31");

        List<FrontEndSalesStatistics> frontEndSalesStatistics =
                dao.selectStoreMonthSaleStatisticsByMonthAndStoreId(formatMonthFirst,formatMonthEnd, storeId);

        return HttpResponse.successGenerics(frontEndSalesStatistics);
    }
}
