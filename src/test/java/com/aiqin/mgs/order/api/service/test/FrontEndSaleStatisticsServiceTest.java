package com.aiqin.mgs.order.api.service.test;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.OrderApiBootApplication;
import com.aiqin.mgs.order.api.dao.CartOrderDao;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderReqVo;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.service.CartOrderService;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsService;
import com.aiqin.mgs.order.api.service.OrderListService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertNull;

/**
 * FrontEndSaleStatisticsServiceTest
 *
 * @author wangpp25
 * @createTime FrontEndSaleStatisticsServiceTest 单元测试
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderApiBootApplication.class)
public class FrontEndSaleStatisticsServiceTest {


    @Resource
    private FrontEndSalesStatisticsService service;

    @Test
    public void selectByConditionTest () {
        FrontEndSalesStatistics font = new FrontEndSalesStatistics();
        font.setStoreId("1");
        font.setMonth(202002);
        List<FrontEndSalesStatistics> frontEndSalesStatistics = service.selectByCondition(font);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(frontEndSalesStatistics.get(0)+"----------------->"+frontEndSalesStatistics.size());
    }

    @Test
    public void selectCurrentMonthSalesStatisticsTest() {
        List<FrontEndSalesStatistics> frontEndSalesStatistics = service.selectCurrentMonthSalesStatistics();
        for (FrontEndSalesStatistics frontEndSalesStatistic : frontEndSalesStatistics) {
            System.out.println(frontEndSalesStatistic+"===============");
        }
    }

    @Test
    public void insertSalesStatisticsListTest() {
        List<FrontEndSalesStatistics> list = new ArrayList<>();
        Integer count = 0;
        for (int i = 0; i < 10; i++) {
            FrontEndSalesStatistics entity = new FrontEndSalesStatistics();
            entity.setSaleStatisticsId(IdUtil.uuid());
            entity.setStoreId(String.valueOf(count++));
            entity.setSkuCode(UUID.randomUUID().toString());
            entity.setOrderDetailId(UUID.randomUUID().toString());
            entity.setMonth(202002);
            entity.setSkuCode("123");
            list.add(entity);
        }

        service.insertSalesStatisticsList(list);
    }
    @Test
    public void deleteSalesStatisticsByMonthAndStoreIdListTest() {
        service.deleteSalesStatisticsByMonth(202002);
    }

   /* @Test
    public void updateBatchByMonthAndStoreIdTest() {
        List<FrontEndSalesStatistics> list = new ArrayList<>();
        Integer count = 0;

        for (int i = 0; i < 10; i++) {
            FrontEndSalesStatistics entity = new FrontEndSalesStatistics();
            entity.setSaleStatisticsId(IdUtil.uuid());
            entity.setStoreId(String.valueOf(count++));
            entity.setSkuCode(UUID.randomUUID().toString());
            entity.setOrderDetailId(UUID.randomUUID().toString());
            entity.setMonth(202002);
            entity.setSkuCode("234");
            list.add(entity);
        }

        service.updateBatchByMonthAndStoreId(list);
    }*/

}
