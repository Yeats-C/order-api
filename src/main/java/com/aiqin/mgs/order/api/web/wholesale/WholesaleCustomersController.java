package com.aiqin.mgs.order.api.web.wholesale;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomerVO;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;
import com.aiqin.mgs.order.api.service.wholesale.WholesaleCustomersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wholesaleCustomers")
@Api(tags = "批发客户相关接口")
public class WholesaleCustomersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WholesaleCustomersController.class);
    @Resource
    private WholesaleCustomersService wholesaleCustomersService;

    /**
     * 批发客户列表
     * @param wholesaleCustomers
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "批发客户列表")
    public HttpResponse<PageResData<WholesaleCustomers>> list(WholesaleCustomers wholesaleCustomers){
        return wholesaleCustomersService.list(wholesaleCustomers);
    }

    @ApiOperation("新增批发客户")
    @PostMapping("/insert")
    public HttpResponse insert(@RequestBody WholesaleCustomers wholesaleCustomers) {
        return wholesaleCustomersService.insert(wholesaleCustomers);
    }

    /**
     * 通过customerCode查询批发客户
     * @param customerCode
     * @return
     */
    @GetMapping("/getCustomerByCode")
    @ApiOperation(value = "通过code查询批发客户")
    public HttpResponse<WholesaleCustomerVO> getCustomerByCode(String customerCode){
        return wholesaleCustomersService.getCustomerAndAccountByCode(customerCode);
    }

    @ApiOperation("修改批发客户")
    @PostMapping("/update")
    public HttpResponse update(@RequestBody WholesaleCustomers wholesaleCustomers) {
        return wholesaleCustomersService.update(wholesaleCustomers);
    }

    /**
     * 通过名称或者账户查询批发客户
     * @param parameter
     * @return
     */
    @GetMapping("/getCustomerByNameOrAccount")
    @ApiOperation(value = "通过名称或者账户查询批发客户")
    public HttpResponse<List<WholesaleCustomers>> getCustomerByNameOrAccount(String parameter){
        return wholesaleCustomersService.getCustomerByNameOrAccount(parameter);
    }

    /**
     * 校验账户是否存在
     * @param customerAccount
     * @return
     */
    @GetMapping("/checkAccountExists")
    @ApiOperation(value = "校验账户是否存在 true 不存在 false 存在")
    public HttpResponse checkAccountExists(String customerAccount){
        return wholesaleCustomersService.checkAccountExists(customerAccount);
    }



}
