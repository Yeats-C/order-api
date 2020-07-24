package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.StoreMonthDao;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.StoreMonthResponse;
import com.aiqin.mgs.order.api.service.StoreMonthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class StoreMonthServiceImpl implements StoreMonthService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(StoreMonthServiceImpl.class);
    private static Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

    @Autowired
    private StoreMonthDao storeMonthDao;


    /**
     * 门店名称和门店编码
     * @param storeName
     * @return
     */
    @Override
    public HttpResponse selectStoreMonth(String storeName) {
        LOGGER.info("查询门店上月销量的入参：{}" + storeName);
        StoreInfo storeInfo = new StoreInfo();
        if (StringUtils.isNotBlank(storeName)) {
            Matcher isNum = NUMBER_PATTERN.matcher(storeName);
            if (isNum.matches()) {//如果为纯数字，则为门店编码
                storeInfo.setStoreCode(storeName);
            } else {//门店名称
                storeInfo.setStoreName(storeName);
            }
        }
        if (storeInfo != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            Calendar instance = Calendar.getInstance();
            Date date = new Date();
            instance.setTime(date);
            instance.add(Calendar.MONTH,-1);
            date = instance.getTime();
            storeInfo.setContacts(simpleDateFormat.format(date));
            return HttpResponse.success(storeMonthDao.selectStoreByName(storeInfo));
        }
        return null;
    }

    @Override
    public HttpResponse selectStoreMonths(List<String> storeAll) {
        LOGGER.info("查询门店上月销量的入参：{}" + storeAll);
        List<StoreMonthResponse> storeMonthResponses = new ArrayList<>();
        List<StoreInfo> storeInfo = new ArrayList<>();
        if (null != storeAll || !storeAll.isEmpty()) {
            for (String storeName : storeAll) {
                StoreInfo storeInfo1 = new StoreInfo();
                Matcher isNum = NUMBER_PATTERN.matcher(storeName);
                if (isNum.matches()) {//如果为纯数字，则为门店编码
                    storeInfo1.setStoreCode(storeName);
                } else {//门店名称
                    storeInfo1.setStoreName(storeName);
                }
                storeInfo.add(storeInfo1);
            }
        }
        if (null != storeInfo || !storeInfo.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            Calendar instance = Calendar.getInstance();
            Date date = new Date();
            instance.setTime(date);
            instance.add(Calendar.MONTH, -1);
            date = instance.getTime();
            String format = simpleDateFormat.format(date);
            for (StoreInfo s : storeInfo) {
                StoreMonthResponse storeMonthResponse = new StoreMonthResponse();
                s.setStatYearMonth(format);
                storeMonthResponse = storeMonthDao.selectStoreByNames(s);
                storeMonthResponses.add(storeMonthResponse);
            }
        }
        return HttpResponse.success(storeMonthResponses);
    }
}
