package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.domain.OrderMonthCalculateInfo;
import com.aiqin.mgs.order.api.domain.ReportCategoryVo;
import com.aiqin.mgs.order.api.domain.ReportCopartnerSaleVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.OrderCalculateService;
import com.aiqin.mgs.order.api.service.ReportCopartnerSaleService;
import com.aiqin.mgs.order.api.util.DateUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:新建合伙人销售报表
 *
 * @author huangzy
 * @create 2020-02-19
 */
@Component
@Transactional
public class ReportCopartnerSaleJob {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportCopartnerSaleJob.class);
	
	@Resource
    private ReportCopartnerSaleService reportCopartnerSaleService;
	
	@Resource
    private CopartnerAreaService copartnerAreaService;
	@Resource
	private OrderCalculateService orderCalculateService;

    /**
     * 新建合伙人销售报表
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void saveReportCopartnerSale() {
    	save();
    }

	private void save() {
		try {
			//获取订单统计
			 List<OrderMonthCalculateInfo> list = orderCalculateService.copartnerMonth();
			
			if(CollectionUtils.isNotEmpty(list)) {
				String year = DateUtil.afterMonth(0).split("-")[0];
				String month = DateUtil.afterMonth(0).split("-")[1];
				
				reportCopartnerSaleService.delete(year,month);
				
				//门店订单入库
				for(OrderMonthCalculateInfo orderMonthCalculateInfo : list) {
					ReportCopartnerSaleVo vo = new ReportCopartnerSaleVo();
					try {
						BeanUtils.copyProperties(vo, orderMonthCalculateInfo);
						vo.setReportYear(year);
						vo.setReportMonth(month);
						vo.setReportSubtotalType(1); //门店
						//区域
						CopartnerAreaUp area =  copartnerAreaService.qryInfo(orderMonthCalculateInfo.getStoreCode());
						if(area !=null) {
							vo.setCopartnerAreaId(area.getCopartnerAreaId());
							vo.setCopartnerAreaName(area.getCopartnerAreaName());
						}else {
							vo.setCopartnerAreaId("0000");
							vo.setCopartnerAreaName("其他");
						}
						//先删后增
						reportCopartnerSaleService.save(vo);
					} catch (Exception e) {
						LOGGER.error("新建合伙人销售报表订单入库失败,{}",e);
					}
				}
				
				//添加区域数据,汇总入库调整为实时查询
				List<ReportCopartnerSaleVo> areaList = reportCopartnerSaleService.qryAreaInit(year,month);
				if(CollectionUtils.isNotEmpty(areaList)) {
					for(ReportCopartnerSaleVo vo : areaList) {
						//先删后增
						vo.setCopartnerAreaName(vo.getCopartnerAreaName()+"-小计");
//						reportCopartnerSaleService.deleteByArea(vo.getCopartnerAreaId(),year,month);
						vo.setReportSubtotalType(2); //HUANGZYTODO
						reportCopartnerSaleService.save(vo);
					}
				}
				
				//添加月份数据,汇总入库调整为实时查询
				//先删后增
				ReportCopartnerSaleVo monthVo = new ReportCopartnerSaleVo();
//				reportCopartnerSaleService.qryMonthTotal(year,month);
//				reportCopartnerSaleService.deleteByMonth(year,month);
				monthVo.setReportYear(year);
				monthVo.setReportMonth(month);
				monthVo.setReportSubtotalType(3); //HUANGZYTODO
				reportCopartnerSaleService.save(monthVo);
				
			}
		}catch(Exception e) {
			LOGGER.error("{}",e);
		}
		
	}
}
