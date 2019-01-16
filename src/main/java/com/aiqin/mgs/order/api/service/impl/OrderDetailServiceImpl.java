/*****************************************************************

* 模块名称：订单明细后台-实现层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CartDao;
import com.aiqin.mgs.order.api.dao.OrderDao;
import com.aiqin.mgs.order.api.dao.OrderDetailDao;
import com.aiqin.mgs.order.api.dao.OrderReceivingDao;
import com.aiqin.mgs.order.api.dao.SettlementDao;
import com.aiqin.mgs.order.api.domain.CartInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderDetailQuery;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.OrderListInfo;
import com.aiqin.mgs.order.api.domain.OrderLog;
import com.aiqin.mgs.order.api.domain.OrderQuery;
import com.aiqin.mgs.order.api.domain.OrderodrInfo;
import com.aiqin.mgs.order.api.domain.ProductCycle;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.OrderDetailRequest;
import com.aiqin.mgs.order.api.domain.response.OrderDetailByMemberResponse;
import com.aiqin.mgs.order.api.domain.response.OrderProductsResponse;
import com.aiqin.mgs.order.api.domain.response.ProdisorResponse;
import com.aiqin.mgs.order.api.domain.response.SkuSumResponse;
import com.aiqin.mgs.order.api.service.CartService;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderLogService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.fasterxml.jackson.core.type.TypeReference;

@SuppressWarnings("all")
@Service
public class OrderDetailServiceImpl implements OrderDetailService{

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
		LOGGER.info("查询模糊查询订单明细列表开始......", orderDetailQuery);
		List<OrderDetailInfo> orderDetailList =null;
		
			orderDetailList = orderDetailDao.selectorderDetail(orderDetailQuery);
			
			//计算总数据量
			Integer totalCount = 0;
			Integer icount =null;
			orderDetailQuery.setIcount(icount);
			List<OrderDetailInfo> Icount_list= orderDetailDao.selectorderDetail(orderDetailQuery);
			if(Icount_list !=null && Icount_list.size()>0) {
				totalCount = Icount_list.size();
			}
			return HttpResponse.success(new PageResData(totalCount,orderDetailList));
		} catch (Exception e) {
			LOGGER.info("查询模糊查询订单明细列表失败......", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


    //查询订单明细部分汇总-（支持活动ID汇总、）
	@Override
	public HttpResponse selectorderdetailsum(@Valid OrderDetailQuery orderDetailQuery) {
		
		LOGGER.info("查询订单明细部分汇总-（支持活动ID汇总、）......", orderDetailQuery);
		try {
			
			return HttpResponse.success(orderDetailDao.selectorderdetailsum(orderDetailQuery));
		
		} catch (Exception e) {
			
			LOGGER.info("查询订单明细部分汇总-（支持活动ID汇总、）失败......", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)
	@Override
	public HttpResponse productOverviewByMonth(@Valid String distributorId,String year, String month) {
		
		try {
			//月销售额
			Integer actualPrice = 0;
			List<Integer> originTypeList = null;
			String yearMonth = year+"-"+month; //"YYYY-MM"
			actualPrice = orderDao.selectByMonthAllAmt(distributorId,originTypeList,yearMonth);
			
			//月销量
			Integer amount = 0;
			amount = orderDao.selectByMonthAcount(distributorId,originTypeList,yearMonth);
			
			OrderDetailInfo info = new OrderDetailInfo();
			info.setActualPrice(actualPrice);
			info.setAmount(amount);
			return HttpResponse.success(info);
		} catch (Exception e) {
			LOGGER.info("商品概览菜单-月销量、月销售额(产品确认：统计维度为订单)......", e);
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
	public HttpResponse productOverviewByOrderTop(String distributor_id,String year, String month) {
		
		try {
			return HttpResponse.success(orderDetailDao.productOverviewByOrderTop(distributor_id,year,month));
		} catch (Exception e) {
			LOGGER.info("接口--商品概览产品销量、销售额-前5名失败......", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}
	
	
	//接口--商品概览产品销量、销售额-后5名
	@Override
	public HttpResponse productOverviewByOrderLast(String distributor_id,String year, String month) {
		
		try {
			return HttpResponse.success(orderDetailDao.productOverviewByOrderLast(distributor_id,year,month));
		} catch (Exception e) {
			LOGGER.info("接口--商品概览产品销量、销售额-后5名失败......", e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


    //接口--会员管理-会员消费记录
    @Override
    public HttpResponse byMemberOrder(@Valid OrderDetailQuery orderDetailQuery) {
      
       List<OrderDetailByMemberResponse> list = null;
	   try {
		   list = orderDetailDao.byMemberOrder(orderDetailQuery);
	   } catch (Exception e1) {
		   LOGGER.error("查询接口--会员管理-会员消费记录异常",e1);
	   }
      
      List<String> projectList = new ArrayList();
      String project ="";
      OrderDetailByMemberResponse info = new OrderDetailByMemberResponse();
      
      if(list !=null && list.size()>0) {
    	  for(int i=0;i<list.size();i++) { 
    		  info = list.get(i);
    		  project =info.getProductId();
    		  projectList.add(project);
      	}
    	
      }
      String url = product_ip+product_cycle;
      LOGGER.info("请求会员管理-会员消费记录，url为{}，参数为{}", url, projectList);
          try {
        	  HttpClient httpPost = HttpClient.post("http://"+url).json(projectList); 
        	  httpPost.action().status();
        	  System.out.println("status=========="+httpPost.action().status());
        	  HttpResponse result = httpPost.action().result(new TypeReference<HttpResponse>(){});
              
        	  LOGGER.info("请求结果，result为{}", result.getData());
              List<Map> listmap =  (List<Map>) result.getData();
              
              //获取consumecycle并且计算周期结束时间
              if(listmap !=null && listmap.size()>0) {
            	  for(Map map : listmap) {
                	  String product_id = String.valueOf(map.get("product_id"));
                	  String cycleString = String.valueOf(map.get("cycle"));
                	  
                	  if(list !=null && list.size()>0) {
                	  for(int i=0;i<list.size();i++) {
                		  info = list.get(i);
                		  if(info.getProductId() !=null && info.getProductId().equals(product_id)) {
                			  
                			  Integer cycle =0;
                			  if(cycleString !=null && !cycleString.equals("")) {
                				  cycle = Integer.valueOf(cycleString);
                        	  }
                			  info.setConsumecycle(cycle);
                			  
                			  Integer amount = 0;
                			  if(info.getAmount() !=null ) {
                				  amount = info.getAmount();
                			  }
                			  Integer countenddate = cycle*amount;
                			  
                			  info.setCycleenddate(OrderPublic.afterThirdMonth(countenddate));           			  
                			  list.set(i, info);
                		  }
                	  }
                	  }
                  } 
              }
              
  			//计算总数据量
  			Integer totalCount = 0;
  			Integer icount =null;
  			orderDetailQuery.setIcount(icount);
  			List<OrderDetailByMemberResponse> Icount_list= orderDetailDao.byMemberOrder(orderDetailQuery);
  			if(Icount_list !=null && Icount_list.size()>0) {
  				totalCount = Icount_list.size();
  			}
              
//              return HttpResponse.success(OrderPublic.getData(list)); 
              return HttpResponse.success(new PageResData(totalCount,list));
              
          } catch (Exception e) {
              LOGGER.error("查询接口--会员管理-会员消费记录异常",e);
              return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
          }
 
   }


    //查询BYorderid-返回订单明细数据、订单主数据、收货信息
	@Override
	public HttpResponse selectorderany(@Valid String orderId) {
		
		OrderodrInfo info = new OrderodrInfo();
		
		OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
		orderDetailQuery.setOrderId(orderId);
		try {
		//订单明细数据
	    List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);
		
		info.setDetailList(detailList);

		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单明细数据",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		try {
		//订单主数据
		OrderInfo orderInfo = new OrderInfo();
		orderInfo = orderDao.selecOrderById(orderDetailQuery);
		//获取SKU数量
		Integer skuSum =0;
		skuSum = getSkuSum(orderId);
		orderInfo.setSkuSum(skuSum);
		info.setOrderInfo(orderInfo);
		
		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单主数据",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		try {
		//收货信息
		info.setReceivingInfo(orderReceivingDao.selecReceivingById(orderDetailQuery));
		     
		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单收货信息",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		try {
			//结算信息
			OrderQuery orderQuery = new OrderQuery();
			orderQuery.setOrderId(orderId);
			info.setSettlementInfo(settlementDao.jkselectsettlement(orderQuery));
			
			return HttpResponse.success(info);
			} catch (Exception e) {
				LOGGER.error("查询BYorderid-返回订单结算信息",e);
				return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
			}				
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<OrderDetailInfo> addDetailList(@Valid List<OrderDetailInfo> detailList, @Valid String orderId) throws Exception {
		
		List<OrderDetailInfo> list = new ArrayList();
		if(detailList !=null && detailList.size()>0) {
			for(OrderDetailInfo info : detailList) {
				
				//订单ID、订单明细ID
				info.setOrderId(orderId);
				info.setOrderDetailId(OrderPublic.getUUID());
				//保存
				orderDetailDao.addDetailList(info);
				list.add(info);
			}
		}else {
			LOGGER.error("为获取到订单明细数据....");
		}
		return list;
	}


	//查询会员下的所有订单ID下的商品集合...
	@Override
	public HttpResponse selectproductbyorders(@Valid List<String> orderidslList, @Valid String memberId) {
				
		        List<OrderProductsResponse> product_list=null;
				try {
					if(orderidslList !=null && orderidslList.size()>0) {
					//保存
				    product_list = orderDetailDao.selectproductbyorders(orderidslList,memberId);
					}
					return HttpResponse.success(product_list);
				} catch (Exception e) {
					LOGGER.error("查询会员下的所有订单ID下的商品集合失败",e);
					return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
				}
	}



	//查询BY会员ID,订单状态
	@Override
	public HttpResponse selectorderdbumemberid(@Valid String memberId, @Valid Integer orderStatus,@Valid String pageSize, @Valid String pageNo) {
		
		try {
			OrderQuery orderQuery = new OrderQuery();
			OrderDetailQuery orderDetailQuery = new OrderDetailQuery();
			orderQuery.setMemberId(memberId);
			orderQuery.setOrderStatus(orderStatus);
			if(pageSize !=null && !pageSize.equals("")) {
				orderQuery.setPageSize(Integer.valueOf(pageSize));
			}
			if(pageNo !=null && !pageNo.equals("")) {
				orderQuery.setPageNo(Integer.valueOf(pageNo));
			}

			List<OrderInfo> order_list= orderDao.selectOrder(orderQuery);
			
			//计算总数据量
			Integer totalCount = 0;
			Integer icount =null;
			orderQuery.setIcount(icount);
			List<OrderInfo> Icount_list= orderDao.selectOrder(orderQuery);
			if(Icount_list !=null && Icount_list.size()>0) {
				totalCount = Icount_list.size();
			}
			
			if(order_list !=null && order_list.size()>0) {
			for(int i=0;i<order_list.size();i++){
				OrderInfo OrderInfo = order_list.get(i);
				orderDetailQuery.setOrderId(OrderInfo.getOrderId());
				List<OrderDetailInfo> detail_list = orderDetailDao.selectDetailById(orderDetailQuery);
				OrderInfo.setOrderdetailList(detail_list);
				order_list.set(i, OrderInfo);
			}
			}

//			return HttpResponse.success(OrderPublic.getData(order_list));
			return HttpResponse.success(new PageResData(totalCount,order_list));
					
		} catch (Exception e) {
			LOGGER.error("查询BY会员ID,订单状态失败",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//修改订单明细退货数据
	@Override
	public void returnStatus(@Valid String orderDetailId,Integer returnStatus,Integer returnAmount, String updateBy) throws Exception {
		
		OrderDetailInfo info = new OrderDetailInfo();
		
		info.setOrderDetailId(orderDetailId);
		info.setReturnStatus(returnStatus);
		info.setReturnAmount(returnAmount);
		info.setUpdateBy(updateBy);
		
		//更新数据
	    orderDetailDao.returnStatus(info);
	    
	    //生成订单日志
	    OrderLog rderLog = OrderPublic.addOrderLog(orderDetailId,Global.STATUS_2,"OrderDetailServiceImpl.returnStatus()",
	  	OrderPublic.getStatus(Global.STATUS_2,returnStatus),updateBy);
	    orderLogService.addOrderLog(rderLog);

	}


	//接口-统计商品在各个渠道的订单数.
	@Override
	public HttpResponse prodisor(@Valid List<String> sukList,@Valid List<Integer> originTypeList) {

		try {
			//返回
			List<ProdisorResponse> list = new ArrayList();
			
			if(sukList !=null && sukList.size()>0) {
				
				//查询条件
				OrderDetailQuery query = new OrderDetailQuery();
				query.setSukList(sukList);
				query.setOriginTypeList(originTypeList);
				list = orderDetailDao.prodisor(OrderPublic.getOrderDetailQuery(query));
			}else {
				
			}
			
			return HttpResponse.success(list);
		} catch (Exception e) {
			LOGGER.error("接口-统计商品在各个渠道的订单数失败",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//订单中商品sku数量
	@Override
	public Integer getSkuSum(@Valid String orderId) {
		Integer acount = 0;
		OrderDetailQuery query = new OrderDetailQuery();
		query.setOrderId(orderId);
		try {
			acount = orderDetailDao.getSkuSum(query);
			
		} catch (Exception e) {
			LOGGER.error("订单中商品sku数量失败",e);
		}
		return acount;
		
	}

	//sku销量统计
	@Override
	public HttpResponse skuSum(@Valid List<String> sukList) {
		
		try {
		List<SkuSumResponse> list = new ArrayList();
		if(sukList !=null) {
			
			//销量
			Integer amount = null;
			//金额
			Integer price = null;
			//交易日期
			String transDate = "";
			
            for(String skuCode : sukList) {
            	
            	for(int i=-14;i<1;i++) {
            		amount = orderDetailDao.getSkuAmount(skuCode,OrderPublic.NextDate(i));
            		price = orderDetailDao.getSkuPrice(skuCode,OrderPublic.NextDate(i));
            		transDate = OrderPublic.NextDate(i);
               	    
            		SkuSumResponse skuSumResponse = new SkuSumResponse();
            		skuSumResponse.setAmount(amount==null?0:amount);
               	    skuSumResponse.setPrice(price==null?0:price);
               	    skuSumResponse.setSkuCode(skuCode);
               	    skuSumResponse.setTransDate(transDate);
               	    
               	    list.add(skuSumResponse);
            	}
            	 
            }
		 }
		
		return HttpResponse.success(list);
		
		} catch (Exception e) {
			LOGGER.error("订单中商品sku数量失败",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
	}


	//批量添加sku销量
	@Override
	public HttpResponse saveBatch(@Valid List<String> sukList) {
		
		//商品库存
		String url = product_ip+product_sku;
		
		if(sukList !=null && sukList.size()>0) {
			
		}
		
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
			if(orderInfo !=null && orderInfo.getOrderId() !=null ) {
				orderId = orderInfo.getOrderId();
				orderDetailQuery.setOrderId(orderInfo.getOrderId());
			}
			
			//获取SKU数量
			Integer skuSum =0;
			skuSum = getSkuSum(orderId);
			orderInfo.setSkuSum(skuSum);
			info.setOrderInfo(orderInfo);
			
		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单主数据",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		
		try {
		//订单明细数据
	    List<OrderDetailInfo> detailList = orderDetailDao.selectDetailById(orderDetailQuery);
		
		info.setDetailList(detailList);

		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单明细数据",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		try {
		//收货信息
		info.setReceivingInfo(orderReceivingDao.selecReceivingById(orderDetailQuery));
		     
		} catch (Exception e) {
			LOGGER.error("查询BYorderid-返回订单收货信息",e);
			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
		}
		try {
			//结算信息
			OrderQuery orderQuery = new OrderQuery();
			orderQuery.setOrderId(orderId);
			info.setSettlementInfo(settlementDao.jkselectsettlement(orderQuery));
			
			return HttpResponse.success(info);
			} catch (Exception e) {
				LOGGER.error("查询BYorderid-返回订单结算信息",e);
				return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
			}				
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
//		    System.out.println(info);
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
//			System.out.println(info);
//			return HttpResponse.success(info);
//		} catch (Exception e) {
//			LOGGER.error("查询BYorderid-返回订单结算信息",e);
//			return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
//		}				
//	}
	
	
}


