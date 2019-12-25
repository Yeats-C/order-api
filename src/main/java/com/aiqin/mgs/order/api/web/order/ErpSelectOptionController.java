package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeCategoryControlEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.domain.SelectOptionItem;
import com.aiqin.mgs.order.api.domain.response.order.ErpStoreOrderStatusTabResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/erpSelectOptionController")
@Api(tags = "ERP选项列表")
public class ErpSelectOptionController {

    @GetMapping("/findOrderTypeList")
    @ApiOperation(value = "获取订单类型选项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型 1：运营ERP查询销售单列表； 2：运营ERP查询货架订单列表； 3：运营ERP查询购物车可创建订单类型类别； 4：运营ERP查询货架订单可创建订单类型类别； 5：爱掌柜查询订单列表订单类型类别； 6：爱掌柜查询购物车可创建订单类型类别；", dataType = "int", required = true)
    })
    public HttpResponse<List<SelectOptionItem>> findOrderTypeList(Integer queryType) {
        HttpResponse response = HttpResponse.success();
        try {
            List<SelectOptionItem> list = ErpOrderTypeCategoryControlEnum.getOrderTypeSelectOptionList(queryType);
            response.setData(list);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/findOrderCategoryList")
    @ApiOperation(value = "获取订单类别选项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型 1：运营ERP查询销售单列表； 2：运营ERP查询货架订单列表； 3：运营ERP查询购物车可创建订单类型类别； 4：运营ERP查询货架订单可创建订单类型类别； 5：爱掌柜查询订单列表订单类型类别； 6：爱掌柜查询购物车可创建订单类型类别；", dataType = "int", required = true),
            @ApiImplicitParam(name = "orderType", value = "订单类型", dataType = "int", required = true)
    })
    public HttpResponse<List<SelectOptionItem>> findOrderCategoryList(Integer queryType, Integer orderType) {
        HttpResponse response = HttpResponse.success();
        try {
            List<SelectOptionItem> list = ErpOrderTypeCategoryControlEnum.getOrderCategorySelectOptionList(queryType, orderType);
            response.setData(list);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/findOrderStatusList")
    @ApiOperation(value = "获取订单状态选项列表")
    public HttpResponse<List<SelectOptionItem>> findOrderStatusList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(ErpOrderStatusEnum.getSelectOptionList());
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/findStoreTabOrderStatus")
    @ApiOperation(value = "爱掌柜查询订单列表tab页订单状态")
    public HttpResponse<ErpStoreOrderStatusTabResponse> findStoreTabOrderStatus() {
        HttpResponse response = HttpResponse.success();
        try {
            ErpStoreOrderStatusTabResponse tabStatusResponse = new ErpStoreOrderStatusTabResponse();
            tabStatusResponse.setUnpaidStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
            tabStatusResponse.setWaitReceive(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
            response.setData(tabStatusResponse);
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @GetMapping("/findOrderPaymentStatusList")
    @ApiOperation(value = "获取订单支付状态（二进制）选项列表")
    public HttpResponse<List<SelectOptionItem>> findOrderPaymentStatusList() {
        HttpResponse response = HttpResponse.success();
        try {
            response.setData(StatusEnum.getPaymentSelectOptionList());
        } catch (BusinessException e) {
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

}
