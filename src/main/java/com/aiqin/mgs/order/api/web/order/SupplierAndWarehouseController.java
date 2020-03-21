package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.service.order.SupplierAndWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: QuerySupplierAndWarehouseController
 * date: 2020/3/20 19:37
 * author: hantao
 * version: 1.0
 */
@RestController
@RequestMapping("/supplierAndWarehouse")
@Api(tags = "查询供应商或库房信息")
public class SupplierAndWarehouseController {

    @Autowired
    private SupplierAndWarehouseService supplierAndWarehouseService;

    @ApiOperation("查询供应商信息")
    @GetMapping("/getSupplierInfo")
    @ApiImplicitParams({@ApiImplicitParam(name = "supplier_code", value = "供应商编码", dataType = "string", paramType = "query", required = true)})
    public HttpResponse getSupplierInfo(@RequestParam String supplier_code){
        return supplierAndWarehouseService.getSupplierInfo(supplier_code);
    }

    @ApiOperation("查询供应商信息")
    @GetMapping("/getWarehouseInfo")
    @ApiImplicitParams({@ApiImplicitParam(name = "transport_center_code", value = "仓库编码", dataType = "string", paramType = "query", required = true)})
    public HttpResponse getWarehouseInfo(@RequestParam String transport_center_code){
        return supplierAndWarehouseService.getWarehouseInfo(transport_center_code);
    }



}
