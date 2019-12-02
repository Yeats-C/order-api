package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.dao.OrderStoreCartDao;
import com.aiqin.mgs.order.api.domain.OrderStoreCart;
import com.aiqin.mgs.order.api.service.ErpCartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpCartServiceImpl implements ErpCartService {

    @Resource
    private OrderStoreCartDao orderStoreCartDao;


    @Override
    public List<OrderStoreCart> findCartProductList(OrderStoreCart orderStoreCart) {
        if (orderStoreCart == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreCart.getStoreId())) {
            throw new BusinessException("门店id不能为空");
        }
        if (orderStoreCart.getProductType() == null) {
            throw new BusinessException("商品类型不能为空");
        }
        return orderStoreCartDao.findCartProductList(orderStoreCart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCartProduct(OrderStoreCart orderStoreCart) {

        //校验商品
        validateCartProduct(orderStoreCart);

        //检查购物车是否存在该商品，如果存在怎么处理？


        //TODO 当前登录人
//        orderStoreCart.setCreateById("");
//        orderStoreCart.setCreateByName("");
//        orderStoreCart.setUpdateById("");
//        orderStoreCart.setUpdateByName("");

        //添加商品到购物车
        orderStoreCartDao.insert(orderStoreCart);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartProduct(OrderStoreCart orderStoreCart) {
        if (orderStoreCart == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreCart.getCartId())) {
            throw new BusinessException("id为空");
        }
        String cartIds = orderStoreCart.getCartId();
        String[] cartIdArray = cartIds.split(",");
        for (String cartId :
                cartIdArray) {
            if (StringUtils.isEmpty(cartId)) {
                continue;
            }
            //根据cartId删除购物车商品
            Integer integer = orderStoreCartDao.deleteByCartId(cartId);
        }
    }

    /**
     * 校验添加商品数据
     *
     * @param orderStoreCart
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 14:28
     */
    private void validateCartProduct(OrderStoreCart orderStoreCart) {

        if (orderStoreCart == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreCart.getProductId())) {
            throw new BusinessException("商品id为空");
        }
        if (StringUtils.isEmpty(orderStoreCart.getStoreId())) {
            throw new BusinessException("门店id为空");
        }
        if (StringUtils.isEmpty(orderStoreCart.getSkuId())) {
            throw new BusinessException("商品sku为空");
        }
        if (StringUtils.isEmpty(orderStoreCart.getSpuId())) {
            throw new BusinessException("商品spu为空");
        }
        if (orderStoreCart.getProductType() == null) {
            throw new BusinessException("商品类型不能为空");
        }
        if (orderStoreCart.getAmount() == null) {
            throw new BusinessException("数量不能为空");
        }

        //TODO 获取商品后获取部分字段


    }

}
