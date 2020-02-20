/*****************************************************************

* 模块名称：订单统计-接口层
* 开发人员: 黄祉壹
* 开发时间: 2020-02-19 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;

import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.request.ProductStoreRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.ProdisorRequest;
import com.aiqin.mgs.order.api.domain.response.SkuSaleResponse;

@SuppressWarnings("all")
public interface OrderCalculateService {

	List<OrderMonthCalculateInfo> copartnerMonth();

}
