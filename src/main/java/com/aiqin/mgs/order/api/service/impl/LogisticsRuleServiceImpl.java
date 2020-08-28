package com.aiqin.mgs.order.api.service.impl;


import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.component.LogisticsRuleEnum;
import com.aiqin.mgs.order.api.component.LogisticsRuleTypesEnum;
import com.aiqin.mgs.order.api.dao.LogisticsRuleDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.logisticsRule.*;
import com.aiqin.mgs.order.api.domain.response.LogisticsAllResponse;
import com.aiqin.mgs.order.api.service.LogisticsRuleService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.ResultModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogisticsRuleServiceImpl implements LogisticsRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogisticsRuleServiceImpl.class);

    @Autowired
    private LogisticsRuleDao logisticsRuleDao;


    /**
     * 生成规则编码
     */
    public static String createLogisticsCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }


    /**
     * 修改生效状态
     *
     * @param logisticsRuleInfo
     * @return
     */
    @Override
    public HttpResponse updateLogisticsStatus(LogisticsRuleInfo logisticsRuleInfo) {
        LOGGER.info("修改-物流减免生效状态-入参：{}", logisticsRuleInfo);
        AuthToken currentAuth = AuthUtil.getCurrentAuth();
        logisticsRuleInfo.setUpdateByName(currentAuth.getPersonName());
        try {
            logisticsRuleDao.updateStatus(logisticsRuleInfo);
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.info("修改-物流减免生效状态-请求：{},{}", logisticsRuleInfo, e);
            return HttpResponse.failure(MessageId.create(Project.ZERO, 03, "修改出现未知异常,请联系系统管理员."));
        }
    }


    /**
     * 删除物流减免规则
     *
     * @param rultCode
     * @return
     */
    @Override
    public HttpResponse deleteLogistics(String rultCode) {
        LOGGER.info("删除物流减免规则：{}", rultCode);
        try {
            logisticsRuleDao.deleteLogisticsRule(rultCode);
            logisticsRuleDao.deleteLogisticsProduct(rultCode);
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.info("删除物流减免规则请求：{},{}", rultCode, e);
            return HttpResponse.failure(MessageId.create(Project.ZERO, 01, "删除出现未知异常,请联系系统管理员."));
        }
    }

    /**
     * 回显物流减免规则
     *
     * @param rultCode
     * @return
     */
    @Override
    public HttpResponse<echoLogisticsRule> selectLogistics(String rultCode) {
        LOGGER.info("回显-物流减免规则-入参：{}", rultCode);
        echoLogisticsRule echoLogisticsRule = new echoLogisticsRule();
        List<LogisticsRuleInfo> list = new ArrayList<>();
        Integer rultType1 = null;
        try {
            list = logisticsRuleDao.getLogisticsRule(rultCode);
            for (LogisticsRuleInfo logisticsRuleInfo : list) {
                rultType1 = logisticsRuleInfo.getRultType();
            }
            echoLogisticsRule.setRultType(rultType1);
            echoLogisticsRule.setLogisticsList(list);
            return HttpResponse.success(echoLogisticsRule);
        } catch (Exception e) {
            LOGGER.error("回显-物流减免规则-请求：{},{}", rultCode, e);
            return HttpResponse.failure(MessageId.create(Project.ZERO, 01, "查询出现未知异常,请联系系统管理员."));
        }
    }

    /**
     * 编辑物流减免规则
     *
     * @param logisticsRuleInfoList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse updateLogistics(LogisticsRuleInfoList logisticsRuleInfoList) {
        LOGGER.info("编辑物流减免规则-入参：{}", logisticsRuleInfoList);
        try {
//           if (CollectionUtils.isNotEmpty(logisticsRuleInfoList.getLogisticsList())) {
            //获取商品物流减免集合
            List<LogisticsRuleInfo> logisticsList = logisticsRuleInfoList.getLogisticsList();
            //获取规则唯一编码
            String rultCode = logisticsRuleInfoList.getLogisticsRuleType().getRultCode();
            if (LogisticsRuleEnum.SINGLE_BUY_QUANTITY.getkey().equals(logisticsRuleInfoList.getLogisticsRuleType().getRultType())
                    || LogisticsRuleEnum.SINGLE_BUY_AMOUNT.getkey().equals(logisticsRuleInfoList.getLogisticsRuleType().getRultType())){
                //修改单品的门槛值
                for (LogisticsRuleInfo lo : logisticsList) {
                    LogisticsRuleInfo logisticsRuleInfo = new LogisticsRuleInfo();
                    BeanUtils.copyProperties(lo, logisticsRuleInfo);
                    logisticsRuleInfo.setRultCode(rultCode);
                    logisticsRuleInfo.setRultType(logisticsRuleInfoList.getLogisticsRuleType().getRultType());
                    AuthToken currentAuth = AuthUtil.getCurrentAuth();
                    logisticsRuleInfo.setUpdateByName(currentAuth.getPersonName());
                    LOGGER.info("编辑-单品免运费-实体：{}", logisticsRuleInfo);
                    logisticsRuleDao.updateSingle(logisticsRuleInfo);
                }
            }else {
                //组合商品
                //原有组合中的商品信息
                List<LogisticsRuleInfo> logisticsRule = logisticsRuleDao.getLogisticsRule(rultCode);
                //新增商品
                if (logisticsList.size() > logisticsRule.size()) {   //如果传的集合大于查的集合，为新增
                    LOGGER.info("组合规则-传参-新增商品信息-实体:{}",logisticsList);
                    //将查出来的商品集合转成只有编码的集合
                    List<String> productCodeList = logisticsRule.stream().map(LogisticsRuleInfo::getProductCode).collect(Collectors.toList());
                    //将传过来的集合对编码集合进行过滤
                    List<LogisticsRuleInfo> newlyLogisticsInfo = logisticsList.stream().filter(LogisticsRuleInfo -> !productCodeList.contains(LogisticsRuleInfo.getProductCode())).collect(Collectors.toList());
                    LOGGER.info("组合规则-新增商品信息-实体：{}",newlyLogisticsInfo);
                    List<LogisticsRuleInfo> newlyLogisticsRuleInfo = new ArrayList<>();
                    for (LogisticsRuleInfo logisticsRuleInfo: newlyLogisticsInfo) {
                        logisticsRuleInfo.setRultCode(rultCode);
                        logisticsRuleInfo.setRultType(logisticsRuleInfoList.getLogisticsRuleType().getRultType());
                        logisticsRuleInfo.setBrand("-");
                        logisticsRuleInfo.setCategory("-");
                        logisticsRuleInfo.setSalesStandard("-");
                        Integer rultType = logisticsRuleInfoList.getLogisticsRuleType().getRultType();
                        if (LogisticsRuleEnum.CONSTITUTE_BUY_QUANTITY.getkey().equals(rultType)){
                            logisticsRuleInfo.setTypes(LogisticsRuleTypesEnum.LOGISTICS_PIECE.getCode());
                        }else {
                            logisticsRuleInfo.setTypes(LogisticsRuleTypesEnum.LOGISTICS_YUAN.getCode());
                        }
                        newlyLogisticsRuleInfo.add(logisticsRuleInfo);
                    }
                    logisticsRuleDao.addProduct(newlyLogisticsRuleInfo);
                    //会有新增的同时修改免运费门槛场景
//                     List<LogisticsRuleInfo> Logisticslist = new ArrayList<>();
                    for (LogisticsRuleInfo info :logisticsList ) {
                        LogisticsRuleInfo logisticsRuleInfos = new LogisticsRuleInfo();
                        BeanUtils.copyProperties(info,logisticsRuleInfos);
                        logisticsRuleInfos.setRultCode(rultCode);
                        logisticsRuleInfos.setRultType(logisticsRuleInfoList.getLogisticsRuleType().getRultType());
                        AuthToken currentAuth = AuthUtil.getCurrentAuth();
                        logisticsRuleInfos.setUpdateByName(currentAuth.getPersonName());
                        logisticsRuleDao.updatefareSill(logisticsRuleInfos);
                    }

                    //删除商品
                }else if (logisticsList.size() < logisticsRule.size()) {  //如果传的集合小于查的集合 为删除
                    LOGGER.info("组合规则-传参-删除商品信息-实体：{}", logisticsList);
                    List<String> newlyRultCodeList = logisticsList.stream().map(LogisticsRuleInfo::getProductCode).collect(Collectors.toList());
                    List<LogisticsRuleInfo> deleteLogisticsRuleInfo = logisticsRule.stream().filter(LogisticsRuleInfo -> !newlyRultCodeList.contains(LogisticsRuleInfo.getProductCode())).collect(Collectors.toList());
//                     List<String> deleteProductCodeList = deleteLogisticsRuleInfo.stream().map(LogisticsRuleInfo::getProductCode).collect(Collectors.toList());
                    List<LogisticsRuleInfo> delecteProductList = new ArrayList<>();
                    for (LogisticsRuleInfo logisticsRuleInfo : deleteLogisticsRuleInfo) {
                        logisticsRuleInfo.setRultCode(rultCode);
                        logisticsRuleInfo.setRultType(logisticsRuleInfoList.getLogisticsRuleType().getRultType());
                        delecteProductList.add(logisticsRuleInfo);
                    }
                    LOGGER.info("组合规则-删除商品信息-实体：{}", delecteProductList);
                    logisticsRuleDao.deleteProduct(delecteProductList);
                    //会有删除的同时修改免运费门槛场景
                    if (logisticsList.size() != 0){
                        for (LogisticsRuleInfo info :logisticsList ) {
                            LogisticsRuleInfo logisticsRuleInfos = new LogisticsRuleInfo();
                            BeanUtils.copyProperties(info,logisticsRuleInfos);
                            logisticsRuleInfos.setRultCode(rultCode);
                            logisticsRuleInfos.setRultType(logisticsRuleInfoList.getLogisticsRuleType().getRultType());
                            AuthToken currentAuth = AuthUtil.getCurrentAuth();
                            logisticsRuleInfos.setUpdateByName(currentAuth.getPersonName());
                            logisticsRuleDao.updatefareSill(logisticsRuleInfos);
                        }

                    }
                }else {
                    LOGGER.info("组合规则-修改商品属性-未处理属性-实体：{}",logisticsList);
//                     List<LogisticsRuleInfo> logisticsInfoList = new ArrayList<>();
                    for (LogisticsRuleInfo ruleInfo : logisticsList) {
                        LogisticsRuleInfo logisticsRuleInfo = new LogisticsRuleInfo();
                        BeanUtils.copyProperties(ruleInfo,logisticsRuleInfo);
                        logisticsRuleInfo.setRultCode(rultCode);
                        logisticsRuleInfo.setRultType(logisticsRuleInfoList.getLogisticsRuleType().getRultType());
//                         logisticsInfoList.add(ruleInfo);
                        AuthToken currentAuth = AuthUtil.getCurrentAuth();
                        logisticsRuleInfo.setUpdateByName(currentAuth.getPersonName());
                        LOGGER.info("组合规则-修改商品属性-实体：{}",logisticsRuleInfo);
                        logisticsRuleDao.updateSingle(logisticsRuleInfo);
                    }
                }
            }
            return HttpResponse.success(true);
//           }
//            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }catch (Exception e){
            LOGGER.error("编辑-物流减免规则-请求：{},{}",logisticsRuleInfoList,e);
            throw new GroundRuntimeException("修改出现未知异常,请联系系统管理员");
        }
    }

    /**
     * 多条件查询列表
     * @param logisticsRuleRequest
     * @return
     */
    @Override
    public HttpResponse<ResultModel<LogisticsAllResponse>> selectLogisticsList(LogisticsRuleRequest logisticsRuleRequest) {
        LOGGER.info("多条件查询列表-方法入参：{}",logisticsRuleRequest);
        Integer PageNo = logisticsRuleRequest.getPageNo();
        Integer PageSize = logisticsRuleRequest.getPageSize();
        if (PageNo == null && PageSize == null) {
            PageNo = 1;
            PageSize = 10;
        }
        if (PageNo == 0 && PageSize == 0) {
            PageNo = 1;
            PageSize = 10;
        }
        PageHelper.startPage(PageNo, PageSize);
        List<LogisticsAllResponse> list = new ArrayList<>();
        try{
            list = logisticsRuleDao.selectAll(logisticsRuleRequest);
            LOGGER.info("查询返回结果：{}",list);
            ResultModel resultModel = new ResultModel();
            resultModel.setTotal(((Page) list).getTotal());
            resultModel.setResult(list);
            return HttpResponse.success(resultModel);
        }catch (Exception e){
            LOGGER.error("查询-多条件列表-请求：{},{}",logisticsRuleRequest,e);
            return HttpResponse.failure(MessageId.create(Project.ZERO, 01, "查询出现未知异常,请联系系统管理员."));
        }
    }



    /**
     * 新增物流免减规则
     *
     * @param logisticsRuleInfoList
     * @return
     */
    @Override
    public HttpResponse saveLogistics(LogisticsRuleInfoList logisticsRuleInfoList) {
        LOGGER.info("新增物流免减规则-方法入参：{}", logisticsRuleInfoList);
        String logisticsCode = createLogisticsCode();
        try {
            if (CollectionUtils.isNotEmpty(logisticsRuleInfoList.getLogisticsList())) {
                //规则信息
                LogisticsRuleType logisticsRuleType = logisticsRuleInfoList.getLogisticsRuleType();
                logisticsRuleType.setRultCode(logisticsCode);
                AuthToken currentAuth = AuthUtil.getCurrentAuth();
                logisticsRuleType.setCreateById(currentAuth.getTicketPersonId());
                logisticsRuleType.setCreateByName(currentAuth.getPersonName());
                logisticsRuleDao.saveRule(logisticsRuleType);
                //物流减免商品集合
                List<LogisticsRuleInfo> logisticsList = logisticsRuleInfoList.getLogisticsList();
                //判断是数量还是金额
                if (LogisticsRuleEnum.SINGLE_BUY_QUANTITY.getkey().equals(logisticsRuleInfoList.getLogisticsRuleType().getRultType()) ||
                        LogisticsRuleEnum.CONSTITUTE_BUY_QUANTITY.getkey().equals(logisticsRuleInfoList.getLogisticsRuleType().getRultType())) {
                    if (LogisticsRuleEnum.CONSTITUTE_BUY_QUANTITY.getkey().equals(logisticsRuleInfoList.getLogisticsRuleType().getRultType())) {
                        //组合数量商品信息
                        for (LogisticsRuleInfo fo : logisticsList) {
                            fo.setTypes(0);
                            LogisticsRuleInfo logisticsRuleInfo = new LogisticsRuleInfo();
                            BeanUtils.copyProperties(fo, logisticsRuleInfo);
                            logisticsRuleInfo.setRultCode(logisticsCode);
                            logisticsRuleInfo.setRultType(logisticsRuleType.getRultType());
                            logisticsRuleInfo.setBrand("-");
                            logisticsRuleInfo.setCategory("-");
                            logisticsRuleInfo.setSalesStandard("-");
                            LOGGER.info("新增-物流减免规则-数量-实体：{}", logisticsRuleInfo);
                            logisticsRuleDao.saveProduct(logisticsRuleInfo);
                        }
                    }else {
                        //物流减免商品信息-单品
                        for (LogisticsRuleInfo fo : logisticsList) {
                            fo.setTypes(0);
                            LogisticsRuleInfo logisticsRuleInfo = new LogisticsRuleInfo();
                            BeanUtils.copyProperties(fo, logisticsRuleInfo);
                            logisticsRuleInfo.setRultCode(logisticsCode);
                            logisticsRuleInfo.setRultType(logisticsRuleType.getRultType());
                            LOGGER.info("新增-物流减免规则-数量-实体：{}", logisticsRuleInfo);
                            logisticsRuleDao.saveProduct(logisticsRuleInfo);
                        }
                    }
                }else {
                    if (LogisticsRuleEnum.CONSTITUTE_BUY_AMOUNT.getkey().equals(logisticsRuleInfoList.getLogisticsRuleType().getRultType())){
                        //组合金额商品信息
                        for (LogisticsRuleInfo fo : logisticsList) {
                            fo.setTypes(1);
                            LogisticsRuleInfo logisticsRuleInfo = new LogisticsRuleInfo();
                            BeanUtils.copyProperties(fo, logisticsRuleInfo);
                            logisticsRuleInfo.setRultCode(logisticsCode);
                            logisticsRuleInfo.setRultType(logisticsRuleType.getRultType());
                            logisticsRuleInfo.setBrand("-");
                            logisticsRuleInfo.setCategory("-");
                            logisticsRuleInfo.setSalesStandard("-");
                            LOGGER.info("新增-物流减免规则-金额-实体：{}", logisticsRuleInfo);
                            logisticsRuleDao.saveProduct(logisticsRuleInfo);
                        }
                    }else {
                        for (LogisticsRuleInfo fo : logisticsList) {
                            fo.setTypes(1);
                            LogisticsRuleInfo logisticsRuleInfo = new LogisticsRuleInfo();
                            BeanUtils.copyProperties(fo, logisticsRuleInfo);
                            logisticsRuleInfo.setRultCode(logisticsCode);
                            logisticsRuleInfo.setRultType(logisticsRuleType.getRultType());
                            LOGGER.info("新增-物流减免规则-金额-实体：{}", logisticsRuleInfo);
                            logisticsRuleDao.saveProduct(logisticsRuleInfo);
                        }
                    }
                }
                return HttpResponse.success(true);
            }
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        } catch (Exception e) {
            LOGGER.error("新增-物流减免规则-请求：{},{}",logisticsRuleInfoList,e);
            return HttpResponse.failure(MessageId.create(Project.ZERO, 01, "保存出现未知异常,请联系系统管理员."));
        }
    }

    @Override
    public HttpResponse<List<LogisticsAllResponse>> selectRuleBuSpuCode(List<String> spuCodes) {
        LOGGER.info("通过spuList查询规则-方法入参：{}",spuCodes);
        HttpResponse response=HttpResponse.success();
        try{
            LogisticsRuleRequest ruleRequest=new LogisticsRuleRequest();
            ruleRequest.setSpuCodes(spuCodes);
            List<LogisticsAllResponse> list = logisticsRuleDao.selectAll(ruleRequest);
            response.setData(list);
        }catch (Exception e){
            LOGGER.error("通过spuList查询规则-请求异常：{},{}",spuCodes,e);
            return HttpResponse.failure(MessageId.create(Project.ZERO, 01, "查询出现未知异常,请联系系统管理员."));
        }

        return response;
    }

    /**
     * 新规则-物流减免规则
     * @param newLogisticsRequest
     * @return
     */
    @Override
    @Transactional
    public HttpResponse addNewLogisticsRule(NewLogisticsRequest newLogisticsRequest) {
        LOGGER.info("新规则-物流减免规则入参： " + JsonUtil.toJson(newLogisticsRequest));
        List<NewReduceInfo> newReduceInfoList = newLogisticsRequest.getNewReduceInfoList();
        LOGGER.info("物流减免商品类型集合： " + newReduceInfoList);
        Assert.notEmpty(newReduceInfoList,"缺少物流减免规则商品");
        NewLogisticsInfo newLogisticsInfo = newLogisticsRequest.getNewLogisticsInfo();
        //生成编码
        String logisticsCode = createLogisticsCode();
        //获取创建人
        AuthToken currentAuth = AuthUtil.getCurrentAuth();
        String personName = currentAuth.getPersonName();
        String personId = currentAuth.getPersonId();
        //因与原物流规则有区分，所以以rultType为区分新旧规则
        newLogisticsInfo.setRultType(10);
        newLogisticsInfo.setCreateById(personId);
        newLogisticsInfo.setCreateByName(personName);
        newLogisticsInfo.setRultCode(logisticsCode);
        LOGGER.info("物流减免主表-实体： " + newLogisticsInfo);
        int count = logisticsRuleDao.addLogistics(newLogisticsInfo);
        if (count < 0){
            return HttpResponse.failure(ResultCode.ADD_LOGISTICS_INFO_EXCEPTION);
        }
        newReduceInfoList.forEach(item->{
            item.setEffectiveStatus(1);
            item.setRultCode(logisticsCode);
            item.setCreateByName(personName);
            item.setSpuCode("1");
            item.setSpuName("1");
            item.setIsDelete(2);
            item.setRultId(createLogisticsCode());
        });
        LOGGER.info("物流减免商品类型集合： " + JsonUtil.toJson(newReduceInfoList));
        int counts = logisticsRuleDao.addLogisticsList(newReduceInfoList);
        if (counts == 0){
            LOGGER.info("物流减免商品类型集合-新增失败");
            return HttpResponse.failure(ResultCode.ADD_LOGISTICS_INFO_LIST_EXCEPTION);
        }
        LOGGER.info("新增活动成功");
        return HttpResponse.success();
    }

    /**
     * 物流减免规则列表
     * @return
     */
    @Override
    public HttpResponse selectAll(Integer pageNo, Integer pageSize) {
        ResultModel resultModel = new ResultModel();
        if(pageNo==null&&pageSize==null){
            pageNo=1;
            pageSize=10;
        }
        if(pageNo==0&&pageSize==0){
            pageNo=1;
            pageSize=10;
        }
        PageHelper.startPage(pageNo,pageSize);
        List<NewAllLogistics> newAllLogistics = logisticsRuleDao.selectAllLogistics();
        LOGGER.info("查询物流减免-返回结果： " + newAllLogistics);
        if (newAllLogistics.isEmpty()){
            return HttpResponse.success();
        }
        resultModel.setResult(newAllLogistics);
        resultModel.setTotal(((Page)newAllLogistics).getTotal());
        return HttpResponse.success(resultModel);
    }

    /**
     * 修改物流减免生效状态
     * @param rultCode
     * @param
     * @return
     */
    @Override
    public HttpResponse updateStatusByCode(String rultCode,String rultId,Integer effectiveStatus) {
        LOGGER.info("新规则-物流减免生效状态修改-入参: "+ rultCode + ", " + rultId + "," + effectiveStatus);
        NewAllLogistics newAllLogistics = new NewAllLogistics();
        newAllLogistics.setRultCode(rultCode);
        newAllLogistics.setRultId(rultId);
        newAllLogistics.setEffectiveStatus(effectiveStatus);
        LOGGER.info("修改生效状态： " + newAllLogistics);
        int count = logisticsRuleDao.updateLogisticsStatus(newAllLogistics);
        if (count < 0){
            return HttpResponse.failure(ResultCode.UPDATE_EXCEPTION);
        }
        return HttpResponse.success();
    }

    /**
     * 删除物流减免
     * @param rultCode
     * @param rultId
     * @return
     */
    @Override
    public HttpResponse deleteLogisticsByCodeAndId(String rultCode, String rultId) {
        LOGGER.info("删除物流减免-入参： " + rultCode + "," + rultId);
        int count = logisticsRuleDao.updateByCodeAndId(rultCode, rultId);
        if (count < 0){
            return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
        return HttpResponse.success();
    }
}


