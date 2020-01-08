package com.aiqin.mgs.order.api.dao.test;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.OrderApiBootApplication;
import com.aiqin.mgs.order.api.dao.RejectRecordDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo3;
import com.aiqin.mgs.order.api.domain.request.RejectRequest;
import com.aiqin.mgs.order.api.domain.response.RejectVoResponse;
import com.aiqin.mgs.order.api.domain.response.ReturnOrderDetailBySearchResponse;
import com.aiqin.mgs.order.api.service.ReturnOrderDetailService;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderApiBootApplication.class)
public class test {

    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;

    @Autowired
    private ReturnOrderDetailService returnOrderDetailService;

    @Test
    public void testReturnOrder(){
        List<ReturnOrderInfo> returnOrderInfoList = returnOrderInfoDao.selectByParames(new OrderListVo3("2020-01-01 00:37:16", "2020-01-03 10:37:16", null, null, null, null, null, null,"14"));
        for (ReturnOrderInfo returnOrderInfo : returnOrderInfoList) {
            System.out.print("=======================================");
            System.out.println(returnOrderInfo.getReturnOrderCode()+"    "+returnOrderInfo.getOrderStoreCode());
        }
    }
    @Test
    public void testReturnOrderService(){
        OrderListVo3 orderListVo3 = new OrderListVo3(null, null, "202001021400004", null, null, null, null, null,"14");
        HttpResponse<List<ReturnOrderDetailBySearchResponse>> listHttpResponse = returnOrderDetailService.searchList(orderListVo3);
        System.out.println(listHttpResponse);
        List<ReturnOrderDetailBySearchResponse> data = listHttpResponse.getData();
        System.out.println(data);
    }
    @Autowired
    private RejectRecordDao rejectRecordDao;

    @Test
    public void testRejectDao(){
        RejectRequest rejectRequest = new RejectRequest("2020-01-01 00:37:16", "2020-01-05 10:37:16", null, null, null, null, null, null,"14");
        List<RejectRecord> rejectRecordList = rejectRecordDao.selectByRequest(rejectRequest);
        System.out.println(rejectRecordList);
    }

    @Autowired
    private RejectRecordService rejectRecordService;

    @Test
    public void testRejectService(){
//        RejectRequest rejectRequest = new RejectRequest("2020-01-01 00:37:16", "2020-01-03 10:37:16", null, null, null, null, null, null);
//        List<RejectResponse> responses = rejectRecordService.selectByRejectRequest(rejectRequest);
//        System.out.println(responses);
        HttpResponse<RejectVoResponse> response = rejectRecordService.searchRejectDetailByRejectCode("202001021400022");
        RejectVoResponse data = response.getData();
        System.out.println(data);
    }


}
