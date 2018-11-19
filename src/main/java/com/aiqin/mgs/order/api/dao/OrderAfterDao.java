/*****************************************************************

* 模块名称：订单后台-DAO接口层
* 开发人员: 黄祉壹
* 开发时间: 2018-11-08 

* ****************************************************************************/
package com.aiqin.mgs.order.api.dao;

import javax.validation.Valid;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.*;

public interface OrderAfterDao {

	HttpResponse selectOrderAfter(@Valid OrderAfterSaleInfo orderAfterInfo);

	HttpResponse selectOrderAfterDetail(String afterSaleId);


	
}
