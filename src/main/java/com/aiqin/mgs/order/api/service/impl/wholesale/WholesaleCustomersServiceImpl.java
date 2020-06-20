package com.aiqin.mgs.order.api.service.impl.wholesale;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.wholesale.WholesaleCustomersDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.wholesale.JoinMerchant;
import com.aiqin.mgs.order.api.domain.wholesale.MerchantAccount;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleRule;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.wholesale.WholesaleCustomersService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private BridgeProductService bridgeProductService;


    @Override
    public HttpResponse<PageResData<WholesaleCustomers>> list(WholesaleCustomers wholesaleCustomers) {
        LOGGER.info("批发客户列表 参数wholesaleCustomers为{}"+wholesaleCustomers);
        HttpResponse httpResponse=HttpResponse.success();
        PageResData pageResData=new PageResData();
        List<WholesaleCustomers> wholesaleCustomersList=wholesaleCustomersDao.list(wholesaleCustomers);
        for(WholesaleCustomers customer:wholesaleCustomersList){
            List<WholesaleRule> wholesaleRuleList=wholesaleCustomersDao.getWholesaleRuleList(customer.getCustomerCode());
            List<String> deliveryCenterList=new ArrayList<>();
            if(null!=wholesaleRuleList&&wholesaleRuleList.size()>0){
                for(WholesaleRule rule:wholesaleRuleList){
                    switch (rule.getType()) {
                        case 1:
                            deliveryCenterList.add(rule.getWarehouseName());
                            break;
                        default:
                            //nothing
                            break;

                    }
                }
                customer.setDeliveryCenterList(deliveryCenterList);
            }
        }
        pageResData.setDataList(wholesaleCustomersList);
        pageResData.setTotalCount(wholesaleCustomersDao.totalCount(wholesaleCustomers));
        httpResponse.setData(pageResData);
        return httpResponse;
    }

    @Override
    @Transactional
    public HttpResponse insert(WholesaleCustomers wholesaleCustomers) {
        LOGGER.info("新增批发客户 参数wholesaleCustomers为{}"+wholesaleCustomers);
        if(null==wholesaleCustomers){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        boolean checkAccount=checkAccountExists(wholesaleCustomers.getCustomerAccount()).getData();
        if(!checkAccount){
            return HttpResponse.failure(ResultCode.ACCOUNT_ALREADY_EXISTS);
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

        //创建主控账户
        HttpResponse<JoinMerchant> response = addFranchisee(wholesaleCustomers,auth);
        wholesaleCustomers.setCustomerAccount(response.getData().getUserName());

        //将批发客户信息推送到结算
        accountRegister(wholesaleCustomers);
        wholesaleCustomersDao.insert(wholesaleCustomers);
        wholesaleCustomersDao.bulkInsertionRules(wholesaleRuleList);
        return httpResponse;
    }

    private void accountRegister(WholesaleCustomers wholesaleCustomers) {
        MerchantAccount merchantAccount=new MerchantAccount();
        merchantAccount.setFranchiseeCode(wholesaleCustomers.getCustomerAccount());
        merchantAccount.setFranchiseeId(wholesaleCustomers.getCustomerCode());
        merchantAccount.setFranchiseeName(wholesaleCustomers.getCustomerName());
        merchantAccount.setCompanyCode(wholesaleCustomers.getCompanyCode());
        merchantAccount.setCompanyName(wholesaleCustomers.getCompanyName());
        merchantAccount.setRelationType(1);

        bridgeProductService.accountRegister(merchantAccount);
    }

    private HttpResponse addFranchisee(WholesaleCustomers wholesaleCustomers,AuthToken auth) {
        JoinMerchant joinMerchant=new JoinMerchant();
        joinMerchant.setFranchiseeCode(wholesaleCustomers.getCustomerAccount());
        joinMerchant.setFranchiseeName(wholesaleCustomers.getCustomerName());
        joinMerchant.setMobile(wholesaleCustomers.getPhoneNumber());
        joinMerchant.setCardNo(wholesaleCustomers.getIdentityNumber());
        joinMerchant.setCardType("1");
        joinMerchant.setCompanyCode(wholesaleCustomers.getCompanyCode());
        joinMerchant.setCompanyName(wholesaleCustomers.getCompanyName());
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(wholesaleCustomers.getProvinceName());
        stringBuffer.append(wholesaleCustomers.getCityName());
        stringBuffer.append(wholesaleCustomers.getDistrictName());
        stringBuffer.append(wholesaleCustomers.getStreetAddress());
        joinMerchant.setAddress(stringBuffer.toString());
        joinMerchant.setProperty(2);
        joinMerchant.setCreateBy(auth.getPersonName());
        joinMerchant.setPersonId(auth.getPersonId());

        HttpResponse<JoinMerchant> httpResponse=bridgeProductService.addFranchisee(joinMerchant);
        JoinMerchant result=httpResponse.getData();

        joinMerchant.setDepartmentCode(result.getDepartmentCode());
        joinMerchant.setDepartmentName(result.getDepartmentName());
        joinMerchant.setDepartmentLevel(result.getDepartmentLevel());
        joinMerchant.setCompanyCode(result.getCompanyCode());
        joinMerchant.setCompanyName(result.getCompanyName());
        String[] roleId = {"JS0089"};
        joinMerchant.setRoleId(roleId);

        HttpResponse<JoinMerchant> response=bridgeProductService.addFranchiseeAccount(joinMerchant);
        return response;
    }

    @Override
    public HttpResponse<WholesaleCustomers> getCustomerByCode(String customerCode) {
        LOGGER.info("通过customerCode查询批发客户 参数 customerCode 为{}"+customerCode);
        if(null==customerCode){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        HttpResponse httpResponse=HttpResponse.success();
        WholesaleCustomers wholesaleCustomer=new WholesaleCustomers();
        wholesaleCustomer.setCustomerCode(customerCode);
        List<WholesaleCustomers> wholesaleCustomers=wholesaleCustomersDao.list(wholesaleCustomer);
        if(null==wholesaleCustomers||wholesaleCustomers.size()<=0){
            return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
        }else{
            wholesaleCustomer=wholesaleCustomers.get(0);
        }
        List<WholesaleRule> wholesaleRuleList=wholesaleCustomersDao.getWholesaleRuleList(customerCode);
        //仓库List
        List<WholesaleRule> warehouseList=new ArrayList<>();
        //品牌List
        List<WholesaleRule> brandList=new ArrayList<>();
        //品类List
        List<WholesaleRule> categoryList=new ArrayList<>();
        //单品List
        List<WholesaleRule> productList=new ArrayList<>();
        if(null!=wholesaleRuleList&&wholesaleRuleList.size()>0){
            for(WholesaleRule rule:wholesaleRuleList){
                switch (rule.getType()) {
                    case 1:
                        warehouseList.add(rule);
                        break;
                    case 2:
                        brandList.add(rule);
                        break;
                    case 3:
                        categoryList.add(rule);
                        break;
                    case 4:
                        productList.add(rule);
                        break;
                    default:
                        //nothing
                        break;
                }
            }
            wholesaleCustomer.setWarehouseList(warehouseList);
            wholesaleCustomer.setBrandList(brandList);
            wholesaleCustomer.setCategoryList(categoryList);
            wholesaleCustomer.setProductList(productList);
        }

        httpResponse.setData(wholesaleCustomer);

        return httpResponse;
    }

    @Override
    @Transactional
    public HttpResponse update(WholesaleCustomers wholesaleCustomers) {
        LOGGER.info("修改批发客户 参数wholesaleCustomers为{}"+wholesaleCustomers);
        if(null==wholesaleCustomers){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        HttpResponse httpResponse=HttpResponse.success();
        List<WholesaleRule> wholesaleRuleList=new ArrayList<>();
        if(null!=wholesaleCustomers.getWarehouseList()){
            for (WholesaleRule rule:wholesaleCustomers.getWarehouseList()){
                rule.setCustomerCode(wholesaleCustomers.getCustomerCode());
                rule.setType(1);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getWarehouseList());
        }
        if(null!=wholesaleCustomers.getBrandList()){
            for (WholesaleRule rule:wholesaleCustomers.getBrandList()){
                rule.setCustomerCode(wholesaleCustomers.getCustomerCode());
                rule.setType(2);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getBrandList());
        }
        if(null!=wholesaleCustomers.getCategoryList()){
            for (WholesaleRule rule:wholesaleCustomers.getCategoryList()){
                rule.setCustomerCode(wholesaleCustomers.getCustomerCode());
                rule.setType(3);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getCategoryList());
        }
        if(null!=wholesaleCustomers.getProductList()){
            for (WholesaleRule rule:wholesaleCustomers.getProductList()){
                rule.setCustomerCode(wholesaleCustomers.getCustomerCode());
                rule.setType(4);
            }
            wholesaleRuleList.addAll(wholesaleCustomers.getProductList());
        }
        AuthToken auth = AuthUtil.getCurrentAuth();
        wholesaleCustomers.setUpdateBy(auth.getPersonName());
        wholesaleCustomersDao.update(wholesaleCustomers);
        wholesaleCustomersDao.clearRules(wholesaleCustomers.getCustomerCode());
        wholesaleCustomersDao.bulkInsertionRules(wholesaleRuleList);
        return httpResponse;
    }

    @Override
    public HttpResponse<List<WholesaleCustomers>> getCustomerByNameOrAccount(String parameter) {
        LOGGER.info("通过名称或者账户查询批发客户 参数 parameter 为{}"+parameter);
        if(null==parameter){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        HttpResponse httpResponse=HttpResponse.success();
        WholesaleCustomers wholesaleCustomer=new WholesaleCustomers();
        wholesaleCustomer.setCustomerName(parameter);
        wholesaleCustomer.setCustomerAccount(parameter);
        List<WholesaleCustomers> wholesaleCustomers=wholesaleCustomersDao.list(wholesaleCustomer);
        for(WholesaleCustomers customer:wholesaleCustomers){
            List<WholesaleRule> wholesaleRuleList=wholesaleCustomersDao.getWholesaleRuleList(customer.getCustomerCode());
            //仓库List
            List<WholesaleRule> warehouseList=new ArrayList<>();
            //品牌List
            List<WholesaleRule> brandList=new ArrayList<>();
            //品类List
            List<WholesaleRule> categoryList=new ArrayList<>();
            //单品List
            List<WholesaleRule> productList=new ArrayList<>();
            if(null!=wholesaleRuleList&&wholesaleRuleList.size()>0){
                for(WholesaleRule rule:wholesaleRuleList){
                    switch (rule.getType()) {
                        case 1:
                            warehouseList.add(rule);
                            break;
                        case 2:
                            brandList.add(rule);
                            break;
                        case 3:
                            categoryList.add(rule);
                            break;
                        case 4:
                            productList.add(rule);
                            break;
                    }
                }
                customer.setWarehouseList(warehouseList);
                customer.setBrandList(brandList);
                customer.setCategoryList(categoryList);
                customer.setProductList(productList);
            }
        }


        httpResponse.setData(wholesaleCustomers);

        return httpResponse;
    }

    @Override
    public HttpResponse<Boolean> checkAccountExists(String customerAccount) {
        LOGGER.info("校验账户是否已存在 参数 customerAccount 为{}"+customerAccount);
        if(null==customerAccount){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        HttpResponse httpResponse=HttpResponse.success();
        int countNum=wholesaleCustomersDao.selectCustomerByParameter(customerAccount);
        if(countNum>0){
            httpResponse.setData(false);
        }else{
            httpResponse.setData(true);
        }
        return httpResponse;
    }
}