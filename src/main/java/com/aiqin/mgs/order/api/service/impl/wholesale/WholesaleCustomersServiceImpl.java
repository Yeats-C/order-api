package com.aiqin.mgs.order.api.service.impl.wholesale;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.wholesale.WholesaleCustomersDao;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;
import com.aiqin.mgs.order.api.service.wholesale.WholesaleCustomersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author csf
 */
@Service
public class WholesaleCustomersServiceImpl implements WholesaleCustomersService {


    private static final Logger LOGGER = LoggerFactory.getLogger(WholesaleCustomersServiceImpl.class);

    @Resource
    private WholesaleCustomersDao wholesaleCustomersDao;


    @Override
    public HttpResponse<PageResData<WholesaleCustomers>> list(WholesaleCustomers wholesaleCustomers) {
        LOGGER.info("批发客户列表 参数wholesaleCustomers为{}"+wholesaleCustomers);
        HttpResponse httpResponse=HttpResponse.success();
        PageResData pageResData=new PageResData();
        pageResData.setDataList(wholesaleCustomersDao.list(wholesaleCustomers));
        pageResData.setTotalCount(wholesaleCustomersDao.totalCount(wholesaleCustomers));
        httpResponse.setData(pageResData);
        return httpResponse;
    }
}