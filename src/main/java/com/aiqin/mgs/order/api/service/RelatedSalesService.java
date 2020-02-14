package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
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

    HttpResponse insert(RelatedSales entity);

    PageResData<RelatedSales> getList(PageRequestVO<RelatedSalesVo> searchVo);

}
