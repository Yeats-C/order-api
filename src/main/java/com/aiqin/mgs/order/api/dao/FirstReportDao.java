package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.FirstReportStoreAndOrderRerequest;
import com.aiqin.mgs.order.api.domain.response.FirstReportResponse;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FirstReportDao {

    //查询区域名称和id
    List<FirstReportResponse> QueryArea();
    //通过区域id获取门店id集合
    List<String> QueryStore(@Param("araeId") List<String> araeId);
    //通过门店id集合去查询订单集合
    BigDecimal selectOrder(FirstReportStoreAndOrderRerequest firstReportStoreAndOrderRerequest);

    List<String> QueryStoreByAreaId(@Param("araeId") String araeId);
    List<ErpOrderInfo> selectOrders(FirstReportStoreAndOrderRerequest firstReportStoreAndOrderRerequest);
}
