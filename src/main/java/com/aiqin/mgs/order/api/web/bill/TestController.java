package com.aiqin.mgs.order.api.web.bill;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.order.*;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderItemSplitGroupResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/testController")
public class TestController {

    @Resource
    private ErpOrderCreateService erpOrderCreateService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;

    @Resource
    private ErpOrderInfoService erpOrderInfoService;

    @Resource
    private ErpOrderDeliverService erpOrderDeliverService;

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderPayService erpOrderPayService;


    @GetMapping("/test1")
    public HttpResponse test1(@RequestParam  String orderStoreCode) {
        HttpResponse response = HttpResponse.success();
        try {

            AuthToken auth = new AuthToken();
            auth.setPersonId("123456");
            auth.setPersonName("1234567890");
            erpOrderPayService.orderPaySuccessMethodGroup(orderStoreCode, auth);

        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/test2")
    @ApiOperation(value = "发货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_code", value = "订单号", dataType = "String", required = true)
    })
    public HttpResponse test2(@RequestParam("order_code") String orderCode) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderInfo order = erpOrderQueryService.getOrderAndItemByOrderCode(orderCode);

            List<ErpOrderDeliverItemRequest> itemList = new ArrayList<>();
            for (ErpOrderItem item :
                    order.getItemList()) {
                ErpOrderDeliverItemRequest itemRequest = new ErpOrderDeliverItemRequest();
                itemRequest.setLineCode(item.getLineCode());
                itemRequest.setActualProductCount(item.getProductCount());
                itemList.add(itemRequest);
            }
            ErpOrderDeliverRequest erpOrderDeliverRequest = new ErpOrderDeliverRequest();
            erpOrderDeliverRequest.setOrderCode(order.getOrderStoreCode());
            erpOrderDeliverRequest.setDeliveryTime(new Date());
            erpOrderDeliverRequest.setPersonId(order.getCreateById());
            erpOrderDeliverRequest.setPersonName(order.getCreateByName());
            erpOrderDeliverRequest.setItemList(itemList);
            erpOrderDeliverService.orderDeliver(erpOrderDeliverRequest);

            ErpOrderTransportLogisticsRequest erpOrderTransportLogisticsRequest = new ErpOrderTransportLogisticsRequest();
            erpOrderTransportLogisticsRequest.setLogisticsCentreCode("1234");
            erpOrderTransportLogisticsRequest.setLogisticsCentreName("顺丰物流");
            erpOrderTransportLogisticsRequest.setSendRepertoryCode("1001");
            erpOrderTransportLogisticsRequest.setSendRepertoryName("华东配送中心");
            erpOrderTransportLogisticsRequest.setLogisticsFee(new BigDecimal(100));
            erpOrderTransportLogisticsRequest.setLogisticsCode(System.currentTimeMillis() + "");
            List<String> orderCodeList = new ArrayList<>();
            orderCodeList.add(order.getOrderStoreCode());
            ErpOrderTransportRequest erpOrderTransportRequest = new ErpOrderTransportRequest();
            erpOrderTransportRequest.setPersonName(order.getCreateByName());
            erpOrderTransportRequest.setPersonId(order.getCreateById());
            erpOrderTransportRequest.setDistributionModeCode("1");
            erpOrderTransportRequest.setDistributionModeName("配送方式1");
            erpOrderTransportRequest.setTransportStatus(0);
            erpOrderTransportRequest.setTransportTime(new Date());
            erpOrderTransportRequest.setOrderCodeList(orderCodeList);
            erpOrderTransportRequest.setLogistics(erpOrderTransportLogisticsRequest);
            erpOrderDeliverService.orderTransport(erpOrderTransportRequest);

        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/test3")
    @ApiOperation(value = "签收")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order_code", value = "订单号", dataType = "String", required = true)
    })
    public HttpResponse test3(@RequestParam("order_code") String orderCode) {
        HttpResponse response = HttpResponse.success();
        try {
            ErpOrderInfo order = erpOrderQueryService.getOrderAndItemByOrderCode(orderCode);

            List<ErpOrderProductItemRequest> itemList = new ArrayList<>();
            for (ErpOrderItem item :
                    order.getItemList()) {
                ErpOrderProductItemRequest itemRequest = new ErpOrderProductItemRequest();
                itemRequest.setLineCode(item.getLineCode());
                itemRequest.setActualInboundCount(item.getActualProductCount());
                itemRequest.setSignDifferenceReason("签收测试");
                itemList.add(itemRequest);
            }
            ErpOrderSignRequest erpOrderSignRequest = new ErpOrderSignRequest();
            erpOrderSignRequest.setOrderCode(order.getOrderStoreCode());
            erpOrderSignRequest.setPersonId(order.getCreateById());
            erpOrderSignRequest.setPersonName(order.getCreateByName());
            erpOrderSignRequest.setItemList(itemList);
            erpOrderInfoService.orderSign(erpOrderSignRequest);

        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }
}
