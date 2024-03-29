package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayWayEnum;
import com.aiqin.mgs.order.api.dao.OrderSplitsNumDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderLogisticsDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderOperationLogDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderSplitsNum;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetail;
import com.aiqin.mgs.order.api.domain.po.gift.NewStoreGradient;
import com.aiqin.mgs.order.api.domain.po.gift.StoreGradient;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCarryOutNextStepRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderProductItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSignRequest;
import com.aiqin.mgs.order.api.domain.response.ReturnOrder;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderItemSplitGroupResponse;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import com.aiqin.mgs.order.api.service.SequenceGeneratorService;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.gift.GiftQuotasUseDetailService;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import com.aiqin.mgs.order.api.util.CopyBeanUtil;
import com.aiqin.mgs.order.api.util.DLRequestUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ErpOrderInfoServiceImpl implements ErpOrderInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderInfoServiceImpl.class);

    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private PurchaseOrderService purchaseOrderService;
    @Resource
    private SequenceGeneratorService sequenceGeneratorService;
    @Resource
    private ReturnOrderInfoService returnOrderInfoService;
    @Resource
    private ErpOrderPayNoTransactionalService erpOrderPayNoTransactionalService;
    @Resource
    private ErpOrderCancelNoTransactionalService erpOrderCancelNoTransactionalService;
    @Resource
    private ErpStoreLockDetailsService erpStoreLockDetailsService;
    @Resource
    private OrderSplitsNumDao orderSplitsNumDao;
    @Resource
    private CouponRuleService couponRuleService;
    @Resource
    private BridgeProductService bridgeProductService;
    @Resource
    private GiftQuotasUseDetailService giftQuotasUseDetailService;
    @Resource
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Resource
    private ErpOrderOperationLogDao erpOrderOperationLogDao;
    @Resource
    private ErpOrderLogisticsDao erpOrderLogisticsDao;

    @Value("${bridge.url.dl}")
    private String dlUrl;

    @Value("${bridge.key.dl}")
    private String dlKey;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(ErpOrderInfo po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        po.setUseStatus(StatusEnum.YES.getCode());
        Integer insert = erpOrderInfoDao.insert(po);

        //保存日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderStoreCode(), ErpLogOperationTypeEnum.ADD, ErpOrderStatusEnum.ORDER_STATUS_1.getCode(), null, auth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderNoLog(ErpOrderInfo po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderInfoDao.insert(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByPrimaryKeySelective(ErpOrderInfo po, AuthToken auth) {

        //更新订单数据
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderInfoDao.updateByPrimaryKeySelective(po);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderStoreCode(), ErpLogOperationTypeEnum.ADD, po.getOrderStatus(), null, auth);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByPrimaryKeySelectiveNoLog(ErpOrderInfo po, AuthToken auth) {
        //更新订单数据
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderInfoDao.updateByPrimaryKeySelective(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void orderSplit(String orderCode, AuthToken auth) {
        logger.info("订单拆单--开始,入参orderCode={},auth={}",orderCode,auth);
        //原订单
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        logger.info("订单拆单--查询主单返回结果order={}",order);
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        //是否需要执行拆单操作
        boolean flag = false;
        if (ErpOrderStatusEnum.ORDER_STATUS_3.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_5.getCode().equals(order.getOrderNodeStatus())) {
                flag = true;
            }
        }
        logger.info("订单拆单--是否需要执行拆单如果为true则进行拆单,flag={}",flag);
        if (!flag) {
            //不是拆单状态
            return;
        }


        //原订单商品明细
        List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
        logger.info("订单拆单--原订单商品明细 orderItemList={}",orderItemList);
        order.setItemList(orderItemList);
        //原订单日志
        List<ErpOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOrderOperationLogList(order.getOrderStoreCode());
        logger.info("订单拆单--原订单日志 orderOperationLogList={}",orderOperationLogList);
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        logger.info("订单拆单--查询枚举 processTypeEnum={}",processTypeEnum);
        if (processTypeEnum == null) {
            throw new BusinessException("订单数据异常");
        }
        logger.info("订单拆单--拆单类型 processType={}",processTypeEnum.isSplitByRepertory());
        if (processTypeEnum.isSplitByRepertory()) {
            //按照库存分组拆单

            List<ErpOrderInfo> splitOrderList = new ArrayList<>();

            //请求供应链获取分组情况
            //TODO 修改成从锁库的时候保存的数据获取仓库库房分组信息
//            List<ErpOrderItemSplitGroupResponse> lineSplitGroupList = erpOrderRequestService.getRepositorySplitGroup(order);
            //新的数据
            List<ErpOrderItemSplitGroupResponse> lineSplitGroupList = erpStoreLockDetailsService.getNewRepositorySplitGroup(order);
            if (lineSplitGroupList == null || lineSplitGroupList.size() == 0) {
                throw new BusinessException("未获取到供应链商品分组");
            }

            //行号 -（仓库库房 - 数量）
            Map<Long, Map<String, Long>> lineRepertoryMap = new HashMap<>(16);
            //行号 - 参数根据行号分组的list
            Map<Long, List<ErpOrderItemSplitGroupResponse>> lineParamListMap = new HashMap<>(16);
            //仓库库房 -（仓库库房编码名称信息）
            Map<String, ErpOrderItemSplitGroupResponse> repertoryDetailMap = new HashMap<>(16);

            //行分组结果map  仓库库房 - 明细行
            Map<String, List<ErpOrderItem>> repertorySplitMap = new HashMap<>(16);

            //遍历分组参数
            for (ErpOrderItemSplitGroupResponse item :
                    lineSplitGroupList) {
                //行号
                Long lineCode = item.getLineCode();

                //仓库编码
                String transportCenterCode = item.getTransportCenterCode();
                //库房编码
                String warehouseCode = item.getWarehouseCode();
                //仓库编码+库房编码
                String repertoryKey = transportCenterCode + warehouseCode;

                if (lineCode == null) {
                    throw new BusinessException("缺失行号");
                }
                if (StringUtils.isEmpty(transportCenterCode)) {
                    throw new BusinessException("缺失仓库编码");
                }
                if (StringUtils.isEmpty(warehouseCode)) {
                    throw new BusinessException("缺失库房编码");
                }
                if (item.getLockCount() == null) {
                    throw new BusinessException("缺失锁定数量");
                }

                //记录仓库库房编码名称
                if (!repertoryDetailMap.containsKey(repertoryKey)) {
                    repertoryDetailMap.put(repertoryKey, item);
                }

                //行号对应参数再按照仓库编码分组
                Map<String, Long> mapItem = new HashMap<>(16);
                if (lineRepertoryMap.containsKey(lineCode)) {
                    mapItem = lineRepertoryMap.get(lineCode);
                }
                mapItem.put(repertoryKey, item.getLockCount());
                lineRepertoryMap.put(lineCode, mapItem);

                //行号对应的拆分数据
                List<ErpOrderItemSplitGroupResponse> list = new ArrayList<>();
                if (lineParamListMap.containsKey(lineCode)) {
                    list.addAll(lineParamListMap.get(lineCode));
                }
                list.add(item);
                lineParamListMap.put(lineCode, list);
            }
            logger.info("订单拆单--行号 -（仓库库房 - 数量）lineRepertoryMap={}",lineRepertoryMap);
            logger.info("订单拆单--行号 - 参数根据行号分组的list lineParamListMap={}",lineParamListMap);
            logger.info("订单拆单--行号 -仓库库房 -（仓库库房编码名称信息） repertoryDetailMap={}",repertoryDetailMap);
            //遍历原订单明细行
            for (ErpOrderItem item :
                    orderItemList) {

                if (!lineParamListMap.containsKey(item.getLineCode())) {
                    throw new BusinessException("未找到行号为" + item.getLineCode() + "的商品分组信息");
                }

                //该行的拆分结果参数
                List<ErpOrderItemSplitGroupResponse> lineParamList = lineParamListMap.get(item.getLineCode());
                //订货数量汇总
                long lineProductCount = 0L;
                //优惠分摊总金额（分摊后金额）
                BigDecimal lineTotalPreferentialAmount = BigDecimal.ZERO;
                //活动优惠总金额
                BigDecimal lineTotalAcivityAmount = BigDecimal.ZERO;

                //遍历原订单明细行一行的拆分情况
                for (int i = 0; i < lineParamList.size(); i++) {
                    ErpOrderItemSplitGroupResponse lineParam = lineParamList.get(i);
                    String repertoryKey = lineParam.getTransportCenterCode() + lineParam.getWarehouseCode();
                    List<ErpOrderItem> lineSplitList = new ArrayList<>();
                    if (repertorySplitMap.containsKey(repertoryKey)) {
                        lineSplitList.addAll(repertorySplitMap.get(repertoryKey));
                    }

                    Long lockCount = lineParam.getLockCount();

                    lineProductCount += lockCount;

                    ErpOrderItem newSplitItem = new ErpOrderItem();
                    CopyBeanUtil.copySameBean(newSplitItem, item);
                    newSplitItem.setLineCode(null);

                    //拆出来的行订货金额
                    newSplitItem.setTotalProductAmount(item.getProductAmount().multiply(new BigDecimal(lockCount)));
                    //拆出来的行订货数量
                    newSplitItem.setProductCount(lockCount);
                    if (i < lineParamList.size() - 1) {

                        //拆出来的行均摊总金额--原来的逻辑=原始订单行总价*（锁库数量/商品总数量）
//                        BigDecimal totalPreferentialAmount = item.getTotalProductAmount().multiply(new BigDecimal(lockCount)).divide(new BigDecimal(item.getProductCount()), 2, RoundingMode.HALF_UP);
                        //拆出来的行均摊总金额 = 原始订单分摊总价*（锁库数量/商品总数量）
                        BigDecimal totalPreferentialAmount = item.getTotalPreferentialAmount().multiply(new BigDecimal(lockCount)).divide(new BigDecimal(item.getProductCount()), 2, RoundingMode.HALF_UP);
                        logger.info("订单拆单--行号 -拆单 lockCount={},totalPreferentialAmount={}",lockCount,totalPreferentialAmount);
                        lineTotalPreferentialAmount = lineTotalPreferentialAmount.add(totalPreferentialAmount);
                        newSplitItem.setTotalPreferentialAmount(totalPreferentialAmount);

                        //拆出来的行活动优惠金额
                        BigDecimal totalAcivityAmount = item.getTotalAcivityAmount().multiply(new BigDecimal(lockCount)).divide(new BigDecimal(item.getProductCount()), 2, RoundingMode.HALF_UP);
                        lineTotalAcivityAmount = lineTotalAcivityAmount.add(totalAcivityAmount);
                        newSplitItem.setTotalAcivityAmount(totalAcivityAmount);

                    } else {
                        //拆出来的行均摊总金额=原始分摊总金额-本次遍历优惠分摊总金额（分摊后金额）
                        newSplitItem.setTotalPreferentialAmount(item.getTotalPreferentialAmount().subtract(lineTotalPreferentialAmount));
//                        newSplitItem.setTotalPreferentialAmount(item.getTotalProductAmount().subtract(lineTotalPreferentialAmount));

                        //拆出来的行活动优惠金额
                        newSplitItem.setTotalAcivityAmount(item.getTotalAcivityAmount().subtract(lineTotalAcivityAmount));
                    }
                    logger.info("订单拆单--行号 -拆单newSplitItem={}",newSplitItem);
                    lineSplitList.add(newSplitItem);
                    repertorySplitMap.put(repertoryKey, lineSplitList);

                }

                if (!item.getProductCount().equals(lineProductCount)) {
                    throw new BusinessException("行号为" + item.getLineCode() + "的行库存分组数不等于订货数量");
                }

            }
            logger.info("行分组结果map  仓库库房 - 明细行 repertorySplitMap={}",repertorySplitMap);
            if (repertorySplitMap.size() > 1) {
                //多个仓库库房组，拆分订单

                for (Map.Entry<String, List<ErpOrderItem>> entry :
                        repertorySplitMap.entrySet()) {
                    ErpOrderInfo newOrder = new ErpOrderInfo();
                    CopyBeanUtil.copySameBean(newOrder, order);

                    String newOrderCode = sequenceGeneratorService.generateOrderCode();
                    String orderId = OrderPublic.getUUID();
                    newOrder.setId(null);
                    newOrder.setOrderStoreCode(newOrderCode);
                    newOrder.setOrderStoreId(orderId);
                    newOrder.setOrderLevel(ErpOrderLevelEnum.SECONDARY.getCode());
                    newOrder.setMainOrderCode(order.getOrderStoreCode());
                    String repertoryKey = entry.getKey();
                    ErpOrderItemSplitGroupResponse repertoryDetail = repertoryDetailMap.get(repertoryKey);
                    newOrder.setTransportCenterCode(repertoryDetail.getTransportCenterCode());
                    newOrder.setTransportCenterName(repertoryDetail.getTransportCenterName());
                    newOrder.setWarehouseCode(repertoryDetail.getWarehouseCode());
                    newOrder.setWarehouseName(repertoryDetail.getWarehouseName());

                    List<ErpOrderItem> splitItemList = new ArrayList<>();

                    //商品总价
                    BigDecimal totalProductAmount = BigDecimal.ZERO;
                    //实际支付金额
                    BigDecimal orderAmount = BigDecimal.ZERO;
                    //商品毛重(kg)
                    BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
                    //商品包装体积(mm³)
                    BigDecimal boxVolumeTotal = BigDecimal.ZERO;

                    long lineCode = 1L;
                    for (ErpOrderItem item :
                            entry.getValue()) {

                        ErpOrderItem newOrderItem = new ErpOrderItem();
                        CopyBeanUtil.copySameBean(newOrderItem, item);
                        newOrderItem.setId(null);
                        newOrderItem.setOrderStoreId(orderId);
                        newOrderItem.setOrderStoreCode(newOrderCode);
                        newOrderItem.setOrderInfoDetailId(OrderPublic.getUUID());
                        newOrderItem.setLineCode(lineCode++);

                        //新拆出来的明细增加仓库信息
                        newOrderItem.setTransportCenterCode(repertoryDetail.getTransportCenterCode());
                        newOrderItem.setTransportCenterName(repertoryDetail.getTransportCenterName());
                        newOrderItem.setWarehouseCode(repertoryDetail.getWarehouseCode());
                        newOrderItem.setWarehouseName(repertoryDetail.getWarehouseName());

                        splitItemList.add(newOrderItem);

                        //商品总价
                        totalProductAmount = totalProductAmount.add(item.getTotalProductAmount());
                        //实际支付金额 取分摊后金额汇总
                        orderAmount = orderAmount.add(item.getTotalPreferentialAmount());
                        //商品毛重汇总
                        boxGrossWeightTotal = boxGrossWeightTotal.add((newOrderItem.getBoxGrossWeight() == null ? BigDecimal.ZERO : newOrderItem.getBoxGrossWeight()).multiply(new BigDecimal(newOrderItem.getProductCount())));
                        //商品体积汇总
                        boxVolumeTotal = boxVolumeTotal.add((newOrderItem.getBoxVolume() == null ? BigDecimal.ZERO : newOrderItem.getBoxVolume()).multiply(new BigDecimal(newOrderItem.getProductCount())));

                    }
                    newOrder.setTotalProductAmount(totalProductAmount);
                    newOrder.setOrderAmount(orderAmount);
                    newOrder.setDiscountAmount(totalProductAmount.subtract(orderAmount));
                    newOrder.setTotalWeight(boxGrossWeightTotal);
                    newOrder.setTotalVolume(boxVolumeTotal);
                    newOrder.setItemList(orderItemList);

                    erpOrderItemService.saveOrderItemList(splitItemList, auth);
                    erpOrderOperationLogService.copySplitOrderLog(newOrderCode, orderOperationLogList);
                    this.saveOrderNoLog(newOrder, auth);
                    splitOrderList.add(newOrder);

                }

                order.setSplitStatus(StatusEnum.YES.getCode());
            }else{
                for (Map.Entry<String, List<ErpOrderItem>> entry :
                        repertorySplitMap.entrySet()) {
                    for (ErpOrderItem item :
                            entry.getValue()) {

                        //新拆出来的明细增加仓库信息
                        String repertoryKey = entry.getKey();
                        ErpOrderItemSplitGroupResponse repertoryDetail = repertoryDetailMap.get(repertoryKey);
                        item.setTransportCenterCode(repertoryDetail.getTransportCenterCode());
                        item.setTransportCenterName(repertoryDetail.getTransportCenterName());
                        item.setWarehouseCode(repertoryDetail.getWarehouseCode());
                        item.setWarehouseName(repertoryDetail.getWarehouseName());
                    }
                    erpOrderItemService.updateOrderItemList(entry.getValue(),auth);
                }
            }

            if (splitOrderList.size() > 0) {
                for (ErpOrderInfo item :
                        splitOrderList) {
                    ErpOrderInfo updateOrder = erpOrderQueryService.getOrderByOrderCode(item.getOrderStoreCode());
                    updateOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
                    updateOrder.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
                    updateOrder.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode());
                    this.updateOrderByPrimaryKeySelective(updateOrder, auth);
                }
            } else {
                ErpOrderItemSplitGroupResponse repertoryDetail = null;
                for (Map.Entry<String, ErpOrderItemSplitGroupResponse> entry :
                        repertoryDetailMap.entrySet()) {
                    repertoryDetail = entry.getValue();
                    if (repertoryDetail != null) {
                        break;
                    }
                }
                order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode());
                if (repertoryDetail != null) {
                    order.setTransportCenterCode(repertoryDetail.getTransportCenterCode());
                    order.setTransportCenterName(repertoryDetail.getTransportCenterName());
                    order.setWarehouseCode(repertoryDetail.getWarehouseCode());
                    order.setWarehouseName(repertoryDetail.getWarehouseName());
                }
            }
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
            this.updateOrderByPrimaryKeySelective(order, auth);
            //删除本地锁库明细
//            for(ErpOrderItemSplitGroupResponse res1:lineSplitGroupList){
//                //删除本地缓存
//                erpStoreLockDetailsService.deleteBySkuCode(orderCode,res1.getSkuCode());
//            }
            //根据仓库拆单--添加到子订单发货表中
            if(null!=repertorySplitMap&&repertorySplitMap.size()>0){
                insertOrderSplitsNum(order.getOrderStoreCode(),repertorySplitMap.size());
            }

        } else {
            //按照供应商拆单

            //根据供应商把订单行分组
            Map<String, List<ErpOrderItem>> supplierItemMap = new HashMap<>(16);
            Map<String, String> supplierCodeNameMap = new HashMap<>(16);
            for (ErpOrderItem item :
                    orderItemList) {
                String supplierCode = null;
                List<ErpOrderItem> list = new ArrayList<>();
                if (StringUtils.isNotEmpty(item.getSupplierCode())) {
                    supplierCode = item.getSupplierCode();
                }
                if (supplierItemMap.containsKey(supplierCode)) {
                    list.addAll(supplierItemMap.get(supplierCode));
                }
                list.add(item);
                supplierItemMap.put(supplierCode, list);
                if (!supplierCodeNameMap.containsKey(supplierCode)) {
                    supplierCodeNameMap.put(supplierCode, item.getSupplierName());
                }
            }

            List<ErpOrderInfo> splitOrderList = new ArrayList<>();
            if (supplierItemMap.size() > 1) {
                //不同的供应商，拆分

                for (Map.Entry<String, List<ErpOrderItem>> entry :
                        supplierItemMap.entrySet()) {
                    ErpOrderInfo newOrder = new ErpOrderInfo();
                    CopyBeanUtil.copySameBean(newOrder, order);

                    String newOrderCode = sequenceGeneratorService.generateOrderCode();
                    String orderId = OrderPublic.getUUID();
                    newOrder.setId(null);
                    newOrder.setOrderStoreCode(newOrderCode);
                    newOrder.setOrderStoreId(orderId);
                    newOrder.setOrderLevel(ErpOrderLevelEnum.SECONDARY.getCode());
                    newOrder.setMainOrderCode(order.getOrderStoreCode());
                    newOrder.setSupplierCode(entry.getKey());
                    newOrder.setSupplierName(supplierCodeNameMap.get(entry.getKey()));

                    List<ErpOrderItem> splitItemList = new ArrayList<>();

                    //商品总价
                    BigDecimal totalProductAmount = BigDecimal.ZERO;
                    //实际支付金额
                    BigDecimal orderAmount = BigDecimal.ZERO;
                    //商品毛重(kg)
                    BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
                    //商品包装体积(mm³)
                    BigDecimal boxVolumeTotal = BigDecimal.ZERO;

                    long lineCode = 1L;
                    for (ErpOrderItem item :
                            entry.getValue()) {

                        ErpOrderItem newOrderItem = new ErpOrderItem();
                        CopyBeanUtil.copySameBean(newOrderItem, item);
                        newOrderItem.setId(null);
                        newOrderItem.setOrderStoreId(orderId);
                        newOrderItem.setOrderStoreCode(newOrderCode);
                        newOrderItem.setOrderInfoDetailId(OrderPublic.getUUID());
                        newOrderItem.setLineCode(lineCode++);

                        splitItemList.add(newOrderItem);

                        //TODO 如果后续增加了活动和优惠券，就需要考虑这样计算精不精确

                        //商品总价
                        totalProductAmount = totalProductAmount.add(item.getTotalProductAmount());

                        //实际支付金额 取分摊后金额汇总
                        orderAmount = orderAmount.add(item.getTotalPreferentialAmount());

                        //商品毛重汇总
                        boxGrossWeightTotal = boxGrossWeightTotal.add((newOrderItem.getBoxGrossWeight() == null ? BigDecimal.ZERO : newOrderItem.getBoxGrossWeight()).multiply(new BigDecimal(newOrderItem.getProductCount())));
                        //商品体积汇总
                        boxVolumeTotal = boxVolumeTotal.add((newOrderItem.getBoxVolume() == null ? BigDecimal.ZERO : newOrderItem.getBoxVolume()).multiply(new BigDecimal(newOrderItem.getProductCount())));

                    }
                    newOrder.setTotalProductAmount(totalProductAmount);
                    newOrder.setOrderAmount(orderAmount);
                    newOrder.setDiscountAmount(totalProductAmount.subtract(orderAmount));
                    newOrder.setTotalWeight(boxGrossWeightTotal);
                    newOrder.setTotalVolume(boxVolumeTotal);
                    newOrder.setItemList(orderItemList);

                    erpOrderItemService.saveOrderItemList(splitItemList, auth);
                    erpOrderOperationLogService.copySplitOrderLog(newOrderCode, orderOperationLogList);
                    this.saveOrderNoLog(newOrder, auth);
                    splitOrderList.add(newOrder);

                }

                order.setSplitStatus(StatusEnum.YES.getCode());

            }


            if (splitOrderList.size() > 0) {
                for (ErpOrderInfo item :
                        splitOrderList) {
                    ErpOrderInfo updateOrder = erpOrderQueryService.getOrderByOrderCode(item.getOrderStoreCode());
                    updateOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
                    updateOrder.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
                    updateOrder.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode());
                    this.updateOrderByPrimaryKeySelective(updateOrder, auth);
                }
            } else {
                for (Map.Entry<String, String> entry :
                        supplierCodeNameMap.entrySet()) {
                    order.setSupplierCode(entry.getKey());
                    order.setSupplierName(supplierCodeNameMap.get(entry.getKey()));
                    if (entry.getValue() != null) {
                        break;
                    }
                }
                order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_WAIT.getCode());
            }

            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
            this.updateOrderByPrimaryKeySelective(order, auth);

            //根据供应商拆单--添加到子订单发货表中
            if(null!=supplierItemMap&&supplierItemMap.size()>0){
                insertOrderSplitsNum(order.getOrderStoreCode(),supplierItemMap.size());
            }

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void orderSendToSupply(String orderCode, AuthToken auth) {
        logger.info("同步订单到供应链--入参,orderCode={}",orderCode);
        logger.info("同步订单到供应链--入参, auth={}",auth);
        List<ErpOrderInfo> list = new ArrayList<>();
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        logger.info("同步订单到供应链--原始订单信息, order={}",order);
        if (ErpOrderLevelEnum.PRIMARY.getCode().equals(order.getOrderLevel()) && StatusEnum.YES.getCode().equals(order.getSplitStatus())) {
            list.addAll(erpOrderQueryService.getSecondOrderListByPrimaryCode(orderCode));
        } else {
            list.add(order);
        }
        logger.info("同步订单到供应链--拆单信息, list={}", JsonUtil.toJson(list));
        List<ErpOrderInfo> newList = new ArrayList<>();
        for (ErpOrderInfo item :
                list) {
            if (ErpOrderStatusEnum.ORDER_STATUS_4.getCode().equals(item.getOrderStatus())) {
                if (ErpOrderNodeStatusEnum.STATUS_6.getCode().equals(item.getOrderNodeStatus())) {
                    List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(item.getOrderStoreId());
                    item.setItemList(itemList);
                    //同步订单到供应链，只调用一次接口，不管成功失败都算执行完成这一步
                    //HttpResponse httpResponse = purchaseOrderService.createPurchaseOrder(item);
                    newList.add(item);
                }
            }
        }
        logger.info("同步订单到供应链--拆单信息--真正入参, newList={}", JsonUtil.toJson(newList));
        if(newList!=null&&newList.size()>0){
            //同步订单到供应链，只调用一次接口，不管成功失败都算执行完成这一步
            HttpResponse httpResponse = purchaseOrderService.createPurchaseOrder(newList);
        }else {
            throw new BusinessException("同步订单到供应链--拆单信息为空");
        }

        for (ErpOrderInfo item :
                list) {
            if (ErpOrderStatusEnum.ORDER_STATUS_4.getCode().equals(item.getOrderStatus())) {
                if (ErpOrderNodeStatusEnum.STATUS_6.getCode().equals(item.getOrderNodeStatus())) {
                    List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(item.getOrderStoreId());
                    item.setItemList(itemList);

                    //同步订单到供应链，只调用一次接口，不管成功失败都算执行完成这一步
//                    HttpResponse httpResponse = purchaseOrderService.createPurchaseOrder(item);

                    if (ErpOrderTypeEnum.ASSIST_PURCHASING.getValue().equals(item.getOrderTypeCode())) {
                        //如果是货架订单，直接变成已签收状态

                        long actualProductCount = 0L;
                        for (ErpOrderItem orderLineItem :
                                itemList) {
                            actualProductCount += orderLineItem.getProductCount();
                            orderLineItem.setActualProductCount(orderLineItem.getProductCount());
                            orderLineItem.setActualInboundCount(orderLineItem.getProductCount());
                        }
                        erpOrderItemService.updateOrderItemList(itemList, auth);

                        Date now = new Date();
                        item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_13.getCode());
                        item.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_12.getCode());
                        item.setActualProductCount(actualProductCount);
                        item.setActualTotalProductAmount(item.getOrderAmount());
//                        item.setDeliveryTime(now);
                        item.setActualTotalVolume(item.getTotalVolume());
                        item.setActualTotalWeight(item.getTotalWeight());
//                        item.setTransportStatus(StatusEnum.YES.getCode());
//                        item.setTransportTime(now);
                        item.setReceiveTime(now);
                        this.updateOrderByPrimaryKeySelective(item, auth);
                    } else {
                        //非货架订单同步之后修改订单状态为等待拣货状态
                        item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_6.getCode());
                        item.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_8.getCode());
                        this.updateOrderByPrimaryKeySelective(item, auth);
                    }

                }
            }
        }
        //同步订单数据到结算开始
        for (ErpOrderInfo item:list){
            ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(item.getFeeId());
            item.setOrderFee(orderFee);
            List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(item.getOrderStoreId());
            item.setItemList(itemList);
        }
        bridgeProductService.settlementSaveOrder(list,1);
        //同步订单数据到结算结束
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSign(ErpOrderSignRequest erpOrderSignRequest) {
        logger.info("签收开始--erpOrderSignRequest={}",erpOrderSignRequest);
        if (erpOrderSignRequest == null || StringUtils.isEmpty(erpOrderSignRequest.getPersonId())) {
            throw new BusinessException("缺失用户id");
        }
        if (StringUtils.isEmpty(erpOrderSignRequest.getPersonName())) {
            throw new BusinessException("缺失用户名称");
        }
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderSignRequest.getPersonId());
        auth.setPersonName(erpOrderSignRequest.getPersonName());
        if (StringUtils.isEmpty(erpOrderSignRequest.getOrderCode())) {
            throw new BusinessException("订单编号为空");
        }

        List<ErpOrderProductItemRequest> paramItemList = erpOrderSignRequest.getItemList();
        if (paramItemList == null || paramItemList.size() <= 0) {
            throw new BusinessException("商品明细为空");
        }
        Map<Long, ErpOrderProductItemRequest> orderItemSignMap = new HashMap<>(16);
        for (ErpOrderProductItemRequest item :
                paramItemList) {
            if (item.getLineCode() == null) {
                throw new BusinessException("缺失行号");
            }
            if (item.getActualInboundCount() == null || item.getActualInboundCount() < 0) {
                throw new BusinessException("第" + item.getLineCode() + "行签收数量有误");
            }
            orderItemSignMap.put(item.getLineCode(), item);
        }

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderSignRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        //节点控制类型
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum == null) {
            throw new BusinessException("订单类型有误");
        }
        if (processTypeEnum.isHasLogisticsFee()) {
            //需要先支付物流费用
            if (!ErpOrderStatusEnum.ORDER_STATUS_12.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该订单不是" + ErpOrderStatusEnum.ORDER_STATUS_12.getDesc() + "的订单，不能签收");
            }
        } else {
            if (!ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(order.getOrderStatus())) {
                throw new BusinessException("该订单不是" + ErpOrderStatusEnum.ORDER_STATUS_11.getDesc() + "的订单，不能签收");
            }
        }
        List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
        List<ErpOrderItem> updateItemList = new ArrayList<>();
        //只有18A商品会发放赠品额度
        BigDecimal commodityAmountOfTop=BigDecimal.ZERO;
        //查询A品卷使用规则code Map
        Map ruleMap=couponRuleService.couponRuleMap();

        for (ErpOrderItem item :
                itemList) {
            if (!orderItemSignMap.containsKey(item.getLineCode())) {
                throw new BusinessException("第" + item.getLineCode() + "行签收信息为空");
            }
            ErpOrderProductItemRequest signItem = orderItemSignMap.get(item.getLineCode());
            ErpOrderItem updateItem = new ErpOrderItem();
            updateItem.setId(item.getId());
            updateItem.setActualInboundCount(signItem.getActualInboundCount());
            updateItem.setSignDifferenceReason(signItem.getSignDifferenceReason());
            updateItemList.add(updateItem);

            //订单类型为配送    订单类别为普通补货  【只有这个组合类型调物流卷和发放赠品额度】
            if(ErpOrderTypeEnum.DISTRIBUTION.getCode().toString().equals(order.getOrderTypeCode())
                    && ErpOrderCategoryEnum.ORDER_TYPE_1.getCode().toString().equals(order.getOrderCategoryCode())
                    &&ruleMap.containsKey(item.getProductPropertyCode())
                    && ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductType())){
                commodityAmountOfTop=commodityAmountOfTop.add(item.getTotalPreferentialAmount());
            }


        }
        erpOrderItemService.updateOrderItemList(updateItemList, auth);

        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_13.getCode());
        order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_12.getCode());
        order.setReceiveTime(new Date());
        this.updateOrderByPrimaryKeySelective(order, auth);

        /*****************************************签收完毕，发放赠品额度start*****************************************/
        logger.info("签收结束--判断是否发放赠品额度--主订单信息为 order={}",JsonUtil.toJson(order));


        logger.info("签收完毕--主订单商品18A类型总金额为commodityAmountOfTop={}",commodityAmountOfTop);
            //判断18A商品金额总和大于0
        if(commodityAmountOfTop.compareTo(BigDecimal.ZERO)>0){
            //赠品返回额度比例
            Double rebatesProportion=0.00;
            NewStoreGradient gradient=bridgeProductService.selectStoreGiveawayByStoreCode(order.getStoreCode());

            if(null!=gradient&&null!=gradient.getStoreGradientList()&&gradient.getStoreGradientList().size()>0){
                for(StoreGradient storeGradient:gradient.getStoreGradientList()){
                    if(commodityAmountOfTop.compareTo(BigDecimal.valueOf(storeGradient.getMinimumValue()))>0){
                        rebatesProportion=storeGradient.getRebatesProportion();
                    }
                }
                logger.info("签收完毕--赠品比例为 rebatesProportion={}",rebatesProportion);
                if(rebatesProportion>0){
                    //18A主商品金额总和乘以赠品比例
                    commodityAmountOfTop=commodityAmountOfTop.multiply(BigDecimal.valueOf(rebatesProportion)).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);

                    //查詢門店员赠品额度
                    BigDecimal availableGiftQuota=bridgeProductService.getStoreAvailableGiftGuota(order.getStoreId());
                    //计算订单赠品额度发放后的赠品额度
                    BigDecimal newAvailableGiftQuota=availableGiftQuota.add(commodityAmountOfTop);
                    //更新订单使用过后的赠品额度
                    bridgeProductService.updateAvailableGiftQuota(order.getStoreId(),newAvailableGiftQuota);
                    logger.info("签收完毕--赠品额度更新信息为：应发放赠品额度 commodityAmountOfTop={}",commodityAmountOfTop
                            +"查詢門店员赠品额度 availableGiftQuota={}"+availableGiftQuota+"计算订单赠品额度发放后的赠品额度 newAvailableGiftQuota={}"+newAvailableGiftQuota);
                    //新建一个赠品额度使用明细对象
                    GiftQuotasUseDetail giftQuotasUseDetail=new GiftQuotasUseDetail();
                    giftQuotasUseDetail.setStoreId(order.getStoreId());
                    giftQuotasUseDetail.setChangeInGiftQuota("+"+commodityAmountOfTop);
                    giftQuotasUseDetail.setBillCode(order.getOrderStoreCode());
                    giftQuotasUseDetail.setType(3);
                    giftQuotasUseDetail.setCreateBy(auth.getPersonName());
                    giftQuotasUseDetail.setUpdateBy(auth.getPersonName());
                    //插入一条赠品明细使用记录
                    giftQuotasUseDetailService.add(giftQuotasUseDetail);

                    //更新订单费用信息表-发放赠品额度字段
                    ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByOrderId(order.getOrderStoreId());
                    if(null!=orderFee){
                        if(null!=orderFee.getComplimentaryAmount()){
                            BigDecimal complimentaryAmount=orderFee.getComplimentaryAmount();
                            complimentaryAmount=complimentaryAmount.add(commodityAmountOfTop);
                            orderFee.setComplimentaryAmount(complimentaryAmount);
                        }else{
                            orderFee.setComplimentaryAmount(commodityAmountOfTop);
                        }

                        erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee,auth);
                    }else{
                        logger.info("分摊计算结束，发放赠品额度-查询支付信息异常，查询订单号为"+order.getOrderStoreCode());
                    }

                }

            }


        }
        /*****************************************分摊计算结束，发放赠品额度end*****************************************/

        /*****************************************同步订单数据到结算开始*****************************************/
        List<ErpOrderInfo> list=new ArrayList<>();
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
        order.setOrderFee(orderFee);
        order.setItemList(itemList);
        list.add(order);
        bridgeProductService.settlementSaveOrder(list,3);
        /*****************************************同步订单数据到结算结束*****************************************/

        //此处调用DL接口，将订单签收信息同步给DL
        syncToDL(order,itemList,orderItemSignMap);

        //首单，修改门店状态
        if (processTypeEnum.getOrderCategoryEnum().isFirstOrder()) {
            erpOrderRequestService.updateStoreStatus(order.getStoreId(), "010201");
        }
    }

    private void syncToDL(ErpOrderInfo order, List<ErpOrderItem> itemList, Map<Long, ErpOrderProductItemRequest> orderItemSignMap) {
        OrderToDL orderToDL=new OrderToDL();
        List<ImplData> implData=new ArrayList();

        for (ErpOrderItem item :
                itemList) {
            ImplData data=new ImplData();
            data.setDetailId(item.getOrderInfoDetailId());
            data.setImpQty(orderItemSignMap.get(item.getLineCode()).getActualInboundCount().toString());
            implData.add(data);
        }
        orderToDL.setImpData(implData);
        orderToDL.setMethod("orderImp");
        orderToDL.setOrderType(order.getOrderTypeCode());
        orderToDL.setOrderStoreCode(order.getOrderStoreId());

        logger.info("签收信息同步到DL开始，参数为{}"+JsonUtil.toJson(orderToDL));

        String sign = DLRequestUtil.EncoderByMd5(dlKey, JsonUtil.toJson(orderToDL));
        HttpClient httpClient = HttpClient.post(dlUrl, "utf-8");
        httpClient.setHeader("Content-Encoding", "UTF-8");
        httpClient.setHeader("key", dlKey);//双方约定的密钥
        httpClient.setHeader("sign", sign);
        httpClient.addParameter("data", JsonUtil.toJson(orderToDL));
        DLResponse response=httpClient.timeout(200000).action().result(DLResponse.class);
        logger.info("订单签收信息同步到DL的回调为{}"+JsonUtil.toJson(response));
    }

    @Override
    public void orderCarryOutNextStep(ErpOrderCarryOutNextStepRequest erpOrderCarryOutNextStepRequest, AuthToken auth) {

        if (erpOrderCarryOutNextStepRequest == null || StringUtils.isEmpty(erpOrderCarryOutNextStepRequest.getOrderCode())) {
            throw new BusinessException("缺失订单号");
        }

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderCarryOutNextStepRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }

        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum == null) {
            throw new BusinessException("订单类型异常");
        }
        ErpOrderNodeStatusEnum orderNodeStatusEnum = ErpOrderNodeStatusEnum.getEnum(order.getOrderNodeStatus());

        //需不需要发起支付或者轮询
        boolean payFlag = false;
        //需不需要支付成功后续操作
        boolean payAfterFlag = false;

        switch (orderNodeStatusEnum) {
            case STATUS_1:
                if (processTypeEnum.isAutoPay()) {
                    payFlag = true;
                }
                break;
            case STATUS_2:
                payFlag = true;
                break;
            case STATUS_3:
                payAfterFlag = true;
                break;
            case STATUS_4:
                if (processTypeEnum.isAutoPay()) {
                    payFlag = true;
                }
                break;
            case STATUS_5:
                payAfterFlag = true;
                break;
            case STATUS_6:
                payAfterFlag = true;
                break;
            default:
                ;
        }

        if (payFlag) {
            //重新发起支付或者轮询结果

            ErpOrderPayRequest erpOrderPayRequest = new ErpOrderPayRequest();
            erpOrderPayRequest.setOrderCode(order.getOrderStoreCode());
            erpOrderPayRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
            erpOrderPayNoTransactionalService.orderPayStartMethodGroup(erpOrderPayRequest, auth, true);
        }
        if (payAfterFlag) {
            //重新执行支付成功后续操作

            erpOrderPayNoTransactionalService.orderPaySuccessMethodGroup(order.getOrderStoreCode(), auth);
        }

        if (ErpOrderNodeStatusEnum.STATUS_31.getCode() <= order.getOrderNodeStatus() && order.getOrderNodeStatus() < ErpOrderNodeStatusEnum.STATUS_37.getCode()) {
            //取消订单后续操作
            erpOrderCancelNoTransactionalService.cancelOrderRequestGroup(order.getOrderStoreCode(), auth);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderScourSheet(String orderCode) {
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order != null) {
            if (ErpOrderScourSheetStatusEnum.WAIT.getCode().equals(order.getScourSheetStatus())) {
                HttpResponse response = returnOrderInfoService.saveWriteDownOrder(orderCode);
                if (RequestReturnUtil.validateHttpResponse(response)) {
                    AuthToken auth = new AuthToken();
                    auth.setPersonId(order.getCreateById());
                    auth.setPersonName(order.getCreateByName());
                    order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.SUCCESS.getCode());
                    this.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderReturnStatus(String orderCode, ErpOrderReturnRequestEnum orderReturnRequestEnum, List<ErpOrderItem> returnQuantityList, String personId, String personName) {

        if (StringUtils.isEmpty(orderCode)) {
            throw new BusinessException("缺失订单号");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderAndItemByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }

        AuthToken auth = new AuthToken();
        auth.setPersonId(personId);
        auth.setPersonName(personName);

        if (orderReturnRequestEnum == ErpOrderReturnRequestEnum.CANCEL) {
            order.setOrderReturnProcess(StatusEnum.NO.getCode());
        } else if (orderReturnRequestEnum == ErpOrderReturnRequestEnum.WAIT) {
            order.setOrderReturnProcess(StatusEnum.YES.getCode());
        } else if (orderReturnRequestEnum == ErpOrderReturnRequestEnum.SUCCESS) {
            order.setOrderReturnProcess(StatusEnum.NO.getCode());

            //修改退货数量
            if (returnQuantityList == null || returnQuantityList.size() == 0) {
                throw new BusinessException("缺失退货数量");
            }
            Map<Long, Long> returnQuantityMap = new HashMap<>(16);
            for (ErpOrderItem item :
                    returnQuantityList) {
                if (item.getLineCode() == null) {
                    throw new BusinessException("退货参数缺失行号");
                }
                if (item.getReturnProductCount() == null) {
                    throw new BusinessException("退货参数缺失数量");
                }
                returnQuantityMap.put(item.getLineCode(), item.getReturnProductCount());
            }


            List<ErpOrderItem> itemList = order.getItemList();
            List<ErpOrderItem> returnUpdateList = new ArrayList<>();
            for (ErpOrderItem item :
                    itemList) {
                if (ErpProductGiftEnum.GIFT.getCode().equals(item.getProductType())) {
                    continue;
                }
                if (returnQuantityMap.containsKey(item.getLineCode())) {

                    //本次退货数量
                    Long thisTimeReturnCount = returnQuantityMap.get(item.getLineCode());
                    if (thisTimeReturnCount > (item.getActualInboundCount() - (item.getReturnProductCount() == null ? 0L : item.getReturnProductCount()))) {
                        throw new BusinessException("行号为" + item.getLineCode() + "的商品退货数量超出最大可退数量");
                    }
                    item.setReturnProductCount((item.getReturnProductCount() == null ? 0L : item.getReturnProductCount()) + thisTimeReturnCount);
                    returnUpdateList.add(item);
                }
            }
            if (returnUpdateList.size() > 0) {
                erpOrderItemService.updateOrderItemList(returnUpdateList, auth);
            }

        }

        //最新商品行数据
        List<ErpOrderItem> newItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());

        //是否发起退货
        boolean returnStartFlag = false;
        //是否全部退货
        boolean returnEndFlag = true;
//        //商品实收数量 ---加
//        Long actualInboundCount = 0L;
        for (ErpOrderItem item :
                newItemList) {
            if (ErpProductGiftEnum.GIFT.getCode().equals(item.getProductType())) {
                continue;
            }
            if (item.getActualInboundCount() > (item.getReturnProductCount() == null ? 0L : item.getReturnProductCount())) {
                //如果有一行没有退完，则不算退货完成
                returnEndFlag = false;
            }
            if ((item.getReturnProductCount() == null ? 0L : item.getReturnProductCount()) > 0L) {
                //如果有一行有退货数量，则算部分退货
                returnStartFlag = true;
            }
//            //获取门店实收数量 --加
//            actualInboundCount = item.getActualInboundCount();
        }

        if (returnEndFlag) {
            order.setOrderReturn(ErpOrderReturnStatusEnum.SUCCESS.getCode());
        } else if (returnStartFlag) {
            order.setOrderReturn(ErpOrderReturnStatusEnum.WAIT.getCode());
        } else {
            order.setOrderReturn(ErpOrderReturnStatusEnum.NONE.getCode());
        }
        this.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        //判断是否还有可退数量-没有就不能发起退货  ---加
        List<ReturnOrder> returnOrders = returnOrderInfoDao.selectReturnOrderCode(orderCode);
        logger.info("查询退货数据中-上单-已退货数量和申请退货数量:{}",returnOrders);
//        Long quantityReturnedCount = 0L;
//        Long returnProductCount = 0L;
//        if (returnOrder == null){
//        }else {
//          quantityReturnedCount = returnOrder.getQuantityReturnedCount() == null? 0L : returnOrder.getQuantityReturnedCount();
//         returnProductCount = returnOrder.getReturnProductCount();
//        }
//       if((returnProductCount + quantityReturnedCount) == actualInboundCount){
//           //修改原订单中的退货流程状态
//           erpOrderInfoDao.updateOrderReturnProcess(orderCode);
//       }else {
//           //修改原订单中的退货流程状态
//           erpOrderInfoDao.updateOrderReturnProcessStatus(orderCode);
//       }
        List<String> returnButtions = new ArrayList<>();
        List<String> noRefund = new ArrayList<>();
        for (ErpOrderItem erpOrderItem : newItemList){
            if (ErpProductGiftEnum.GIFT.getCode().equals(erpOrderItem.getProductType())) {
                continue;
            }
            for (ReturnOrder returnOrder : returnOrders){
                if (erpOrderItem.getSkuCode().equals(returnOrder.getSkuCode())&& erpOrderItem.getSkuName().equals(returnOrder.getSkuName())){
                    Long actualInboundCount = erpOrderItem.getActualInboundCount();
                    Long returnProductCount = returnOrder.getReturnProductCount();
                    Long quantityReturnedCount = returnOrder.getQuantityReturnedCount() == null ? 0L :returnOrder.getQuantityReturnedCount();
                    if ((actualInboundCount - quantityReturnedCount) != 0){
                        if (((actualInboundCount - quantityReturnedCount) - returnProductCount) == 0){//说明没有可退的商品数量，修改订单状态e

                        }else {
                            returnButtions.add("1");
                        }
                    }else { //如果减去已退货等于0，表示没有可退货
                        noRefund.add("2");
                    }
                }
            }
        }
        logger.info("修改退货流程状态结果-noRefund： " + noRefund + ",--returnButtions: " + returnButtions);
        if (CollectionUtils.isEmpty(noRefund) && CollectionUtils.isNotEmpty(returnButtions)){
            logger.info("修改---原订单退货流程节点--无进行退货中");
            erpOrderInfoDao.updateOrderReturnProcessStatus(orderCode);
        }else {
            logger.info("修改---原订单退货流程节点--正在退货");
            erpOrderInfoDao.updateOrderReturnProcess(orderCode);
        }
    }

    @Override
    @Transactional
    public void synchronizeOrders(ErpOrderInfo orderInfo) {
        if (orderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderInfo.getStoreId())) {
            throw new BusinessException("请传入门店id");
        }
        if (orderInfo.getOrderTypeCode() == null) {
            throw new BusinessException("请传入订单类型");
        } else {
            if (!ErpOrderTypeEnum.exist(orderInfo.getOrderTypeCode())) {
                throw new BusinessException("无效的订单类型");
            }
        }
        if (StringUtils.isEmpty(orderInfo.getOrderStoreCode())) {
            throw new BusinessException("缺失订单号");
        }

        StoreInfo storeInfo=erpOrderRequestService.getStoreInfoByOriginStoreId(orderInfo.getStoreId());

        orderInfo.setStoreId(storeInfo.getStoreId());

        AuthToken auth=new AuthToken();
        auth.setPersonId("DL同步");
        auth.setPersonName("DL同步");
        ErpOrderInfo order = erpOrderQueryService.getOrderAndItemByOrderCode(orderInfo.getOrderStoreCode());
        if(null==order){
            //此订单在本地不存在，新增订单
            saveOrder(orderInfo, auth);
            if(null!=orderInfo.getItemList()&&orderInfo.getItemList().size()>0){
                //保存订单明细行
                erpOrderItemService.saveOrderItemList(orderInfo.getItemList(), auth);
            }
            if(null!=orderInfo.getOperationLogList()&&orderInfo.getOperationLogList().size()>0){
                for(ErpOrderOperationLog log:orderInfo.getOperationLogList()){
                    erpOrderOperationLogDao.insert(log);
                }
            }
            if(null!=orderInfo.getOrderLogistics()){
                erpOrderLogisticsDao.insert(orderInfo.getOrderLogistics());
            }
            if(null!=orderInfo.getOrderFee()){
                erpOrderFeeService.saveOrderFee(orderInfo.getOrderFee(),auth);
            }
        }else{
            if(!ErpOrderStatusEnum.ORDER_STATUS_13.getCode().equals(orderInfo.getOrderStatus())){
                updateOrderByOrderStoreId(orderInfo, auth);
            }else{
                updateOrderByOrderStoreId1(orderInfo, auth);
            }

            //删除原有订单明细
            erpOrderItemService.deleteItemByOrderCode(order.getOrderStoreCode());
            //保存订单明细行
            erpOrderItemService.saveOrderItemList(orderInfo.getItemList(), auth);
        }

        //递归保存子订单信息
        if(null!=orderInfo.getSecondaryOrderList()&& orderInfo.getSecondaryOrderList().size()>0){
            for (ErpOrderInfo info:orderInfo.getSecondaryOrderList()){
                synchronizeOrders(info);
            }
        }



    }

    private void updateOrderByOrderStoreId1(ErpOrderInfo orderInfo, AuthToken auth) {
        //更新订单数据
        orderInfo.setUpdateById(auth.getPersonId());
        orderInfo.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderInfoDao.updateOrderByOrderStoreId1(orderInfo);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(orderInfo.getOrderStoreCode(), ErpLogOperationTypeEnum.ADD, orderInfo.getOrderStatus(), null, auth);
    }

    /**
     * 将拆单数量存入拆单数量表中
     * @param num
     * @param orderCode
     */
    private void insertOrderSplitsNum(String orderCode,Integer num){
        OrderSplitsNum record=new OrderSplitsNum();
        record.setNum(num);
        record.setOrderCode(orderCode);
        orderSplitsNumDao.insertSelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByOrderStoreId(ErpOrderInfo po, AuthToken auth) {

        //更新订单数据
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderInfoDao.updateOrderByOrderStoreId(po);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(po.getOrderStoreCode(), ErpLogOperationTypeEnum.ADD, po.getOrderStatus(), null, auth);

    }
}
