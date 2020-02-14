package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.RelatedSales;

/**
 * description: RelatedSalesService
 * date: 2020/2/14 11:29
 * author: hantao
 * version: 1.0
 */
public interface RelatedSalesService {

    HttpResponse insert(RelatedSales entity);

}
