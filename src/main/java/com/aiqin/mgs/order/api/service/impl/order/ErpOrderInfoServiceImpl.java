package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCarryOutNextStepRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderEditRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderProductItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSignRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderItemSplitGroupResponse;
import com.aiqin.mgs.order.api.service.bill.PurchaseOrderService;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.CopyBeanUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    private ErpOrderCreateService erpOrderCreateService;
    @Resource
    private PurchaseOrderService purchaseOrderService;

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
    @Transactional(rollbackFor = Exception.class)
    public void addProductGift(ErpOrderEditRequest erpOrderEditRequest) {

        AuthToken auth = AuthUtil.getCurrentAuth();

        if (erpOrderEditRequest == null || StringUtils.isEmpty(erpOrderEditRequest.getOrderCode())) {
            throw new BusinessException("缺失订单编号");
        }
        if (erpOrderEditRequest.getProductGiftList() == null || erpOrderEditRequest.getProductGiftList().size() == 0) {
            throw new BusinessException("缺失赠品行");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderEditRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum == null) {
            throw new BusinessException("异常的订单类型");
        }

        if (!processTypeEnum.isAddProductGift()) {
            throw new BusinessException("该类型的订单不能增加赠品行");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("只有" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "的订单才能增加赠品行");
        }
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
        if (orderFee == null) {
            throw new BusinessException("订单费用信息异常");
        }
        if (ErpPayStatusEnum.PAYING.getCode().equals(orderFee.getPayStatus())) {
            throw new BusinessException("订单正在支付中，不能添加赠品");
        }
        if (ErpPayStatusEnum.SUCCESS.getCode().equals(orderFee.getPayStatus())) {
            throw new BusinessException("订单已经支付成功，不能添加赠品");
        }

        //订单原商品明细行
        List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
        //记录最大的行号
        long maxLineIndex = 0;
        for (ErpOrderItem item :
                orderItemList) {
            maxLineIndex = maxLineIndex > item.getLineCode() ? maxLineIndex : item.getLineCode();
        }

        List<ErpOrderItem> addGiftList = new ArrayList<>();
        int lineIndex = 0;
        for (ErpOrderProductItemRequest item :
                erpOrderEditRequest.getProductGiftList()) {
            lineIndex++;
            maxLineIndex++;
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺失sku");
            }
            if (item.getQuantity() == null) {
                throw new BusinessException("赠品行第" + lineIndex + "行缺少数量");
            }
            ProductInfo product = erpOrderRequestService.getSkuDetail(order.getCompanyCode(), item.getSkuCode());
            if (product == null) {
                throw new BusinessException("赠品行第" + lineIndex + "行商品未找到");
            }

            ErpOrderItem orderItem = new ErpOrderItem();
            //spu编码
            orderItem.setSpuCode(product.getSpuCode());
            //spu名称
            orderItem.setSpuName(product.getSpuName());
            //sku编码
            orderItem.setSkuCode(product.getSkuCode());
            //sku名称
            orderItem.setSkuName(product.getSkuName());
            //条形码
            orderItem.setBarCode(product.getBarCode());
            //图片地址
            orderItem.setPictureUrl(product.getPictureUrl());
            //规格
            orderItem.setProductSpec(product.getProductSpec());
            //颜色编码
            orderItem.setColorCode(product.getColorCode());
            //颜色名称
            orderItem.setColorName(product.getColorName());
            //型号
            orderItem.setModelCode(product.getModelCode());
            //单位编码
            orderItem.setUnitCode(product.getUnitCode());
            //单位名称
            orderItem.setUnitName(product.getUnitName());
            //折零系数 不填
            orderItem.setZeroDisassemblyCoefficient(null);
            //商品类型  0商品 1赠品
            orderItem.setProductType(ErpProductGiftEnum.PRODUCT.getCode());
            //商品属性编码
            orderItem.setProductPropertyCode(product.getProductPropertyCode());
            //商品属性名称
            orderItem.setProductPropertyName(product.getProductPropertyName());
            //供应商编码
            orderItem.setSupplierCode(product.getSupplierCode());
            //供应商名称
            orderItem.setSupplierName(product.getSupplierName());
            //商品数量
            orderItem.setProductCount((long) item.getQuantity());
            //商品单价
            orderItem.setProductAmount(BigDecimal.ZERO);
            //商品总价
            orderItem.setTotalProductAmount(BigDecimal.ZERO);
            //实际商品总价
            orderItem.setActualTotalProductAmount(BigDecimal.ZERO);
            //优惠分摊总金额
            orderItem.setTotalPreferentialAmount(BigDecimal.ZERO);
            //分摊后单价
            orderItem.setTotalPreferentialAmount(BigDecimal.ZERO);
            //活动优惠总金额
            orderItem.setTotalAcivityAmount(BigDecimal.ZERO);
            //实际商品数量
            orderItem.setActualProductCount(null);
            //税率
            orderItem.setTaxRate(product.getTaxRate());
            //公司编码
            orderItem.setCompanyCode(order.getCompanyCode());
            //公司名称
            orderItem.setCompanyName(order.getCompanyName());
            addGiftList.add(orderItem);
        }

        if (processTypeEnum.isLockStock()) {
            //追加锁库存
            erpOrderRequestService.lockStockInSupplyChain(order, addGiftList, auth);
        }

        erpOrderItemService.saveOrderItemList(addGiftList, auth);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSplit(String orderCode, AuthToken auth) {

        //原订单
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
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

        if (!flag) {
            //不是拆单状态
            return;
        }


        //原订单商品明细
        List<ErpOrderItem> orderItemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
        order.setItemList(orderItemList);
        //原订单日志
        List<ErpOrderOperationLog> orderOperationLogList = erpOrderOperationLogService.selectOrderOperationLogList(order.getOrderStoreCode());

        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (processTypeEnum == null) {
            throw new BusinessException("订单数据异常");
        }

        if (processTypeEnum.isSplitByRepertory()) {
            //按照库存分组拆单

            if (1 == 1) {
                //TODO CT 暂时没有真实数据，先跳过拆单步骤
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
                this.updateOrderByPrimaryKeySelective(order, auth);
                return;
            }

            List<ErpOrderInfo> splitOrderList = new ArrayList<>();

            //请求供应链获取分组情况
            List<ErpOrderItemSplitGroupResponse> lineSplitGroupList = erpOrderRequestService.getRepositorySplitGroup(order);
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

                if (lineCode != null) {
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
                lineParamListMap.put(lineCode, list);
            }

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
                    if (i < lineParamList.size() - 1) {

                        //拆出来的行均摊总金额
                        BigDecimal totalPreferentialAmount = item.getTotalProductAmount().multiply(new BigDecimal(lockCount)).divide(new BigDecimal(item.getProductCount()), 4, RoundingMode.HALF_UP);
                        lineTotalPreferentialAmount = lineTotalPreferentialAmount.add(totalPreferentialAmount);
                        newSplitItem.setTotalPreferentialAmount(totalPreferentialAmount);

                        //拆出来的行活动优惠金额
                        BigDecimal totalAcivityAmount = item.getTotalAcivityAmount().multiply(new BigDecimal(lockCount)).divide(new BigDecimal(item.getProductCount()), 4, RoundingMode.HALF_UP);
                        lineTotalAcivityAmount = lineTotalAcivityAmount.add(totalAcivityAmount);
                        newSplitItem.setTotalAcivityAmount(totalAcivityAmount);

                    } else {
                        //最后一条，用减法避免误差

                        //拆出来的行均摊总金额
                        newSplitItem.setTotalPreferentialAmount(item.getTotalPreferentialAmount().subtract(lineTotalPreferentialAmount));

                        //拆出来的行活动优惠金额
                        newSplitItem.setTotalAcivityAmount(item.getTotalAcivityAmount().subtract(lineTotalAcivityAmount));
                    }

                    lineSplitList.add(newSplitItem);
                    repertorySplitMap.put(repertoryKey, lineSplitList);

                }

                if (!item.getProductCount().equals(lineProductCount)) {
                    throw new BusinessException("行号为" + item.getLineCode() + "的行库存分组数不等于订货数量");
                }

            }

            if (repertorySplitMap.size() > 1) {
                //多个仓库库房组，拆分订单

                for (Map.Entry<String, List<ErpOrderItem>> entry :
                        repertorySplitMap.entrySet()) {
                    ErpOrderInfo newOrder = new ErpOrderInfo();
                    CopyBeanUtil.copySameBean(newOrder, order);

                    String newOrderCode = erpOrderCreateService.getOrderCode();
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

                        //商品总价
                        totalProductAmount = totalProductAmount.add(item.getProductAmount());

                        //实际支付金额 取分摊后金额汇总
                        orderAmount = orderAmount.add(item.getTotalPreferentialAmount());

                    }
                    newOrder.setTotalProductAmount(totalProductAmount);
                    newOrder.setOrderAmount(orderAmount);
                    newOrder.setDiscountAmount(totalProductAmount.multiply(orderAmount));
                    newOrder.setItemList(orderItemList);

                    erpOrderItemService.saveOrderItemList(splitItemList, auth);
                    erpOrderOperationLogService.copySplitOrderLog(orderCode, orderOperationLogList);
                    this.saveOrderNoLog(newOrder, auth);
                    splitOrderList.add(newOrder);

                }
            }

            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
            this.updateOrderByPrimaryKeySelective(order, auth);

            if (splitOrderList.size() > 0) {
                for (ErpOrderInfo item :
                        splitOrderList) {
                    item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
                    item.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
                    this.updateOrderByPrimaryKeySelective(item, auth);
                }
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

                    String newOrderCode = erpOrderCreateService.getOrderCode();
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
                        totalProductAmount = totalProductAmount.add(item.getProductAmount());

                        //实际支付金额 取分摊后金额汇总
                        orderAmount = orderAmount.add(item.getTotalPreferentialAmount());

                    }
                    newOrder.setTotalProductAmount(totalProductAmount);
                    newOrder.setOrderAmount(orderAmount);
                    newOrder.setDiscountAmount(totalProductAmount.multiply(orderAmount));
                    newOrder.setItemList(orderItemList);

                    erpOrderItemService.saveOrderItemList(splitItemList, auth);
                    erpOrderOperationLogService.copySplitOrderLog(orderCode, orderOperationLogList);
                    this.saveOrderNoLog(newOrder, auth);
                    splitOrderList.add(newOrder);

                }

                order.setSplitStatus(StatusEnum.YES.getCode());

            }

            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
            this.updateOrderByPrimaryKeySelective(order, auth);

            if (splitOrderList.size() > 0) {
                for (ErpOrderInfo item :
                        splitOrderList) {
                    item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_4.getCode());
                    item.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_6.getCode());
                    this.updateOrderByPrimaryKeySelective(item, auth);
                }
            }

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSendToSupply(String orderCode, AuthToken auth) {

        List<ErpOrderInfo> list = new ArrayList<>();
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderLevelEnum.PRIMARY.getCode().equals(order.getOrderLevel()) && StatusEnum.YES.getCode().equals(order.getSplitStatus())) {
            list.addAll(erpOrderQueryService.getSecondOrderListByPrimaryCode(orderCode));
        } else {
            list.add(order);
        }


        for (ErpOrderInfo item :
                list) {
            if (ErpOrderStatusEnum.ORDER_STATUS_4.getCode().equals(item.getOrderStatus())) {
                if (ErpOrderNodeStatusEnum.STATUS_6.getCode().equals(item.getOrderNodeStatus())) {
                    item.setItemList(erpOrderItemService.selectOrderItemListByOrderId(item.getOrderStoreId()));

                    //TODO CT 同步订单
                    HttpResponse httpResponse = purchaseOrderService.createPurchaseOrder(item);
                    System.out.println(JSON.toJSON(httpResponse));
                    if (RequestReturnUtil.validateHttpResponse(httpResponse)) {
                        //同步之后修改状态
                        item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_6.getCode());
                        item.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_8.getCode());
                        this.updateOrderByPrimaryKeySelective(order, auth);
                    }
                }
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderSign(ErpOrderSignRequest erpOrderSignRequest) {
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
            if (item.getActualInboundCount() == null || item.getActualInboundCount() <= 0) {
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
        }
        erpOrderItemService.updateOrderItemList(updateItemList, auth);

        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_13.getCode());
        order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_12.getCode());
        order.setReceiveTime(new Date());
        this.updateOrderByPrimaryKeySelective(order, auth);

        //首单，修改门店状态
        if (order.getOrderTypeCode().equals("2") || order.getOrderTypeCode().equals("4")) {
            erpOrderRequestService.updateStoreStatus(order.getStoreId(), "010201");
        }
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

        if (ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {

        }

    }

}
