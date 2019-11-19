package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.dao.CartOrderDao;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.response.cart.CartResponse;
import com.aiqin.mgs.order.api.service.CartOrderService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CartOrderServiceImpl implements CartOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    @Resource
    private CartOrderDao cartOrderDao;

    @Resource
    private BridgeProductService bridgeProductService;


    /**
     * 将商品加入购物车
     *
     * @param shoppingCartRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse addCart(ShoppingCartRequest shoppingCartRequest) {
        //入参校验
        checkParam(shoppingCartRequest);
        //通过商品id、门店Id、skuid调用商品模块，返回商品信息
        HttpResponse<CartOrderInfo> productInfo = bridgeProductService.getProduct(shoppingCartRequest);
        //商品数量不能大于999
        int productNumber = shoppingCartRequest.getAmount();
        if (productNumber > 999) {
            return HttpResponse.failure(ResultCode.OVER_LIMIT);
        }
        //库存数量小于10,库存紧张
        if (productInfo.getData().getAmount() < 10) {
            return HttpResponse.failure(ResultCode.STOCK_SHORT1);
        }
        //订购数量大于库存
        if (productNumber > productInfo.getData().getAmount()) {
            return HttpResponse.failure(ResultCode.STORE_SHORT);
        }
        CartOrderInfo cartOrderInfo = new CartOrderInfo();
        cartOrderInfo.setSkuId(productInfo.getData().getSkuId());//skuId
        cartOrderInfo.setProductId(productInfo.getData().getProductId());//商品id
        cartOrderInfo.setStoreId(productInfo.getData().getStoreId());//门店id
        cartOrderInfo.setPrice(productInfo.getData().getPrice());//商品价格
        cartOrderInfo.setProductType(productInfo.getData().getProductType());//商品类型
        cartOrderInfo.setProductName(productInfo.getData().getProductName());//商品名称
        cartOrderInfo.setColor(productInfo.getData().getColor());//商品颜色
        cartOrderInfo.setProductSize(productInfo.getData().getProductSize());//商品尺寸
        //Todo 商品返回什么添加什么
        try {
            if (cartOrderInfo != null) {
                //判断sku是否在购物车里面存在
                LOGGER.info("判断SKU商品是否已存在购物车中:{}", cartOrderInfo.getSkuId());
                String oldAount = cartOrderDao.isYesCart(cartOrderInfo);
                if (oldAount != null && !oldAount.equals("")) {
                    //已存在购物车的、新添加+已存在购物车的数量=真实数量
                    LOGGER.info("更新购物车:{}", cartOrderInfo);
                    int newAount = Integer.valueOf(oldAount) + cartOrderInfo.getAmount();
                    cartOrderInfo.setAmount(newAount);
                    //更新购物车
                    cartOrderDao.updateCartById(cartOrderInfo);
                } else {
                    if (cartOrderInfo.getCartId() != null) {
                        cartOrderDao.insertCart(cartOrderInfo);
                    } else {
                        //生成购物车id
                        String cartId = IdUtil.uuid();
                        cartOrderInfo.setCartId(cartId);
                        //生成商品加入购物车的时间
                        cartOrderInfo.setCreateTime(new Date());
                        //添加购物车
                        LOGGER.info("添加购物车:{}", cartOrderInfo);
                        cartOrderDao.insertCart(cartOrderInfo);
                    }
                }
                return HttpResponse.success();
            } else {
                LOGGER.warn("购物车信息为空!");
                return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
            }
        } catch (Exception e) {
            LOGGER.error("添加购物车异常：{}", e);
            return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }

    }

    private void checkParam(ShoppingCartRequest shoppingCartRequest) {
        if (shoppingCartRequest == null) {
            throw new BusinessException("参数为空");
        }
        if (shoppingCartRequest.getSkuIds() == null) {
            throw new BusinessException("skuId为空");
        }
        if (shoppingCartRequest.getProductId() == null) {
            throw new BusinessException("productId为空");
        }
        if (shoppingCartRequest.getAmount() == null) {
            throw new BusinessException("商品数量为空");
        }
        if (shoppingCartRequest.getStoreId() == null) {
            throw new BusinessException("门店id为空");
        }
    }

    /**
     * 根据门店ID显示购物车中商品信息
     *
     * @param storeId
     * @param productType
     * @return
     */
    @Override
    public HttpResponse selectCartByStoreId(String storeId, Integer productType, String skuId, Integer lineCheckStatus) {
        HttpResponse<Object> response = new HttpResponse<>();
        try {
            CartOrderInfo cartOrderInfo = new CartOrderInfo();
            cartOrderInfo.setStoreId(storeId);
            cartOrderInfo.setProductType(productType);
            //默认标志为0
            lineCheckStatus = lineCheckStatus == null ? 0 : lineCheckStatus;
            //检查商品是否被勾选，勾选后，更新数据库标识
            LOGGER.info("商品勾选后，更新勾选状态：{}", lineCheckStatus);
            if (null != skuId && lineCheckStatus == Global.LINECHECKSTATUS_1) {
                cartOrderInfo.setSkuId(skuId);
                cartOrderInfo.setLineCheckStatus(lineCheckStatus);
                cartOrderDao.updateProductList(cartOrderInfo);
                return HttpResponse.success();
            } else {
                //购物车数据
                List<CartOrderInfo> cartInfoList = cartOrderDao.selectCartByStoreId(cartOrderInfo);
                //计算商品总价格
                BigDecimal acountActualprice = new BigDecimal(0);
                for (CartOrderInfo cartOrderInfo1 : cartInfoList) {
                    BigDecimal total = cartOrderInfo1.getPrice().multiply(new BigDecimal(cartOrderInfo1.getAmount()));
                    acountActualprice = acountActualprice.add(total);
                }
                //计算商品总数量
                int totalNumber = 0;
                for (CartOrderInfo cartOrderInfo2 : cartInfoList) {
                    totalNumber += cartOrderInfo2.getAmount();
                }
                CartResponse cartResponse = new CartResponse();
                cartResponse.setCartInfoList(cartInfoList);
                cartResponse.setAcountActualprice(acountActualprice);
                cartResponse.setTotalNumber(totalNumber);
                LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
                response.setData(cartResponse);
                return response;
            }

        } catch (Exception e) {
            LOGGER.error("根据门店ID查询购物车数据异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    /**
     * 根据门店id返回商品的总数量
     *
     * @param storeId
     * @return
     */
    @Override
    public HttpResponse getTotal(String storeId) {
        HttpResponse<Integer> response = new HttpResponse<>();
        try {
            if (storeId != null) {
                Integer total = cartOrderDao.getTotal(storeId);
                LOGGER.info("返回总数量给购物车：{}", total);
                return response.setData(total);
            }
        } catch (Exception e) {
            LOGGER.error("查询商品总量异常：{}", e);
            return HttpResponse.failure(ResultCode.GETTOTAL);
        }
        return response;
    }


    /**
     * 删除单条商品，清空整个购物车，删除勾选商品
     *
     * @param storeId
     * @param skuId
     * @param lineCheckStatus
     * @return
     */
    @Override
    public HttpResponse deleteCartInfo(String storeId, String skuId, Integer lineCheckStatus) {
        try {
            //清空购物车
            if (storeId != null) {
                LOGGER.info("通过门店id清空购物车：{}", storeId);
                cartOrderDao.deleteCart(storeId, null, null);
            }
            //删除单条商品
            if (skuId != null) {
                LOGGER.info("通过skuId删除商品：{}", skuId);
                cartOrderDao.deleteCart(null, skuId, null);
            }
            //删除选中的商品
            if (lineCheckStatus != null) {
                LOGGER.info("清空全部勾选商品：{}", lineCheckStatus);
                cartOrderDao.deleteCart(null, null, lineCheckStatus);
            }
            return HttpResponse.success();

        } catch (Exception e) {
            LOGGER.error("清空购物车失败", e);
            return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
    }

    /**
     * 显示购物车中的勾选商品
     * @param cartOrderInfo
     * @return
     */
    @Override
    public HttpResponse<List<CartOrderInfo>> displayCartLineCheckProduct(CartOrderInfo cartOrderInfo) {
        //TODO 调用门店接口，返回门店的基本信息

        HttpResponse response = new HttpResponse();
        try {
            if (cartOrderInfo != null) {
                List<CartOrderInfo> cartOrderInfos = cartOrderDao.selectCartByLineCheckStatus(cartOrderInfo);
                //TODO  计算订货金额合计
                response.setData(cartOrderInfos);
            }
        } catch (Exception e) {
            LOGGER.error("显示勾选商品失败", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }
}