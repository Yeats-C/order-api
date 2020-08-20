/*****************************************************************
 * 模块名称：订单明细后台-实现层
 * 开发人员: 黄祉壹
 * 开发时间: 2018-11-05
 * ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.OrderIdAndAmountRequest;
import com.aiqin.mgs.order.api.domain.request.ProdisorRequest;
import com.aiqin.mgs.order.api.domain.request.ProductStoreRequest;
import com.aiqin.mgs.order.api.domain.response.*;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("all")
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailServiceImpl.class);


    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderDetailDao orderDetailDao;

    @Resource
    private OrderReceivingDao orderReceivingDao;

    @Resource
    private SettlementDao settlementDao;

    @Resource
    private OrderLogService orderLogService;
    @Resource
    private PrestorageOrderSupplyDetailDao prestorageOrderSupplyDetailDao;
    //商品项目地址
    @Value("${product_ip}")
    public String product_ip;

    //优惠券项目地址
    @Value("${product_cycle}")
    public String product_cycle;

    //批量添加sku销量
    @Value("${product_sku}")
    public String product_sku;


    //模糊查询订单明细列表......
    @Override
    public HttpResponse selectorderDetail(@Valid OrderDetailQuery orderDetailQuery) {


        try {

            //返回参数
            List<OrderDetailInfo> orderDetailList = new ArrayList();

            //根据参数查询所有的订单信息。
            List<OrderInfo> orderInfoList = new ArrayList();
            if (orderDetailQuery != null) {
                OrderQuery orderQuery = new OrderQuery();
                if (orderDetailQuery.getMemberidList() != null && orderDetailQuery.getMemberidList().size() > 0) {
                    orderQuery.setMemberidList(orderDetailQuery.getMemberidList());
                }
                if (orderDetailQuery.getMemberName() != null && !orderDetailQuery.getMemberName().equals("")) {
                    orderQuery.setMemberName(orderDetailQuery.getMemberName());
                }
                if (orderDetailQuery.getMemberPhone() != null && !orderDetailQuery.getMemberPhone().equals("")) {
                    orderQuery.setMemberPhone(orderDetailQuery.getMemberPhone());
                }
                if (orderDetailQuery.getOrderId() != null && !orderDetailQuery.getOrderId().equals("")) {
                    orderQuery.setOrderId(orderDetailQuery.getOrderId());
                }
                if (orderDetailQuery.getOrderStatus() != null) {
                    orderQuery.setOrderStatus(orderDetailQuery.getOrderStatus());
                }
                if (orderDetailQuery.getOriginTypeList() != null && orderDetailQuery.getOriginTypeList().size() > 0) {
                    orderQuery.setOriginTypeList(orderDetailQuery.getOriginTypeList());
                }
                if (orderDetailQuery.getDistributorId() != null && !orderDetailQuery.getDistributorId().equals("")) {
                    orderQuery.setDistributorId(orderDetailQuery.getDistributorId());
                }
                if (orderDetailQuery.getOrderIdList() != null && orderDetailQuery.getOrderIdList().size() > 0) {
                    orderQuery.setOrderIdList(orderDetailQuery.getOrderIdList());

                }

                //取出所有的订单Id
                orderInfoList = orderDao.selectOrder(orderQuery);
                if (orderDetailQuery.getOrderIdList() != null && orderDetailQuery.getOrderIdList().size() > 0) {
                    if (orderInfoList != null && orderInfoList.size() > 0) {
                        List<String> orderIdList = new ArrayList();
                        for (int i = 0; i < orderInfoList.size(); i++) {
                            OrderInfo info = new OrderInfo();
                            info = orderInfoList.get(i);
                            orderIdList.add(info.getOrderId());
                        }
                        orderDetailQuery.setOrderIdList(orderIdList);
                    }
                }
            }

            //通过订单查询订单明细
            orderDetailList = orderDetailDao.selectorderDetail(orderDetailQuery);

            //订单明细要带出订单的信息
            if (orderInfoList != null && orderInfoList.size() > 0) {
                if (orderDetailList != null && orderDetailList.size() > 0) {
                    for (int i = 0; i < orderDetailList.size(); i++) {
                        OrderDetailInfo detailInfo = new OrderDetailInfo();
                        detailInfo = orderDetailList.get(i);
                        for (OrderInfo orderInfo : orderInfoList) {
                            if (detailInfo.getOrderId().equals(orderInfo.getOrderId())) {
                                detailInfo.setMemberId(orderInfo.getMemberId());
                                detailInfo.setMemberName(orderInfo.getMemberName());
                                detailInfo.setMemberPhone(orderInfo.getMemberPhone());
                                if (orderInfo.getOriginType() != null) {
                                    detailInfo.setOriginType(String.valueOf(orderInfo.getOriginType()));
                                }
                                if (orderInfo.getReceiveType() != null) {
                                    detailInfo.setReceiveType(String.valueOf(orderInfo.getReceiveType()));
                                }
                            }
                        }
                    }
                }
            }

            //计算总数据量
            Integer totalCount = 0;
            Integer icount = null;
            orderDetailQuery.setIcount(icount);
            List<OrderDetailInfo> Icount_list = orderDetailDao.selectorderDetail(orderDetailQuery);
            if (Icount_list != null && Icount_list.size() > 0) {
                totalCount = Icount_list.size();
            }
            return HttpResponse.success(new PageResData(totalCount, orderDetailList));
        } catch (Exception e) {
            LOGGER.error("查询模糊查询订单明细列表失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //查询订单明细部分汇总-（支持活动ID汇总、）
    @Override
    public HttpResponse selectorderdetailsum(@Valid OrderDetailQuery orderDetailQuery) {

        try {
            OrderDetailInfo orderDetailInfo = orderDetailDao.selectorderdetailsum(orderDetailQuery);
            return HttpResponse.success(orderDetailInfo);

        } catch (Exception e) {

            LOGGER.error("查询订单明细部分汇总-（支持活动ID汇总、）失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)
    @Override
    public HttpResponse productOverviewByMonth(@Valid String distributorId, String year, String month) {

        try {

            List<Integer> originTypeList = null;
            String yearMonth = year + "-" + month + "-" + "01"; //"YYYY-MM"

            //月销售额
            Integer actualPrice = null;
            actualPrice = orderDao.selectByMonthAllAmt(distributorId, originTypeList, DateUtil.getFristOfMonthDay(DateUtil.formatDate(yearMonth)), DateUtil.getLashOfMonthDay(DateUtil.formatDate(yearMonth)));

            //月销量
            Integer amount = null;
            amount = orderDao.selectByMonthAcount(distributorId, originTypeList, DateUtil.getFristOfMonthDay(DateUtil.formatDate(yearMonth)), DateUtil.getLashOfMonthDay(DateUtil.formatDate(yearMonth)));

            OrderDetailInfo info = new OrderDetailInfo();
            info.setActualPrice(actualPrice != null ? actualPrice : 0);
            info.setAmount(amount != null ? amount : 0);
            return HttpResponse.success(info);

        } catch (Exception e) {
            LOGGER.error("商品概览菜单-月销量、月销售额(产品确认：统计维度为订单) 异常 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }

        //接口--商品概览门店sku月销量、月销售额
//		try {
//			return HttpResponse.success(orderDetailDao.productOverviewByMonth(distributorId,year,month));
//		} catch (Exception e) {
//			LOGGER.info("接口--商品概览门店sku月销量、月销售额失败......", e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}
    }


    //接口--商品概览产品销量、销售额-前5名
    @Override
    public HttpResponse productOverviewByOrderTop(String distributoId, String year, String month) {

        try {
            String yearMonth = year + "-" + month + "-" + "01"; //"YYYY-MM"

            return HttpResponse.success(orderDetailDao.productOverviewByOrderTop(distributoId, DateUtil.getFristOfMonthDay(DateUtil.formatDate(yearMonth)), DateUtil.getLashOfMonthDay(DateUtil.formatDate(yearMonth))));
        } catch (Exception e) {
            LOGGER.error("接口--商品概览产品销量、销售额-前5名失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //接口--商品概览产品销量、销售额-后5名
    @Override
    public HttpResponse productOverviewByOrderLast(String distributoId, String year, String month) {

        try {
            String yearMonth = year + "-" + month + "-" + "01"; //"YYYY-MM"

            return HttpResponse.success(orderDetailDao.productOverviewByOrderLast(distributoId, DateUtil.getFristOfMonthDay(DateUtil.formatDate(yearMonth)), DateUtil.getLashOfMonthDay(DateUtil.formatDate(yearMonth))));
        } catch (Exception e) {
            LOGGER.error("接口--商品概览产品销量、销售额-后5名失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    //接口--商品概览产品销量、销售额-前5名
    @Override
    public HttpResponse productFrontTop10(String distributorId, String beginTime, String endTime, String categoryId) {

        try {
            //String yearMonth = year+"-"+month+"-"+"01"; //"YYYY-MM"

            return HttpResponse.success(orderDetailDao.productFrontTop10(distributorId, DateUtil.getFristOfMonthDay(DateUtil.formatDate(beginTime)), DateUtil.getLashOfMonthDay(DateUtil.formatDate(endTime)), categoryId));
        } catch (Exception e) {
            LOGGER.error("接口--商品概览产品销量、销售额-前5名失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //接口--商品概览产品销量、销售额-后5名
    @Override
    public HttpResponse productAfterTop10(String distributorId, String beginTime, String endTime, String categoryId) {

        try {
            //	String yearMonth = year+"-"+month+"-"+"01"; //"YYYY-MM"

            return HttpResponse.success(orderDetailDao.productAfterTop10(distributorId, DateUtil.getFristOfMonthDay(DateUtil.formatDate(beginTime)), DateUtil.getLashOfMonthDay(DateUtil.formatDate(endTime)), categoryId));
        } catch (Exception e) {
            LOGGER.error("接口--商品概览产品销量、销售额-后5名失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //接口--会员管理-会员消费记录
    @Override
    public HttpResponse byMemberOrder(@Valid OrderDetailQuery orderDetailQuery) {

        List<OrderDetailByMemberResponse> list = null;
        try {
            list = orderDetailDao.byMemberOrder(OrderPublic.getOrderDetailQuery(orderDetailQuery));
        } catch (Exception e1) {
            LOGGER.error("查询接口--会员管理-会员消费记录异常: {}", e1);
        }

       /* List<String> projectList = new ArrayList();
        String project = "";
        OrderDetailByMemberResponse info = new OrderDetailByMemberResponse();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                info = list.get(i);
                project = info.getProductId();
                projectList.add(project);
            }

        }*/

/*

        StringBuilder sb = new StringBuilder();
        sb.append(product_ip).append(product_cycle);

        LOGGER.info("请求会员管理-会员消费记录，url为{}，参数为{}", sb.toString(), projectList);
        try {
            HttpClient httpPost = HttpClient.post("http://" + sb.toString()).json(projectList);
            httpPost.action().status();
            HttpResponse result = httpPost.action().result(new TypeReference<HttpResponse>() {
            });

            LOGGER.info("请求结果，result为{}", result.getData());
            List<Map> listmap = (List<Map>) result.getData();

            //获取consumecycle并且计算周期结束时间
            if (listmap != null && listmap.size() > 0) {
                for (Map map : listmap) {
                    String product_id = String.valueOf(map.get("product_id"));
                    String cycleString = String.valueOf(map.get("cycle"));

                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            info = list.get(i);
                            if (info.getProductId() != null && info.getProductId().equals(product_id)) {

                                Integer cycle = 0;
                                if (cycleString != null && !cycleString.equals("")) {
                                    cycle = Integer.valueOf(cycleString);
                                }
                                info.setConsumecycle(cycle);

                                Integer amount = 0;
                                if (info.getAmount() != null) {
                                    amount = info.getAmount();
                                }
                                Integer countenddate = cycle * amount;

                                info.setCycleenddate(DateUtil.afterThirdMonth(countenddate));
                                list.set(i, info);
                            }
                        }
                    }
                }
            }
*/

            //计算总数据量
            Integer totalCount = 0;
            Integer icount = null;
            orderDetailQuery.setIcount(icount);
            totalCount = orderDetailDao.byMemberOrderCount(OrderPublic.getOrderDetailQuery(orderDetailQuery));


//              return HttpResponse.success(OrderPublic.getData(list)); 
            return HttpResponse.success(new PageResData(totalCount, list));

       /* } catch (Exception e) {
            LOGGER.error("查询接口--会员管理-会员消费记录异常", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }*/

    }


    //查询BYorderid-返回订单明细数据、订单主数据、收货信息
    @Override
    public HttpResponse selectorderany(@Valid String orderId) {

        OrderodrInfo info = new OrderodrInfo();

        OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
        orderDetailQuery.setOrderId(orderId);
        OrderInfo orderInfo = new OrderInfo();
        try {
            //订单主数据

            orderInfo = orderDao.selecOrderById(orderDetailQuery);
            //获取SKU数量
			/*Integer skuSum =0;
			if (orderInfo!=null&&orderInfo.getOrderType()==4){
				//预存订单
				skuSum = getPrestorageSkuSum(orderId);
			}else {
				skuSum = getSkuSum(orderId);
			}*/

            Integer skuSum = getSkuSum(orderId);
            orderInfo.setSkuSum(skuSum);
            info.setOrderInfo(orderInfo);

        } catch (Exception e) {
            LOGGER.error("查询BYorderid-返回订单主数据", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }


        try {
            //订单明细数据
            List<OrderDetailInfo> detailList = new ArrayList<>();
            detailList = orderDetailDao.selectDetailById(orderDetailQuery);
            if (orderInfo != null && orderInfo.getOrderType() == 4) {
                //预存订单
                List<PrestorageOrderSupplyDetail> prestorageOrderSupplyDetails = prestorageOrderSupplyDetailDao.selectprestorageorderDetailsListByOrderId(orderId);
                for (PrestorageOrderSupplyDetail prestorageOrderSupplyDetail : prestorageOrderSupplyDetails) {
                    for (OrderDetailInfo orderDetailInfo : detailList) {
                        if (orderDetailInfo.getOrderDetailId().equals(prestorageOrderSupplyDetail.getOrderDetailId())) {
                            orderDetailInfo.setReturnAmount(prestorageOrderSupplyDetail.getReturnAmount());
                            orderDetailInfo.setSupplyAmount(prestorageOrderSupplyDetail.getSupplyAmount());
                            orderDetailInfo.setReturnPrestorageAmount(prestorageOrderSupplyDetail.getReturnPrestorageAmount());
                            //q取预存订单详情ID，方便退货时使用
                            orderDetailInfo.setOrderDetailId(prestorageOrderSupplyDetail.getPrestorageOrderSupplyDetailId());
                            //可提货数量
                            orderDetailInfo.setAbleSupplyAmount(prestorageOrderSupplyDetail.getAmount() - prestorageOrderSupplyDetail.getSupplyAmount() - prestorageOrderSupplyDetail.getReturnPrestorageAmount());
                        }
                    }

                }
            } /*else {
                detailList = orderDetailDao.selectDetailById(orderDetailQuery);
            }*/

            //为pos端判断是否可退货
            int state = checkTurn(detailList);
            info.setTurnReturnView(state);
            info.getOrderInfo().setTurnReturnView(state);


            info.setDetailList(detailList);

        } catch (Exception e) {
            LOGGER.error("查询BYorderid-返回订单明细数据", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }

        try {
            //收货信息
            //info.setReceivingInfo(orderReceivingDao.selecReceivingById(orderDetailQuery));

        } catch (Exception e) {
            LOGGER.error("查询BYorderid-返回订单收货信息", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        try {
            //结算信息
            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setOrderId(orderId);
            SettlementInfo settlementInfo = settlementDao.jkselectsettlement(orderQuery);
            if (settlementInfo != null) {
                settlementInfo.setActivityDiscount(Optional.ofNullable(settlementInfo.getActivityDiscount()).orElse(0) + Optional.ofNullable(settlementInfo.getFullSum()).orElse(0) + Optional.ofNullable(settlementInfo.getLuckySum()).orElse(0)+Optional.ofNullable(settlementInfo.getShopOrderPreferential()).orElse(0));
                settlementInfo.setTotalCouponsDiscount(settlementInfo.getActivityDiscount());
                if (orderInfo.getOrderStatus() == 0) {
                    settlementInfo.setOrderActual(0);
                    settlementInfo.setOrderReceivable(0);
                }
                info.setSettlementInfo(settlementInfo);
                if (orderInfo != null && orderInfo.getOrderType() == 4) {
                    //预存订单 储值卡金额
                }

            }


            return HttpResponse.success(info);
        } catch (Exception e) {
            LOGGER.error("查询BYorderid-返回订单结算信息", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    private Integer checkTurn(List<OrderDetailInfo> detailList) {
        if (detailList == null || detailList.size() == 0) {
            return 1;
        }
        for (OrderDetailInfo orderDetailInfo : detailList) {
            if (Optional.ofNullable(orderDetailInfo.getAmount()).orElse(0) > Optional.ofNullable(orderDetailInfo.getReturnAmount()).orElse(0) + Optional.ofNullable(orderDetailInfo.getReturnPrestorageAmount()).orElse(0)) {
                return 0;
            }
        }
        return 1;
    }

    private Integer getPrestorageSkuSum(String orderId) {
        Integer acount = null;
        OrderDetailQuery query = new OrderDetailQuery();
        query.setOrderId(orderId);
        try {
            acount = prestorageOrderSupplyDetailDao.getSkuSum(query);

        } catch (Exception e) {
            LOGGER.error("订单中商品sku数量失败 {}", e);
        }
        return acount != null ? acount : 0;
    }


    @Override
//	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Transactional
    public List<OrderDetailInfo> addDetailList(@Valid List<OrderDetailInfo> detailList, @Valid String orderId, @Valid String orderCode) throws Exception {

        List<OrderDetailInfo> list = new ArrayList();
        if (detailList != null && detailList.size() > 0) {
            for (OrderDetailInfo info : detailList) {

                //订单ID、订单明细ID
                info.setOrderId(orderId);
                info.setOrderCode(orderCode);
                info.setOrderDetailId(OrderPublic.getUUID());
                //保存
                orderDetailDao.addDetailList(info);
                list.add(info);
            }
        } else {
            LOGGER.warn("未获取订单明细数据.orderId: {}", orderId);
        }
        return list;
    }

    @Override
    @Transactional
    public List<OrderDetailInfo> updateDetailList(@Valid List<OrderDetailInfo> detailList, @Valid String orderId, @Valid String orderCode) throws Exception {

        List<OrderDetailInfo> list = new ArrayList();
        if (detailList != null && detailList.size() > 0) {
            for (OrderDetailInfo info : detailList) {
                info.setOrderCode(orderCode);

                orderDetailDao.updateOrderDetail(info);
                list.add(info);
            }
        } else {
            LOGGER.warn("未获取订单明细数据.orderId: {}", orderId);
        }
        return list;
    }

//	//查询会员下的所有订单ID下的商品集合...
//	@Override
//	public HttpResponse selectproductbyorders(@Valid List<String> orderidslList, @Valid String memberId) {
//				
//		        List<OrderProductsResponse> product_list=null;
//				try {
//					if(orderidslList !=null && orderidslList.size()>0) {
//					//保存
//				    product_list = orderDetailDao.selectproductbyorders(orderidslList,memberId);
//					}
//					return HttpResponse.success(product_list);
//				} catch (Exception e) {
//					LOGGER.error("查询会员下的所有订单ID下的商品集合失败",e);
//					return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//				}
//	}


    //查询BY会员ID,订单状态
    @Override
    public HttpResponse selectorderdbumemberid(@Valid String memberId, @Valid Integer orderStatus, @Valid String pageSize, @Valid String pageNo) {

        try {
            OrderQuery orderQuery = new OrderQuery();
            OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
            orderQuery.setMemberId(memberId);
            orderQuery.setOrderStatus(orderStatus);
            if (pageSize != null && !pageSize.equals("")) {
                orderQuery.setPageSize(Integer.valueOf(pageSize));
            }
            if (pageNo != null && !pageNo.equals("")) {
                orderQuery.setPageNo(Integer.valueOf(pageNo));
            }

            List<OrderInfo> order_list = orderDao.selectOrder(orderQuery);

            //计算总数据量
            Integer totalCount = 0;
            Integer icount = null;
            orderQuery.setIcount(icount);
            List<OrderInfo> Icount_list = orderDao.selectOrder(orderQuery);
            if (Icount_list != null && Icount_list.size() > 0) {
                totalCount = Icount_list.size();
            }

            if (order_list != null && order_list.size() > 0) {
                for (int i = 0; i < order_list.size(); i++) {
                    OrderInfo OrderInfo = order_list.get(i);
                    orderDetailQuery.setOrderId(OrderInfo.getOrderId());
                    List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);
                    OrderInfo.setOrderdetailList(detailList);
                    order_list.set(i, OrderInfo);
                }
            }

//			return HttpResponse.success(OrderPublic.getData(order_list));
            return HttpResponse.success(new PageResData(totalCount, order_list));

        } catch (Exception e) {
            LOGGER.error("查询BY会员ID,订单状态失败", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //修改订单明细退货数据
    @Override
    @Transactional
    public void returnStatus(@Valid String orderDetailId, Integer returnStatus, Integer returnAmount, String updateBy) throws Exception {

        OrderDetailInfo info = new OrderDetailInfo();

        info.setOrderDetailId(orderDetailId);
        info.setReturnStatus(returnStatus);
        info.setReturnAmount(returnAmount);
        info.setUpdateBy(updateBy);

        //更新数据
        LOGGER.info("更新退货状态参数：{}", info);
        orderDetailDao.returnStatus(info);

        //生成订单日志
        OrderLog rderLog = OrderPublic.addOrderLog(orderDetailId, Global.STATUS_2, "OrderDetailServiceImpl.returnStatus()",
                OrderPublic.getStatus(Global.STATUS_2, returnStatus), updateBy);

        LOGGER.info("生成订单日志参数：{}", rderLog);
        orderLogService.addOrderLog(rderLog);

    }


    //接口-统计商品在各个渠道的订单数.
    @Override
    public HttpResponse prodisor(@Valid ProdisorRequest prodisorRequest) {

        try {
            List<String> sukList = new ArrayList();
            List<Integer> originTypeList = new ArrayList();
            if (prodisorRequest != null) {
                sukList = prodisorRequest.getSkuList();
                originTypeList = prodisorRequest.getOriginTypeList();
            }
            //返回
            List<ProdisorResponse> list = new ArrayList();

            if (sukList != null && sukList.size() > 0) {

                //查询条件
                OrderDetailQuery query = new OrderDetailQuery();
                query.setSukList(sukList);
                query.setOriginTypeList(originTypeList);
                query.setStoreId(prodisorRequest.getStoreId());
                list = orderDetailDao.prodisor(OrderPublic.getOrderDetailQuery(query));
            } else {
                return HttpResponse.success(null);
            }

            return HttpResponse.success(list);
        } catch (Exception e) {
            LOGGER.error("接口-统计商品在各个渠道的订单数失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //订单中商品sku数量
    @Override
    public Integer getSkuSum(@Valid String orderId) {
        Integer acount = null;
        OrderDetailQuery query = new OrderDetailQuery();
        query.setOrderId(orderId);
        try {
            acount = orderDetailDao.getSkuSum(query);

        } catch (Exception e) {
            LOGGER.error("订单中商品sku数量失败 {}", e);
        }
        return acount != null ? acount : 0;

    }

    //sku销量统计
    @Override
    public HttpResponse skuSum(@Valid List<String> sukList) {

        try {
            List<SkuSumResponse> list = new ArrayList();
            if (sukList != null) {

                //销量
                Integer amount = null;
                //金额
                Integer price = null;
                //交易日期
                String transDate = "";

                for (String skuCode : sukList) {

                    for (int i = -13; i < 1; i++) {
                        amount = orderDetailDao.getSkuAmount(skuCode, DateUtil.NextDate(i));
                        price = orderDetailDao.getSkuPrice(skuCode, DateUtil.NextDate(i));
                        transDate = DateUtil.NextDate(i);

                        SkuSumResponse skuSumResponse = new SkuSumResponse();
                        skuSumResponse.setAmount(amount == null ? 0 : amount);
                        skuSumResponse.setPrice(price == null ? 0 : price);
                        skuSumResponse.setSkuCode(skuCode);
                        skuSumResponse.setTransDate(transDate);

                        list.add(skuSumResponse);
                    }

                }
            }

            return HttpResponse.success(list);

        } catch (Exception e) {
            LOGGER.error("订单中商品sku数量失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    //批量添加sku销量
    @Override
    public HttpResponse saveBatch(@Valid List<String> sukList) {

//		//商品库存
//		String url = product_ip+product_sku;
//		
//		if(sukList !=null && sukList.size()>0) {
//			
//		}

        return null;
    }


    //查询BYordercode-返回订单明细数据、订单数据、收货信息、结算数据
    @Override
    public HttpResponse selectorderSelde(@Valid String orderCode) {

        //返回数据
        OrderodrInfo info = new OrderodrInfo();

        //查询条件
        OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
        orderDetailQuery.setOrderCode(orderCode);
        String orderId = "";

        try {
            //订单主数据
            OrderInfo orderInfo = new OrderInfo();
            orderInfo = orderDao.selecOrderById(orderDetailQuery);
            if (orderInfo != null && orderInfo.getOrderId() != null) {
                orderId = orderInfo.getOrderId();
                orderDetailQuery.setOrderId(orderInfo.getOrderId());
                orderDetailQuery.setOrderCode(orderInfo.getOrderCode());
            }

            //获取SKU数量
            Integer skuSum = null;
            skuSum = getSkuSum(orderId);
            orderInfo.setSkuSum(skuSum != null ? skuSum : 0);
            if (orderInfo != null) {
                info.setOrderInfo(orderInfo);
            }
        } catch (Exception e) {
            LOGGER.error("查询BYorderid-返回订单主数据 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }

        try {
            //订单明细数据
            List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);

            if (detailList != null && detailList.size() > 0) {
                info.setDetailList(detailList);
            }

        } catch (Exception e) {
            LOGGER.error("查询BYorderid-返回订单明细数据 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
		/*try {
		//收货信息
	    OrderReceivingInfo orderReceivingInfo = new OrderReceivingInfo();
	    orderReceivingInfo=orderReceivingDao.selecReceivingById(orderDetailQuery);

	    if(orderReceivingInfo !=null ) {
	    	info.setReceivingInfo(orderReceivingInfo);
	    }
	     
		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单收货信息异常 {}",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}*/
        try {
            //结算信息
            OrderQuery orderQuery = new OrderQuery();
            orderQuery.setOrderId(orderId);
            info.setSettlementInfo(settlementDao.jkselectsettlement(orderQuery));

            return HttpResponse.success(info);
        } catch (Exception e) {
            LOGGER.error("查询BYorderid-返回订单结算信息异常 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }


    @Override
    public List<SkuSaleResponse> selectSkuSale(@Valid List<String> orderList) {

        try {
            return orderDetailDao.selectSkuSale(orderList);
        } catch (Exception e) {
            LOGGER.error("查询SKU+销量 异常 {}", e);
            return null;
        }
    }


    //顾客可能还想购买
    @Override
    public HttpResponse wantBuy(@Valid List<String> sukList) {
        try {
            return HttpResponse.success(orderDetailDao.wantBuy(sukList));
        } catch (Exception e) {
            LOGGER.error("查询顾客可能还想购买 异常 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    @Override
    public HttpResponse productStore(@Valid ProductStoreRequest productStoreRequest) {
        try {
            List<String> sukList = new ArrayList();
            List<Integer> originTypeList = new ArrayList();
            if (productStoreRequest != null) {
                sukList = productStoreRequest.getSkuList();
                originTypeList = productStoreRequest.getOriginTypeList();
            }
            //返回
            List<ProductStoreResponse> list = new ArrayList();

            if (sukList != null && sukList.size() > 0) {
                //查询条件
//				list = orderDetailDao.productStore(productStoreRequest);
            } else {
                return HttpResponse.success(null);
            }

            return HttpResponse.success(list);
        } catch (Exception e) {
            LOGGER.error("接口-统计商品在各个渠道的订单数失败 {}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    @Override
    public HttpResponse batchAddOrder(List<OrderodrInfo> cartInfo) {
        if (cartInfo == null) {
            return new HttpResponse();
        }
        for (OrderodrInfo orderodrInfo : cartInfo) {

            String orderCode = DateUtil.sysDate() + Global.ORIGIN_COME_3 + String.valueOf(Global.ORDERID_CHANNEL_4) + OrderPublic.randomNumberF();


            orderodrInfo.getOrderInfo().setOrderCode(orderCode);
            try {
                orderDao.addOrderInfo(orderodrInfo.getOrderInfo());
                for (OrderDetailInfo orderDetailInfo : orderodrInfo.getDetailList()) {
                    orderDetailInfo.setOrderCode(orderCode);
                    orderDetailInfo.setOrderDetailId(OrderPublic.getUUID());
                    orderDetailInfo.setOrderId(orderodrInfo.getOrderInfo().getOrderId());
                    orderDetailDao.addDetailList(orderDetailInfo);

                }
                //新增订单结算数据
                orderodrInfo.getSettlementInfo().setSettlementId(OrderPublic.getUUID());

                settlementDao.addSettlement(orderodrInfo.getSettlementInfo());


                //新增订单支付数据
             /*   if (orderPayList != null && orderPayList.size() > 0) {
                    try {
                        settlementService.addOrderPayList(orderPayList, orderId, orderCode);
                    } catch (Exception e) {
                        LOGGER.error("新增订单支付数据异常：{}", e);
                    }
                }*/
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }

        }

        return HttpResponse.successGenerics(cartInfo);
    }

    @Override
    public HttpResponse findListByOrderCode(List<String> orderCodeList) {
        List<OrderInfo> orderInfoList = orderDao.findListByOrderCode(orderCodeList);
        return HttpResponse.successGenerics(orderInfoList);
    }

    @Override
    public HttpResponse findOrderDetailById(String orderDetailId) {
        return HttpResponse.success(orderDetailDao.findOrderDetailById(orderDetailId));
    }


//	//查询BYorderid-返回订单明细数据、订单数据、收货信息、结算数据、退货数据
//	@Override
//	public HttpResponse selectorderjoin(@Valid String orderId) {
//
//		OrderodrInfo info = new OrderodrInfo();
//		OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
//		orderDetailQuery.setOrderId(orderId);
//		    
//		try {
//		    //组装订单明细数据
//	        List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);
//		
//		    info.setDetailList(detailList);
//
//		} catch (Exception e) {
//			LOGGER.error("查询BYorderid-返回订单明细数据",e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}
//		try {
//		    //组装订单主数据
//		    info.setOrderInfo(orderDao.selecOrderById(orderDetailQuery));
//		} catch (Exception e) {
//			LOGGER.error("查询BYorderid-返回订单主数据",e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}
//		try {
//		    //组装收货信息
//		    info.setOrderReceivingInfo(orderReceivingDao.selecReceivingById(orderDetailQuery));
//		
//		     
//		} catch (Exception e) {
//			LOGGER.error("查询BYorderid-返回订单收货信息",e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}
//		try {
//			//组装结算信息
//			OrderQuery orderQuery = new OrderQuery();
//			orderQuery.setOrderId(orderId);
//			info.setSettlementInfo(settlementDao.jkselectsettlement(orderQuery));
//			
//			return HttpResponse.success(info);
//		} catch (Exception e) {
//			LOGGER.error("查询BYorderid-返回订单结算信息",e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}				
//	}


}


