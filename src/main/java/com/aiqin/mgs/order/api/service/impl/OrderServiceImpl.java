/*****************************************************************
 * 模块名称：订单后台-实现层
 * 开发人员: 黄祉壹
 * 开发时间: 2018-11-05
 *
 * 2019-01-08 调整接口addordta;返回值由order_id 调整为order_code
 *            调整接口addOrderList;返回值由order_id 调整为order_code
 *            调整接口addpamo;orderId 通过后台查询出来
 * ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;


import javax.annotation.Resource;
import javax.validation.Valid;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.mgs.order.api.component.*;
import com.aiqin.mgs.order.api.component.PayTypeEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.pay.PayReq;
import com.aiqin.mgs.order.api.domain.request.*;
import com.aiqin.mgs.order.api.domain.response.*;
import com.aiqin.mgs.order.api.service.*;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.util.DayUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import com.aiqin.mgs.order.api.util.DateUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.mgs.order.api.domain.request.DevelRequest;
import com.aiqin.mgs.order.api.domain.request.DistributorMonthRequest;
import com.aiqin.mgs.order.api.domain.request.MemberByDistributorRequest;
import com.aiqin.mgs.order.api.domain.request.OrderAndSoOnRequest;
import com.aiqin.mgs.order.api.domain.request.OrderIdAndAmountRequest;
import com.aiqin.mgs.order.api.domain.request.ReorerRequest;
import com.aiqin.mgs.order.api.domain.response.OrderOverviewMonthResponse;
import com.aiqin.mgs.order.api.domain.response.OrderResponse;
import com.aiqin.mgs.order.api.domain.response.OrderbyReceiptSumResponse;
import com.aiqin.mgs.order.api.domain.response.SelectByMemberPayCountResponse;
import com.aiqin.mgs.order.api.domain.response.WscSaleResponse;
import com.aiqin.mgs.order.api.domain.response.WscWorkViewResponse;
import com.aiqin.mgs.order.api.domain.response.DistributorMonthResponse;
import com.aiqin.mgs.order.api.domain.response.LastBuyResponse;
import com.aiqin.mgs.order.api.domain.response.LatelyResponse;
import com.aiqin.mgs.order.api.domain.response.MevBuyResponse;
import com.aiqin.mgs.order.api.domain.response.OradskuResponse;

@SuppressWarnings("all")
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderLogDao orderLogDao;

    @Resource
    private OrderCouponDao orderCouponDao;

    @Resource
    private OrderPayDao orderPayDao;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private SettlementService settlementService;

    @Resource
    private OrderLogService orderLogService;

    @Resource
    private CartService cartService;

    @Resource
    private OrderDetailDao orderDetailDao;

    @Resource
    private OrderAfterDao orderAfterDao;
    @Resource
    private OrderService orderService;
    @Resource
    private OrderAfterDetailDao orderAfterDetailDao;
    @Resource
    private PrestorageOrderSupplyDetailDao prestorageOrderSupplyDetailDao;
    @Resource
    private PrestorageOrderSupplyDao prestorageOrderSupplyDao;
    @Resource
    private BridgeProductService bridgeProductService;
    @Resource
    private PrestorageOrderSupplyLogsDao prestorageOrderSupplyLogsDao;
    @Resource
    private PayService payService;
    @Resource
    private OrderAfterService orderAfterService;
    @Resource
    private CashierShiftScheduleDao cashierShiftScheduleDao;
    @Resource
    private BridgePayService bridgePayService;
    //商品项目地址
    @Value("${slcsIp}")
    public String slcsIp;
    @Resource
    private UrlProperties urlProperties;

    //模糊查询订单列表
    @Override
    public HttpResponse selectOrder(OrderQuery orderQuery) {

        //根据支付类型查询订单
        payOrderIdList(OrderPublic.getOrderQuery(orderQuery));

        if (orderQuery.getPayType() != null && !orderQuery.getPayType().equals("") && orderQuery.getOrderCode() != null && !orderQuery.getOrderCode().equals("")) {
            if (orderQuery.getOrderIdList() != null && orderQuery.getOrderIdList().size() > 0) {

            } else {
                return HttpResponse.success(null);
            }
        }
        try {

            List<OrderInfo> OrderInfolist = orderDao.selectOrder(OrderPublic.getOrderQuery(orderQuery));
            if (OrderInfolist != null && OrderInfolist.size() > 0) {
                for (int i = 0; i < OrderInfolist.size(); i++) {
                    OrderInfo info = new OrderInfo();
                    info = OrderInfolist.get(i);

                    //订单明细数据
                    OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
                    orderDetailQuery.setOrderId(info.getOrderId());
                    List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);
                    //未付款的支付金额为空
                    if (info.getOrderStatus() == 0) {
                        info.setActualPrice(0);
                        info.setPayType(null);

                    }
                    info.setOrderdetailList(detailList);

                    //特殊处理   (前端控制退货按钮使用字段:1:订单已全数退完)
                    ReorerRequest reorerRequest = new ReorerRequest();
                    reorerRequest.setOrderCode(info.getOrderCode());

                    //查询购买数量
                    List<OrderIdAndAmountRequest> buyIdAndAmounts = new ArrayList();
                    buyIdAndAmounts = orderDetailDao.buyAmount(reorerRequest);

                    //查询退货数量
                    List<OrderIdAndAmountRequest> returnIdAndAmounts = new ArrayList();
                    returnIdAndAmounts = orderAfterDetailDao.returnAmount(reorerRequest);

                    //已完全退货完成订单
                    if (returnIdAndAmounts != null) {
                        for (OrderIdAndAmountRequest returnInfo : returnIdAndAmounts) {
                            for (OrderIdAndAmountRequest buyInfo : buyIdAndAmounts) {
                                if (returnInfo.getId().equals(buyInfo.getId())) {
                                    if (returnInfo.getAmount() >= buyInfo.getAmount()) {
                                        info.setTurnReturnView(1);
                                    }
                                }
                            }
                        }
                    }
                    OrderInfolist.set(i, info);
                }
            }

            //计算总数据量
            Integer totalCount = 0;
            Integer icount = null;
            totalCount = orderDao.selectOrderCount(OrderPublic.getOrderQuery(orderQuery));

            return HttpResponse.success(new PageResData(totalCount, OrderInfolist));

        } catch (Exception e) {
            LOGGER.error("模糊查询订单列表异常 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }

    }

    @Override
    public HttpResponse selectPrestorageOrder(OrderQuery orderQuery) {
        List<PrestorageResponse> OrderInfolist = orderDao.selectPrestorageOrder(orderQuery);
        Integer totalCount = 0;
        Integer icount = null;
        totalCount = orderDao.selectPrestorageOrderCount(OrderPublic.getOrderQuery(orderQuery));
        return HttpResponse.success(new PageResData(totalCount, OrderInfolist));
    }

    @Override
    public HttpResponse selectprestorageorderDetails(String prestorageOrderSupplyDetailId) {
        PrestorageResponse prestorageResponse = orderDao.selectprestorageorderDetails(prestorageOrderSupplyDetailId);
        return HttpResponse.success(prestorageResponse);
    }

    @Override
    @Transactional
    public HttpResponse prestorageOut(PrestorageOutInfo prestorageOutVo) {
        boolean isUpdate = false;
        //查询
        PrestorageOrderSupplyDetail prestorageOrderSupplyDetail = prestorageOrderSupplyDetailDao.selectprestorageorderDetailsById(prestorageOutVo.getPrestorageOrderSupplyDetailId());
        //判断可取数量
        if (prestorageOrderSupplyDetail != null && prestorageOrderSupplyDetail.getAmount() - prestorageOrderSupplyDetail.getSupplyAmount() >= prestorageOutVo.getAmount()) {

            //修改
            prestorageOrderSupplyDetail.setSupplyAmount(prestorageOrderSupplyDetail.getSupplyAmount() + prestorageOutVo.getAmount());
            prestorageOrderSupplyDetailDao.updateById(prestorageOrderSupplyDetail);
            //添加日志
            PrestorageOrderSupplyLogs prestorageOrderSupplyLogs = new PrestorageOrderSupplyLogs();
            prestorageOrderSupplyLogs.setBarCode(prestorageOrderSupplyDetail.getBarCode());
            prestorageOrderSupplyLogs.setPrestorageOrderSupplyDetailId(prestorageOrderSupplyDetail.getPrestorageOrderSupplyId());
            prestorageOrderSupplyLogs.setPrestorageOrderSupplyId(prestorageOrderSupplyDetail.getPrestorageOrderSupplyId());
            prestorageOrderSupplyLogs.setSpuCode(prestorageOrderSupplyDetail.getSpuCode());
            prestorageOrderSupplyLogs.setSkuCode(prestorageOrderSupplyDetail.getSkuCode());
            prestorageOrderSupplyLogs.setSupplyAmount(prestorageOutVo.getAmount());
            prestorageOrderSupplyLogs.setSurplusAmount(prestorageOrderSupplyDetail.getAmount() - prestorageOrderSupplyDetail.getSupplyAmount());
            prestorageOrderSupplyLogsDao.addLogs(prestorageOrderSupplyLogs);

        }
        //购买数量<=已提货数量+未提货的退货数量
        if (prestorageOrderSupplyDetail != null && prestorageOrderSupplyDetail.getAmount() <= prestorageOrderSupplyDetail.getSupplyAmount() + prestorageOrderSupplyDetail.getReturnPrestorageAmount()) {
            //修改状态
            prestorageOrderSupplyDetail.setPrestorageOrderSupplyStatus(Global.ORDER_STATUS_6);
            prestorageOrderSupplyDetailDao.updateById(prestorageOrderSupplyDetail);
            // updateprestorageOrderSupplyStatus(prestorageOrderSupplyDetail.getPrestorageOrderSupplyId());
            isUpdate = true;
        }
        //修改订单状态
        if (isUpdate) {
            boolean stateUpdate = true;
            List<PrestorageOrderSupplyDetail> prestorageOrderSupplyDetails = prestorageOrderSupplyDetailDao.selectprestorageorderDetailsListByOrderId(prestorageOrderSupplyDetail.getOrderId());
            for (PrestorageOrderSupplyDetail prestorageOrderSupplyDetail1 : prestorageOrderSupplyDetails) {
                if (prestorageOrderSupplyDetail1.getPrestorageOrderSupplyStatus() == 2) {
                    stateUpdate = false;
                }
            }
            if (stateUpdate) {
                PrestorageOrderSupply prestorageOrderSupply = new PrestorageOrderSupply();
                prestorageOrderSupply.setPrestorageOrderSupplyStatus(Global.ORDER_STATUS_6);
                prestorageOrderSupply.setPrestorageOrderSupplyId(prestorageOrderSupplyDetail.getPrestorageOrderSupplyId());
                prestorageOrderSupply.setUpdateBy(prestorageOrderSupplyDetail.getUpdateBy());
                prestorageOrderSupplyDao.updateById(prestorageOrderSupply);
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderStatus(Global.ORDER_STATUS_6);
                orderInfo.setOrderId(prestorageOrderSupplyDetail.getOrderId());
                try {
                    orderDao.updateOrder(orderInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        return HttpResponse.success();
    }

    @Override
    public HttpResponse callback(PartnerPayGateRep vo) {
        if (Objects.nonNull(vo) && Constants.PAYSTATUS_1000.equals(vo.getDealCode()) && StringUtils.isNotEmpty(vo.getOrderNo())) {
            //支付成功
            log.info("回调修改订单状态，库存{}", "===========" + vo);
            OrderodrInfo orderInfo = null;
            HttpResponse response = orderDetailService.selectorderSelde(vo.getOrderNo());
            if (Objects.nonNull(response) && Objects.equals(response.getCode(), "0")) {
                orderInfo = (OrderodrInfo) response.getData();
            }


            if (Objects.nonNull(orderInfo)) {
                log.info("------------------------------------------------------orderInfo：" + orderInfo.getDetailList());
                log.info("------------------------------------------------------防止多次处理start");
                //防止多次处理
                if (orderInfo.getOrderInfo().getPayStatus().equals(PayStatusEnum.HAS_PAY.getCode())) {
                    log.info("------------------------------------------------------防止多次处理ing");
                    return HttpResponse.success();
                }
                log.info("------------------------------------------------------防止多次处理end");
                //Toc 订单
                orderInfo.getOrderInfo().setPayType(String.valueOf(vo.getPayType()));
                orderInfo.getOrderInfo().setActualPrice(vo.getOrderAmount());
                //会员消费更新会员消费时间记录
                updateMemberSale(orderInfo);
                return tocOrderCallback(orderInfo);


            }
            return HttpResponse.failure(ResultCode.STATUS_CHANGE_ERROR);
        } else {
            log.info("回调参数{}", vo);
            return HttpResponse.failure(MessageId.create(Project.ORDER_API, -1, vo.getDealMsg()));
        }
    }
    //会员消费更新会员消费时间记录
    private void updateMemberSale(OrderodrInfo orderInfo) {
        if (orderInfo==null||orderInfo.getOrderInfo()==null||orderInfo.getOrderInfo().getMemberId()==null||orderInfo.getOrderInfo().getMemberId().equals("")){
            return;
        }
        MemberSaleRequest memberSaleRequest=new MemberSaleRequest();
        memberSaleRequest.setMemberId(orderInfo.getOrderInfo().getMemberId());
        memberSaleRequest.setLastSaleAmount(orderInfo.getSettlementInfo().getOrderReceivable());

        bridgePayService.updateMemberSale(memberSaleRequest);
    }

    private HttpResponse tocOrderCallback(OrderodrInfo orderInfo) {
        //修改状态 支付类型
        try {
            int i = 0;
            if (orderInfo.getOrderInfo().getOrderType() == 4) {
                i = orderService.updateOrderStatuss(orderInfo.getOrderInfo().getOrderId(), Global.ORDER_STATUS_2, PayStatusEnum.HAS_PAY.getCode(), "系统设置", orderInfo.getOrderInfo().getPayType(), orderInfo.getOrderInfo().getActualPrice());

            } else {
                i = orderService.updateOrderStatuss(orderInfo.getOrderInfo().getOrderId(), Global.ORDER_STATUS_5, PayStatusEnum.HAS_PAY.getCode(), "系统设置", orderInfo.getOrderInfo().getPayType(), orderInfo.getOrderInfo().getActualPrice());

            }
            if (i == 0) {
                return HttpResponse.success();
            }
            if (orderInfo.getOrderInfo().getOrderType() == 1) {
                //修改库存
                changeProductStock(orderInfo);
            } else if (orderInfo.getOrderInfo().getOrderType() == 4) {
                //预存订单提出记录初始化
                log.info("预存订单提出记录初始化{}", "===========" + orderInfo.getDetailList().toString());
                createPrestorageOrder(orderInfo);
            }


            return HttpResponse.success();
        } catch (Exception e) {
            log.info("修改订单状态失败{}", e.getMessage());
            return HttpResponse.failure(ResultCode.CHANGE_STORE_ERROR);
        }
    }

    @Override
    public HttpResponse selectPrestorageOrderList(OrderQuery orderQuery) {
        List<PrestorageOrderInfo> prestorageOrderSupplies = prestorageOrderSupplyDao.selectPrestorageOrderList(trans(orderQuery));
        //是否可退货
        couldReturn(prestorageOrderSupplies);
        int totalCount = prestorageOrderSupplyDao.selectPrestorageOrderListCount(trans(orderQuery));

        return HttpResponse.success(new PageResData(totalCount, prestorageOrderSupplies));
    }

    private void couldReturn(List<PrestorageOrderInfo> prestorageOrderSupplies) {
        prestorageOrderSupplies.stream().forEach(one -> {
            //设置不可退货状态
            if (one.getOrderStatus().equals(Global.ORDER_STATUS_0)) {
                one.setTurnReturnView(1);
            } else if (one.getOrderStatus().equals(Global.ORDER_STATUS_5) || one.getOrderStatus().equals(Global.ORDER_STATUS_6)) {
                //设置不可退货状态：
                //可退货 ：已提货-已提货退货 >0
                List<PrestorageOrderSupplyDetail> p = prestorageOrderSupplyDetailDao.selectPrestorageOrderDetailByOrderId(one.getOrderId());
                one.setTurnReturnView(1);
                for (PrestorageOrderSupplyDetail prestorageOrderSupplyDetail : p) {
                    if (prestorageOrderSupplyDetail.getSupplyAmount() > prestorageOrderSupplyDetail.getReturnAmount()) {
                        one.setTurnReturnView(0);
                    }
                }

            }
            //未付款的支付金额为0
            if (one.getOrderStatus() == 0) {
                one.setActualPrice(0L);
                one.setPayType(null);
            }

        });
    }

    @Override
    public HttpResponse selectPrestorageOrderLogs(OrderQuery orderQuery) {
        List<PrestorageOrderLogsInfo> prestorageOrderLogsInfos = prestorageOrderSupplyDetailDao.selectPrestorageOrderLogs(orderQuery);
        int totalCount = prestorageOrderSupplyDetailDao.selectPrestorageOrderLogsCount(orderQuery);
        return HttpResponse.success(new PageResData(totalCount, prestorageOrderLogsInfos));
    }

    @Override
    public HttpResponse selectPrestorageOrderDetail(String orderId) {
        return HttpResponse.success(prestorageOrderSupplyDetailDao.selectPrestorageOrderDetailByOrderId(orderId));
    }

    @Override
    public HttpResponse updateRejectPrestoragProduct(PrestorageOrderSupplyDetailVo vo) {
        int updateLine = prestorageOrderSupplyDetailDao.updateRejectPrestoragProduct(vo);
        return HttpResponse.success();
    }

    @Override
    public HttpResponse updateRejectPrestoragState(RejectPrestoragStateVo vo) {
        PrestorageOrderSupply prestorageOrderSupply = new PrestorageOrderSupply();
        prestorageOrderSupply.setPrestorageOrderSupplyId(vo.getPrestorageOrderSupplyId());
        prestorageOrderSupply.setPrestorageOrderSupplyStatus(vo.getProductStatus());
        prestorageOrderSupplyDao.updateById(prestorageOrderSupply);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(vo.getOrderId());
        orderInfo.setOrderStatus(vo.getProductStatus());

        //更新订单主数据
        try {
            int i = orderDao.updateOrder(orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpResponse.success();
    }

    @Override
    public HttpResponse getUnPayNum(UnPayVo unPayVo) {
        int num = orderDao.getUnPayNum(unPayVo);
        return HttpResponse.success(num);
    }

    @Override
    public HttpResponse getUnPayMemberIdList(UnPayVo unPayVo) {
        List<String> MemberIds = orderDao.getUnPayMemberIdList(unPayVo);
        return HttpResponse.success(MemberIds);
    }

    @Transactional
    @Override
    public HttpResponse batchUpdateRejectPrestoragProduct(PrestoragProductAfter vos) throws Exception {
        for (PrestorageOrderSupplyDetailVo rejectPrestoragStateVo : vos.getPrestorageOrderSupplyDetailVos()) {
            updateRejectPrestoragProduct(rejectPrestoragStateVo);
        }
        vos.getAfterSaleSaveVo().setOrderType(Global.ORDER_TYPE_1);
        return orderAfterService.addAfterOrder(vos.getAfterSaleSaveVo());
    }

    @Override
    public HttpResponse cashierQuery(CashierReqVo cashierReqVo) {
        // 新增 交接班时间
        Integer type = cashierShiftScheduleDao.cashierQuery(cashierReqVo);
        if (type == 1) {
            return HttpResponse.success(true);
        } else {
            return HttpResponse.failure(ResultCode.CASHIER_SHIFT_SCHEDULE);
        }
    }

    @Override
    public HttpResponse orderCount(String skuCode, String storeId, int day) {
        return HttpResponse.successGenerics(orderDao.querySaleSkuCount(skuCode, DateUtil.getBeforeDate(new Date(), day), storeId));
    }

    @Override
    public HttpResponse<Integer> orderStoreCount(OrderCountReq orderCountReq) {
        //1）正常销售订单-已完成状态2）预存订单-未提货状态3）服务订单已完成
        int count1 = orderDao.orderPrestorageCount(orderCountReq.getStoreId(), orderCountReq.getStartDay(), orderCountReq.getEndDay());
        int count2 = orderDao.orderStoreCount(orderCountReq.getStoreId(), orderCountReq.getStartDay(), orderCountReq.getEndDay());
        return HttpResponse.successGenerics(count1 + count2);
    }

    private OrderQuery trans(OrderQuery orderQuery) {
        if (orderQuery.getOrderStatus() != null) {
            if (orderQuery.getOrderStatus() == 2) {
                orderQuery.setOrderStatus(0);
            }
            if (orderQuery.getOrderStatus() == 6) {
                orderQuery.setOrderStatus(1);
            }
        }
        return OrderPublic.getOrderQuery(orderQuery);
    }

    /**
     * 预存订单提出记录初始化
     *
     * @param orderInfo
     */
    @Transactional
    private synchronized void createPrestorageOrder(OrderodrInfo orderInfo) {
        PrestorageOrderSupply prestorageOrderSupply = new PrestorageOrderSupply();
        prestorageOrderSupply.setPrestorageOrderSupplyId(OrderPublic.getUUID());
        prestorageOrderSupply.setPrestorageOrderSupplyStatus(Global.ORDER_STATUS_2);
        prestorageOrderSupply.setCreateBy(orderInfo.getOrderInfo().getCreateBy());
        prestorageOrderSupply.setDistributorCode(orderInfo.getOrderInfo().getDistributorCode());
        prestorageOrderSupply.setDistributorId(orderInfo.getOrderInfo().getDistributorId());
        prestorageOrderSupply.setDistributorName(orderInfo.getOrderInfo().getDistributorName());
        prestorageOrderSupply.setMemberId(orderInfo.getOrderInfo().getMemberId());
        prestorageOrderSupply.setMemberName(orderInfo.getOrderInfo().getMemberName());
        prestorageOrderSupply.setMemberPhone(orderInfo.getOrderInfo().getMemberPhone());
        prestorageOrderSupply.setOrderCode(orderInfo.getOrderInfo().getOrderCode());
        prestorageOrderSupply.setOrderId(orderInfo.getOrderInfo().getOrderId());
        prestorageOrderSupplyDao.addPrestorageOrder(prestorageOrderSupply);

        for (OrderDetailInfo detailInfo : orderInfo.getDetailList()) {
            PrestorageOrderSupplyDetail prestorageOrderSupplyDetail = new PrestorageOrderSupplyDetail();
            prestorageOrderSupplyDetail.setPrestorageOrderSupplyStatus(Global.ORDER_STATUS_2);
            prestorageOrderSupplyDetail.setAmount(detailInfo.getAmount());
            prestorageOrderSupplyDetail.setBarCode(detailInfo.getBarCode());
            prestorageOrderSupplyDetail.setCreateBy(detailInfo.getCreateBy());
            prestorageOrderSupplyDetail.setOrderDetailId(detailInfo.getOrderDetailId());
            prestorageOrderSupplyDetail.setOrderId(detailInfo.getOrderId());
            prestorageOrderSupplyDetail.setPrestorageOrderSupplyDetailId((OrderPublic.getUUID()));
            prestorageOrderSupplyDetail.setPrestorageOrderSupplyId(prestorageOrderSupply.getPrestorageOrderSupplyId());
            prestorageOrderSupplyDetail.setProductName(detailInfo.getProductName());
            prestorageOrderSupplyDetail.setReturnAmount(0);
            prestorageOrderSupplyDetail.setReturnPrestorageAmount(0);
            prestorageOrderSupplyDetail.setReturnPrestorageAmount(0);
            prestorageOrderSupplyDetail.setSkuCode(detailInfo.getSkuCode());
            prestorageOrderSupplyDetail.setBarCode(detailInfo.getBarCode());
            prestorageOrderSupplyDetail.setSupplyAmount(0);
            prestorageOrderSupplyDetailDao.addPrestorageOrderDetail(prestorageOrderSupplyDetail);
        }

    }

    private HttpResponse changeProductStock(OrderodrInfo orderInfo) {
        List<OperateStockVo> operateStockVos = Lists.newArrayList();
        orderInfo.getDetailList().stream().forEach(input -> {
            OperateStockVo stockVo = new OperateStockVo();
            stockVo.setStoreCode(orderInfo.getOrderInfo().getDistributorCode());
            stockVo.setStoreId(orderInfo.getOrderInfo().getDistributorId());
            stockVo.setRecordNumber(Optional.ofNullable(input.getAmount()).orElse(0));
            stockVo.setProductSku(input.getSkuCode());
            stockVo.setRecordType(StockChangeTypeEnum.OUT_STORAGE.getCode());
            stockVo.setBillType(BillTypeEnum.DOOR_SALE.getCode());
            stockVo.setStorageType(StorageTypeEnum.DOOR_STORE.getCode());
            /*stockVo.setStoragePosition(ReturnGoodsToStockEnum.DISPLAY_STOCK.getCode());*/
            stockVo.setProductSku(input.getSkuCode());
            stockVo.setOperator(orderInfo.getOrderInfo().getCashierName());
            stockVo.setCreateByName(orderInfo.getOrderInfo().getCashierName());
            stockVo.setStoragePosition(1);
            stockVo.setReleaseStatus(ReleaseStatusEnum.RELEASE.getCode());
            stockVo.setRelateNumber(orderInfo.getOrderInfo().getOrderId());
            operateStockVos.add(stockVo);
        });
        return bridgeProductService.changeStock(operateStockVos);
    }


    private void updateprestorageOrderSupplyStatus(String prestorageOrderSupplyId) {
        List<PrestorageOrderSupplyDetail> prestorageOrderSupplyDetails = prestorageOrderSupplyDetailDao.selectprestorageorderDetailsListBySupplyId(prestorageOrderSupplyId);
        for (PrestorageOrderSupplyDetail prestorageOrderSupplyDetail : prestorageOrderSupplyDetails) {
            if (prestorageOrderSupplyDetail.getPrestorageOrderSupplyStatus() == 0) {
                return;
            }
        }
        //修改预存订单为以提取成功
        PrestorageOrderSupply prestorageOrderSupply = new PrestorageOrderSupply();
        prestorageOrderSupply.setPrestorageOrderSupplyId(prestorageOrderSupplyId);
        prestorageOrderSupply.setPrestorageOrderSupplyStatus(Global.ORDER_STATUS_5);
        prestorageOrderSupplyDao.updateById(prestorageOrderSupply);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(prestorageOrderSupplyDetails.get(0).getOrderId());
        orderInfo.setOrderStatus(Global.ORDER_STATUS_5);
        try {
            orderDao.updateOrder(orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payOrderIdList(OrderQuery orderQuery) {

        if (orderQuery != null) {
            if (orderQuery.getPayType() != null && !orderQuery.getPayType().equals("")) {

                List<String> orderIdList = new ArrayList();
                try {
                    OrderPayInfo info = new OrderPayInfo();
                    if (orderQuery.getPayType() != null && !orderQuery.getPayType().equals("")) {
                        info.setPayType(Integer.valueOf(orderQuery.getPayType()));
                    }
                    if (orderQuery.getOrderId() != null && !orderQuery.getOrderId().equals("")) {
                        info.setOrderId(orderQuery.getOrderId());
                    }
                    if (orderQuery.getOrderCode() != null && !orderQuery.getOrderCode().equals("")) {
                        info.setOrderCode(orderQuery.getOrderCode());
                    }
                    orderIdList = orderPayDao.orderIDListPay(info);
                    if (orderIdList != null && orderIdList.size() > 0) {
                        orderQuery.setOrderIdList(orderIdList);
                    }
                } catch (Exception e) {
                    LOGGER.error("查询支付订单号异常 {}", e);
                }
            }
        }
    }


    //添加新的订单主数据
    @Override
    @Transactional
    public OrderInfo addOrderInfo(@Valid OrderInfo orderInfo) throws Exception {

        String orderId = ""; //订单Id
        String orderCode = ""; //订单编号
        String receiveCode = ""; //订单提取码

        //生成订单ID
        orderId = OrderPublic.getUUID();
        orderInfo.setOrderId(orderId);

        //生成订单号
        String logo = "";
        if (orderInfo.getOriginType().intValue() == Global.ORIGIN_TYPE_0) {
            logo = Global.ORIGIN_COME_3;
        }
        if (orderInfo.getOriginType().intValue() == Global.ORIGIN_TYPE_1) {
            logo = Global.ORIGIN_COME_4;
        }
        if (orderInfo.getOriginType().intValue() == Global.ORIGIN_TYPE_3) {
            logo = Global.ORIGIN_COME_5;
        }
        //公司标识
        String companyCode = "";
        if (orderInfo.getCompanyCode() != null && orderInfo.getCompanyCode().equals(Global.COMPANY_01)) {
            companyCode = Global.COMPANY_01;
        } else {
            companyCode = Global.COMPANY_01;
        }
        //yyMMddHHmmss+订单来源+销售渠道标识+公司标识+4位数的随机数
        //orderCode = DateUtil.sysDate() + logo + String.valueOf(Global.ORDERID_CHANNEL_4) + companyCode + OrderPublic.randomNumberF();
        orderCode = DateUtil.sysDate() + logo + String.valueOf(Global.ORDERID_CHANNEL_4) + OrderPublic.randomNumberF();
        orderInfo.setOrderCode(orderCode);

        //初始化提货码
        if (orderInfo.getOriginType().intValue() == Global.ORIGIN_TYPE_1) {
            if (orderInfo.getReceiveType().equals(Global.RECEIVE_TYPE_0)) {
                receiveCode = OrderPublic.randomNumberE();
                orderInfo.setReceiveCode(receiveCode);
            }
        }

        //订单主数据
        orderDao.addOrderInfo(orderInfo);

        return orderInfo;
    }


    //添加订单日志
    @Override
    @Transactional
    public HttpResponse addOrderLog(@Valid OrderLog logInfo) {
        try {

            //生成日志ID
            orderLogDao.addOrderLog(logInfo);
            return HttpResponse.success();

        } catch (Exception e) {
            LOGGER.error("添加订单日志报错：{}", e);
            return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
    }


    //添加新的订单优惠券关系表数据
    @Override
    @Transactional
    public void addOrderCoupon(@Valid List<OrderRelationCouponInfo> orderCouponList, @Valid String orderId) throws Exception {

        if (orderCouponList != null && orderCouponList.size() > 0) {
            for (OrderRelationCouponInfo info : orderCouponList) {
                info.setOrderId(orderId);
                info.setOrdercouponId(OrderPublic.getUUID());
                //保存优惠券信息
                orderCouponDao.addOrderCoupon(info);
            }
        }
    }

//	//接口-分销机构维度-总销售额 返回INT
//	@Override
//	public HttpResponse selectOrderAmt(String distributorId, String originType) {
//		
//		try {
//			Integer  total_price = orderDao.selectOrderAmt(distributorId,originType);
//			
//			return HttpResponse.success(total_price);
//		} catch (Exception e) {
//
//			LOGGER.info("接口-分销机构维度-总销售额 返回INT報錯", e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}
//		
//		
//	}


    //接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
    @Override
    public HttpResponse selectorderbymonth(@Valid String distributorId, List<Integer> originTypeList) {

        //系统日期:年月
        String yearMonth = DateUtil.sysDateyyyyMM();

        //取昨日日期
        String yesterday = DateUtil.NextDate(-1);

        OrderOverviewMonthResponse info = new OrderOverviewMonthResponse();

        if (originTypeList != null && originTypeList.size() > 0) {
            for (Integer originType : originTypeList) {
                if (originType.equals(Global.ORIGIN_TYPE_2)) {
                    originTypeList = null;
                }
            }
        } else {
            originTypeList = null;
        }

        //总销售额
        Integer total_price;
        //当月销售额
        Integer monthtotal_price;
        //昨日销售额
        Integer yesdaytotal_price;
        //当月支付订单数
        Integer monthorder_acount;
        //昨日支付订单数
        Integer yesdayorder_acount;
        //当月实收
        Integer monthreal_amt;
        //昨日实收
        Integer yesdayreal_amt;


        try {
            LOGGER.info("接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数 : {},originTypeList : {}", distributorId, originTypeList);

//			总销售额：包含退货、取消的订单==
            total_price = orderDao.selectOrderAmt(distributorId, originTypeList);
//			当月销售额：包含退货、取消的订单==
            monthtotal_price = orderDao.selectByMonthAllAmt(distributorId, originTypeList, DateUtil.getDayBegin(DateUtil.getFirstMonth(0)), DateUtil.getDayEnd(DateUtil.getLastMonth(1)));
//			昨日销售额：包含退货、取消的订单==
            yesdaytotal_price = orderDao.selectByYesdayAllAmt(distributorId, originTypeList, DateUtil.getDayBegin(yesterday), DateUtil.getDayEnd(yesterday));
//			当月支付订单数：包含退货、取消的订单==
            monthorder_acount = orderDao.selectByMonthAcount(distributorId, originTypeList, DateUtil.getDayBegin(DateUtil.getFirstMonth(0)), DateUtil.getDayEnd(DateUtil.getLastMonth(1)));
//			昨日支付订单数：包含退货、取消的订单==
            yesdayorder_acount = orderDao.selectByYesdayAcount(distributorId, originTypeList, DateUtil.getDayBegin(yesterday), DateUtil.getDayEnd(yesterday));
//			当月实收：不包含退货、取消的订单==
            monthreal_amt = orderDao.selectbByMonthRetailAmt(distributorId, originTypeList, DateUtil.getDayBegin(DateUtil.getFirstMonth(0)), DateUtil.getDayEnd(DateUtil.getLastMonth(1)));
//			昨日实收：不包含退货、取消的订单==
            yesdayreal_amt = orderDao.selectbByYesdayRetailAmt(distributorId, originTypeList, DateUtil.getDayBegin(yesterday), DateUtil.getDayEnd(yesterday));


            if (total_price == null) {
                total_price = 0;
            }
            if (monthtotal_price == null) {
                monthtotal_price = 0;
            }
            if (monthreal_amt == null) {
                monthreal_amt = 0;
            }
            if (monthorder_acount == null) {
                monthorder_acount = 0;
            }
            if (yesdaytotal_price == null) {
                yesdaytotal_price = 0;
            }
            if (yesdayreal_amt == null) {
                yesdayreal_amt = 0;
            }
            if (yesdayorder_acount == null) {
                yesdayorder_acount = 0;
            }

            info.setAllIncome(total_price);
            info.setMonthSalesVolume(monthtotal_price);
            info.setMonthCash(monthreal_amt);
            info.setMonthPaymentOrderNum(monthorder_acount);

            info.setYesterdaySalesVolume(yesdaytotal_price);
            info.setYesterdayCash(yesdayreal_amt);
            info.setYesterdayPaymentOrderNum(yesdayorder_acount);

            return HttpResponse.success(info);

        } catch (Exception e) {

            LOGGER.error("接口-分销机构+当月维度-当月销售额、当月实收、当月支付订单数报错：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }


    }


    //接口-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量
    @Override
    public HttpResponse selectOrderByNineDate(@Valid String distributorId, List<Integer> originTypeList) {

        String beginDate = DateUtil.NextDate(-8);
        try {
            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setDistributorId(distributorId);
            orderQuery.setOriginTypeList(originTypeList);
            orderQuery.setBeginDate(beginDate);
            List<OrderResponse> list = new ArrayList();
            list = orderDao.selectOrderByNineDate(OrderPublic.getOrderQuery(orderQuery));
            return HttpResponse.success(list);
        } catch (Exception e) {

            LOGGER.error("接口-订单概览-分销机构、小于当前日期9天内的实付金额、订单数量报错：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }


    }


    //接口-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量
    @Override
    public HttpResponse selectOrderByNineWeek(@Valid String distributorId, List<Integer> originTypeList) {

        try {

            //查询条件
            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setDistributorId(distributorId);
            orderQuery.setOriginTypeList(originTypeList);

            //返回
            List<OrderResponse> list = new ArrayList();
            OrderResponse info = new OrderResponse();
            for (int i = 0; i < 9; i++) {
                orderQuery.setAny(i);
                info = orderDao.selectOrderByNineWeek(OrderPublic.getOrderQuery(orderQuery));
                list.add(info);
            }
            return HttpResponse.success(list);
        } catch (Exception e) {

            LOGGER.error("接口-订单概览-分销机构、小于当前日期9周内的实付金额、订单数量报错：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    //接口-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量
    @Override
    public HttpResponse selectOrderByNineMonth(@Valid String distributorId, List<Integer> originTypeList) {

        String beginDate = DateUtil.afterMonth(-8); //YYYY-MM

        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setDistributorId(distributorId);
        orderQuery.setOriginTypeList(originTypeList);
        orderQuery.setBeginDate(beginDate);

        try {
            List<OrderResponse> list = orderDao.selectOrderByNineMonth(OrderPublic.getOrderQuery(orderQuery));
            return HttpResponse.success(list);
        } catch (Exception e) {

            LOGGER.error("接口-订单概览-分销机构、小于当前日期9个月内的实付金额、订单数量报错：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //添加新的订单主数据以及其他订单关联数据
    @Override
    @Transactional
    public HttpResponse addOrderList(@Valid OrderAndSoOnRequest orderAndSoOnRequest) {

        try {
            OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
            List<OrderDetailInfo> detailList = orderAndSoOnRequest.getDetailList();
            SettlementInfo settlementInfo = orderAndSoOnRequest.getSettlementInfo();
            List<OrderPayInfo> orderPayList = orderAndSoOnRequest.getOrderPayList();
            List<OrderRelationCouponInfo> orderCouponList = orderAndSoOnRequest.getOrderCouponList();

            //新增订单主数据
            String orderId = "";
            String orderCode = "";
            if (orderInfo != null) {
                orderInfo = addOrderInfo(orderInfo);
                orderId = orderInfo.getOrderId();
                orderCode = orderInfo.getOrderCode();
                if (orderId.equals("")) {
                    return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
                }

                //生成订单日志
                OrderLog rderLog = OrderPublic.addOrderLog(orderId, Global.STATUS_0, "OrderServiceImpl.addOrderInfo()",
                        OrderPublic.getStatus(Global.STATUS_0, orderInfo.getOrderStatus()), orderInfo.getCreateBy());
                orderLogService.addOrderLog(rderLog);


                //新增订单明细数据
                if (detailList != null && detailList.size() > 0) {
                    detailList = orderDetailService.addDetailList(detailList, orderId, orderCode);
                }
                //新增订单结算数据
                if (settlementInfo != null) {
                    settlementService.addSettlement(settlementInfo, orderId);
                }
                //新增订单支付数据
                if (orderPayList != null && orderPayList.size() > 0) {
                    settlementService.addOrderPayList(orderPayList, orderId, orderCode);
                }
                //新增订单与优惠券关系数据
                if (orderCouponList != null && orderCouponList.size() > 0) {
                    addOrderCoupon(orderCouponList, orderId);
                }
                //新增订单明细与优惠券关系数据
                if (detailList != null && detailList.size() > 0) {

                    for (OrderDetailInfo orderDetailInfo : detailList) {
                        OrderRelationCouponInfo info = new OrderRelationCouponInfo();
                        info = orderDetailInfo.getCouponInfo();
                        if (info != null) {
                            if (orderDetailInfo.getOrderDetailId() != null) {
                                info.setOrderDetailId(orderDetailInfo.getOrderDetailId());
                            }
                            orderCouponList.add(info);
                        }

                    }
                    addOrderCoupon(orderCouponList, orderId);
                }

                //删除购物车数据
                String memberId = "";
                List<String> skuCodes = new ArrayList();
                String distributorId = "";
                if (orderInfo != null) {

                    //微商城删除购物车
                    if (orderInfo.getOriginType() != null && orderInfo.getOriginType().equals(Global.ORIGIN_TYPE_1)) {
                        if (orderInfo.getMemberId() != null) {
                            memberId = orderInfo.getMemberId();
                        }
                        if (orderInfo.getDistributorId() != null) {
                            distributorId = orderInfo.getDistributorId();
                        }
                        if (detailList != null && detailList.size() > 0) {
                            for (OrderDetailInfo info : detailList) {
                                if (info != null && info.getSkuCode() != null) {
                                    skuCodes.add(info.getSkuCode());
                                }
                            }
                        }

                        //删除
                        LOGGER.info("开始删除购物车====== 会员：{},sku {},门店ID :{}", memberId, skuCodes, distributorId);
                        cartService.deleteCartInfoById(memberId, skuCodes, distributorId);

                    }

                }
            }
            String order_code = orderCode;
            return HttpResponse.success(order_code);
        } catch (Exception e) {
            LOGGER.error("添加新的订单主数据以及其他订单关联数据报错：{}", e);
            throw new RuntimeException("添加新的订单主数据以及其他订单关联数据报错");
        }
    }

    //接口-关闭订单
    @Override
    @Transactional
    public HttpResponse closeorder(@Valid String orderId, String updateBy) {

        try {

            //关闭订单
            OrderInfo info = new OrderInfo();
            info.setOrderId(orderId);
            info.setUpdateBy(updateBy);
            info.setOrderStatus(Global.ORDER_STATUS_4);
            orderDao.updateOrder(info);

            //生成订单日志
            OrderLog rderLog = OrderPublic.addOrderLog(orderId, Global.STATUS_0, "OrderServiceImpl.closeorder()",
                    OrderPublic.getStatus(Global.STATUS_0, Global.ORDER_STATUS_4), updateBy);
            orderLogService.addOrderLog(rderLog);

            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("接口-关闭订单异常：{}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }


    //接口-更新商户备注
    @Override
    @Transactional
    public HttpResponse updateorderbusinessnote(@Valid String orderId, String updateBy, String businessNote) {

        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setUpdateBy(updateBy);
            orderInfo.setBusinessNote(businessNote);
            orderDao.updateOrder(orderInfo);
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("接口-更新商户备注异常： {}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }

    }


    //更改订单状态/支付状态/修改员...
    @Override
    public HttpResponse updateOrderStatus(@Valid String orderId, Integer orderStatus, Integer payStatus,
                                          String updateBy, String payType) {
        try {
            updateOrderStatuss(orderId, orderStatus, payStatus, updateBy, payType, null);

            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("更改订单状态/支付状态/支付方式/修改员报错：{}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public int updateOrderStatuss(@Valid String orderId, Integer orderStatus, Integer payStatus,
                                  String updateBy, String payType, Integer actualPrice) throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setOrderStatus(orderStatus);
        orderInfo.setPayStatus(payStatus);
        orderInfo.setUpdateBy(updateBy);
        orderInfo.setPayType(payType);
        orderInfo.setActualPrice(actualPrice);
        //更新订单主数据
        int i = orderDao.updateOrder(orderInfo);

        OrderPayInfo orderPayInfo = new OrderPayInfo();
        orderPayInfo.setOrderId(orderId);
        orderPayInfo.setPayStatus(payStatus);
        orderPayInfo.setUpdateBy(updateBy);
        //更新支付数据
        orderPayDao.usts(orderPayInfo);

        //生成订单日志
        OrderLog rderLog = OrderPublic.addOrderLog(orderId, Global.STATUS_1, "OrderServiceImpl.updateOrderStatus()",
                OrderPublic.getStatus(Global.STATUS_1, payStatus), updateBy);
        orderLogService.addOrderLog(rderLog);

        return i;
    }

    //仅更改退货状态-订单主表
    @Override
    @Transactional
    public void retustus(@Valid String orderId, Integer returnStatus, String updateBy) throws Exception {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setReturnStatus(returnStatus);
        orderInfo.setUpdateBy(updateBy);

        //更新订单
        orderDao.updateOrder(orderInfo);

        //生成订单日志
        OrderLog rderLog = OrderPublic.addOrderLog(orderId, Global.STATUS_2, "OrderServiceImpl.returnStatus()",
                OrderPublic.getStatus(Global.STATUS_2, returnStatus), updateBy);
        orderLogService.addOrderLog(rderLog);
    }


    //仅变更订单状态
    @Override
    @Transactional
    public HttpResponse onlyStatus(@Valid String orderId, Integer orderStatus, String updateBy) {
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setOrderStatus(orderStatus);
            orderInfo.setUpdateBy(updateBy);

            //更新订单
            orderDao.updateOrder(orderInfo);

            //判断是否变更为待提货、待提货订单需重新生成提货码.
            if (orderStatus.intValue() == Global.RECEIVE_TYPE_0) {
                rede(orderId);
            }

            //生成订单日志
            OrderLog rderLog = OrderPublic.addOrderLog(orderId, Global.STATUS_0, "OrderServiceImpl.onlyStatus()",
                    OrderPublic.getStatus(Global.STATUS_0, orderStatus), updateBy);
            orderLogService.addOrderLog(rderLog);

            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("仅变更订单状态-BY order_id,update_by modity order_status报错：{}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }


    //接口-收银员交班收银情况统计
    @Override
    public HttpResponse cashier(@Valid String cashierId, String endTime) {
        try {

            // 获取改收银员最后一次交接时间
            String beginTime = cashierShiftScheduleDao.selectTimeByCashierId(cashierId);
            if (StringUtils.isEmpty(beginTime)) {
                beginTime = DayUtil.getYearStr();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OrderbyReceiptSumResponse receiptSumInfo = new OrderbyReceiptSumResponse();

            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setCashierId(cashierId);
            orderQuery.setBeginTime(formatter.parse(beginTime));
            orderQuery.setEndTime(formatter.parse(endTime));

            //获取收银员、支付类型金额
            List<OrderbyReceiptSumResponse> list = orderDao.cashier(orderQuery);

            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    OrderbyReceiptSumResponse info = new OrderbyReceiptSumResponse();
                    info = list.get(i);

                    receiptSumInfo.setCashierId(info.getCashierId());
                    receiptSumInfo.setCashierName(info.getCashierId());
                    if (info.getPayType().equals(Global.P_TYPE_3)) {
                        receiptSumInfo.setCash(info.getPayPrice());
                    } else if (info.getPayType().equals(Global.P_TYPE_4)) {
                        receiptSumInfo.setWeChat(info.getPayPrice());
                    } else if (info.getPayType().equals(Global.P_TYPE_5)) {
                        receiptSumInfo.setAliPay(info.getPayPrice());
                    } else if (info.getPayType().equals(Global.P_TYPE_6)) {
                        receiptSumInfo.setBankCard(info.getPayPrice());
                    } else {
                        receiptSumInfo.setCash(info.getPayPrice());
                    }
                }
            }

            //获取销售额、销售订单数
            OrderbyReceiptSumResponse buyReceiptSum = new OrderbyReceiptSumResponse();
            buyReceiptSum = orderDao.buyByCashierSum(orderQuery);

            //获取退款金额、退款订单数、
            OrderbyReceiptSumResponse returnReceiptSum = new OrderbyReceiptSumResponse();
            returnReceiptSum = orderDao.returnByCashierSum(orderQuery);

            receiptSumInfo.setSalesAmount(buyReceiptSum.getSalesAmount() != null ? buyReceiptSum.getSalesAmount() : 0);
            receiptSumInfo.setSalesOrderAmount(buyReceiptSum.getSalesOrderAmount() != null ? buyReceiptSum.getSalesOrderAmount() : 0);
            receiptSumInfo.setReturnOrderAmount(returnReceiptSum.getReturnOrderAmount() != null ? returnReceiptSum.getReturnOrderAmount() : 0);
            receiptSumInfo.setReturnPrice(returnReceiptSum.getReturnPrice() != null ? returnReceiptSum.getReturnPrice() : 0);
            receiptSumInfo.setLoadingStart(beginTime);
            return HttpResponse.success(receiptSumInfo);
        } catch (Exception e) {
            LOGGER.error("接口-收银员交班收银情况统计异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //接口-通过会员查询最后一次的消费记录.
    @Override
    public HttpResponse last(@Valid String memberId) {


        OrderInfo info = new OrderInfo();
        LastBuyResponse lastBuyResponse = new LastBuyResponse();
        info.setMemberId(memberId);
        try {

            List<LastBuyResponse> list = orderDao.last(info);

            if (list != null && list.size() > 0) {
                lastBuyResponse = list.get(0);
                List<String> prodcuts = new ArrayList();
//				for(LastBuyResponse lastBuyInfo : list) {
//					if(lastBuyInfo !=null && lastBuyInfo.getProduct() !=null) {
//						prodcuts.add(lastBuyInfo.getProduct());
//					}
//				}
                for (int i = 0; i < list.size(); i++) {
                    if (i < 4) {
                        LastBuyResponse lastBuyInfo = new LastBuyResponse();
                        lastBuyInfo = list.get(i);
                        if (lastBuyInfo != null && lastBuyInfo.getProduct() != null) {
                            prodcuts.add(lastBuyInfo.getProduct());
                        }
                    }
                }
                lastBuyResponse.setNewConsumeProduct(prodcuts);
                lastBuyResponse.setProduct("");
                return HttpResponse.success(lastBuyResponse);
            } else {
                return HttpResponse.success(null);
            }
        } catch (Exception e) {
            LOGGER.error("接口-通过会员查询最后一次的消费记录异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }

    }

    //接口-生成提货码
    @Override
    @Transactional
    public HttpResponse rede(@Valid String orderId) {

        //参数
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOrderId(orderId);

        //生成8位提货码
        String receiveCode = "";
        receiveCode = OrderPublic.randomNumberE();
        orderQuery.setReceiveCode(receiveCode);

        //将提货码更新订单表中
        try {

            orderDao.rede(orderQuery);

            return HttpResponse.success(receiveCode);

        } catch (Exception e) {
            LOGGER.error("接口-将提货码插入订单表中异常：{}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }


    //接口-可退货的订单查询分页
    @Override
    public HttpResponse reorer(@Valid ReorerRequest reorerRequest) {

        try {

            if (reorerRequest != null) {
                if (reorerRequest.getBeginTime() != null && !reorerRequest.getBeginTime().equals("")) {
                    String beginTimetime = reorerRequest.getBeginTime();
                    reorerRequest.setBeginTime(DateUtil.formatDateLong(DateUtil.getDayBegin(beginTimetime)));
                    if (reorerRequest.getEndTime() == null || reorerRequest.getEndTime().equals("")) {
                        reorerRequest.setEndTime(DateUtil.formatDateLong(DateUtil.getDayEnd(beginTimetime)));
                    }
                } else if (reorerRequest.getEndTime() != null && !reorerRequest.getEndTime().equals("")) {
                    reorerRequest.setEndTime(DateUtil.formatDateLong(DateUtil.getDayEnd(reorerRequest.getEndTime())));
                }
            }
            //去掉关联查询
            List<OrderInfo> list = new ArrayList();
            list = orderDao.reorer(reorerRequest);

//			//查询订单列表
//			List<OrderInfo> orderInfoList = new ArrayList();
//			
//			//拼接参数
//			OrderQuery orderQuery= new OrderQuery();
//			if(reorerRequest !=null) {
//				if(reorerRequest.getBeginIndex() !=null) {
//					orderQuery.setBeginIndex(reorerRequest.getBeginIndex());
//				}
//				if(reorerRequest.getPageNo() !=null) {
//					orderQuery.setPageNo(reorerRequest.getPageNo());
//				}
//				if(reorerRequest.getPageSize() !=null) {
//					orderQuery.setPageSize(reorerRequest.getPageSize());
//				}
//				if(reorerRequest.getDistributorId() !=null && !reorerRequest.getDistributorId().equals("")) {
//					orderQuery.setDistributorId(reorerRequest.getDistributorId());
//				}
//				if(reorerRequest.getOrderCode() !=null && !reorerRequest.getOrderCode().equals("")) {
//					orderQuery.setOrderCode(reorerRequest.getOrderCode());
//				}
//				if(reorerRequest.getOrderId() !=null && !reorerRequest.getOrderId().equals("")) {
//					orderQuery.setOrderId(reorerRequest.getOrderId());
//				}
//				
//				if(reorerRequest.getStatusList() !=null && reorerRequest.getStatusList().size()>0) {
//					orderQuery.setOrderStatusList(reorerRequest.getStatusList());
//				}
//				
//				if(reorerRequest.getBeginTime() !=null && !reorerRequest.getBeginTime().equals("")) {
//					orderQuery.setBeginTime(DateUtil.getDayBegin(reorerRequest.getBeginTime()));
//					if(reorerRequest.getEndTime() ==null || reorerRequest.getEndTime().equals("")) {
//						orderQuery.setEndTime(DateUtil.getDayEnd(reorerRequest.getBeginTime()));
//					}
//				}
//				if(reorerRequest.getEndTime() !=null && !reorerRequest.getEndTime().equals("")) {
//					orderQuery.setEndTime(DateUtil.getDayEnd(reorerRequest.getEndTime()));
//					if(reorerRequest.getBeginTime() ==null || reorerRequest.getBeginTime().equals("")) {
//						orderQuery.setBeginTime(DateUtil.getDayBegin(reorerRequest.getEndTime()));
//					}
//				}
//			}
//			
//			//不分页
//			Integer orderIcount =null;
//			orderQuery.setIcount(orderIcount);
//			orderInfoList = orderDao.selectOrder(orderQuery);
//			
//			List<String> orderCodes = new ArrayList();
//			if(orderInfoList !=null && orderInfoList.size()>0) {
//				for(int i=0;i<orderInfoList.size();i++) {
//					OrderInfo info = new OrderInfo();
//					info = orderInfoList.get(i);
//					reorerRequest.setOrderCode(info.getOrderCode());
//					//查询购买数量
//					List<OrderIdAndAmountRequest> buyIdAndAmounts= new ArrayList();
//					buyIdAndAmounts = orderDetailDao.buyAmount(reorerRequest);
//					
//					//查询退货数量
//					List<OrderIdAndAmountRequest> returnIdAndAmounts= new ArrayList();
//					returnIdAndAmounts = orderAfterDetailDao.returnAmount(reorerRequest);
//					
//					//已完全退货完成订单
//					if(returnIdAndAmounts !=null) {
//						for(OrderIdAndAmountRequest returnInfo :returnIdAndAmounts) {
//							for(OrderIdAndAmountRequest buyInfo :buyIdAndAmounts) {
//								if(returnInfo.getId().equals(buyInfo.getId())) {
//									if(returnInfo.getAmount() >= buyInfo.getAmount()) {
//										orderCodes.add(returnInfo.getId());
//									}
//								}
//							}
//						}
//					}	
//				}
//			}
//			
//			//返回订单列表-分页
//			List<OrderInfo> list = new ArrayList();
//			orderQuery.setIcount(1);
//            if(orderCodes !=null && orderCodes.size()>0) {
//            	orderQuery.setNoExistOrderCodeList(orderCodes);
//			}
//            list = orderDao.selectOrder(orderQuery);

            //计算总数据量
            Integer totalCount = null;
            totalCount = orderDao.reorerCount(reorerRequest);

            return HttpResponse.success(new PageResData(totalCount, list));

        } catch (Exception e) {
            LOGGER.error("接口-可退货的订单查询异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }

    }

    //接口-注销提货码
    @Override
    @Transactional
    public HttpResponse reded(@Valid String orderId) {

        try {
            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setOrderId(orderId);
            orderDao.reded(orderQuery);
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("接口-注销提货码异常：{}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }

    //未付款订单30分钟后自动取消
    @Override
    public List<String> nevder() {

        List<String> list = null;
        try {

            list = orderDao.nevder();
        } catch (Exception e) {

            LOGGER.error("未付款订单30分钟后自动取消异常：{}", e);
        }
        return list;
    }

    //提货码10分钟后失效.
    @Override
    public List<String> nevred() {
        List<String> list = null;
        try {
            list = orderDao.nevred();
        } catch (Exception e) {
            LOGGER.error("提货码10分钟后失效.异常：{}", e);
        }
        return list;
    }


    //接口-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
    @Override
    public HttpResponse devel(@Valid String distributorId, @Valid List<String> memberList) {

        try {
            //参数
            DevelRequest develRequest = new DevelRequest();
            develRequest.setDistributorId(distributorId);
            develRequest.setMemberList(memberList);

            //查询
            List<DevelRequest> list = new ArrayList();
            list = orderDao.devel(develRequest);


            //返回
            MevBuyResponse info = new MevBuyResponse();
            if (list != null && list.size() > 0) {
                info = getMevBuyResponse(list);
            }
            return HttpResponse.success(info);

        } catch (Exception e) {
            LOGGER.error("接口-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.异常：{}", e);
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
    }


    //返回值处理  通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
    private MevBuyResponse getMevBuyResponse(List<DevelRequest> list) {
        MevBuyResponse info = new MevBuyResponse();
        if (list != null && list.size() > 0) {
            for (DevelRequest develRequest : list) {
                if (develRequest.getTrante().equals(DateUtil.NextDate(0))) {
                    info.setSysDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setSysNumber(develRequest.getAcount());
                    }
                }
                if (develRequest.getTrante().equals(DateUtil.NextDate(-1))) {
                    info.setOneDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setOneNumber(develRequest.getAcount());
                    }
                }
                if (develRequest.getTrante().equals(DateUtil.NextDate(-2))) {
                    info.setTwoDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setTwoNumber(develRequest.getAcount());
                    }
                }
                if (develRequest.getTrante().equals(DateUtil.NextDate(-3))) {
                    info.setThreeDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setThreeNumber(develRequest.getAcount());
                    }
                }
                if (develRequest.getTrante().equals(DateUtil.NextDate(-4))) {
                    info.setFourDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setFourNumber(develRequest.getAcount());
                    }
                }
                if (develRequest.getTrante().equals(DateUtil.NextDate(-5))) {
                    info.setFiveDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setFiveNumber(develRequest.getAcount());
                    }
                }
                if (develRequest.getTrante().equals(DateUtil.NextDate(-6))) {
                    info.setSixDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setSixNumber(develRequest.getAcount());
                    }
                }
                if (develRequest.getTrante().equals(DateUtil.NextDate(-7))) {
                    info.setSevenDate(develRequest.getTrante());
                    if (develRequest.getAcount() != null) {
                        info.setSevenNumber(develRequest.getAcount());
                    }
                }
            }
        }

        return info;
    }


    //模糊查询订单列表+订单中商品sku数量分页
    @Override
    public HttpResponse oradsku(@Valid OrderQuery orderQuery) {

        try {

            //根据支付类型查询订单
            payOrderIdList(orderQuery);

            if (orderQuery.getPayType() != null && !orderQuery.getPayType().equals("") && orderQuery.getOrderCode() != null && !orderQuery.getOrderCode().equals("")) {
                if (orderQuery.getOrderIdList() != null && orderQuery.getOrderIdList().size() > 0) {

                } else {
                    return HttpResponse.success(null);
                }
            }

            //订单列表
            List<OradskuResponse> OrderInfolist = orderDao.selectskuResponse(OrderPublic.getOrderQuery(orderQuery));
            OradskuResponse oradskuResponse = new OradskuResponse();

            //订单中商品sku数量
            Integer skuSum = 0;
            if (OrderInfolist != null && OrderInfolist.size() > 0) {
                for (int i = 0; i < OrderInfolist.size(); i++) {
                    oradskuResponse = OrderInfolist.get(i);
                    skuSum = orderDetailService.getSkuSum(oradskuResponse.getOrderId());
                    oradskuResponse.setSkuSum(skuSum);

                    //特殊处理   (前端控制退货按钮使用字段:1:订单已全数退完)
                    ReorerRequest reorerRequest = new ReorerRequest();
                    reorerRequest.setOrderCode(oradskuResponse.getOrderCode());

                    //查询购买数量
                    List<OrderIdAndAmountRequest> buyIdAndAmounts = new ArrayList();
                    buyIdAndAmounts = orderDetailDao.buyAmount(reorerRequest);

                    //查询退货数量
                    List<OrderIdAndAmountRequest> returnIdAndAmounts = new ArrayList();
                    returnIdAndAmounts = orderAfterDetailDao.returnAmount(reorerRequest);

                    //已完全退货完成订单
                    if (returnIdAndAmounts != null) {
                        for (OrderIdAndAmountRequest returnInfo : returnIdAndAmounts) {
                            for (OrderIdAndAmountRequest buyInfo : buyIdAndAmounts) {
                                if (returnInfo.getId().equals(buyInfo.getId())) {
                                    if (returnInfo.getAmount() >= buyInfo.getAmount()) {
                                        oradskuResponse.setTurnReturnView(1);
                                    }
                                }
                            }
                        }
                    }

                    OrderInfolist.set(i, oradskuResponse);
                }
            }

            //计算总数据量
            Integer totalCount = null;
            totalCount = orderDao.skuResponseCount(OrderPublic.getOrderQuery(orderQuery));

            return HttpResponse.success(new PageResData(totalCount, OrderInfolist));

        } catch (Exception e) {
            LOGGER.error("模糊查询订单列表+订单中商品sku数量异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    //查询订单日志数据
    @Override
    public HttpResponse orog(@Valid String orderId) {

        return orderLogService.orog(orderId);
    }


    //导出订单列表
    @Override
    public HttpResponse exorder(@Valid OrderQuery orderQuery) {

        try {

            //根据支付类型查询订单
            payOrderIdList(orderQuery);

            if (orderQuery.getPayType() != null && !orderQuery.getPayType().equals("") && orderQuery.getOrderCode() != null && !orderQuery.getOrderCode().equals("")) {
                if (orderQuery.getOrderIdList() != null && orderQuery.getOrderIdList().size() > 0) {

                } else {
                    return HttpResponse.success(null);
                }
            }

            Integer icount = null;
            orderQuery.setIcount(icount);
            List<OrderInfo> OrderInfolist = orderDao.selectOrder(OrderPublic.getOrderQuery(orderQuery));

            return HttpResponse.success(OrderInfolist);

        } catch (Exception e) {
            LOGGER.error("导出订单列表异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //添加订单主数据+添加订单明细数据+返回订单编号
    @Override
    @Transactional
    public HttpResponse addOrdta(@Valid OrderAndSoOnRequest orderAndSoOnRequest) {

        try {
            //获取订单信息
            OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
            //获取订单详情列表
            List<OrderDetailInfo> detailList = orderAndSoOnRequest.getDetailList();

            //新增订单主数据
            String orderId = "";
            String orderCode = "";
            if (orderInfo != null) {
                orderInfo = addOrderInfo(orderInfo);
                //存入订单主数据
                orderId = orderInfo.getOrderId();
                orderCode = orderInfo.getOrderCode();
                if (orderId.equals("")) {
                    return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
                }

                if (orderId != null && !orderId.equals("")) {

                    //生成订单日志
                    OrderLog rderLog = OrderPublic.addOrderLog(orderId, Global.STATUS_0, "OrderServiceImpl.addOrderInfo()",
                            OrderPublic.getStatus(Global.STATUS_0, orderInfo.getOrderStatus()), orderInfo.getCreateBy());
                    orderLogService.addOrderLog(rderLog);

                    //新增订单明细数据
                    if (detailList != null && detailList.size() > 0) {
                        detailList = orderDetailService.addDetailList(detailList, orderId, orderCode);
                    }

                    //删除购物车数据
                    String memberId = "";
                    List<String> skuCodes = new ArrayList();
                    String distributorId = "";
                    if (orderInfo != null) {

                        //微商城删除购物车
                        if (orderInfo.getOriginType() != null && orderInfo.getOriginType().equals(Global.ORIGIN_TYPE_1)) {
                            if (orderInfo.getMemberId() != null) {
                                memberId = orderInfo.getMemberId();
                            }
                            if (orderInfo.getDistributorId() != null) {
                                distributorId = orderInfo.getDistributorId();
                            }
                            if (detailList != null && detailList.size() > 0) {
                                for (OrderDetailInfo info : detailList) {
                                    if (info != null && info.getSkuCode() != null) {
                                        skuCodes.add(info.getSkuCode());
                                    }
                                }
                            }

                            //删除
                            LOGGER.info("开始删除购物车====== memberId:{},skuCodes:{},distributorId:{}", memberId, skuCodes, distributorId);
                            cartService.deleteCartInfoById(memberId, skuCodes, distributorId);

                        }
                    }
                }
            }
            String order_code = orderCode;
            return HttpResponse.success(order_code);
        } catch (Exception e) {
            LOGGER.error("添加新的订单主数据以及其他订单关联数据异常：{}", e);
            throw new RuntimeException("添加新的订单主数据以及其他订单关联数据异常");
        }
    }


    //添加结算数据+添加支付数据+添加优惠关系数据+修改订单主数据+修改订单明细数据
    @Override
    @Transactional
    public HttpResponse addPamo(@Valid OrderAndSoOnRequest orderAndSoOnRequest) {

        try {
            OrderInfo orderInfo = orderAndSoOnRequest.getOrderInfo();
            List<OrderDetailInfo> detailList = orderAndSoOnRequest.getDetailList();
            SettlementInfo settlementInfo = orderAndSoOnRequest.getSettlementInfo();
            List<OrderPayInfo> orderPayList = orderAndSoOnRequest.getOrderPayList();
            List<OrderRelationCouponInfo> orderCouponList = orderAndSoOnRequest.getOrderCouponList();

            //修改订单主数据
            String orderId = "";
            String orderCode = "";
            if (orderInfo != null) {
                orderCode = orderInfo.getOrderCode();
                try {

                    //获取数据库已存在的订单编号

                    //查询订单ID参数
                    OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
                    orderDetailQuery.setOrderCode(orderCode);
                    OrderInfo detailQueryInfo = new OrderInfo();

                    //判断订单类型
                    if (orderInfo.getOrderType().equals(Global.ORDER_TYPE_3)) {
                        orderDetailQuery.setOrderType(Global.ORDER_TYPE_3);
                    }
                    if (orderInfo.getOrderType().equals(Global.ORDER_TYPE_4)) {
                        orderDetailQuery.setOrderType(Global.ORDER_TYPE_4);
                    }
                    detailQueryInfo = orderDao.selecOrderById(orderDetailQuery);
                    if (detailQueryInfo != null) {
                        orderId = detailQueryInfo.getOrderId();
                    }

                } catch (Exception e) {
                    LOGGER.error("orderDao.getOrderIdByCode(orderCode)：{}", e);
                }
                orderInfo.setOrderId(orderId);
                if (orderId != null && !orderId.equals("")) {

                    //初始化提货码
                    if (orderInfo.getOriginType().intValue() == Global.ORIGIN_TYPE_1) {
                        if (orderInfo.getReceiveType().equals(Global.RECEIVE_TYPE_0)) {
                            orderInfo.setReceiveCode(OrderPublic.randomNumberE());
                        }
                    }

                    //删除订单主数据
                    try {
                        orderDao.deleteOrderInfo(orderInfo);
                    } catch (Exception e) {
                        LOGGER.error("orderDao.deleteOrderInfo(orderInfo)：{}", e);
                    }

                    //新增订单主数据
                    try {
                        orderDao.addOrderInfo(orderInfo);
                    } catch (Exception e) {
                        LOGGER.error("orderDao.addOrderInfo(orderInfo)：{}", e);
                    }
                    //同步营业阶段
                    try {
                        updateOpenStatus(orderInfo.getDistributorId());
                    } catch (Exception e) {
                        LOGGER.info("同步营业阶段报错(1364)：{}", e.getMessage());
                    }
                    //生成订单日志
                    OrderLog rderLog = OrderPublic.addOrderLog(orderId, Global.STATUS_0, "OrderServiceImpl.addOrderInfo()",
                            OrderPublic.getStatus(Global.STATUS_0, orderInfo.getOrderStatus()), orderInfo.getCreateBy());
                    try {
                        orderLogService.addOrderLog(rderLog);
                    } catch (Exception e) {
                        LOGGER.error("生成订单日志异常：{}", e);
                    }
                    //删除订单明细数据
                    try {
                        //    orderDetailDao.deleteOrderDetailInfo(orderInfo);
                    } catch (Exception e) {
                        LOGGER.error("删除订单明细数据异常：{}", e);
                    }
                    //新增订单明细数据
                    if (detailList != null && detailList.size() > 0) {
                        try {
                            detailList = orderDetailService.updateDetailList(detailList, orderId, orderCode);
                        } catch (Exception e) {
                            LOGGER.error("新增订单明细数据异常：{}", e);
                        }
                    }
                    //新增订单结算数据
                    if (settlementInfo != null) {
                        try {
                            settlementService.addSettlement(settlementInfo, orderId);
                        } catch (Exception e) {
                            LOGGER.error("新增订单结算数据异常：{}", e);
                        }
                    }
                    //新增订单支付数据
                    if (orderPayList != null && orderPayList.size() > 0) {
                        try {
                            settlementService.addOrderPayList(orderPayList, orderId, orderCode);
                        } catch (Exception e) {
                            LOGGER.error("新增订单支付数据异常：{}", e);
                        }
                    }
                    //新增订单与优惠券关系数据
                    if (orderCouponList != null && orderCouponList.size() > 0) {
                        addOrderCoupon(orderCouponList, orderId);
                    }
                    //新增订单明细与优惠券关系数据
                    if (detailList != null && detailList.size() > 0) {

                        for (OrderDetailInfo orderDetailInfo : detailList) {
                            OrderRelationCouponInfo info = new OrderRelationCouponInfo();
                            info = orderDetailInfo.getCouponInfo();
                            if (info != null) {
                                if (orderDetailInfo.getOrderDetailId() != null) {
                                    info.setOrderDetailId(orderDetailInfo.getOrderDetailId());
                                }
                                orderCouponList.add(info);
                            }

                        }
                        addOrderCoupon(orderCouponList, orderId);
                    }
                }
                //如果订单状态是已支付此处应该写调用支付中心现金支付
                if (orderInfo.getOrderStatus().equals(Global.ORDER_STATUS_2) || orderInfo.getOrderStatus().equals(Global.ORDER_STATUS_5)) {
                    PayReq payReq = new PayReq();
                    payReq.setOrderAmount(Long.valueOf(orderInfo.getActualPrice()));
                    payReq.setAiqinMerchantId(orderInfo.getDistributorId());
                    payReq.setOrderNo(orderInfo.getOrderCode());
                    payReq.setOrderTime(orderInfo.getCreateTime());
                    payReq.setPayType(orderAndSoOnRequest.getOrderPayList().get(0).getPayType());
                    payReq.setOrderSource(orderInfo.getOrderType());
                    payReq.setCreateName(orderInfo.getCashierName());
                    payReq.setStoreName(orderInfo.getDistributorName());
                    payReq.setCreateBy(orderInfo.getCashierId());
                    NewFranchiseeResponse newFranchiseeResponse = bridgeProductService.getStoreFranchiseeData(orderInfo.getDistributorId());
                    payReq.setFranchiseeId(newFranchiseeResponse.getFranchiseeId());
                    payReq.setMemberPhone(orderInfo.getMemberPhone());
                    payReq.setMemberName(orderInfo.getMemberName());
                    payReq.setMemberId(orderInfo.getMemberId());
                    payReq.setStoreName(orderInfo.getDistributorName());
                    payReq.setBackUrl(urlProperties.getOrderApi() + "/order/back");
                    payReq.setBusinessType(choseBusinessType(orderInfo.getOrderType()));
                    payService.doPay(payReq);
                }
            }

            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("添加新的订单主数据以及其他订单关联数据异常：{}", e);
            throw new RuntimeException("添加新的订单主数据以及其他订单关联数据异常");
        }
    }

    /**
     * 根据订单类型选择支付业务
     *
     * @param orderType
     * @return
     */
    private Integer choseBusinessType(Integer orderType) {
        if (orderType == null) {
            return Global.PAY_ORDER_TYPE_0;
        }
        if (orderType.intValue() == 3) {
            return Global.PAY_ORDER_TYPE_3;
        }
        if (orderType.intValue() == 4) {
            return Global.PAY_ORDER_TYPE_1;
        }
        return Global.PAY_ORDER_TYPE_0;
    }


    //删除订单主数据+删除订单明细数据
    @Override
    @Transactional
    public HttpResponse deordta(@Valid String orderId) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        try {
            //删除订单主数据
            orderDao.deleteOrderInfo(orderInfo);

            //删除订单明细数据
            orderDetailDao.deleteOrderDetailInfo(orderInfo);

            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("删除订单主数据+删除订单明细数据异常：{}", e);
            return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }

    }


    //微商城-销售总览
    @Override
    public HttpResponse wssev(@Valid String distributorId) {


        try {
            WscSaleResponse wscSaleResponse = new WscSaleResponse();
            String beginDate = "";
            String endDate = "";

            //今日订单数
            Integer todayCount = null;
            //昨日订单数
            Integer yesterdayCount = null;
            //近七日数据订单数
            Integer weekCount = null;

            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setDistributorId(distributorId);

            orderQuery.setBeginDate(DateUtil.NextDate(0));
            orderQuery.setEndDate(DateUtil.NextDate(0));
            todayCount = orderDao.selectAcountByEcshop(orderQuery);

            orderQuery.setBeginDate(DateUtil.NextDate(-1));
            orderQuery.setEndDate(DateUtil.NextDate(-1));
            yesterdayCount = orderDao.selectAcountByEcshop(orderQuery);

            orderQuery.setBeginDate(DateUtil.NextDate(-7));
            orderQuery.setEndDate(DateUtil.NextDate(0));
            weekCount = orderDao.selectAcountByEcshop(orderQuery);

            //昨日订单金额
            Integer todayAmount = null;
            //今日订单金额
            Integer yesterdayAmount = null;
            //近七日数据订单金额
            Integer weekAmount = null;

            orderQuery.setBeginDate(DateUtil.NextDate(0));
            orderQuery.setEndDate(DateUtil.NextDate(0));
            todayAmount = orderDao.selectAmountByEcshop(orderQuery);

            orderQuery.setBeginDate(DateUtil.NextDate(-1));
            orderQuery.setEndDate(DateUtil.NextDate(-1));
            yesterdayAmount = orderDao.selectAmountByEcshop(orderQuery);

            orderQuery.setBeginDate(DateUtil.NextDate(-7));
            orderQuery.setEndDate(DateUtil.NextDate(0));
            weekAmount = orderDao.selectAmountByEcshop(orderQuery);

            //今日成交客户
            Integer todayMembers = null;
            orderQuery.setBeginDate(DateUtil.NextDate(0));
            orderQuery.setEndDate(DateUtil.NextDate(0));
            todayMembers = orderDao.selectMembersByEcshop(orderQuery);

            //昨日成交客户
            Integer yesterdayMembers = null;
            orderQuery.setBeginDate(DateUtil.NextDate(-1));
            orderQuery.setEndDate(DateUtil.NextDate(-1));
            yesterdayMembers = orderDao.selectMembersByEcshop(orderQuery);

            wscSaleResponse.setTodayAmount(todayAmount);
            wscSaleResponse.setTodayCount(todayCount);
            wscSaleResponse.setTodayMembers(todayMembers);
            wscSaleResponse.setWeekAmount(weekAmount);
            wscSaleResponse.setWeekCount(weekCount);
            wscSaleResponse.setYesterdayAmount(yesterdayAmount);
            wscSaleResponse.setYesterdayCount(yesterdayCount);
            wscSaleResponse.setYesterdayMembers(yesterdayMembers);

            return HttpResponse.success(wscSaleResponse);

        } catch (Exception e) {
            LOGGER.error("微商城-销售总览异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //微商城-事务总览
    @Override
    public HttpResponse wsswv(@Valid String distributorId) {

        try {
            WscWorkViewResponse wscWorkViewResponse = new WscWorkViewResponse();

            //待付款订单
            Integer unPaid = null;
            //待提货订单
            Integer notPick = null;
            //待发货订单
            Integer notDelivery = null;
            //待确认售后单
            Integer notSure = null;
            //待处理退款申请
            Integer notRefund = null;

            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setDistributorId(distributorId);

            orderQuery.setOrderStatus(Global.ORDER_STATUS_0);
            unPaid = orderDao.selectCountByStatus(orderQuery);

            orderQuery.setOrderStatus(Global.ORDER_STATUS_2);
            notPick = orderDao.selectCountByStatus(orderQuery);

            orderQuery.setOrderStatus(Global.ORDER_STATUS_1);
            notDelivery = orderDao.selectCountByStatus(orderQuery);


            wscWorkViewResponse.setNotDelivery(notDelivery);
            wscWorkViewResponse.setNotPick(notPick);
            wscWorkViewResponse.setNotRefund(notRefund);
            wscWorkViewResponse.setNotSure(notSure);
            wscWorkViewResponse.setUnPaid(unPaid);

            return HttpResponse.success(wscWorkViewResponse);

        } catch (Exception e) {
            LOGGER.error("微商城-事务总览异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)
    @Override
    @Transactional
    public HttpResponse repast(@Valid String orderId, @Valid String payType, @Valid Integer orderStatus, @Valid List<OrderPayInfo> orderPayList) {

        try {

            //删除订单支付数据
            settlementService.deleteOrderPayList(orderId);

            //查询订单编码
            OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
            orderDetailQuery.setOrderId(orderId);
            OrderInfo order = new OrderInfo();
            order = orderDao.selecOrderById(orderDetailQuery);
            String orderCode = "";
            if (order != null) {
                orderCode = order.getOrderCode();
            }
            //新增订单支付数据
            if (orderPayList != null && orderPayList.size() > 0) {
                settlementService.addOrderPayList(orderPayList, orderId, orderCode);
            }

            //修改订单支付方式信息
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setPayType(payType);
            orderInfo.setOrderStatus(orderStatus);
            //现金单支付-支持单支付 根据逻辑层做的调整.
            if (orderPayList != null && orderPayList.size() > 0) {
                OrderPayInfo info = new OrderPayInfo();
                info = orderPayList.get(0);
                if (info.getPayType().equals(Global.P_TYPE_3)) {
                    orderInfo.setPayStatus(info.getPayStatus());
                }

            }
            orderDao.updateOrder(orderInfo);
            //如果订单状态是已支付此处增加支付中心现金支付
            if (orderInfo.getOrderStatus().equals(Global.ORDER_STATUS_2) || orderInfo.getOrderStatus().equals(Global.ORDER_STATUS_5)) {
                NewFranchiseeResponse newFranchiseeResponse = bridgeProductService.getStoreFranchiseeData(order.getDistributorId());
                PayReq payReq = new PayReq();
                payReq.setOrderAmount(Long.valueOf(order.getActualPrice()));
                payReq.setAiqinMerchantId(order.getDistributorId());
                payReq.setOrderNo(order.getOrderCode());
                payReq.setOrderTime(order.getCreateTime());
                payReq.setPayType(orderPayList.get(0).getPayType());
                payReq.setOrderSource(order.getOrderType());
                payReq.setStoreId(order.getDistributorId());
                payReq.setOrderSource(order.getOriginType());
                payReq.setOrderNo(order.getOrderCode());
                if (newFranchiseeResponse != null) {
                    payReq.setFranchiseeId(newFranchiseeResponse.getFranchiseeId());
                }
                payReq.setCreateName(order.getCashierName());
                payReq.setStoreName(order.getDistributorName());
                payReq.setCreateBy(order.getCashierId());
                payReq.setBusinessType(choseBusinessType(order.getOrderType()));
                payService.doPay(payReq);
            }
            return HttpResponse.success();

        } catch (Exception e) {
            LOGGER.error("已存在订单更新支付状态、重新生成支付数据(更改订单表、删除新增支付表)异常：{}", e);
            return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
        }
    }

    //销售目标管理-分销机构-月销售额
    @Override
    public HttpResponse selectDistributorMonth(@Valid DistributorMonthRequest detailCouponRequest) {
        try {
            List<DistributorMonthResponse> list = new ArrayList();
            //当月销售额：包含退货、不包含取消、未付款的订单
            Integer monthtotalPrice = 0;

            //处理参数
            if (detailCouponRequest.getDistributorCodeList() != null && detailCouponRequest.getDistributorCodeList().size() > 0) {

            } else {
                detailCouponRequest.setDistributorCodeList(null);
            }

            if (detailCouponRequest.getBeginTime() != null && !detailCouponRequest.getBeginTime().equals("")) {

            } else {
                detailCouponRequest.setBeginTime(null);
            }

            if (detailCouponRequest.getEndTime() != null && !detailCouponRequest.getEndTime().equals("")) {

            } else {
                detailCouponRequest.setEndTime(null);
            }

            //查询结果
            if (detailCouponRequest.getDistributorCodeList() != null && detailCouponRequest.getDistributorCodeList().size() > 0) {
                for (String distributorCode : detailCouponRequest.getDistributorCodeList()) {
                    DistributorMonthResponse info = new DistributorMonthResponse();
                    monthtotalPrice = orderDao.selectDistributorMonth(distributorCode, DateUtil.getDayBegin(detailCouponRequest.getBeginTime()), DateUtil.getDayEnd(detailCouponRequest.getEndTime()));
                    info.setDistributorCode(distributorCode);
                    if (monthtotalPrice == null) {
                        info.setPrice(0);
                    } else {
                        info.setPrice(monthtotalPrice);
                    }
                    list.add(info);
                }
            }
            return HttpResponse.success(list);
        } catch (Exception e) {
            LOGGER.error("销售目标管理-分销机构-月销售额异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日周月.
    @Override
    public HttpResponse selectByMemberPayCount(@Valid String distributorId, @Valid Integer dateType) {

        //会员数量统计
        List<SelectByMemberPayCountResponse> list = new ArrayList();
        if (dateType != null && distributorId != null && !distributorId.equals("")) {
            //日计算会员数
            if (dateType.equals(Global.DATE_TYPE_1)) {

//				for(int i=0;i>-7;i--) {
                for (int i = -6; i <= 0; i++) {
                    Integer countMember = null;
                    String countDate = "";
                    countDate = DateUtil.NextDate(i);
                    SelectByMemberPayCountResponse info = new SelectByMemberPayCountResponse();
                    info.setCountDate(countDate);
                    try {
                        countMember = orderDao.selectByMemberPayCountDay(info);
                    } catch (Exception e) {
                        LOGGER.error("会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日异常：{}", e);
                        return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
                    }
                    if (countMember == null) {
                        info.setCountMember(0);
                    } else {
                        info.setCountMember(countMember);
                    }
                    list.add(info);
                }

                return HttpResponse.success(list);

                //周计算会员数
            } else if (dateType.equals(Global.DATE_TYPE_2)) {

//				for(int i=0;i<7;i++) {
                for (int i = 6; i >= 0; i--) {
                    Integer countMember = null;
                    String countDate = "";
                    SelectByMemberPayCountResponse info = new SelectByMemberPayCountResponse();
                    try {
                        countMember = orderDao.selectByMemberPayCountWeek(i);
                    } catch (Exception e) {
                        LOGGER.error("会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、周报错：{}", e);
                        return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
                    }
                    info.setCountDate(String.valueOf(i));
                    if (countMember == null) {
                        info.setCountMember(0);
                    } else {
                        info.setCountMember(countMember);
                    }
                    list.add(info);
                }

                return HttpResponse.success(list);

            }
            //月计算会员数
            else {

//				for(int i=0;i>-7;i--) {
                for (int i = -6; i <= 0; i++) {
                    Integer countMember = null;
                    String countDate = "";
                    countDate = DateUtil.afterMonth(i);
                    SelectByMemberPayCountResponse info = new SelectByMemberPayCountResponse();
                    info.setCountDate(countDate);
                    try {
                        countMember = orderDao.selectByMemberPayCountMonth(info);
                    } catch (Exception e) {
                        LOGGER.error("会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、月报错：{}", e);
                        return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
                    }
                    if (countMember == null) {
                        info.setCountMember(0);
                    } else {
                        info.setCountMember(countMember);
                    }
                    list.add(info);
                }

                return HttpResponse.success(list);

            }
        } else {
            LOGGER.error("会员活跃情况-通过当前门店,等级会员list、 统计订单使用的会员数、日周月.参数确实报错： {}");
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //判断会员是否在当前门店时候有过消费记录
    @Override
    public HttpResponse selectMemberByDistributor(@Valid MemberByDistributorRequest memberByDistributorRequest) {
        if (memberByDistributorRequest != null) {
            if (memberByDistributorRequest.getMemberList() != null && memberByDistributorRequest.getMemberList().size() > 0) {

            } else {
                memberByDistributorRequest.setMemberList(null);
            }
            List<String> list = new ArrayList();
            try {
                list = orderDao.selectMemberByDistributor(memberByDistributorRequest);
                return HttpResponse.success(list);
            } catch (Exception e) {
                LOGGER.error("判断会员是否在当前门店时候有过消费记录  查询异常:{}", e);
                return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
            }
        } else {
            LOGGER.warn("判断会员是否在当前门店时候有过消费记录 参数异常: {}", memberByDistributorRequest);
            return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
    }


    //查询未统计销量的已完成订单
    @Override
    public List<String> selectsukReturn() {

        try {
            return orderDao.selectsukReturn(DateUtil.getTimeBegin(DateUtil.sysDateYYYYMMDD()), DateUtil.getTimeEnd(DateUtil.sysDateYYYYMMDD()));
        } catch (Exception e) {
            LOGGER.error("查询未统计销量的已完成订单异常: {}", e);
            return null;
        }
    }


    //修改统计销量状态
    @Override
    public void updateSukReturn(@Valid String orderId) {

        try {
            orderDao.updateSukReturn(orderId);
        } catch (Exception e) {
            LOGGER.error("修改统计销量状态异常 :{}", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse updateOrderInfo(StoreValueOrderPayRequest storeValueOrderPayRequest) {
        try {
            OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
            orderDetailQuery.setOrderCode(storeValueOrderPayRequest.getOrderNo());
            OrderInfo orderInfo = orderDao.selecOrderById(orderDetailQuery);
            if (Objects.isNull(orderInfo)) {
                throw new GroundRuntimeException("未查询到订单信息");
            }
            Integer actualPrice = storeValueOrderPayRequest.getActualPrice();
            String updateBy = storeValueOrderPayRequest.getUpdateBy();
            //支付完成由回调修改
            //orderInfo.setOrderStatus(5);
            orderInfo.setPayType("7");
            orderInfo.setActualPrice(actualPrice);
            orderInfo.setUpdateBy(updateBy);
            orderInfo.setDistributorCode(storeValueOrderPayRequest.getStoreCode());
            orderInfo.setDistributorName(storeValueOrderPayRequest.getStoreName());
            // orderDao.updateOrder(orderInfo);
            orderDetailQuery.setOrderId(orderInfo.getOrderId());
            List<OrderDetailInfo> orderDetailInfoList = orderDetailDao.selectDetailById(orderDetailQuery);

            Integer productCount = 0;
            for (OrderDetailInfo orderDetailInfo : orderDetailInfoList) {
                productCount += orderDetailInfo.getAmount();
                orderDetailInfo.setUpdateBy(updateBy);

                orderDetailInfo.setActualPrice(orderDetailInfo.getActualPrice() * orderInfo.getActualPrice() / storeValueOrderPayRequest.getActualPrice());


                // orderDetailDao.updateOrderDetail(orderDetailInfo);
            }
            //增加结算信息
            //  addSettlementInfo(orderInfo.getOrderId(), productCount, updateBy, actualPrice);
            //支付信息
            addOrderPay(orderInfo, updateBy);
            orderInfo.setOrderdetailList(orderDetailInfoList);

            return HttpResponse.success(orderInfo);
        } catch (GroundRuntimeException e) {
            LOGGER.error("储值支付成功,更新订单异常:{}", e.getMessage());
            throw new GroundRuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new GroundRuntimeException("");
        }
    }

    private void addSettlementInfo(String orderId, Integer productCount, String updateBy, Integer actualPrice) throws Exception {
        SettlementInfo settlementInfo = new SettlementInfo();
        settlementInfo.setProductSum(productCount);
        settlementInfo.setOrderReceivable(actualPrice);
        settlementInfo.setOrderSum(actualPrice);
        settlementInfo.setOrderActual(actualPrice);
        settlementInfo.setCreateBy(updateBy);
        settlementInfo.setUpdateBy(updateBy);
        settlementService.addSettlement(settlementInfo, orderId);
    }

    private void addOrderPay(OrderInfo orderInfo, String updateBy) throws Exception {
        OrderPayInfo orderPayInfo = new OrderPayInfo();
        orderPayInfo.setOrderId(orderInfo.getOrderId());
        orderPayInfo.setOrderCode(orderInfo.getOrderCode());
        orderPayInfo.setPayId(IdUtil.uuid());
        orderPayInfo.setPayType(PayTypeEnum.BALANCE_PAY.getCode());
        orderPayInfo.setPayName(PayTypeEnum.BALANCE_PAY.getDesc());
        orderPayInfo.setPayWay(5);
        //支付成功
        orderPayInfo.setPayStatus(1);
        orderPayInfo.setPayPrice(orderInfo.getActualPrice());
        orderPayInfo.setCreateBy(updateBy);
        orderPayInfo.setUpdateBy(updateBy);
        orderPayDao.addOrderPay(orderPayInfo);
    }

    @Override
    public void updateOpenStatus(@Valid String distributorId) {


        String businessName = "";
        //最早成单日
        String createTime = orderDao.isExistOrder(distributorId);

        if (createTime.equals(DateUtil.sysDateYYYYMMDD())) {
            businessName = "新店";
        } else if (DateUtil.getBeforeDate(DateUtil.dateAfterMonth(createTime, 3), -1).equals(DateUtil.sysDateYYYYMMDD())) {
            businessName = "次新店1";
        } else if (DateUtil.getBeforeDate(DateUtil.dateAfterMonth(createTime, 6), -1).equals(DateUtil.sysDateYYYYMMDD())) {
            businessName = "次新店2";
        } else if (DateUtil.getBeforeDate(DateUtil.dateAfterMonth(createTime, 12), -1).equals(DateUtil.sysDateYYYYMMDD())) {
            businessName = "同店";
        } else if (DateUtil.getBeforeDate(DateUtil.dateAfterMonth(createTime, 24), -1).equals(DateUtil.sysDateYYYYMMDD())) {
            businessName = "老店";
        } else {

        }

        if (StringUtils.isNotBlank(businessName)) {

            //查询营业编码
            StringBuilder codeUrl = new StringBuilder();
            codeUrl.append(slcsIp).append("/basics/business/name");

            HttpClient httpGet = HttpClient.get("http://" + codeUrl.toString() + "?business_name=" + businessName);
            httpGet.action().status();
            HttpResponse result = httpGet.action().result(new TypeReference<HttpResponse>() {
            });
            String openStatus = (String) result.getData();

            LOGGER.info("门店编码ID:{},营业状态:{}", distributorId, openStatus);

            //访问地址
            StringBuilder sb = new StringBuilder();
            sb.append(slcsIp).append("/store/open/Status");

            HttpClient httpPost = HttpClient.get("http://" + sb.toString());
            httpPost.addParameter("store_id", distributorId);
            httpPost.addParameter("business_stage_code", openStatus);
            httpPost.action().status();
            HttpResponse httpResponse = httpPost.action().result(new TypeReference<HttpResponse>() {
            });
        }

    }


    @Override
    public HttpResponse memberLately(@Valid String memberId, @Valid String distributorId) {

        List<LatelyResponse> list = new ArrayList();
        try {
            list = orderDao.memberLately(memberId, distributorId);
            if (list != null && list.size() > 0) {
                list = list.stream().filter(l -> l.getPrice() != null && l.getPrice() > 0).collect(Collectors.toList());
            }
            return HttpResponse.success(list);
        } catch (Exception e) {
            LOGGER.info("error 最近消费订单 (消费时间/消费金额)e:{}", e);
            return HttpResponse.success(list);
        }
    }
}


