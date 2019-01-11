package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.component.ParamUnit;
import com.aiqin.mgs.order.api.component.SequenceService;
import com.aiqin.mgs.order.api.dao.OrderListDao;
import com.aiqin.mgs.order.api.dao.OrderListLogisticsDao;
import com.aiqin.mgs.order.api.dao.OrderListProductDao;
import com.aiqin.mgs.order.api.dao.OrderStatusDao;
import com.aiqin.mgs.order.api.domain.OrderList;
import com.aiqin.mgs.order.api.domain.OrderListLogistics;
import com.aiqin.mgs.order.api.domain.OrderListProduct;
import com.aiqin.mgs.order.api.domain.OrderStatus;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListDetailsVo;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo2;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderStockVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
import com.aiqin.mgs.order.api.service.OrderListService;
import com.aiqin.ground.util.id.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-04 15:27
 */
@Service
@Slf4j
public class OrderListServiceImpl implements OrderListService {
    @Autowired
    private OrderListDao orderListDao;
    @Autowired
    private OrderListLogisticsDao orderListLogisticsDao;
    @Autowired
    private OrderListProductDao orderListProductDao;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private OrderStatusDao orderStatusDao;

    /**
     * 订单列表后台
     * @param param
     * @return
     */
    @Override
    public PageResData<OrderList> searchOrderList(OrderListVo param) {
        List<OrderList> inventories = orderListDao.searchOrderList(param);
        int count = orderListDao.searchOrderListCount(param);
        return new PageResData<>(count, inventories);
    }

    /**
     * 订单列表后台
     * @param param
     * @return
     */
    @Override
    public PageResData<OrderList> searchOrderReceptionList(OrderListVo2 param) {
        ParamUnit.isNotNull(param,"storeId");
        List<OrderList> inventories = orderListDao.searchOrderReceptionList(param);
        int count = orderListDao.searchOrderReceptionListCount(param);
        return new PageResData<>(count, inventories);
    }

    @Override
    public Boolean addLogistics(OrderListLogistics param) {
        ParamUnit.isNotNull(param,"orderListId","orderCode","invoiceCode","logisticsCentreCode","logisticsCentreName","deliveryTime","implementBy","implementTime","implementContent");

        return orderListLogisticsDao.insertLogistics(param);
    }

    @Override
    public Boolean updateOrderStatus(String code, Integer status) {
        if (code==null||code.length()==0||status==null){
            throw new IllegalArgumentException( "参数不能为空");
        }
        OrderStatus orderStatus =orderStatusDao.searchStatus(status);
        if (orderStatus==null){
            throw new IllegalArgumentException( "状态值未找到");
        }

        return orderListDao.updateByCode(code,status,orderStatus.getPaymentStatus());
    }

    @Override
    public List<OrderStockReVo> getStockValue(OrderStockVo vo) {
        return orderListDao.getStockValue(vo);
    }

    @Override
    public OrderListDetailsVo getOrderByCode(String code) {
        //todo
        OrderListDetailsVo vo = orderListDao.searchOrderByCode(code);

        List<OrderListLogistics> list1 = orderListLogisticsDao.searchOrderListLogisticsByCode(code);
        vo.setOrderListLogisticsList(list1);
        List<OrderListProduct> list2 = orderListProductDao.searchOrderListProductByCode(code);
        vo.setOrderListProductList(list2);
        return vo;
    }

    @Override
    public List<String> add(List<OrderListDetailsVo> paramList) {
        List<String> reList=new ArrayList<>();
        for (OrderListDetailsVo param : paramList) {
            //验证添加数据
            ParamUnit.isNotNull(param, "orderType", "orderStatus", "paymentStatus", "storeId", "storeCode", "storeName", "storeType", "totalOrders",
                  "orderCode",  "logisticsRemissionRatio", "placeOrderBy", "receivingAddress", "consignee", "consigneePhone", "companyCode","original", "orderListProductList",  "createBy"
            );

            List<OrderListProduct> orderListProductList = param.getOrderListProductList();
            if (orderListProductList == null || orderListProductList.size() == 0) {
                throw new IllegalArgumentException("商品不可为空");
            }
            for (OrderListProduct orderListProduct : orderListProductList) {
                ParamUnit.isNotNull(orderListProduct, "skuCode", "skuName", "productPrice", "originalProductPrice", "productNumber", "gift", "specifications", "unit");
            }
//           String orderCode= sequenceService.generateOrderCode(param.getCompanyCode(), param.getOrderType());
//            param.setOrderCode(orderCode);
            String orderCode=param.getOrderCode();
            String orderId = IdUtil.uuid();
            param.setId(orderId);
            Boolean re = orderListDao.insertOrderListDetailsVo(param);
            for (OrderListProduct orderListProduct : orderListProductList) {
                orderListProduct.setId(IdUtil.uuid());
                orderListProduct.setOrderListId(orderId);
                orderListProduct.setOrderCode(orderCode);
            }
            Boolean re2 = orderListProductDao.insertList(param.getOrderListProductList());
            reList.add(orderCode);
        }
        return reList;
    }


}
