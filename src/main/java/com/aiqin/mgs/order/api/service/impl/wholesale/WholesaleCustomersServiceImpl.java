package com.aiqin.mgs.order.api.service.impl.wholesale;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.wholesale.WholesaleCustomersDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleRule;
import com.aiqin.mgs.order.api.service.wholesale.WholesaleCustomersService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public HttpResponse insert(WholesaleCustomers wholesaleCustomers) {
        LOGGER.info("新增批发客户 参数wholesaleCustomers为{}"+wholesaleCustomers);
        if(null==wholesaleCustomers){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        HttpResponse httpResponse=HttpResponse.success();
        String customerCode= OrderPublic.getUUID();
        wholesaleCustomers.setCustomerCode(customerCode);
        List<WholesaleRule> wholesaleRuleList=new ArrayList<>();
        if(null!=wholesaleCustomers.getWarehouseList()){
            for (WholesaleRule rule:wholesaleCustomers.getWarehouseList()){
                rule.setCustomerCode(customerCode);
                rule.setType(1);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getWarehouseList());
        }
        if(null!=wholesaleCustomers.getBrandList()){
            for (WholesaleRule rule:wholesaleCustomers.getBrandList()){
                rule.setCustomerCode(customerCode);
                rule.setType(2);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getBrandList());
        }
        if(null!=wholesaleCustomers.getCategoryList()){
            for (WholesaleRule rule:wholesaleCustomers.getCategoryList()){
                rule.setCustomerCode(customerCode);
                rule.setType(3);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getCategoryList());
        }
        if(null!=wholesaleCustomers.getProductList()){
            for (WholesaleRule rule:wholesaleCustomers.getProductList()){
                rule.setCustomerCode(customerCode);
                rule.setType(4);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getProductList());
        }
        AuthToken auth = AuthUtil.getCurrentAuth();
        wholesaleCustomers.setCreateBy(auth.getPersonName());
        wholesaleCustomers.setUpdateBy(auth.getPersonName());
        wholesaleCustomersDao.insert(wholesaleCustomers);
        return httpResponse;
    }
}