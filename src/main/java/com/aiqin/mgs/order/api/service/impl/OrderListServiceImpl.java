package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
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
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
import com.aiqin.mgs.order.api.service.OrderListService;
import com.aiqin.ground.util.id.IdUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-04 15:27
 */
@SuppressWarnings("all")
@Service
@Slf4j
public class OrderListServiceImpl implements OrderListService {
    @Resource
    private OrderListDao orderListDao;
    @Resource
    private OrderListLogisticsDao orderListLogisticsDao;
    @Resource
    private OrderListProductDao orderListProductDao;
    @Resource
    private SequenceService sequenceService;
    @Resource
    private OrderStatusDao orderStatusDao;



    //商品项目地址
    @Value("${product_ip}")
    public String product_ip;

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
     * 订单列表前台
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
        ParamUnit.isNotNull(param,"orderCode","invoiceCode","logisticsCentreCode","logisticsCentreName","implementBy","implementTime","implementContent");

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
//        //查询此code为父订单标示还是子订单
//        List<OrderList> orderLists=orderListDao.searchFZ(code);
//        if (orderLists==null||orderLists.size()==0){
//            throw new IllegalArgumentException( "code不存在");
//        }
//        String orderCode=orderLists.get(0).getOrderCode();
//        String original=orderLists.get(0).getOriginal();

        Boolean br=  orderListDao.updateByCode(code,status,orderStatus.getPaymentStatus());
        //将订单状态改完支付,将订单发送给供应链
        //todo  判断状态 待更新
        if (status==2 && br==true){
            //获取订单信息
            List<SupplyOrderInfoReqVO> vo = orderListDao.searchOrderByCodeOrOriginal(code);
            List<SupplyOrderProductItemReqVO> list2 = orderListProductDao.searchOrderListProductByCodeOrOriginal(code);
            for (SupplyOrderInfoReqVO reqVO : vo) {
                List<SupplyOrderProductItemReqVO> supplylist=new ArrayList<>();
                for (SupplyOrderProductItemReqVO itemReqVO : list2) {
                      if (reqVO.getOrderCode().equals(itemReqVO.getOrderCode())){
                          supplylist.add(itemReqVO);
                      }
                }
                reqVO.setOrderItems(supplylist);
            }
            SupplyOrderMainReqVO svo  =new  SupplyOrderMainReqVO();
            svo.setSubOrderInfo(vo);
            HttpClient httpPost = HttpClient.post(product_ip+"/purchase/order/add").json(svo);
            httpPost.action().status();
            HttpResponse result = httpPost.action().result(new TypeReference<HttpResponse>(){});
            String c =   result.getCode();
        }
        return br;
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
            ParamUnit.isNotNull(param, "orderCode","orderType","orderStatus","storeId","storeCode","placeOrderTime");

            List<OrderListProduct> orderListProductList = param.getOrderListProductList();
            if (orderListProductList == null || orderListProductList.size() == 0) {
                throw new IllegalArgumentException("商品不可为空");
            }
            for (OrderListProduct orderListProduct : orderListProductList) {
                ParamUnit.isNotNull(orderListProduct, "skuCode", "skuName","productNumber","gift");
            }
//           String orderCode= sequenceService.generateOrderCode(param.getCompanyCode(), param.getOrderType());
//            param.setOrderCode(orderCode);
            String orderCode=param.getOrderCode();
            String orderId = IdUtil.uuid();
            param.setId(orderId);
            Boolean re = orderListDao.insertOrderListDetailsVo(param);
            for (OrderListProduct orderListProduct : orderListProductList) {
                orderListProduct.setId(IdUtil.uuid());
                orderListProduct.setOrderCode(orderCode);
            }
            Boolean re2 = orderListProductDao.insertList(param.getOrderListProductList());
            reList.add(orderCode);
        }
        return reList;
    }


}
