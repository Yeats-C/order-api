package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.pay.PayReq;

/**
 * @author jinghaibo
 * Date: 2019/11/12 13:53
 * Description:
 */
public interface PayService {
    /**
     *
     * @param req
     * @return
     */
    HttpResponse doPay(PayReq req);
}
