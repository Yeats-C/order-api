package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.FirstReportDao;
import com.aiqin.mgs.order.api.domain.request.FirstReportStoreAndOrderRerequest;
import com.aiqin.mgs.order.api.domain.response.FirstReportResponse;
import com.aiqin.mgs.order.api.service.FirstReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FirstReportServiceImpl implements FirstReportService {

    @Autowired
    private FirstReportDao firstReportDao;

    @Override
    @Transactional
    public void reportTimedTask() {
        //获取经营区域id和名称
        List<FirstReportResponse> firstReportResponseList = firstReportDao.QueryArea();
        log.info("获取区域id以及名称,返回结果：" + firstReportResponseList);
           if(CollectionUtils.isEmpty(firstReportResponseList)){
            log.info("获取经营区域为空");
            return;
           }
        //遍历获取的区域集合
        List<String> AraeId = new ArrayList<>();
          for (FirstReportResponse Firs : firstReportResponseList) {
                  String copartnerAreaId = Firs.getCopartner_area_id();
                  AraeId.add(copartnerAreaId);
          }
        //根据经营区域id去查询门店
        log.info("集合：" + AraeId);
        List<String> StoreIdList = firstReportDao.QueryStore(AraeId);
        log.info("获取门店id集合，返回结果：" + StoreIdList);
          if(CollectionUtils.isEmpty(StoreIdList)){
              log.info("获取门店id集合为空");
              return;
          }
        FirstReportStoreAndOrderRerequest firstReportStoreAndOrderRerequest = new FirstReportStoreAndOrderRerequest();
        firstReportStoreAndOrderRerequest.setRecords(StoreIdList);

        firstReportStoreAndOrderRerequest.setOrder_category_code("2");
        firstReportStoreAndOrderRerequest.setOrder_type_code("1");
        //销售金额
        BigDecimal bigDecimal = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
        log.info("首单直送金额：" + bigDecimal);


        firstReportStoreAndOrderRerequest.setOrder_category_code("2");
        firstReportStoreAndOrderRerequest.setOrder_type_code("2");
        //销售金额
        BigDecimal bigDecimal1 = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
        log.info("首单配送金额：" + bigDecimal1);


        firstReportStoreAndOrderRerequest.setOrder_category_code("16");
        firstReportStoreAndOrderRerequest.setOrder_type_code("3");
        //销售金额
        BigDecimal bigDecimal2 = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
        log.info("首单货架金额：" + bigDecimal2 );
        log.info("首单直送金额：" + bigDecimal);
        log.info("首单配送金额：" + bigDecimal1);
    }
}
