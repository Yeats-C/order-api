package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderEditRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.util.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单编辑controller
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 11:43
 */
@RestController
@RequestMapping("/erpOrderEditController")
@Api("订单编辑")
public class ErpOrderEditController {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderEditController.class);

    @PostMapping("/addProductGift")
    @ApiOperation(value = "订单添加赠品")
    public HttpResponse addProductGift(@RequestBody ErpOrderEditRequest erpOrderEditRequest) {
        HttpResponse response = HttpResponse.success();
        try {
            AuthUtil.loginCheck();

        } catch (BusinessException e) {
            logger.error("订单添加赠品失败：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("订单添加赠品失败：{}", e);
            response = HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return response;
    }

}
