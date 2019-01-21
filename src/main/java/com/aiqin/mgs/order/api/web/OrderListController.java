package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderList;
import com.aiqin.mgs.order.api.domain.OrderListLogistics;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderSaveRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
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
        return HttpResponse.success(orderListService.save(reqVo));
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
    @PostMapping("/list")
    @ApiOperation(value = "订单列表后台")
    public HttpResponse<PageResData<OrderList>> list(@RequestBody OrderListVo param) {
        log.info("Search  purchasingTarget list:{}", param);
        try {
            return HttpResponse.success(this.orderListService.searchOrderList(param));
        } catch (Exception e) {
            log.error("Get purchasingTarget list failed", e);
            return HttpResponse.failure(MessageId.create(Project.OMS_API, 400, e.getMessage()));
        }
    }


    /**
     * 订单列表后台
     *
     * @param param 请求参数
     * @return 响应结果
     */
    @PostMapping("/list/reception")
    @ApiOperation(value = "订单列表前台")
    public HttpResponse<PageResData<OrderList>> listReception(@RequestBody OrderListVo2 param) {
        log.info("Search  purchasingTarget list:{}", param);
        try {
            return HttpResponse.success(this.orderListService.searchOrderReceptionList(param));
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
     * 添加物流信息
     */
    @PostMapping("add/logistics")
    @ApiOperation("添加物流信息")
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
    @GetMapping("update/Order/status")
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
     * 查询一段时间进货进度量
     */
    @PostMapping("get/stock/value")
    @ApiOperation("查询一段时间进货进度量")
    public HttpResponse<List<OrderStockReVo>> getStockValue(@RequestBody OrderStockVo vo) {
        return HttpResponse.success(orderListService.getStockValue(vo));

    }
}
