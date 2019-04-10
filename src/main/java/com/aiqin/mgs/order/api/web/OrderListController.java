package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderListLogistics;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.response.orderlistre.FirstOrderTimeRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderSaveRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
import com.aiqin.mgs.order.api.domain.response.statistical.StatisticalPurchaseAmount;
import com.aiqin.mgs.order.api.service.OrderListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-04 15:25
 */
@RestController
@Api(tags = "订单B控制器")
@RequestMapping("/order/list")
@Slf4j
public class OrderListController {
    @Autowired
    private OrderListService orderListService;

    /**
     * 订单新增
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/save")
    public HttpResponse<OrderSaveRespVo> save(@RequestBody OrderReqVo reqVo) {
        try {
            return HttpResponse.success(orderListService.save(reqVo));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    @ApiOperation("保存订单（只保存，不支付也不锁库和拆单）")
    @PostMapping("/saveOrder")
    public HttpResponse<Boolean> saveOrder(@RequestBody OrderReqVo reqVo) {
        try {
            return new HttpResponse<>(orderListService.saveOrder(reqVo));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }
//    /**
//     * 订单新增
//     */
//    @PostMapping("/add")
//    @ApiOperation(value = "订单新增")
//    public HttpResponse<List<String>> add(@RequestBody List<OrderListDetailsVo> param) {
//          return HttpResponse.success(orderListService.add(param));
//    }

    /**
     * 订单列表后台
     *
     * @param param 请求参数
     * @return 响应结果
     */
//    @PostMapping("/list")
//    @ApiOperation(value = "订单列表后台(过时)")
//    public HttpResponse<PageResData<OrderList>> list(@RequestBody OrderListVo param) {
//        log.info("Search  purchasingTarget list:{}", param);
//        try {
//            return HttpResponse.success(this.orderListService.searchOrderList(param));
//        } catch (Exception e) {
//            log.error("Get purchasingTarget list failed", e);
//            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
//        }
//    }


    /**
     * 订单列表前台
     *
     * @param param 请求参数
     * @return 响应结果
     */
//    @PostMapping("/list/reception")
//    @ApiOperation(value = "订单列表前台(过时)")
//    public HttpResponse<PageResData<OrderList>> listReception(@RequestBody OrderListVo2 param) {
//        log.info("Search  purchasingTarget list:{}", param);
//        try {
//            return HttpResponse.success(this.orderListService.searchOrderReceptionList(param));
//        } catch (Exception e) {
//            log.error("Get purchasingTarget list failed", e);
//            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
//        }
//    }

    /**
     * 订单列表后台
     *
     * @param param 请求参数
     * @return 响应结果
     */
    @PostMapping("/list/father")
    @ApiOperation(value = "订单列表后台(包含父订单)")
    public HttpResponse<PageResData<OrderListFather>> listFather(@RequestBody OrderListVo param) {
        log.info("Search  purchasingTarget list:{}", param);
        try {
            return HttpResponse.success(this.orderListService.searchOrderListFather(param));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    /**
     * 订单列表前台
     *
     * @param param 请求参数
     * @return 响应结果
     */
    @PostMapping("/list/reception/father")
    @ApiOperation(value = "订单列表前台(包含父订单)")
    public HttpResponse<PageResData<OrderListFather>> listReceptionFather(@RequestBody OrderListVo2 param) {
        log.info("Search  purchasingTarget list:{}", param);
        try {
            return HttpResponse.success(this.orderListService.searchOrderReceptionListFather(param));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    /**
     * 订单列表前台
     *
     * @param param 请求参数
     * @return 响应结果
     */
    @PostMapping("/list/reception/father/product")
    @ApiOperation(value = "订单列表前台(包含父订单包含商品)")
    public HttpResponse<PageResData<OrderListFather>> listReceptionFatherProduct(@RequestBody OrderListVo2 param) {
        log.info("Search  purchasingTarget list:{}", param);
        try {
            return HttpResponse.success(this.orderListService.searchOrderReceptionListFatherProduct(param));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }


    /**
     * 获取订单信息
     */

    @GetMapping("/get/orderbycode")
    @ApiOperation(value = "获取订单信息")
    public HttpResponse<OrderListDetailsVo> getOrderByCode(@ApiParam("订单code") @RequestParam("code") String code) {
        return HttpResponse.success(orderListService.getOrderByCode(code));
    }

    /**
     * 获取通过父订单code获取所有子订单信息
     */

    @GetMapping("/get/orderbycode/father")
    @ApiOperation(value = "获取通过父订单code获取所有子订单信息")
    public HttpResponse<List<OrderListDetailsVo>> getOrderByCodeFather(@ApiParam("父订单code") @RequestParam("code") String code) {
        return HttpResponse.success(orderListService.getOrderByCodeFather(code));
    }

    /**
     * 添加物流信息
     */
//    @PostMapping("add/logistics")
//    @ApiOperation("添加物流信息")
    public HttpResponse<Boolean> addLogistics(@RequestBody OrderListLogistics param) {
        log.info("Search  purchasingTarget list:{}", param);
        try {
            return HttpResponse.success(this.orderListService.addLogistics(param));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    /**
     * 修改订单状态
     */
    @GetMapping("update/order/status")
    @ApiOperation("修改订单状态")
    public HttpResponse<Boolean> updateOrderStatus(@ApiParam("订单code") @RequestParam("code") String code, @ApiParam("订单状态") @RequestParam("status") Integer status) {
        log.info("Search  purchasingTarget list:{}", code, status);
        try {
            return HttpResponse.success(this.orderListService.updateOrderStatus(code, status));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    /**
     * 修改订单状态
     */
    @PostMapping("update/order/status/payment")
    @ApiOperation("修改订单状态支付专用接口")
    public HttpResponse<Boolean> updateOrderStatusPayment(@RequestBody OrderStatusPayment vo) {
        log.info("Search  purchasingTarget list:{}", vo);
        try {
            return HttpResponse.success(this.orderListService.updateOrderStatusPayment(vo));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    /**
     * 修改订单状态
     */
    @PostMapping("update/order/status/deliver")
    @ApiOperation("修改订单为发货状态")
    public HttpResponse<Boolean> updateOrderStatusDeliver(@RequestBody DeliverVo vo) {
        try {
            return HttpResponse.success(this.orderListService.updateOrderStatusDeliver(vo));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    /**
     * 修改订单状态
     */
    @GetMapping("update/order/status/receiving")
    @ApiOperation("修改订单为收货状态")
    public HttpResponse<Boolean> updateOrderStatusReceiving(@ApiParam("订单code") @RequestParam("code") String code, @ApiParam("执行人") @RequestParam("name") String name) {
        try {
            return HttpResponse.success(this.orderListService.updateOrderStatusReceiving(code, name));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    /**
     * 修改订单状态
     */
//    @PostMapping("update/order/actual/deliver")
//    @ApiOperation("添加商品实发数量")
//    public HttpResponse<Boolean> updateOrderActualDeliver(@RequestBody List<ActualDeliverVo> actualDeliverVos) {
//
//        try {
//            return HttpResponse.success(this.orderListService.updateOrderActualDeliver(actualDeliverVos));
//        } catch (Exception e) {
//            log.error("Get purchasingTarget list failed", e);
//            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
//        }
//    }


    /**
     * 修改订单支付状态为已退款
     */
    @GetMapping("update/order/refund")
    @ApiOperation("修改订单支付状态为已退款")
    public HttpResponse<Boolean> updateOrderRefund(@ApiParam("订单code") @RequestParam("code") String code) {
        log.info("Search  purchasingTarget list:{}", code);
        try {
            return HttpResponse.success(this.orderListService.updateOrderRefund(code));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }


    /**
     * 查询一段时间进货进度量
     */
    @PostMapping("get/stock/value")
    @ApiOperation("查询一段时间进货进度量")
    public HttpResponse<List<OrderStockReVo>> getStockValue(@RequestBody OrderStockVo vo) {
        return HttpResponse.success(orderListService.getStockValue(vo));

    }

    @GetMapping("first/order/time")
    @ApiOperation("查询门店首单时间")
    public HttpResponse<List<FirstOrderTimeRespVo>> selectFirstOrderTime(@RequestParam("store_ids") List<String> storeIds) {
        return HttpResponse.success(orderListService.selectFirstOrderTime(storeIds));
    }

    @PostMapping("update/product/return/num")
    @ApiOperation("修改订单商品退货数量")
    public HttpResponse<Boolean> updateProductReturnNum(@RequestBody UpdateProductReturnNumReqVo reqVo) {
        try {
            return new HttpResponse<>(orderListService.updateProductReturnNum(reqVo));
        } catch (Exception e) {
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }

    @GetMapping("/purchase/statistical")
    @ApiOperation("进货额统计数据")
    public HttpResponse<StatisticalPurchaseAmount> getStatisticalPurchaseAmount(@RequestParam("store_id") String storeId) {
        log.info("/order/list/purchase/statistical [{}]", storeId);
        return HttpResponse.success(orderListService.getStatisticalPurchaseAmount(storeId));
    }
}
