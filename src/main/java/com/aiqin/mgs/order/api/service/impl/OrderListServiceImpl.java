package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.component.OrderStatusEnum;
import com.aiqin.mgs.order.api.component.ParamUnit;
import com.aiqin.mgs.order.api.component.SequenceService;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.CouponShareRequest;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.request.stock.StockLockReqVo;
import com.aiqin.mgs.order.api.domain.request.stock.StockLockSkuReqVo;
import com.aiqin.mgs.order.api.domain.response.StoreMarketValueResponse;
import com.aiqin.mgs.order.api.domain.response.orderlistre.FirstOrderTimeRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderSaveRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;
import com.aiqin.mgs.order.api.domain.response.statistical.StatisticalPurchaseAmount;
import com.aiqin.mgs.order.api.domain.response.stock.StockLockRespVo;
import com.aiqin.mgs.order.api.service.BridgeStockService;
import com.aiqin.mgs.order.api.service.OrderListService;
import com.aiqin.mgs.order.api.service.order.ErpOrderItemService;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-04 15:27
 */
@SuppressWarnings("all")
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrderListServiceImpl implements OrderListService {
    @Value("${bridge.url.slcs_api}")
    private String slcsHost;
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
    @Resource
    private BridgeStockService bridgeStockService;
    @Resource
    private OrderSplitsNumDao orderSplitsNumDao;
    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;



    //供应链项目地址
    @Value("${purchase_ip}")
    public String purchase_ip;

    /**
     * 订单列表后台
     *
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
     *
     * @param param
     * @return
     */
    @Override
    public PageResData<OrderList> searchOrderReceptionList(OrderListVo2 param) {
        ParamUnit.isNotNull(param, "storeId");
        List<OrderList> inventories = orderListDao.searchOrderReceptionList(param);
        int count = orderListDao.searchOrderReceptionListCount(param);
        return new PageResData<>(count, inventories);
    }

    @Override
    public Boolean addLogistics(OrderListLogistics param) {
        ParamUnit.isNotNull(param, "orderCode", "invoiceCode", "logisticsCentreCode", "logisticsCentreName", "implementBy", "implementTime", "implementContent");
        Boolean re = orderListLogisticsDao.insertLogistics(param);
        return re;
    }

    @Transactional
    @Override
    public Boolean updateOrderStatus(String code, Integer status) {
        if (code == null || code.length() == 0 || status == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        OrderStatusEnum orderStatus = OrderStatusEnum.getOrderStatusEnum(status);
        if (orderStatus == null) {
            throw new IllegalArgumentException("状态值未找到");
        }
        Boolean br = false;
        if (status != 2) {
            br = orderListDao.updateStatusByCode(code, status);
        }
        //将订单状态改完支付,将订单发送给供应链
        if (status == 2) {
            throw new IllegalArgumentException("请使用支付专用接口");
//            br = orderListDao.updateByCode(code, 2, 1);
//            if (br == true) {
//                //获取订单信息
//                List<SupplyOrderInfoReqVO> vo = orderListDao.searchOrderByCodeOrOriginal(code);
//                List<SupplyOrderProductItemReqVO> list2 = orderListProductDao.searchOrderListProductByCodeOrOriginal(code);
//                for (SupplyOrderInfoReqVO reqVO : vo) {
//                    reqVO.setOrderStatus(4);
//                    List<SupplyOrderProductItemReqVO> supplylist = new ArrayList<>();
//                    for (SupplyOrderProductItemReqVO itemReqVO : list2) {
//                        if (reqVO.getOrderCode().equals(itemReqVO.getOrderCode())) {
//                            supplylist.add(itemReqVO);
//                        }
//                    }
//                    reqVO.setOrderItems(supplylist);
//                }
//                SupplyOrderMainReqVO svo = new SupplyOrderMainReqVO();
//                svo.setSubOrderInfo(vo);
////                JsonUtil.toJson(vo);
//                HttpClient httpPost = HttpClient.post("http://" + purchase_ip + "/purchase/order/add").json(svo);
//                HttpResponse<List<OrderStockReVo>> result =
//                        httpPost.action().result(new TypeReference<HttpResponse<Boolean>>() {
//                        });
//                if (result == null || !(StringUtils.equals(result.getCode(), "0"))) {
//                    throw new GroundRuntimeException("推送订单失败");
//                }
//            }
        }

        return br;
    }

    @Override
    public List<OrderStockReVo> getStockValue(OrderStockVo vo) {
        return orderListDao.getStockValue(vo);
    }

    @Override
    public PageResData<OrderListFather> searchOrderReceptionListFather(OrderListVo2 param) {
        ParamUnit.isNotNull(param, "storeId");
        List<OrderListFather> inventories = orderListDao.searchOrderReceptionListFather(param);
        for (OrderListFather inventory : inventories) {
            for (OrderList orderList : inventory.getOrderList()) {
                orderList.setOrderStatusShow(OrderStatusEnum.getOrderStatusEnum(orderList.getOrderStatus()).getReceptionStatus());
            }
        }
        int count = orderListDao.searchOrderReceptionListFatherCount(param);
        return new PageResData<>(count, inventories);
    }

    @Override
    public PageResData<OrderListFather> searchOrderReceptionListFatherProduct(OrderListVo2 param) {
        ParamUnit.isNotNull(param, "storeId");
        List<OrderListFather> inventories = orderListDao.searchOrderReceptionListFather(param);
        List<String> codeList = new ArrayList<>();
        for (OrderListFather inventory : inventories) {
            for (OrderList orderList : inventory.getOrderList()) {
                orderList.setOrderStatusShow(OrderStatusEnum.getOrderStatusEnum(orderList.getOrderStatus()).getReceptionStatus());
                codeList.add(orderList.getOrderCode());
            }
        }
        //查询所有订单下的商品数据
        if (codeList.size() != 0) {
            List<OrderListProduct> list2 = orderListProductDao.searchOrderListProductByCodeList(codeList);
            for (OrderListFather inventory : inventories) {
                for (OrderList orderList : inventory.getOrderList()) {
                    orderList.setSkuNum(0);
                    int skuNum = 0;
                    List<String> skuList = new ArrayList<>();
                    orderList.setOrderListProductList(new ArrayList<OrderListProduct>());
                    for (OrderListProduct product : list2) {
                        if (product.getOrderCode().equals(orderList.getOrderCode())) {
                            if (skuNum < 3) {
                                orderList.getOrderListProductList().add(product);
                            }
                            Boolean flag = false;
                            for (String sku : skuList) {
                                if (sku.equals(product.getSkuCode())) {
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                skuList.add(product.getSkuCode());
                                orderList.setSkuNum(orderList.getSkuNum() + 1);
                            }
                            skuNum++;
                        }
                    }
                }
            }
        }
        int count = orderListDao.searchOrderReceptionListFatherCount(param);
        return new PageResData<>(count, inventories);
    }

//    @Override
//    public Boolean updateOrderActualDeliver(List<ActualDeliverVo> actualDeliverVos) {
//        for (ActualDeliverVo vo : actualDeliverVos) {
//            orderListProductDao.updateByOrderProductId(vo);
//        }
//        return true;
//    }

    @Override
    public Boolean updateOrderStatusDeliver(DeliverVo deliverVo) {
        log.info("修改订单为发货状态,入参deliverVo={}",JsonUtil.toJson(deliverVo));
        Boolean br = orderListDao.updateStatusByCode(deliverVo.getOrderCode(), 11);
        List<ActualDeliverVo> actualDeliverVos = deliverVo.getActualDeliverVos();
        for (ActualDeliverVo vo : actualDeliverVos) {
            orderListProductDao.updateByOrderProductId(vo, deliverVo.getOrderCode());
        }
        OrderListDetailsVo orderListDetailsVo = orderListDao.searchOrderByCode(deliverVo.getOrderCode());
        OrderListLogistics param = new OrderListLogistics();
        param.setOrderCode(orderListDetailsVo.getOrderCode());
        param.setInvoiceCode(deliverVo.getInvoiceCode());
        param.setLogisticsCentreCode(orderListDetailsVo.getTransportCenterCode());
        param.setLogisticsCentreName(orderListDetailsVo.getTransportCenterName());
        param.setImplementBy(deliverVo.getImplementBy());
        param.setImplementTime(new Date());
        param.setImplementContent("发货完成");
        Boolean re = orderListLogisticsDao.insertLogistics(param);
//        log.info("修改订单为发货状态---进入分摊计算");
//        //判断拆单的子订单是否已经全部发货完成
//        Boolean flag = checkComplete(deliverVo.getOrderCode());
//        log.info("修改订单为发货状态---分摊计算--判断拆单的子订单是否已经全部发货完成flag={}",flag);
//        if(flag){//全部完成，进行分摊计算
//            log.info("修改订单为发货状态---全部发货完成,进行分摊计算");
//            ErpOrderInfo po=new ErpOrderInfo();
//            po.setOrderStoreCode(deliverVo.getOrderCode());
//            //查询主订单
//            List<ErpOrderInfo> select = erpOrderInfoDao.select(po);
//            log.info("修改订单为发货状态---全部发货完成,进行分摊计算--查询主订单数据集合为select={}",select);
//            if(select!=null&&select.size()>0) {
//                ErpOrderInfo erpOrderInfo = select.get(0);
//                log.info("修改订单为发货状态---全部发货完成,进行分摊计算--查询主订单数据为 erpOrderInfo={}",erpOrderInfo);
//                //获取门店信息
//                StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderInfo.getStoreId());
//                log.info("修改订单为发货状态---全部发货完成,进行分摊计算--查询门店信息数据为 storeInfo={}",storeInfo);
//                if(erpOrderInfo!=null&&StringUtils.isNotBlank(erpOrderInfo.getMainOrderCode())){
//                    log.info("修改订单为发货状态---全部发货完成,进行分摊计算--主单号为 mainOrderCode={}",erpOrderInfo.getMainOrderCode());
//                    //查询所有子订单号
//                    po.setOrderStoreCode(null);
//                    po.setMainOrderCode(erpOrderInfo.getMainOrderCode());
//                    List<ErpOrderInfo> splictOrder = erpOrderInfoDao.select(po);
//                    log.info("修改订单为发货状态---全部发货完成,进行分摊计算--查询所有子订单号为 splictOrder={}",splictOrder);
//                    List<ErpOrderItem> erpOrderItemList=new ArrayList<>();
//                    for(ErpOrderInfo orderInfo:splictOrder){
//                        List<ErpOrderItem> erpOrderItems = erpOrderItemService.selectOrderItemListByOrderId(orderInfo.getOrderStoreId());
//                        List<String> topCouponCodeList=null;
////                        ErpOrderFee erpOrderFee = firstGivePrice(storeInfo, erpOrderItems, topCouponCodeList);
//                        chai(erpOrderItems);
//                        log.info("修改订单为发货状态---全部发货完成,进行分摊计算--封装后子单明细数据为 erpOrderItems={}",erpOrderItems);
//                        erpOrderItemList.addAll(erpOrderItems);
//                    }
//                    //获取登陆人信息
//                    AuthToken authToken = AuthUtil.getCurrentAuth();
//                    log.info("修改订单为发货状态---全部发货完成,进行分摊计算--更新明细表实收分摊价格入参 erpOrderItemList={}",JSON.toJSONString(erpOrderItemList));
//                    //更新明细表实收分摊价格
//                    erpOrderItemService.updateOrderItemList(erpOrderItemList,authToken);
//                    //如果子订单中有退货情况，现在更想预生成的退货单的 是否发起退货状态
//                    if(erpOrderItemList!=null&&erpOrderItemList.size()>0){
//                        //遍历出所有的子订单号
//                        List<String> orderCodes=erpOrderItemList.stream().map(ErpOrderItem::getOrderStoreCode).collect(Collectors.toList());
//                        log.info("修改订单为发货状态---全部发货完成,修改退货单是否发起退货状态，入参orderCodes={}",JSON.toJSONString(orderCodes));
//                        if(null!=orderCodes&&orderCodes.size()>0){
//                            returnOrderInfoDao.updateReallyReturn(orderCodes);
//                            log.info("修改订单为发货状态---全部发货完成,修改退货单是否发起退货状态，成功");
//                        }
//                    }
//                }
//            }
//
//        }
        return br;
    }

    @Override
    public Boolean updateOrderStatusReceiving(String code, String name) {
//        Boolean br = orderListDao.updateStatusByCode(code, 12);
        Boolean br = orderListDao.updateStatusByCodeReceiving(code, 12);
        List<OrderListLogistics> listLogistics = orderListLogisticsDao.searchOrderListLogisticsByCode(code);
        if (listLogistics == null) {
            throw new IllegalArgumentException("数据异常");
        }
        OrderListLogistics lo = listLogistics.get(0);
        OrderListLogistics param = new OrderListLogistics();
        param.setOrderCode(lo.getOrderCode());
        param.setInvoiceCode(lo.getInvoiceCode());
        param.setLogisticsCentreCode(lo.getLogisticsCentreCode());
        param.setLogisticsCentreName(lo.getLogisticsCentreName());
        param.setImplementBy(name);
        param.setImplementTime(new Date());
        param.setImplementContent("签收完成");
        Boolean re = orderListLogisticsDao.insertLogistics(param);
        return re;
    }

    @Transactional
    @Override
    public Boolean updateOrderStatusPayment(OrderStatusPayment vop) {
        log.info("订单支付：{}", JsonUtil.toJson(vop));
        if (vop.getOrderCode() == null || vop.getOrderCode().length() == 0) {
            throw new IllegalArgumentException("参数不能为空");
        }
        Boolean br = false;
        //将订单状态改完支付,将订单发送给供应链
        br = orderListDao.updateByCodePayment(vop);
        if (br == true) {
            //获取订单信息
            List<SupplyOrderInfoReqVO> vo = orderListDao.searchOrderByCodeOrOriginal(vop.getOrderCode());
            List<SupplyOrderProductItemReqVO> list2 = orderListProductDao.searchOrderListProductByCodeOrOriginal(vop.getOrderCode());
            for (SupplyOrderInfoReqVO reqVO : vo) {
                reqVO.setOrderStatus(4);
                List<SupplyOrderProductItemReqVO> supplylist = new ArrayList<>();
                for (SupplyOrderProductItemReqVO itemReqVO : list2) {
                    if (reqVO.getOrderCode().equals(itemReqVO.getOrderCode())) {
                        supplylist.add(itemReqVO);
                    }
                }
                reqVO.setOrderItems(supplylist);
            }
            SupplyOrderMainReqVO svo = new SupplyOrderMainReqVO();
            svo.setSubOrderInfo(vo);
            log.info("推送订单：{}", JsonUtil.toJson(svo));
            HttpClient httpPost = HttpClient.post("http://" + purchase_ip + "/purchase/order/add").json(svo);
            httpPost.timeout(50000);
            HttpResponse<List<OrderStockReVo>> result =
                    httpPost.action().result(new TypeReference<HttpResponse<Boolean>>() {
                    });
            if (result == null) {
                throw new GroundRuntimeException("推送订单失败");
            }
            if (!(StringUtils.equals(result.getCode(), "0"))) {
                throw new GroundRuntimeException(result.getMessage());
            }

        }

        return br;
    }

    @Override
    public List<String> selectOrderCancellation(int i, Date date) {
        return orderListDao.selectOrderCancellation(i, date);

    }

    @Override
    public Boolean updateOrderCancellation(List<String> codeString, Integer stu) {
        return orderListDao.updateOrderCancellation(codeString, stu);
    }

    @Override
    public PageResData<OrderListFather> searchOrderListFather(OrderListVo param) {
        List<OrderListFather> inventories = orderListDao.searchOrderListFather(param);
        for (OrderListFather inventory : inventories) {
            for (OrderList orderList : inventory.getOrderList()) {
                orderList.setOrderStatusShow(OrderStatusEnum.getOrderStatusEnum(orderList.getOrderStatus()).getBackstageStatus());
                inventory.setCanUpdate(orderList.getOrderStatus() == 1 && (orderList.getOrderType() == 3 || orderList.getOrderType() == 4));
            }
        }
        int count = orderListDao.searchOrderListFatherCount(param);
        return new PageResData<>(count, inventories);
    }

    @Override
    public Boolean updateOrderRefund(String code) {
        return orderListDao.updateOrderPaymentStatus(code, 2);
    }

    @Override
    public List<OrderListDetailsVo> getOrderByCodeFather(String code) {
        List<OrderListDetailsVo> list = orderListDao.searchOrderByCodeFather(code);
        List<String> orderCode = new ArrayList<>();
        for (OrderListDetailsVo vo : list) {
            OrderStatusEnum statusEnum = OrderStatusEnum.getOrderStatusEnum(vo.getOrderStatus());
            vo.setReceptionStatus(statusEnum.getReceptionStatus());
            vo.setBackstageStatus(statusEnum.getBackstageStatus());
            orderCode.add(vo.getOrderCode());
        }
        if (orderCode.size() > 0) {
            List<OrderListProduct> orderListProducts = orderListProductDao.searchOrderListProductByCodeList(orderCode);
            for (OrderListDetailsVo vo : list) {
                for (OrderListProduct orderListProduct : orderListProducts) {
                    if (vo.getOrderCode().equals(orderListProduct.getOrderCode())) {
                        if (vo.getOrderListProductList() == null) {
                            vo.setOrderListProductList(new ArrayList<>());
                        }
                        vo.getOrderListProductList().add(orderListProduct);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public OrderListDetailsVo getOrderByCode(String code) {

        OrderListDetailsVo vo = orderListDao.searchOrderByCode(code);
        vo.setReceptionStatus(OrderStatusEnum.getOrderStatusEnum(vo.getOrderStatus()).getReceptionStatus());
        vo.setBackstageStatus(OrderStatusEnum.getOrderStatusEnum(vo.getOrderStatus()).getBackstageStatus());
        List<OrderListLogistics> list1 = orderListLogisticsDao.searchOrderListLogisticsByCode(code);
        vo.setOrderListLogisticsList(list1);
        List<OrderListProduct> list2 = orderListProductDao.searchOrderListProductByCode(code);
        vo.setOrderListProductList(list2);
        return vo;
    }

    @Override
    public List<String> add(List<OrderListDetailsVo> paramList) {
        List<String> reList = new ArrayList<>();
        for (OrderListDetailsVo param : paramList) {
            //验证添加数据
            ParamUnit.isNotNull(param, "orderCode", "orderType", "orderStatus", "storeId", "storeCode", "placeOrderTime");

            List<OrderListProduct> orderListProductList = param.getOrderListProductList();
            if (orderListProductList == null || orderListProductList.size() == 0) {
                throw new IllegalArgumentException("商品不可为空");
            }
            for (OrderListProduct orderListProduct : orderListProductList) {
                ParamUnit.isNotNull(orderListProduct, "skuCode", "skuName", "productNumber", "gift");
            }

            String orderCode = param.getOrderCode();
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

    @Transactional
    @Override
    public OrderSaveRespVo save(OrderReqVo reqVo) {
        log.info("===============保存订单======================");
        log.info(JsonUtil.toJson(reqVo));
        Date now = new Date();
        //设置行号
        for (int i = 0; i < reqVo.getProducts().size(); i++) {
            OrderProductReqVo orderProductReqVo = reqVo.getProducts().get(i);
            orderProductReqVo.setOrderProductId(String.valueOf(i + 1));
        }
        AtomicReference<String> orderCode = new AtomicReference<>(reqVo.getOrderCode());
        if (StringUtils.isBlank(reqVo.getOrderCode())) {
            orderCode.set(sequenceService.generateOrderCode(reqVo.getCompanyCode(), reqVo.getOrderType()));
        } else {
            //查询原订单
            OrderListDetailsVo detailsVo = orderListDao.searchOrderByCode(reqVo.getOrderCode());
            Assert.notNull(detailsVo, "编辑失败，订单不存在！");
            Assert.isTrue(detailsVo.getOrderStatus() == 1, "编辑失败，只能编辑待支付订单");
            //删除原订单
            orderListDao.deleteByOrderCode(reqVo.getOrderCode());
            orderListProductDao.deleteByOrderCode(reqVo.getOrderCode());
        }
        List<StockLockSkuReqVo> skuReqVos = reqVo.getProducts().stream().map(product -> {
            StockLockSkuReqVo skuReqVo = new StockLockSkuReqVo();
            skuReqVo.setNum(product.getProductNumber());
            skuReqVo.setSkuCode(product.getSkuCode());
            skuReqVo.setProductType(product.getProductType());
            skuReqVo.setLineNum(product.getOrderProductId());
            return skuReqVo;
        }).collect(Collectors.toList());
        StockLockReqVo stockLockReqVo = new StockLockReqVo();
        stockLockReqVo.setSkuList(skuReqVos);
        stockLockReqVo.setCityId(reqVo.getCityCode());
        stockLockReqVo.setCompanyCode(reqVo.getCompanyCode());
        stockLockReqVo.setProvinceId(reqVo.getProvinceCode());
        stockLockReqVo.setOrderCode(orderCode.get());
        log.info("锁定库存请求入参：{}", JsonUtil.toJson(stockLockReqVo));
        List<StockLockRespVo> lockRespVos = bridgeStockService.lock(stockLockReqVo);
        log.info("锁定库存返回数据：{}", JsonUtil.toJson(lockRespVos));
        Map<String, List<StockLockRespVo>> stockMap = lockRespVos.stream().collect(Collectors.toMap(StockLockRespVo::getLineNum, Lists::newArrayList, (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        }));
        Map<String, StockLockRespVo> warehouseMap = lockRespVos.stream().collect(Collectors.toMap(StockLockRespVo::getWarehouseCode, Function.identity(), (o1, o2) -> o1));
        Map<String, List<OrderListProduct>> productMap = Maps.newLinkedHashMap();
        for (OrderProductReqVo product : reqVo.getProducts()) {
            List<StockLockRespVo> lockRespVoList = stockMap.get(product.getOrderProductId());
            Assert.notEmpty(lockRespVoList, "锁定库存异常");
            //商品价格（单品合计成交价)
            long totalProductPrice = 0L;
            long totalDiscountMoney = 0L;
            long totalPreferentialAllocation = 0;
            for (int i = 0; i < lockRespVoList.size(); i++) {
                StockLockRespVo stockLock = lockRespVoList.get(i);
                OrderListProduct productDTO = new OrderListProduct();
                BeanUtils.copyProperties(product, productDTO);
                productDTO.setId(IdUtil.uuid());
                //重新计算价格
                productDTO.setProductNumber(stockLock.getLockNum());
                productDTO.setAmount(stockLock.getLockNum() * product.getOriginalProductPrice());
                //单品合计成交价
                long productPrice;
                //优惠额度抵扣金额（单品合计）
                long discountMoney;
                //优惠分摊
                long preferentialAllocation;
                if (i < lockRespVoList.size() - 1) {
                    productPrice = stockLock.getLockNum() * product.getProductPrice() / product.getProductNumber();
                    totalProductPrice += productPrice;
                    discountMoney = stockLock.getLockNum() * product.getDiscountMoney() / product.getProductNumber();
                    totalDiscountMoney += discountMoney;
                    preferentialAllocation = stockLock.getLockNum() * product.getPreferentialAllocation() / product.getProductNumber();
                    totalPreferentialAllocation += preferentialAllocation;
                } else {
                    productPrice = product.getProductPrice() - totalProductPrice;
                    discountMoney = product.getDiscountMoney() - totalDiscountMoney;
                    preferentialAllocation = product.getPreferentialAllocation() - totalPreferentialAllocation;
                }
                productDTO.setProductPrice(productPrice);
                productDTO.setDiscountMoney(discountMoney);
                productDTO.setPreferentialAllocation(preferentialAllocation);
                //优惠明细分摊
                if (productDTO.getUseDiscountAmount() == 1) {
                    List<DiscountAmountInfo> infos = product.getDiscountAmountInfo().stream().map(info -> {
                        DiscountAmountInfo newInfo = new DiscountAmountInfo();
                        newInfo.setCode(info.getCode());
                        newInfo.setAmount(info.getAmount() * stockLock.getLockNum() / product.getProductNumber());
                        return newInfo;
                    }).collect(Collectors.toList());
                    productDTO.setDiscountAmountInfo(JSON.toJSONString(infos));
                }
                List<OrderListProduct> orderListProducts = productMap.get(stockLock.getWarehouseCode());
                if (CollectionUtils.isEmpty(orderListProducts)) {
                    productMap.put(stockLock.getWarehouseCode(), Lists.newArrayList(productDTO));
                } else {
                    orderListProducts.add(productDTO);
                }
            }
        }
        List<OrderListProduct> orderListProducts = Lists.newLinkedList();
        List<OrderList> orders = Lists.newArrayList();
        List<String> chirldOrderCodes = Lists.newArrayList();
        productMap.forEach((warehouseCode, products) -> {
            OrderList order = new OrderList();
            BeanUtils.copyProperties(reqVo, order);
            if (productMap.size() > 1) {
                String chirldOrderCode = sequenceService.generateOrderCode(reqVo.getCompanyCode(), reqVo.getOrderType());
                order.setOrderCode(chirldOrderCode);
                long totalOrders = products.stream().mapToLong(OrderListProduct::getAmount).sum();
                order.setTotalOrders(totalOrders);
                long actualAmountPaid = products.stream().mapToLong(OrderListProduct::getProductPrice).sum();
                order.setActualAmountPaid(actualAmountPaid);
                long activityAmount = products.stream().mapToLong(OrderListProduct::getPreferentialAllocation).sum();
                order.setActivityAmount(activityAmount);
                long preferentialQuota = products.stream().mapToLong(OrderListProduct::getDiscountMoney).sum();
                order.setPreferentialQuota(preferentialQuota);
                int productNum = products.stream().mapToInt(OrderListProduct::getProductNumber).sum();
                order.setProductNum(productNum);
                int weight = products.stream().mapToInt(product -> product.getWeight() * product.getProductNumber()).sum();
                chirldOrderCodes.add(chirldOrderCode);
                products.forEach(product -> {
                    product.setOrderCode(chirldOrderCode);
                });
            } else {
                order.setOrderCode(orderCode.get());
                products.forEach(product -> {
                    product.setOrderCode(orderCode.get());
                });
            }
            order.setId(IdUtil.uuid());
            order.setOriginal(orderCode.get());
            order.setPlaceOrderTime(now);
            StockLockRespVo respVo = warehouseMap.get(warehouseCode);
            if (respVo != null) {
                order.setWarehouseCode(warehouseCode);
                order.setWarehouseName(respVo.getWarehouseName());
                order.setTransportCenterCode(respVo.getTransportCenterCode());
                order.setTransportCenterName(respVo.getTransportCenterName());
            }
            orderListProducts.addAll(products);
            orders.add(order);
        });
        orders.forEach(order -> {
            orderListDao.insertSelective(order);
        });
        orderListProductDao.insertList(orderListProducts);
        OrderSaveRespVo respVo = new OrderSaveRespVo();
        respVo.setOrderCode(orderCode.get());
        respVo.setSplitOrder(orders.size() > 1);
        respVo.setChildOrderCode(chirldOrderCodes);
        return respVo;
    }


    @Transactional
    @Override
    public Boolean saveOrder(OrderReqVo reqVo) {
        //设置行号
        for (int i = 0; i < reqVo.getProducts().size(); i++) {
            OrderProductReqVo orderProductReqVo = reqVo.getProducts().get(i);
            orderProductReqVo.setOrderProductId(String.valueOf(i + 1));
        }
        Date now = new Date();
        OrderList order = new OrderList();
        String orderCode = reqVo.getOrderCode();
        BeanUtils.copyProperties(reqVo, order);
        if (StringUtils.isBlank(reqVo.getOrderCode())) {
            orderCode = sequenceService.generateOrderCode(reqVo.getCompanyCode(), reqVo.getOrderType());
        } else {
            //查询原订单
            OrderListDetailsVo detailsVo = orderListDao.searchOrderByCode(reqVo.getOrderCode());
            Assert.notNull(detailsVo, "编辑失败，订单不存在！");
            Assert.isTrue(detailsVo.getOrderStatus() == 1, "编辑失败，只能编辑待支付订单");
            //删除原订单
            orderListDao.deleteByOrderCode(reqVo.getOrderCode());
            orderListProductDao.deleteByOrderCode(reqVo.getOrderCode());
        }
        order.setOrderCode(orderCode);
        order.setId(IdUtil.uuid());
        order.setOriginal(orderCode);
        order.setPlaceOrderTime(now);
        List<OrderListProduct> products = reqVo.getProducts().stream().map(product -> {
            OrderListProduct productDTO = new OrderListProduct();
            BeanUtils.copyProperties(product, productDTO);
            productDTO.setId(IdUtil.uuid());
            return productDTO;
        }).collect(Collectors.toList());
        for (OrderListProduct product : products) {
            product.setOrderCode(orderCode);
        }
        orderListDao.insertSelective(order);
        orderListProductDao.insertList(products);
        return true;
    }

    @Override
    public List<FirstOrderTimeRespVo> selectFirstOrderTime(List<String> storeIds) {
        return orderListDao.selectFirstOrderTime(storeIds);
    }

    @Override
    public Boolean updateProductReturnNum(UpdateProductReturnNumReqVo reqVo) {
        orderListProductDao.updateProductReturnNum(reqVo.getOrderCode(), reqVo.getItems());
        return true;
    }

    @Override
    public StatisticalPurchaseAmount getStatisticalPurchaseAmount(String storeId) {
        StatisticalPurchaseAmount re = new StatisticalPurchaseAmount();
        re.setPurchaseAmount(0L);
        re.setYesterdayPurchaseAmount(0L);
        Long amount = orderListDao.statisticalPurchaseAmount(storeId, DateUtil.getFristOfMonthDay(DateUtil.getCurrentDate()), DateUtil.getLashOfMonthDay(DateUtil.getCurrentDate()));
        Long yesterday_amount = orderListDao.statisticalPurchaseAmount(storeId, DateUtil.getBeginYesterday(DateUtil.getCurrentDate()), DateUtil.getEndYesterDay(DateUtil.getCurrentDate()));
        re.setPurchaseAmount(Optional.ofNullable(amount).orElse(0L).longValue());
        re.setYesterdayPurchaseAmount(Optional.ofNullable(yesterday_amount).orElse(0L).longValue());
        return re;
    }

    /**
     * 发货后计算--首单赠送金额计算
     * @param itemList
     * @param topCouponCodeList
     */
    private ErpOrderFee firstGivePrice(StoreInfo storeInfo, List<ErpOrderItem> itemList, List<String> topCouponCodeList) {
        //赠送市值
        BigDecimal marketValue = new BigDecimal(storeInfo.getMarketValue());
        //赠送市值余额
        BigDecimal marketValueBalance = new BigDecimal(storeInfo.getMarketValueBalance());
        //赠送费用
        BigDecimal freeCost = new BigDecimal(storeInfo.getFreeCost());
        //赠送费用余额
        Double freeCostBalance1=storeInfo.getFreeCostBalance();
        List<CouponShareRequest> detailList = new ArrayList<>();
        for (ErpOrderItem item :
                itemList) {
            CouponShareRequest itemRequest = new CouponShareRequest();
            itemRequest.setLineCode(item.getLineCode());
            itemRequest.setSkuCode(item.getSkuCode());
            //实收数量
            itemRequest.setProductCount(item.getProductCount());
            //实收商品总价=实收数量X单价
            itemRequest.setTotalProductAmount(item.getProductAmount().multiply(new BigDecimal(item.getActualProductCount())));
            itemRequest.setProductPropertyCode(item.getProductPropertyCode());
            itemRequest.setTotalPreferentialAmount(item.getTotalPreferentialAmount());
            itemRequest.setPreferentialAmount(item.getTotalPreferentialAmount().divide(new BigDecimal(item.getProductCount()),2, RoundingMode.DOWN));
            itemRequest.setApinCouponAmount(BigDecimal.ZERO);
            itemRequest.setProductGift(item.getProductType());
            detailList.add(itemRequest);
        }
        //A品券计算均摊金额
//        couponSharePrice(detailList, topCouponCodeList);

        Map<Long, CouponShareRequest> map = new HashMap<>(16);
        for (CouponShareRequest item :
                detailList) {
            map.put(item.getLineCode(), item);
        }

        ErpOrderFee orderFee = new ErpOrderFee();

        //订单总额（元）
        BigDecimal totalMoneyTotal = BigDecimal.ZERO;
        //活动优惠金额（元）
        BigDecimal activityMoneyTotal = BigDecimal.ZERO;
        //服纺券优惠金额（元）
        BigDecimal suitCouponMoneyTotal = BigDecimal.ZERO;
        //A品券优惠金额（元）
        BigDecimal topCouponMoneyTotal = BigDecimal.ZERO;
        //实付金额（元）
        BigDecimal payMoneyTotal = BigDecimal.ZERO;

        for (ErpOrderItem item :
                itemList) {
            CouponShareRequest couponShareRequest = map.get(item.getLineCode());
            item.setTotalPreferentialAmount(couponShareRequest.getTotalPreferentialAmount());
            item.setPreferentialAmount(couponShareRequest.getPreferentialAmount());
            item.setTopCouponDiscountAmount(couponShareRequest.getApinCouponAmount());
            item.setTotalAcivityAmount(item.getActivityDiscountAmount().add(item.getTopCouponDiscountAmount()));

            totalMoneyTotal = totalMoneyTotal.add(item.getTotalProductAmount());
            activityMoneyTotal = activityMoneyTotal.add(item.getActivityDiscountAmount());
            topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponDiscountAmount());
        }
        //计算比例系数=总金额/赠送总市值
        BigDecimal pro=totalMoneyTotal.divide(marketValue);
        //根据系数，算实付金额=赠送费用X系数
        BigDecimal shiFuAmount=pro.multiply(pro);
        //统计分摊总金额和，用于最后一行做减法
        BigDecimal fenTanAmount=BigDecimal.ZERO;
        //遍历详情从新算金额
        for(int i=0;i<itemList.size();i++){
            ErpOrderItem item=itemList.get(i);
            BigDecimal productAmount = item.getProductAmount();
            BigDecimal totalPreferentialAmount=productAmount.divide(totalMoneyTotal).multiply(shiFuAmount);
            fenTanAmount=fenTanAmount.add(totalPreferentialAmount);
            item.setTotalPreferentialAmount(totalPreferentialAmount);
            if(i==itemList.size()){
                //最后一行做减法
                item.setPreferentialAmount(shiFuAmount.subtract(fenTanAmount));
            }
            item.setPreferentialAmount(totalPreferentialAmount.divide(new BigDecimal(item.getActualProductCount())));
//                item.setTopCouponDiscountAmount(couponShareRequest.getApinCouponAmount());
            item.setTotalAcivityAmount(item.getActivityDiscountAmount().add(item.getTopCouponDiscountAmount()));

            totalMoneyTotal = totalMoneyTotal.add(item.getTotalProductAmount());
            activityMoneyTotal = activityMoneyTotal.add(item.getActivityDiscountAmount());
            topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponDiscountAmount());
        }

        //支付金额
//        BigDecimal zhi=totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(topCouponMoneyTotal);
//        //赠送市值余额--用于修改
//        marketValueBalance = marketValueBalance.subtract(totalMoneyTotal);
//        //赠送费用余额--用于修改
//        freeCostBalance1=freeCostBalance1-zhi.doubleValue();
        //

        orderFee.setTotalMoney(totalMoneyTotal);
        orderFee.setActivityMoney(activityMoneyTotal);
        orderFee.setSuitCouponMoney(suitCouponMoneyTotal);
        orderFee.setTopCouponMoney(topCouponMoneyTotal);
        orderFee.setPayMoney(totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(topCouponMoneyTotal));
        if(null!=topCouponCodeList&&topCouponCodeList.size()>0){
            orderFee.setTopCouponCodes(String.join(",", topCouponCodeList));
        }else {
            orderFee.setTopCouponCodes("");
        }
        return orderFee;
    }

    /**
     * 判断拆单的子订单是否已经全部发货完成
     * @param splitOrderCode
     * @return
     */
    private Boolean checkComplete(String splitOrderCode){
        Boolean flag=false;
        ErpOrderInfo po=new ErpOrderInfo();
        po.setOrderStoreCode(splitOrderCode);
        List<ErpOrderInfo> select = erpOrderInfoDao.select(po);
        if(select!=null&&select.size()>0){
            ErpOrderInfo erpOrderInfo=select.get(0);
            if(erpOrderInfo!=null&&StringUtils.isNotBlank(erpOrderInfo.getMainOrderCode())){
                OrderSplitsNum orderSplitsNum = orderSplitsNumDao.selectByOrderCode(erpOrderInfo.getMainOrderCode());
                if(null!=orderSplitsNum&&null!=orderSplitsNum.getNum()){
                    Integer num=orderSplitsNum.getNum();
                    if(num.equals(1)){//本次发货完成为最后一次
                        flag=true;
                    }
                    if(!num.equals(0)){
                        //更新完成次数
                        OrderSplitsNum record=new OrderSplitsNum();
                        record.setOrderCode(erpOrderInfo.getMainOrderCode());
                        record.setNum(num-1);
                        orderSplitsNumDao.updateByOrderCode(record);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 修改slcs门店市值赠余额
     */
    private void upGive(String storeCode,Double marketValueBalance,Double freeCostBalance){
        log.info("修改slcs门店市值赠余额,入参storeCode={},marketValueBalance={},freeCostBalance={}",storeCode,marketValueBalance,freeCostBalance);
        String url=slcsHost+"/store/updateValue";
        JSONObject json=new JSONObject();
        StoreMarketValueResponse storeMarketValueResponse=new StoreMarketValueResponse();
        storeMarketValueResponse.setFreeCostBalance(freeCostBalance);
        storeMarketValueResponse.setMarketValueBalance(marketValueBalance);
        storeMarketValueResponse.setStoreCode(storeCode);
        json.put("storeMarketValueResponse",storeMarketValueResponse);
        log.info("修改slcs门店市值赠余额,调用修改赠送市值余额接口,url={},parm={}",url,json);
        HttpClient httpClient = HttpClient.post(url).json(json);
        Map<String ,Object> res=null;
        res = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("同步到虚拟资产:"+res);
        if(res!=null&&"0".equals(res.get("code"))){
            log.info("修改slcs门店市值赠余额,调用修改赠送市值余额接口,成功");
        }

    }

    //计算分摊
    private void chai(List<ErpOrderItem> orderItemList){
        log.info("修改订单为发货状态---全部发货完成,进行分摊计算--子单明细入参 orderItemList={}",orderItemList);
        //遍历原订单明细行
        for (ErpOrderItem item : orderItemList) {
            //拆出来的行均摊总金额 = 原始订单分摊单价 * 实收数量
            BigDecimal totalPreferentialAmount = item.getPreferentialAmount().multiply(new BigDecimal(item.getActualProductCount()));
            //拆出来的行活动优惠金额=原始订单活动单价 * 实收数量
            BigDecimal totalAcivityAmount = item.getActivityPrice().multiply(new BigDecimal(item.getActualProductCount()));
            item.setTotalPreferentialAmount(totalPreferentialAmount);
            item.setTotalAcivityAmount(totalAcivityAmount);
        }
    }

}
