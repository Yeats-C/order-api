package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.service.ReduceAndAssetRequest;
import com.aiqin.mgs.order.api.domain.request.service.ReduceDetailRequest;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectAsset;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectReduceDetail;
import com.aiqin.mgs.order.api.service.ServiceProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author jinghaibo
 * Date: 2019/12/5 15:55
 * Description: 服务订单，服务订单统计
 */
@RestController
@RequestMapping("/service")
@Api(tags = "服务管理")
public class ServiceProjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProjectController.class);
    @Resource
    private ServiceProjectService serviceProjectService;




    @ApiOperation("添加用户服务项目资产信息")
    @PostMapping("/asset")
    public HttpResponse insertServiceProjectAsset(@RequestBody ServiceProjectAsset serviceProjectAsset) {
        LOGGER.info("添加用户服务项目资产信息......");
        return serviceProjectService.insertServiceProjectAsset(serviceProjectAsset);
    }

    @ApiOperation("批量添加用户的服务项目资产信息")
    @PostMapping("/asset/list")
    public HttpResponse insertList(@RequestBody List<ServiceProjectAsset> serviceProjectAssetList) {
        LOGGER.info("添加用户服务项目资产信息......");
        return serviceProjectService.insertList(serviceProjectAssetList);
    }

    @ApiOperation("更新用户服务商品资产信息")
    @PutMapping("/asset/info")
    public HttpResponse updateSource(@RequestBody @Valid ServiceProjectAsset serviceProjectAsset) {
        LOGGER.info("更新用户服务商品资产信息......");
        return serviceProjectService.updateAsset(serviceProjectAsset);
    }

    @ApiOperation("根据用户手机号查询用户服务项目资产list")
    @GetMapping("/asset/info")
    @ApiImplicitParams({@ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String",required = false),
            @ApiImplicitParam(name = "customer_phone", value = "用户手机号", dataType = "String",required = true)
    })

    public HttpResponse selectServiceProjectAssetByPhone(@RequestParam(name = "customer_phone",required = true) String customerPhone,@RequestParam(name = "store_id",required = false) String storeId) {
        LOGGER.info("根据用户手机号查询用户服务项目资产list......");
        return serviceProjectService.selectServiceProjectAssetByPhone(customerPhone,storeId);
    }

    @ApiOperation("根据资产id查询用户服务项目资产信息")
    @GetMapping("/asset/{asset_id}")
    @ApiImplicitParam(name = "asset_id", value = "用户资产id", dataType = "String")
    public HttpResponse selectServiceProjectAssetBySourceId(@PathVariable(name = "asset_id") String assetId) {
        LOGGER.info("根据资产id查询用户服务项目资产信息......");
        return serviceProjectService.selectServiceProjectAssetByAssetId(assetId);
    }

    @ApiOperation("统计服务项目的转化情况")
    @GetMapping("/project/statistics")
    @ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String",required = false)
    public HttpResponse serviceProjectTransformStatistics(@RequestParam(value = "store_id",required = false) String storeId) {
        LOGGER.info("统计服务项目的转化情况......");
        return serviceProjectService.serviceProjectTransformStatistics(storeId);
    }

    @ApiOperation("统计各类别服务项目的转化情况")
    @PostMapping("/project/type/statistics")
    public HttpResponse serviceProjectTypeTransformStatistics(@RequestBody ReduceDetailRequest reduceDetailRequest) {
        LOGGER.info("统计各类别服务项目的转化情况......");
        return serviceProjectService.serviceProjectTypeTransformStatistics(reduceDetailRequest);
    }

    @ApiOperation("统计各服务项目的转化情况")
    @PostMapping("/project/project/statistics")
    public HttpResponse serviceProjectTransformStatistics(@RequestBody ReduceDetailRequest reduceDetailRequest) {
        LOGGER.info("统计各服务项目的转化情况......");
        return serviceProjectService.serviceProjectTransformStatistics(reduceDetailRequest);
    }

    @ApiOperation("统计各服务项目销量top10")
    @PostMapping("/project/top/statistics")
    public HttpResponse serviceProjectTopTenTransformStatistics(@RequestBody ReduceDetailRequest reduceDetailRequest) {
        LOGGER.info("统计各服务项目销量top10......");
        return serviceProjectService.serviceProjectTopTransformStatistics(reduceDetailRequest);
    }


    @ApiOperation("查询服务订单号")
    @GetMapping("/order/code")
    @ApiImplicitParam(name = "order_no", value = "订单code", dataType = "String")
    public HttpResponse selectServiceProjectAssetCodeByOrderCode(@RequestParam(value = "order_no") String orderCode) {
        LOGGER.info("查询服务订单号......");
        return serviceProjectService.selectServiceProjectAssetCodeByOrderCode(orderCode);
    }


    @ApiOperation("通过门店编号、名称、用户手机号和时间查询订单的信息")
    @PostMapping("/order/list")
    public HttpResponse selectServiceProjectOrder(@RequestBody ReduceDetailRequest reduceDetailRequest) {
        LOGGER.info("通过门店编号、名称、用户手机号和时间查询订单的信息......");
        return serviceProjectService.selectServiceProjectOrder(reduceDetailRequest);
    }

    @ApiOperation("根据订单id和订单类型查询订单信息")
    @GetMapping("/order/info")
    @ApiImplicitParams({@ApiImplicitParam(name = "order_id", value = "订单id", dataType = "String"),
            @ApiImplicitParam(name = "order_type", value = "订单类型", dataType = "Integer"),
            @ApiImplicitParam(name = "store_id", value = "门店id", dataType = "String")
    })
    public HttpResponse selectOrderInfo(@RequestParam(name = "order_id") String orderId, @RequestParam(name = "order_type") Integer orderType,@RequestParam(value = "store_id",required = false) String storeId) {
        LOGGER.info("根据订单id和订单类型查询订单信息......");
        return serviceProjectService.selectOrderInfo(orderId, orderType,storeId);
    }

    @ApiOperation("添加服务项目扣减信息")
    @PutMapping("/reduce/info")
    public HttpResponse insertReduceDetail(@RequestBody @Valid ServiceProjectReduceDetail serviceProjectReduceDetail) {
        LOGGER.info("添加服务项目扣减信息......");
        serviceProjectReduceDetail.setOrderType(0);
        serviceProjectReduceDetail.setOrderId(IdUtil.orderId());
        return serviceProjectService.insertReduceDetail(serviceProjectReduceDetail);
    }

    @ApiOperation("批量添加服务项目扣减信息")
    @PostMapping("/reduce/list")
    public HttpResponse insertReduceDetailList(@RequestBody List<ServiceProjectReduceDetail> serviceProjectReduceDetailList) {
        LOGGER.info("批量添加服务项目扣减信息......");
        return serviceProjectService.insertReduceDetailList(serviceProjectReduceDetailList);
    }



    @ApiOperation("服务商品消费,包括扣减和退次")
    @PostMapping("/project/customer")
    public HttpResponse consumeServiceProject(@RequestBody ReduceAndAssetRequest reduceAndAssetRequest){
        LOGGER.info("服务商品消费,包括扣减和退次......");
        return serviceProjectService.insertReduceAndUpdateAsset(reduceAndAssetRequest);
    }

    @ApiOperation("批量添加服务项目扣减信息和服务商品资产信息")
    @PostMapping("/asset/reduce/list")
    public HttpResponse insertAssetAndReduceDetailList(@RequestBody ReduceAndAssetRequest reduceAndAssetRequest) {
        LOGGER.info("批量添加服务项目扣减信息和服务商品资产信息......");
        return serviceProjectService.insertAssetAndReduceDetailList(reduceAndAssetRequest);
    }

    @ApiOperation("根据订单编号查询用户服务项目资产list")
    @GetMapping("/asset/order")
    @ApiImplicitParam(name = "order_code", value = "订单编号", dataType = "String")
    public HttpResponse selectAssetByOrderCode(@RequestParam(name = "order_code") String orderCode) {
        LOGGER.info("根据订单编号查询用户服务项目资产list......");
        return serviceProjectService.selectAssetByOrderCode(orderCode);
    }

    @ApiOperation("根据资产id查询用户服务项目消费记录list")
    @GetMapping("/asset/id")
    @ApiImplicitParam(name = "asset_id", value = "资产id", dataType = "String")
    public HttpResponse selectServiceProjectReduceDetail(@RequestParam(name = "asset_id") String assetId) {
        LOGGER.info("根据资产id查询用户服务项目消费记录list......");
        return serviceProjectService.selectServiceProjectReduceDetail(assetId);
    }




}
