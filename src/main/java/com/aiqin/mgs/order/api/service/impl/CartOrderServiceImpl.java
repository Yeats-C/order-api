package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartOrderDao;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.service.CartOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Service
public class CartOrderServiceImpl implements CartOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    @Resource
    private CartOrderDao cartOrderDao;


    //将商品添加到购物车
    @Override
    @Transactional
    public HttpResponse addCartInfo(CartOrderInfo cartOrderInfo) {
        try {
            if (cartOrderInfo != null) {
                //检查SKU商品是否在购物车中存在
                LOGGER.info("判断SKU商品是否已存在购物车中:{}", cartOrderInfo);
                String OldAount = cartOrderDao.isYesCart(cartOrderInfo);
                if (OldAount != null && !OldAount.equals("")) {
                    //已存在购物车的、新添加+已存在购物车的数量=真实数量
                    LOGGER.info("更新购物车:{}", cartOrderInfo);
                    int newAount = Integer.valueOf(OldAount) + cartOrderInfo.getAmount();
                    cartOrderInfo.setAmount(newAount);
                    //更新购物车
                    cartOrderDao.updateCartById(cartOrderInfo);
                } else {
                    //生成购物车id
                    String cartId = IdUtil.uuid();
                    cartOrderInfo.setCartOrderId(cartId);

                    //添加购物车
                    LOGGER.info("添加购物车:{}", cartOrderInfo);
                    cartOrderDao.insertCart(cartOrderInfo);
                }
                return HttpResponse.success();
            }else {
                LOGGER.warn("购物车信息为空!");
                return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
            }
        } catch (Exception e) {
            LOGGER.error("添加购物车异常：{}", e);
            return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
    }


    @Override
    public HttpResponse selectCartByMemberId(String distributorId, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public HttpResponse deleteCartInfo(String distributorId) {
        return null;
    }

    @Override
    public HttpResponse deleteCartInfoById(List<String> skuCodes, String distributorId) {
        return null;
    }

    @Override
    public HttpResponse updateCartByMemberId(@Valid CartOrderInfo cartOrderInfo) {
        return null;
    }
}
