package com.aiqin.mgs.order.api.service.test;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.OrderApiBootApplication;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatisticsDetail;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsDetailService;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * FrontEndSaleStatisticsServiceTest
 *
 * @author wangpp25
 * @createTime FrontEndSaleStatisticsServiceTest 单元测试
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderApiBootApplication.class)
public class FrontEndSaleStatisticsDetailServiceTest {


    @Resource
    private FrontEndSalesStatisticsDetailService service;

    @Test
    public void selectYesterdaySalesStatisticsTest () {
        FrontEndSalesStatistics font = new FrontEndSalesStatistics();
        font.setStoreId("1");
        font.setMonth(202002);
        List<FrontEndSalesStatisticsDetail> list = service.selectYesterdaySalesStatistics();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(list.size()+"----------------->"+ list.get(0));
    }
    @Test
    public void insertSalesStatisticsDetailListTest () {

        List<FrontEndSalesStatisticsDetail> selectYesterdaySalesStatistics = service.selectYesterdaySalesStatistics();
        if (selectYesterdaySalesStatistics != null && selectYesterdaySalesStatistics.size() >0) {
            selectYesterdaySalesStatistics.stream().forEach(s->{
                s.setSaleStatisticsDetailId(IdUtil.uuid());
                s.setSaleStatisticsId(IdUtil.uuid());
            });
        }
        service.insertSalesStatisticsDetailList(selectYesterdaySalesStatistics);
        System.out.println();
        System.out.println();
        System.out.println();

    }

    @Test
    public void selectStoreMonthSaleStatisticsByMonthAndStoreIdTest() {
        HttpResponse response = service.selectStoreMonthSaleStatisticsByMonthAndStoreId("2020-02", "ABEC8D65036E5A45DBABCBA413FA56AEA2");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }


}
