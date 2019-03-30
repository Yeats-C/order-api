package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.request.stock.StockLockReqVo;
import com.aiqin.mgs.order.api.domain.response.stock.StockLockRespVo;

import java.util.List;

/**
 * BridgeStockService
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
public interface BridgeStockService {

    List<StockLockRespVo> lock(StockLockReqVo reqVo);
}
