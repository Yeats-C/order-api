package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.RelatedSales;
import com.aiqin.mgs.order.api.domain.request.RelatedSalesVo;

/**
 * description: RelatedSalesService
 * date: 2020/2/14 11:29
 * author: hantao
 * version: 1.0
 */
public interface RelatedSalesService {

    Boolean insert(RelatedSales entity);

    Boolean update(RelatedSales entity);

    Boolean updateStatus(Long id,Integer status);

    PageResData<RelatedSales> getList(PageRequestVO<RelatedSalesVo> searchVo);

    RelatedSales selectBySalseCategoryId(String salseCategoryId);

    RelatedSales getByCategoryLevel(String categoryLevel);

}
