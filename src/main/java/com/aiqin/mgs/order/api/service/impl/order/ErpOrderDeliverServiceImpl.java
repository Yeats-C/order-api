package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityRuleUnitEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityRule;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportLogisticsRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.gift.GiftQuotasUseDetailService;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.OrderPublic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ErpOrderDeliverServiceImpl implements ErpOrderDeliverService {

    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;
    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    private CouponRuleService couponRuleService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Autowired
    private BridgeProductService bridgeProductService;
    @Resource
    private GiftQuotasUseDetailService giftQuotasUseDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderDeliver(ErpOrderDeliverRequest erpOrderDeliverRequest) {

        if (erpOrderDeliverRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderDeliverRequest.getOrderCode())) {
            throw new BusinessException("缺少订单号");
        }
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderDeliverRequest.getPersonId());
        auth.setPersonName(erpOrderDeliverRequest.getPersonName());

        Map<Long, Long> paramLineQuantityMap = new HashMap<>(16);
        List<ErpOrderDeliverItemRequest> paramItemList = erpOrderDeliverRequest.getItemList();
        if (paramItemList == null || paramItemList.size() == 0) {
            throw new BusinessException("缺少发货数量明细");
        }
        for (ErpOrderDeliverItemRequest item :
                paramItemList) {
            paramLineQuantityMap.put(item.getLineCode(), item.getActualProductCount());
        }


        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderDeliverRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }

        boolean flag = false;
        if (order.getOrderTypeCode().equals(ErpOrderTypeEnum.DIRECT_SEND.getValue())) {
            flag = true;
        }
        if (ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_8.getCode().equals(order.getOrderNodeStatus())) {
                    flag = true;
            }
        }
        if (flag) {
            List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
            //实发数量
            Long totalActualProductCount = 0L;
            //实发均摊金额
            BigDecimal actualTotalProductAmount = BigDecimal.ZERO;

            //商品毛重(kg)
            BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
            //商品包装体积(mm³)
            BigDecimal boxVolumeTotal = BigDecimal.ZERO;

            //是否需要标记生成冲减单
            boolean scourSheetFlag = false;

            for (ErpOrderItem item :
                    itemList) {
                //行订货数量
                Long productCount = item.getProductCount();
                //行发货数量
                Long actualProductCount = paramLineQuantityMap.get(item.getLineCode());
                if (actualProductCount == null) {
                    throw new BusinessException("缺少行号为" + item.getLineCode() + "的发货数量");
                }
                if (actualProductCount < 0) {
                    throw new BusinessException("发货数量不能小于0");
                }
                if (actualProductCount > productCount) {
                    throw new BusinessException("发货数量不能超过订货数量");
                }
                if (actualProductCount < productCount) {
                    scourSheetFlag = true;
                }
                item.setActualProductCount(actualProductCount);
                totalActualProductCount += actualProductCount;

                //行实际发货商品均摊金额
                BigDecimal actualProductAmount = BigDecimal.ZERO;
                if (productCount.equals(actualProductCount)) {
                    actualProductAmount = item.getTotalPreferentialAmount();
                } else {
                    actualProductAmount = new BigDecimal(actualProductCount).multiply(item.getPreferentialAmount());
                }
                actualTotalProductAmount = actualTotalProductAmount.add(actualProductAmount);

                //设置单个商品实际发货金额
                item.setActualTotalProductAmount(actualProductAmount);

                //商品毛重汇总
                boxGrossWeightTotal = boxGrossWeightTotal.add((item.getBoxGrossWeight() == null ? BigDecimal.ZERO : item.getBoxGrossWeight()).multiply(new BigDecimal(actualProductCount)));
                //商品体积汇总
                boxVolumeTotal = boxVolumeTotal.add((item.getBoxVolume() == null ? BigDecimal.ZERO : item.getBoxVolume()).multiply(new BigDecimal(actualProductCount)));

            }
            erpOrderItemService.updateOrderItemList(itemList, auth);

            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_9.getCode());
            order.setActualProductCount(totalActualProductCount);
            order.setActualTotalProductAmount(actualTotalProductAmount);
            order.setDeliveryTime(erpOrderDeliverRequest.getDeliveryTime());
            order.setActualTotalVolume(boxVolumeTotal);
            order.setActualTotalWeight(boxGrossWeightTotal);
            if (scourSheetFlag) {
                order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.WAIT.getCode());
            }
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
            //todo 更新主单明细赠品的实际发货数量
            updateGiftGoodsAutCount(order.getMainOrderCode(),itemList,auth);

            if(!order.getOrderTypeCode().equals(ErpOrderTypeEnum.DISTRIBUTION.getValue())){
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
            }
            /*****************************************同步订单数据到结算开始*****************************************/
            List<ErpOrderInfo> list=new ArrayList<>();
            ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
            order.setOrderFee(orderFee);
            order.setItemList(itemList);
            list.add(order);
            bridgeProductService.settlementSaveOrder(list,2);
            /*****************************************同步订单数据到结算结束*****************************************/

        } else {
            throw new BusinessException("只有等待拣货状态且没有出货的订单才能执行该操作");
        }


    }

    /**
     * 根据主订单号修改赠品实发数量
     * @param mainOrderCode
     * @param itemList
     */
    private void updateGiftGoodsAutCount(String mainOrderCode,List<ErpOrderItem> itemList ,AuthToken auth){
        log.info("根据主订单号修改赠品实发数量,mainOrderCode={}",mainOrderCode);
        log.info("根据主订单号修改赠品实发数量,itemList={}", JsonUtil.toJson(itemList));
        log.info("根据主订单号修改赠品实发数量,auth={}",auth);
        List<ErpOrderItem> items=new ArrayList<>();
        Map<String,Long> map=new HashMap<>();
        for(ErpOrderItem eoi:itemList){
            //挑选出赠品明细行
            if(null!=eoi.getProductType()&&!eoi.getProductType().equals(ErpProductGiftEnum.PRODUCT.getCode())){
                map.put(eoi.getSkuCode(),eoi.getActualProductCount());
            }
        }
        log.info("根据主订单号修改赠品实发数量,赠品 map={}",map);
        //判断是否有赠品，如果有则进行后面的数量修改
        if(null!=map&&map.size()>0){
            //根据主订单编码，查询主订单明细，进行赠品数量修改
            List<ErpOrderItem> erpOrderItemList = erpOrderItemService.selectOrderItemListByOrderCode(mainOrderCode);
            for(ErpOrderItem erpOrderItem:erpOrderItemList){
                //挑选出赠品明细行
                if(null!=erpOrderItem.getProductType()&&!erpOrderItem.getProductType().equals(ErpProductGiftEnum.PRODUCT.getCode())&&map.containsKey(erpOrderItem.getSkuCode())){
                    //根据sku拿到赠品实发数量
                    Long count=map.get(erpOrderItem.getSkuCode());
                    if(null!=erpOrderItem.getActualProductCount()){
                        count=count+erpOrderItem.getActualProductCount();
                    }
                    erpOrderItem.setActualProductCount(count);
                    items.add(erpOrderItem);
                }
            }
            log.info("根据主订单号修改赠品实发数量,需修改的明细集合 items={}",JsonUtil.toJson(items));
            //进行赠品实发数量修改
            erpOrderItemService.updateOrderItemList(itemList, auth);
            log.info("根据主订单号修改赠品实发数量,成功");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTransport(ErpOrderTransportRequest erpOrderTransportRequest) {

        if (erpOrderTransportRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderTransportRequest.getPersonId())) {
            throw new BusinessException("缺失操作人id");
        }
        if (StringUtils.isEmpty(erpOrderTransportRequest.getPersonName())) {
            throw new BusinessException("缺失操作人名称");
        }
        //操作人
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderTransportRequest.getPersonId());
        auth.setPersonName(erpOrderTransportRequest.getPersonName());

        //物流信息参数
        ErpOrderTransportLogisticsRequest logistics = erpOrderTransportRequest.getLogistics();

        //物流信息
        ErpOrderLogistics orderLogistics = null;
        if (logistics != null && StringUtils.isNotEmpty(logistics.getLogisticsCode())) {
            if (StringUtils.isEmpty(logistics.getLogisticsCentreCode())) {
                throw new BusinessException("缺失物流公司编号");
            }
            if (StringUtils.isEmpty(logistics.getLogisticsCentreName())) {
                throw new BusinessException("缺失物流公司名称");
            }
            if (StringUtils.isEmpty(logistics.getSendRepertoryCode())) {
                throw new BusinessException("缺失发货仓库编码");
            }
            if (StringUtils.isEmpty(logistics.getSendRepertoryName())) {
                throw new BusinessException("缺失发货仓库名称");
            }
            if (logistics.getLogisticsFee() == null) {
                throw new BusinessException("缺失物流费用");
            }
            orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logistics.getLogisticsCode());
            if (orderLogistics == null) {
                //新的物流信息，先保存物流信息

                ErpOrderLogistics newOrderLogistics = new ErpOrderLogistics();
                newOrderLogistics.setLogisticsId(OrderPublic.getUUID());
                newOrderLogistics.setLogisticsCode(logistics.getLogisticsCode());
                newOrderLogistics.setLogisticsCentreCode(logistics.getLogisticsCentreCode());
                newOrderLogistics.setLogisticsCentreName(logistics.getLogisticsCentreName());
                newOrderLogistics.setSendRepertoryCode(logistics.getSendRepertoryCode());
                newOrderLogistics.setSendRepertoryName(logistics.getSendRepertoryName());
                newOrderLogistics.setLogisticsFee(logistics.getLogisticsFee());
                newOrderLogistics.setPayStatus(ErpPayStatusEnum.UNPAID.getCode());
                erpOrderLogisticsService.saveOrderLogistics(newOrderLogistics, auth);
                orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logistics.getLogisticsCode());
            } else {
                if (orderLogistics.getLogisticsFee().compareTo(logistics.getLogisticsFee()) != 0) {
                    throw new BusinessException("物流单费用与已有物流单费用不相等");
                }
                if (!orderLogistics.getLogisticsCentreCode().equals(logistics.getLogisticsCentreCode())) {
                    throw new BusinessException("物流公司编码与已有物流单物流公司编码不相同");
                }
                if (!orderLogistics.getLogisticsCentreName().equals(logistics.getLogisticsCentreName())) {
                    throw new BusinessException("物流公司名称与已有物流单物流公司名称不相同");
                }
                if (!orderLogistics.getSendRepertoryCode().equals(logistics.getSendRepertoryCode())) {
                    throw new BusinessException("发货仓库编码与已有物流单发货仓库编码不相同");
                }
                if (!orderLogistics.getSendRepertoryName().equals(logistics.getSendRepertoryName())) {
                    throw new BusinessException("发货仓库名称与已有物流单发货仓库名称不相同");
                }
            }
        }

        List<String> orderCodeList = erpOrderTransportRequest.getOrderCodeList();
        if (orderCodeList == null || orderCodeList.size() <= 0) {
            throw new BusinessException("缺失发运订单号");
        }

        //订单类型
        ErpOrderTypeEnum orderTypeEnum = null;
        //加盟商id
        String franchiseeId = null;

        for (String orderCode :
                orderCodeList) {

            if (StringUtils.isEmpty(orderCode)) {
                throw new BusinessException("空订单号");
            }
            ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
            if (order == null) {
                throw new BusinessException("无效的订单号" + orderCode);
            }
            if (StringUtils.isEmpty(franchiseeId)) {
                franchiseeId = order.getFranchiseeId();
            } else {
                if (!franchiseeId.equals(order.getFranchiseeId())) {
                    throw new BusinessException("每次发运只能同时发运同一个加盟商的订单");
                }
            }
            if (orderTypeEnum == null) {
                orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            } else {
                if (!orderTypeEnum.getValue().equals(order.getOrderTypeCode())) {
                    throw new BusinessException("每次发运只能同时发运相同类型的订单");
                }
            }
            if (!ErpOrderNodeStatusEnum.STATUS_9.getCode().equals(order.getOrderNodeStatus())) {
                throw new BusinessException("不是待发运的订单不能发运");
            }

            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
            if (processTypeEnum == null) {
                throw new BusinessException("订单类型数据异常");
            }
            if (orderLogistics != null) {
                order.setLogisticsId(orderLogistics.getLogisticsId());
                order.setTransportCompanyCode(orderLogistics.getLogisticsCentreCode());
                order.setTransportCompanyName(orderLogistics.getLogisticsCentreName());
                order.setTransportCode(orderLogistics.getLogisticsCode());
            } else {
                if (processTypeEnum.isHasLogisticsFee()) {
                    //如果物流单信息为空但是订单类型必须要物流单信息
                    throw new BusinessException("订单" + orderCode + "必须关联物流信息");
                }
            }

            order.setTransportStatus(erpOrderTransportRequest.getTransportStatus());
            order.setTransportTime(erpOrderTransportRequest.getTransportTime());
            order.setDistributionModeCode(erpOrderTransportRequest.getDistributionModeCode());
            order.setDistributionModeName(erpOrderTransportRequest.getDistributionModeName());
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_10.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
            //查询子单是否全部发货完成
            Boolean aBoolean = checkSendOk(order.getMainOrderCode());
            log.info("判断子单是否全部发货完成,返回结果 aBoolean={}",aBoolean);
            //全部发货完成
            if(aBoolean){
                //三步均摊
                shareEqually(order.getMainOrderCode());

                //判断是否拆单，是否是子订单
                if(!order.getOrderStoreCode().equals(order.getMainOrderCode())){
                    //更新子订单里的均摊金额
                    updateSubOrder(order.getMainOrderCode());
                }

                /*****************************************同步订单数据到结算开始*****************************************/
                List<ErpOrderInfo> list=new ArrayList<>();
                ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
                order.setOrderFee(orderFee);
                List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
                order.setItemList(itemList);
                list.add(order);
                bridgeProductService.settlementSaveOrder(list,2);
                /*****************************************同步订单数据到结算结束*****************************************/

                //遍历退货单，查看是否有退单
                ErpOrderInfo e=new ErpOrderInfo();
                e.setMainOrderCode(order.getMainOrderCode());
                List<ErpOrderInfo> orderList = erpOrderQueryService.select(e);
                //如果子订单中有退货情况，现在更想预生成的退货单的 是否发起退货状态
                if(orderList!=null&&orderList.size()>0){
                    //遍历出所有的子订单号
                    List<String> orderCodes=orderList.stream().map(ErpOrderInfo::getOrderStoreCode).collect(Collectors.toList());
                    log.info("修改订单为发货状态---全部发货完成,修改退货单是否发起退货状态，入参orderCodes={}",JsonUtil.toJson(orderCodes));
                    if(null!=orderCodes&&orderCodes.size()>0){
                        returnOrderInfoDao.updateReallyReturn(orderCodes);
                        log.info("修改订单为发货状态---全部发货完成,修改退货单是否发起退货状态，成功");
                    }
                }
            }

            boolean flag=false;
            //这两个都走线下支付物流费用
            //配送
            if(ErpOrderTypeEnum.DISTRIBUTION.getValue().equals(order.getOrderTypeCode())){
                //首单或首单赠送
                if(ErpOrderCategoryEnum.ORDER_TYPE_2.getValue().equals(order.getOrderCategoryCode())||ErpOrderCategoryEnum.ORDER_TYPE_4.getValue().equals(order.getOrderCategoryCode())){
                    flag=true;
                }
            }

            if (!processTypeEnum.isHasLogisticsFee()) {
                //不需要支付物流费用的订单
                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
            } else {
                if ((orderLogistics != null && ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus()))||flag) {
                    order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                    order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
                }
            }
        }

        if (orderLogistics != null) {
            //均摊物流费用
            this.distributeLogisticsFee(orderLogistics.getLogisticsCode());
        }
    }

    /**
     * 均摊完毕更新子订单明细
     * @param mainOrderCode
     */
    private void updateSubOrder(String mainOrderCode) {
        log.info("均摊完毕更新子订单明细--入参 mainOrderCode={}",mainOrderCode);
        ErpOrderInfo orderAndItemByOrderCode = erpOrderQueryService.getOrderAndItemByOrderCode(mainOrderCode);
        log.info("均摊完毕更新子订单明细--查询原始主订单及详情返回结果 orderAndItemByOrderCode={}",JsonUtil.toJson(orderAndItemByOrderCode));
        List<ErpOrderItem> itemList = orderAndItemByOrderCode.getItemList();
        //获取有A品卷均摊数据的明细数据，存入Map
        Map<String,BigDecimal> topMap=new HashMap();
        for(ErpOrderItem item:itemList){
            if(null!=item.getTopCouponAmount()&&item.getTopCouponAmount().compareTo(BigDecimal.ZERO)>0){
                topMap.put(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode(),item.getTopCouponAmount());
            }
        }
        //子订单明细集合
        List<ErpOrderItem> subItemList=new ArrayList<>();
        //需更新的子订单明细集合
        List<ErpOrderItem> updateSubItemList=new ArrayList<>();
        /*********************************通过主订单编码查询子订单code集合**************************/
        List<String> subOrderCodeList=erpOrderInfoDao.subOrderList(mainOrderCode);
        for (String orderCode:subOrderCodeList){
            ErpOrderInfo subOrderAndItemByOrderCode = erpOrderQueryService.getOrderAndItemByOrderCode(orderCode);
            subItemList.addAll(subOrderAndItemByOrderCode.getItemList());
        }

        for(ErpOrderItem item:subItemList){
            if(topMap.containsKey(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode())){
                BigDecimal topCouponAmount=topMap.get(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode());
                item.setTopCouponAmount(topCouponAmount);
                item.setTopCouponDiscountAmount(topCouponAmount.multiply(new BigDecimal(item.getActualProductCount())));
                updateSubItemList.add(item);
            }
        }
        /*****************************************子订单数据更新结束，更新明细表*****************************************/
        log.info("均摊完毕更新子订单明细--所有分摊结束--更新子订单明细集合 resList={}",JsonUtil.toJson(updateSubItemList));
        AuthToken auth=new AuthToken();
        auth.setPersonId("系统操作");
        auth.setPersonName("系统操作");
        erpOrderItemService.updateOrderItemList(updateSubItemList,auth);
        log.info("均摊完毕更新子订单明细--更新明细表结束");

    }

    /**
     * 子单全部发货完后进行均摊
     * @param mainOrderCode
     */
    private void shareEqually(String mainOrderCode){
        log.info("子单全部发货完成进行均摊--入参 mainOrderCode={}",mainOrderCode);
        ErpOrderInfo orderAndItemByOrderCode = erpOrderQueryService.getOrderAndItemByOrderCode(mainOrderCode);
        log.info("子单全部发货完成进行均摊--查询原始主订单及详情返回结果 orderAndItemByOrderCode={}",JsonUtil.toJson(orderAndItemByOrderCode));
        /******************挑选活动和可用A品券商品明细**********************/
        List<ErpOrderItem> itemList = orderAndItemByOrderCode.getItemList();

        //活动id:商品明细行
        Map<String, List<ErpOrderItem>> activityCartMap = new HashMap<>();
        //活动id:商品总金额
        Map<String, BigDecimal> activityMoneyMap = new HashMap<>();
        //活动id:商品总价值
        Map<String, BigDecimal> activityPriceMoneyMap = new HashMap<>();
        //活动id:商品数量 用于活动
        Map<String, Integer> quantityMap = new HashMap<>();
        //筛选出活动明细行
        for(ErpOrderItem item:itemList){
            //商品类型  0商品 1赠品
            Integer productType = item.getProductType();
            //商品总金额=活动价X订货数量（赠品的话：活动价X发货数量）
            BigDecimal totalMoney=BigDecimal.ZERO;
            //商品总价值=分销单价X订货数量
            BigDecimal totalPriceMoney=BigDecimal.ZERO;
            //商品数量
            Integer quantity = 0;
            //活动id:商品明细行 添加数据
                if (null!=item.getActivityId()&&activityCartMap.containsKey(item.getActivityId())) {
                List<ErpOrderItem> items = activityCartMap.get(item.getActivityId());
                items.add(item);
                if(null!=item.getActivityId()){
                    activityCartMap.put(item.getActivityId(),items);
                }
                if(null!=activityMoneyMap.get(item.getActivityId())){
                    totalMoney=activityMoneyMap.get(item.getActivityId());
                }
                //判断是否为赠品
                if(ErpProductGiftEnum.GIFT.getCode().equals(productType)||ErpProductGiftEnum.JIFEN.getCode().equals(productType)){
                    //赠品使用实发数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getActualProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }else{
                    //商品用订货数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }
                activityMoneyMap.put(item.getActivityId(),totalMoney);

                if(null!=activityPriceMoneyMap.get(item.getActivityId())){
                    totalPriceMoney=activityPriceMoneyMap.get(item.getActivityId());
                }
                BigDecimal multiply1 = item.getProductAmount().multiply(new BigDecimal(item.getProductCount()));
                totalPriceMoney=totalPriceMoney.add(multiply1);
                activityPriceMoneyMap.put(item.getActivityId(),totalPriceMoney);

                if(null!=item.getActivityId()){
                    quantity=quantityMap.get(item.getActivityId());
                    if(null==quantity){
                        quantity=0;
                    }
                }
                quantity=quantity+item.getProductCount().intValue();
                quantityMap.put(item.getActivityId(),quantity);
            }else {
                List<ErpOrderItem> items1 =new ArrayList<>();
                items1.add(item);
                activityCartMap.put(item.getActivityId(),items1);
                if(null!=activityMoneyMap.get(item.getActivityId())){
                    totalMoney=activityMoneyMap.get(item.getActivityId());
                }
                if(ErpProductGiftEnum.GIFT.getCode().equals(productType)||ErpProductGiftEnum.JIFEN.getCode().equals(productType)){
                    //赠品使用实发数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getActualProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }else{
                    //商品用订货数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }
                activityMoneyMap.put(item.getActivityId(),totalMoney);

                BigDecimal multiply1 = item.getProductAmount().multiply(new BigDecimal(item.getProductCount()));
                if(null!=activityPriceMoneyMap.get(item.getActivityId())){
                    totalPriceMoney=activityPriceMoneyMap.get(item.getActivityId());
                }
                totalPriceMoney=totalPriceMoney.add(multiply1);
                activityPriceMoneyMap.put(item.getActivityId(),totalPriceMoney);

                if(null!=item.getActivityId()){
                    quantity=quantityMap.get(item.getActivityId());
                    if(null==quantity){
                        quantity=0;
                    }
                }
                quantity=quantity+item.getProductCount().intValue();
                quantityMap.put(item.getActivityId(),quantity);
            }
        }
        log.info("子单全部发货完成进行均摊--活动id:商品明细行 activityCartMap={}",activityCartMap);
        log.info("子单全部发货完成进行均摊--活动id:商品金额 activityMoneyMap={}",activityMoneyMap);
        log.info("子单全部发货完成进行均摊--活动id:商品价值 activityPriceMoneyMap={}",activityPriceMoneyMap);
        log.info("子单全部发货完成进行均摊--活动id:商品数量 quantityMap={}",quantityMap);
        /******************商品挑选完成进行活动均摊**********************/
        //记录活动分摊后的数据
        Map<String,ErpOrderItem> activityAfterMap=new HashMap<>();
        //活动均摊
        for(Map.Entry<String, List<ErpOrderItem>> m:activityCartMap.entrySet()){
            if(m.getKey() != null){
                HttpResponse<ActivityRequest> activityDetailResponse = activityService.getActivityDetail(m.getKey());
                ActivityRequest activityRequest=activityDetailResponse.getData();
                if(activityRequest != null){
                    Activity activity = activityRequest.getActivity();
                    List<ActivityRule> activityRules = activityRequest.getActivityRules();

                    /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单7.买赠*/
                    Integer activityType = activity.getActivityType();
                    String activityId= m.getKey();
                    List<ErpOrderItem> li=m.getValue();
                    //商品总金额
                    BigDecimal productAmount=activityMoneyMap.get(activityId);
                    //商品总价值
                    BigDecimal productPriceAmount=activityPriceMoneyMap.get(activityId);
                    //缓存当前命中的规则
                    ActivityRule curRule = null;
                    //最小梯度
                    ActivityRule firstRule = null;
                    //商品数量
                    Integer quantity = quantityMap.get(activityId);
                    //优惠金额
                    BigDecimal youHuiAmount=BigDecimal.ZERO;
                    switch (activityType){
                        case 1:
                            for(ActivityRule ruleItem:activityRules){

                                //筛选最小梯度
                                if (firstRule == null || ruleItem.getMeetingConditions().compareTo(firstRule.getMeetingConditions()) < 0) {
                                    firstRule = ruleItem;
                                }

                                //是否把当前梯度作为命中梯度
                                boolean flag = false;

                                if (ActivityRuleUnitEnum.BY_NUM.getCode().equals(ruleItem.getRuleUnit())) {
                                    //按照数量

                                    if (ruleItem.getMeetingConditions().compareTo(new BigDecimal(quantity)) <= 0) {
                                        if (curRule == null) {
                                            flag = true;
                                        } else {
                                            if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                                flag = true;
                                            }
                                        }
                                    }

                                } else if (ActivityRuleUnitEnum.BY_MONEY.getCode().equals(ruleItem.getRuleUnit())) {
                                    //按照金额

                                    if (ruleItem.getMeetingConditions().compareTo(productPriceAmount) <= 0) {
                                        if (curRule == null) {
                                            flag = true;
                                        } else {
                                            if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                                flag = true;
                                            }
                                        }
                                    }

                                } else {

                                }

                                if (flag) {
                                    curRule = ruleItem;
                                }

                            }
                            if (curRule != null) {//命中规则
                                youHuiAmount=curRule.getPreferentialAmount();
                                //满减活动商品总金额=商品总金额-优惠金额
                                if(productAmount.compareTo(youHuiAmount)>0){
                                    productAmount=productAmount.subtract(youHuiAmount);
                                }else{
                                    //优惠金额大于活动商品总价值
                                    productAmount=BigDecimal.ZERO;
                                }

                            }
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;

                    }

                    for(ErpOrderItem eo:li){
                        //分销总价=商品价值
                        BigDecimal fenxiaozongjia=eo.getProductAmount().multiply(new BigDecimal(eo.getProductCount()));
                        //分摊总额=商品价值X商品总金额/商品总价值
                        if(productAmount.compareTo(BigDecimal.ZERO) > 0 && productPriceAmount.compareTo(BigDecimal.ZERO) > 0){
                            BigDecimal totalPreferentialAmount=fenxiaozongjia.multiply(productAmount).divide(productPriceAmount,2,BigDecimal.ROUND_HALF_UP);
                            //分摊单价
                            BigDecimal preferentialAmount=totalPreferentialAmount.divide(new BigDecimal(eo.getProductCount()),2,BigDecimal.ROUND_HALF_UP);
                            eo.setTotalPreferentialAmount(totalPreferentialAmount);
                            eo.setPreferentialAmount(preferentialAmount);
                        }else {
                            eo.setTotalPreferentialAmount(BigDecimal.ZERO);
                            eo.setPreferentialAmount(BigDecimal.ZERO);
                        }
                        activityAfterMap.put(eo.getOrderInfoDetailId(),eo);
                    }
                }
            }
        }
        log.info("子单全部发货完成进行均摊--活动分摊--活动分摊后的数据 activityAfterMap={}",activityAfterMap);
        //遍历原始明细行，将活动分摊后的数据填充进去
        for(ErpOrderItem e:itemList){
            String orderInfoDetailId = e.getOrderInfoDetailId();
            if(null!=activityAfterMap.get(orderInfoDetailId)){
                ErpOrderItem o= activityAfterMap.get(orderInfoDetailId);
                e.setTotalPreferentialAmount(o.getTotalPreferentialAmount());
                e.setPreferentialAmount(o.getPreferentialAmount());
            }
        }
        log.info("子单全部发货完成进行均摊--活动分摊--分摊后 分摊后原始明细行集合 itemList={}",JsonUtil.toJson(itemList));
        /************************************A品券均摊**********************************/
        //A品券商品明细行
        List<ErpOrderItem> list = new ArrayList<>();
        //符合分摊A品卷规则的商品
        Map map = couponRuleService.couponRuleMap();
        //商品总价值
        BigDecimal priceAmount=BigDecimal.ZERO;
        //商品总金额
        BigDecimal amount=BigDecimal.ZERO;
        String orderId="";
        for(ErpOrderItem o:itemList){
            //商品属性编码
            String productPropertyCode = o.getProductPropertyCode();
            //商品类型  0商品 1赠品
            Integer productType = o.getProductType();
            //判断是否是A品券商品
            if(null!=productPropertyCode&&map.containsKey(productPropertyCode)&&null!=productType&&productType.equals(ErpProductGiftEnum.PRODUCT.getCode())){
                list.add(o);
                priceAmount=priceAmount.add(o.getTotalPreferentialAmount());
            }
            orderId=o.getOrderStoreId();
        }
        log.info("子单全部发货完成进行均摊--A品券分摊--商品价值 priceAmount={}",priceAmount);
        //查询A品券抵扣金额
        ErpOrderFee erpOrderFee=erpOrderFeeService.getOrderFeeByOrderId(orderId);
        log.info("子单全部发货完成进行均摊--A品券分摊--费用信息 erpOrderFee={}",erpOrderFee);
        BigDecimal topCouponMoney = erpOrderFee.getTopCouponMoney();
        log.info("子单全部发货完成进行均摊--A品券分摊--A品券抵扣金额 topCouponMoney={}",topCouponMoney);
        amount=priceAmount.subtract(topCouponMoney);
        log.info("子单全部发货完成进行均摊--A品券分摊--商品金额 amount={}",amount);
        //记录A品券分摊后的数据
        Map<String,ErpOrderItem> couponAfterMap=new HashMap<>();
        //A品卷分摊后最后一行金额
        BigDecimal lastCouponMoney=topCouponMoney;
        for(int i=0;i<list.size();i++){
            //本行商品价值
            BigDecimal totalPreferentialAmount = list.get(i).getTotalPreferentialAmount();
            //分摊总金额
            BigDecimal to=BigDecimal.ZERO;
            //分摊单价
            BigDecimal per=BigDecimal.ZERO;
            if(totalPreferentialAmount.compareTo(BigDecimal.ZERO)>0){
                //分摊总金额=本行商品价值X商品总金额/商品总价值
                 to=totalPreferentialAmount.multiply(amount).divide(priceAmount,2,BigDecimal.ROUND_HALF_UP);
                //分摊单价
                 per=to.divide(new BigDecimal(list.get(i).getProductCount()),2,BigDecimal.ROUND_HALF_UP);
            }
            BigDecimal at=BigDecimal.ZERO;
            //最后一行做减法
            if(i==list.size()-1){
                at=lastCouponMoney;
            }else{
                //A品券单行抵扣总金额=A品券抵扣金额X本行商品价值/商品总价值
                if(totalPreferentialAmount.compareTo(BigDecimal.ZERO)>0) {
                    at = topCouponMoney.multiply(totalPreferentialAmount).divide(priceAmount, 2, BigDecimal.ROUND_HALF_UP);
                    lastCouponMoney = lastCouponMoney.subtract(at);
                }
            }

            //A品券单行每个商品抵扣金额
            BigDecimal ap=at.divide(new BigDecimal(list.get(i).getProductCount()),2,BigDecimal.ROUND_HALF_UP);
            list.get(i).setTotalPreferentialAmount(to);
            list.get(i).setPreferentialAmount(per);
            list.get(i).setTopCouponDiscountAmount(at);
            list.get(i).setTopCouponAmount(ap);
            couponAfterMap.put(list.get(i).getOrderInfoDetailId(),list.get(i));
        }
        log.info("子单全部发货完成进行均摊--A品券分摊--分摊结果map couponAfterMap={}",couponAfterMap);
        for(ErpOrderItem f:itemList){
            String orderInfoDetailId = f.getOrderInfoDetailId();
            if(null!=couponAfterMap.get(orderInfoDetailId)){
                ErpOrderItem o= couponAfterMap.get(orderInfoDetailId);
                f.setTotalPreferentialAmount(o.getTotalPreferentialAmount());
                f.setPreferentialAmount(o.getPreferentialAmount());
            }
        }
        log.info("子单全部发货完成进行均摊--A品券分摊--分摊后原始明细行集合 itemList={}",JsonUtil.toJson(itemList));
        /************************************赠品均摊**********************************/
        //实付金额
        BigDecimal payMoney = erpOrderFee.getPayMoney();
        log.info("子单全部发货完成进行均摊--赠品均摊--实付金额 payMoney={}",payMoney);
        //商品总价值=（商品或活动赠品分摊总额相加）+（自选赠品实发数量X分销价）的和
        BigDecimal totalMoney = erpOrderFee.getPayMoney();
        //遍历明细行，计算商品总价值
        for(ErpOrderItem p:itemList){
            Integer productType = p.getProductType();
            if(ErpProductGiftEnum.JIFEN.getCode().equals(productType)||ErpProductGiftEnum.GIFT.getCode().equals(productType)){
                //兑换赠品本行商品价值=分销价X订货数量
                BigDecimal multiply = p.getProductAmount().multiply(new BigDecimal(p.getActualProductCount()));
                p.setTotalPreferentialAmount(multiply);
                totalMoney=totalMoney.add(multiply);
            }else{
                //其他商品按照分摊总金额累加
//                totalMoney=totalMoney.add(p.getTotalPreferentialAmount());
            }
        }
        log.info("子单全部发货完成进行均摊--赠品均摊--商品总价值 totalMoney={}",totalMoney);
        //最后分摊完的商品明细，用于明细更新使用
        List<ErpOrderItem> resList=new ArrayList<>();
        //遍历明细行，进行分摊
        for(ErpOrderItem k:itemList){
            BigDecimal tper=BigDecimal.ZERO;
            BigDecimal s=BigDecimal.ZERO;
            if(k.getTotalPreferentialAmount().compareTo(BigDecimal.ZERO)>0){
                //分摊总价=商品原始分摊总价X实付金额/商品总价值
                tper = k.getTotalPreferentialAmount().multiply(payMoney).divide(totalMoney, 2, BigDecimal.ROUND_HALF_UP);
                //商品类型
                Integer productType = k.getProductType();

                if(ErpProductGiftEnum.PRODUCT.getCode().equals(productType)){
                    //商品分摊单价=分摊总金额/订货数量
                    s=tper.divide(new BigDecimal(k.getProductCount()),2,BigDecimal.ROUND_HALF_UP);
                }else {
                    //赠品分摊单价=分摊总金额/实发数量
                    s=tper.divide(new BigDecimal(k.getActualProductCount()),2,BigDecimal.ROUND_HALF_UP);
                }
            }

            k.setTotalPreferentialAmount(tper);
            k.setPreferentialAmount(s);
            ErpOrderItem er=new ErpOrderItem();
            er.setTotalPreferentialAmount(k.getTotalPreferentialAmount());
            er.setPreferentialAmount(k.getPreferentialAmount());
            er.setTopCouponDiscountAmount(k.getTopCouponDiscountAmount());
            er.setOrderInfoDetailId(k.getOrderInfoDetailId());
            er.setTopCouponAmount(k.getTopCouponAmount());
            er.setId(k.getId());
            resList.add(er);
        }
        log.info("子单全部发货完成进行均摊--赠品均摊--分摊完后原始订单明细集合 itemList={}",JsonUtil.toJson(itemList));
        /*****************************************分摊计算结束，更新明细表*****************************************/
        log.info("子单全部发货完成进行均摊--所有分摊结束--更新原始订单明细集合 resList={}",JsonUtil.toJson(resList));
        AuthToken auth=new AuthToken();
        auth.setPersonId("系统操作");
        auth.setPersonName("系统操作");
        erpOrderItemService.updateOrderItemList(resList,auth);
        log.info("子单全部发货完成进行均摊--更新明细表结束");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeLogisticsFee(String logisticsCode) {

        AuthToken auth = new AuthToken();
        //物流单
        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logisticsCode);
        if (orderLogistics == null) {
            throw new BusinessException("无效的物流单");
        }
        auth.setPersonId(orderLogistics.getCreateById());
        auth.setPersonName(orderLogistics.getCreateByName());

        if (!ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus())) {
            //物流费用没有支付成功，不做计算
            return;
        }

        List<ErpOrderInfo> orderList = erpOrderQueryService.getOrderByLogisticsId(orderLogistics.getLogisticsId());
        if (orderList == null || orderList.size() == 0) {
            //没有订单
            return;
        }

        //所有订单发货商品总数量
        Long totalCount = 0L;
        for (ErpOrderInfo item :
                orderList) {
            totalCount += item.getActualProductCount();
        }

        //物流单实际支付费用
        BigDecimal fee = orderLogistics.getBalancePayFee();
        //占用费用
        BigDecimal useFee = BigDecimal.ZERO;
        for (int i = 0; i < orderList.size(); i++) {
            ErpOrderInfo order = orderList.get(i);
            BigDecimal thisFee = BigDecimal.ZERO;
            if (i < orderList.size() - 1) {
                //该订单占用运费
                thisFee = new BigDecimal(order.getActualProductCount()).divide(new BigDecimal(totalCount), 2, RoundingMode.HALF_UP);
                useFee = useFee.add(thisFee);
            } else {
                thisFee = fee.subtract(useFee);
            }

            order.setDeliverAmount(thisFee);
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        }
    }

    /**
     * 判断子单是否全部发货完成
     * @param mainOrderCode
     * @return true:全部发货完成 false:未全部发货
     */
    private Boolean checkSendOk(String mainOrderCode){
        log.info("判断子单是否全部发货完成,入参mainOrderCode={}",mainOrderCode);
        ErpOrderInfo po=new ErpOrderInfo();
        po.setMainOrderCode(mainOrderCode);
        List<ErpOrderInfo> list=erpOrderInfoDao.select(po);
        if(list!=null&&list.size()>0){
            log.info("判断子单是否全部发货完成,原始订单集合为 list={}",JsonUtil.toJson(list));
            for(ErpOrderInfo eoi:list){
                Integer orderStatus = eoi.getOrderStatus();
                //判断订单状态是否是 11:发货完成或者 97:缺货终止
                if(orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_11.getCode())
                        ||orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_97.getCode())
                        ||orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_4.getCode())){
                }else{
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        ErpOrderDeliverServiceImpl service=new ErpOrderDeliverServiceImpl();
        service.shareEqually("20200602000007");
    }
}
