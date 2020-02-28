package com.aiqin.mgs.order.api.jobs;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.OrderMonthCalculateInfo;
import com.aiqin.mgs.order.api.domain.ReportCategoryVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.request.activity.ProductCategoryRespVO;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.OrderCalculateService;
import com.aiqin.mgs.order.api.service.ReportCategoryService;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * 描述:新建品类销售报表
 *
 * @author huangzy
 * @create 2020-02-19
 */
@Component
@Transactional
public class ReportCategorySaleJob {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportCategorySaleJob.class);
	
	@Resource
    private CopartnerAreaService copartnerAreaService;
	@Resource
	private OrderCalculateService orderCalculateService;
	@Resource
	private ReportCategoryService reportCategoryService;
	
	@Value("${bridge.url.product-api}")
    private String productUrl;
	

    /**
     * 新增品类销售报表
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void saveReportCategorySale() {
    	try {
			
    		//获取已完成订单统计
			List<ReportCategoryVo> list = orderCalculateService.storeCategoryCopartnerMonth();
			
			if(CollectionUtils.isNotEmpty(list)) {
				String year = DateUtil.afterMonth(0).split("-")[0];
				String month = DateUtil.afterMonth(0).split("-")[1];
				
				reportCategoryService.delete(year,month);
				
				//门店订单入库
				for(ReportCategoryVo reportCategoryVo : list) {
					ReportCategoryVo vo = new ReportCategoryVo();
					try {
						BeanUtils.copyProperties(vo, reportCategoryVo);
						vo.setReportYear(year);
						vo.setReportMonth(month);
						vo.setReportSubtotalType(1); //门店
						//区域
						CopartnerAreaUp area =  copartnerAreaService.qryInfo(reportCategoryVo.getStoreCode());
						if(area !=null) {
							vo.setCopartnerAreaId(area.getCopartnerAreaId());
							vo.setCopartnerAreaName(area.getCopartnerAreaName());
						}else {
							vo.setCopartnerAreaId("0000");
							vo.setCopartnerAreaName("其他");
						}
						
						//获取一级品类
						vo = getlevelA(vo);
						
						//先删后增
						reportCategoryService.save(vo);
						
					} catch (Exception e) {
						LOGGER.error("新建合伙人销售报表订单入库失败,{}",e);
					}
				}
				
				//添加区域数据,汇总入库调整为实时查询
				List<ReportCategoryVo> areaList = reportCategoryService.qryAreaInit(year,month);
				if(CollectionUtils.isNotEmpty(areaList)) {
					for(ReportCategoryVo vo : areaList) {
						//先删后增
						vo.setCopartnerAreaName(vo.getCopartnerAreaName()+"-小计");
//						reportCopartnerSaleService.deleteByArea(vo.getCopartnerAreaId(),year,month);
						vo.setReportSubtotalType(2); //HUANGZYTODO
						reportCategoryService.save(vo);
					}
				}
				
				//添加月份数据,汇总入库调整为实时查询
				//先删后增
				ReportCategoryVo monthVo = new ReportCategoryVo();
//				reportCopartnerSaleService.qryMonthTotal(year,month);
//				reportCopartnerSaleService.deleteByMonth(year,month);
				monthVo.setReportYear(year);
				monthVo.setReportMonth(month);
				monthVo.setReportSubtotalType(3); //HUANGZYTODO
				reportCategoryService.save(monthVo);
				
			}
		}catch(Exception e) {
			LOGGER.error("{}",e);
		}
    }

    
    /**
     * 获取一级品类
     * @param vo
     * @return
     */
	private ReportCategoryVo getlevelA(ReportCategoryVo vo) {
		StringBuffer sb = new StringBuffer();
		sb.append(productUrl+"/product/category/list/categoryCodes?category_codes="+vo.getChildCategoryCode()+"");
		HttpClient httpClient = HttpClient.get(sb.toString());
		HttpResponse response = httpClient.action().result(HttpResponse.class);
		List<ProductCategoryRespVO> list = JsonUtil.fromJson(JsonUtil.toJson(response.getData()),new TypeReference<List<ProductCategoryRespVO>>() {}); 
		if(CollectionUtils.isNotEmpty(list)) {
			ProductCategoryRespVO productCategoryRespVO = list.get(0);
			vo.setCategoryCode(productCategoryRespVO.getCategoryId());
			vo.setCategoryName(productCategoryRespVO.getCategoryName());
		}
		return vo;
	}
}
