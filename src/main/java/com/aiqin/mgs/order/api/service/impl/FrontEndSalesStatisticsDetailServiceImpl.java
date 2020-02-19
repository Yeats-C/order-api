package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.FrontEndSalesStatisticsDao;
import com.aiqin.mgs.order.api.dao.FrontEndSalesStatisticsDetailDao;
import com.aiqin.mgs.order.api.domain.dto.FrontEndSalesStatisticsByCategoryDTO;
import com.aiqin.mgs.order.api.domain.dto.FrontEndSalesStatisticsBySkuDTO;
import com.aiqin.mgs.order.api.domain.dto.FrontEndSalesStatisticsResponse;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatistics;
import com.aiqin.mgs.order.api.domain.statistical.FrontEndSalesStatisticsDetail;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsDetailService;
import com.aiqin.mgs.order.api.service.FrontEndSalesStatisticsService;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatisticsDetailServiceImpl
 * @Description
 * @Date 2020/2/15 14:31
 */
@Service
@Slf4j
public class FrontEndSalesStatisticsDetailServiceImpl implements FrontEndSalesStatisticsDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndSalesStatisticsDetailServiceImpl.class);

    @Resource
    private FrontEndSalesStatisticsDetailDao dao;

    @Override
    public List<FrontEndSalesStatisticsDetail> selectYesterdaySalesStatistics() {

        return dao.selectYesterdaySalesStatistics();
    }

    @Override
    public Integer insertSalesStatisticsDetailList(List<FrontEndSalesStatisticsDetail> list) {
        if (list == null || list.size() == 0) {
            return -1;
        }
        return dao.insertSalesStatisticsDetailList(list);
    }

    @Override
    public HttpResponse selectStoreMonthSaleStatisticsByMonthAndStoreId(String month, String storeId) {
        if (StringUtils.isBlank(storeId) || StringUtils.isBlank(month)) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        Date specifiedMonth = null;
        try {
            specifiedMonth = sdf1.parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer formatMonthFirst = Integer.parseInt(sdf0.format(specifiedMonth)+"01");
        Integer formatMonthEnd = Integer.parseInt(sdf0.format(specifiedMonth)+"31");

        List<FrontEndSalesStatisticsResponse> frontEndSalesStatistics =
                dao.selectStoreMonthSaleStatisticsByMonthAndStoreId(formatMonthFirst,formatMonthEnd, storeId);

        // key : categoryId
        Map<String,List<FrontEndSalesStatisticsResponse>> map = Maps.newHashMap();
        List<FrontEndSalesStatisticsByCategoryDTO> categoryDTOS = Lists.newArrayList();

        if (frontEndSalesStatistics != null && frontEndSalesStatistics.size() > 0) {
            Set<String> categoryIdList = frontEndSalesStatistics.stream().map(f -> f.getCategoryName()).collect(Collectors.toSet());

            for (String categoryName : categoryIdList) {
                map.put(categoryName,new ArrayList<>());
            }
            for (FrontEndSalesStatisticsResponse entity : frontEndSalesStatistics) {
                List<FrontEndSalesStatisticsResponse> dtoList = map.get(entity.getCategoryName());
                dtoList.add(entity);
            }
            Set<Map.Entry<String, List<FrontEndSalesStatisticsResponse>>> entries = map.entrySet();
            // 循环遍历每一个分类id对应的list
            for (Map.Entry<String, List<FrontEndSalesStatisticsResponse>> entry : entries) {
                List<FrontEndSalesStatisticsResponse> responseList = entry.getValue();
                FrontEndSalesStatisticsByCategoryDTO categoryDTO = responseToResponseByCategoryDTO(responseList.get(0));
                Long sale_total_count = 0L;
                Long sale_total_amount = 0L;
                List<FrontEndSalesStatisticsBySkuDTO> skuDTOList = new ArrayList<>();
                for (FrontEndSalesStatisticsResponse response : responseList) {
                    FrontEndSalesStatisticsBySkuDTO skuDTO = new FrontEndSalesStatisticsBySkuDTO();
                    skuDTO.setStoreId(response.getStoreId());
                    skuDTO.setSkuCode(response.getSkuCode());
                    skuDTO.setSkuName(response.getSkuName());
                    skuDTO.setSaleTotalCount(response.getSaleTotalCount());
                    skuDTO.setSaleTotalAmount(response.getSaleTotalAmount());
                    skuDTO.setPriceUnit(response.getPriceUnit());
                    skuDTO.setSkuUnit(response.getSkuUnit());
                    sale_total_amount = response.getSaleTotalAmount() + sale_total_amount;
                    sale_total_count = response.getSaleTotalCount() + sale_total_count;
                    skuDTOList.add(skuDTO);
                }
                categoryDTO.setSkuDTOList(skuDTOList);
                categoryDTO.setSaleTotalAmount(sale_total_amount);
                categoryDTO.setSaleTotalCount(sale_total_count);
                categoryDTOS.add(categoryDTO);
            }
        }

        return HttpResponse.successGenerics(categoryDTOS);
    }


    private FrontEndSalesStatisticsByCategoryDTO responseToResponseByCategoryDTO(FrontEndSalesStatisticsResponse response) {
        FrontEndSalesStatisticsByCategoryDTO dto = new FrontEndSalesStatisticsByCategoryDTO();
        dto.setStoreId(response.getStoreId());
        dto.setSkuDTOList(new ArrayList<>());
        dto.setCategoryId(response.getCategoryId());
        dto.setCategoryName(response.getCategoryName());
        dto.setSaleTotalCount(0L);
        dto.setSaleTotalAmount(0L);
        return dto;
    }
}
