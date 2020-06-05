package com.aiqin.mgs.order.api.web.wholesale;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;
import com.aiqin.mgs.order.api.service.wholesale.WholesaleCustomersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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






}
