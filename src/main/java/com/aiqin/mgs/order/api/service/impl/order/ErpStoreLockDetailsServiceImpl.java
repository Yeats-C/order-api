package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.StoreLockDetailsDao;
import com.aiqin.mgs.order.api.domain.StoreLockDetails;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderItemSplitGroupResponse;
import com.aiqin.mgs.order.api.service.order.ErpStoreLockDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * description: ErpStoreLockDetailsServiceImpl
 * date: 2020/3/17 15:27
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class ErpStoreLockDetailsServiceImpl implements ErpStoreLockDetailsService {

    @Autowired
    private StoreLockDetailsDao storeLockDetailsDao;

    @Override
    @Transactional
    public void insertStoreLockDetails(List<StoreLockDetails> list) {
        log.info("插入商品锁库存信息，入参list={}",list);
        storeLockDetailsDao.insertBatch(list);
        log.info("插入商品锁库存信息成功");
    }

    @Override
    public StoreLockDetails selectByLineCodeAndSkuCodeAndLockCount(StoreLockDetails entity) {
        return storeLockDetailsDao.selectByLineCodeAndSkuCodeAndLockCount(entity);
    }

    @Override
    public List<ErpOrderItemSplitGroupResponse> getNewRepositorySplitGroup(ErpOrderInfo order) {
        log.info("订单拆单--查询商品锁库信息，从本地查询入参order={}",order);
        List<ErpOrderItemSplitGroupResponse> list = new ArrayList<>();
        //存储sku
        Set<String> skuSet=new HashSet<>();
        //将相同sku详情汇总
        Map<String,List<ErpOrderItem>> skuDetailListMap=new HashMap<>();
        for (ErpOrderItem item : order.getItemList()) {
            skuSet.add(item.getSkuCode());
            String skuCode=item.getSkuCode();
            List<ErpOrderItem> eoiList=new ArrayList<>();
            if(null!=skuDetailListMap&&null!=skuDetailListMap.get(skuCode)){
                eoiList=skuDetailListMap.get(skuCode);
            }
            eoiList.add(item);
            skuDetailListMap.put(skuCode,eoiList);
        }
        log.info("订单拆单--将相同sku详情汇总，skuDetailListMap={}",skuDetailListMap);
        //sku:本地锁库
        Map<String,List<StoreLockDetails>> res=new HashMap<>();
        //(仓库+库房+sku):锁库数量
        Map<String,Integer> lockCount=new HashMap<>();
        //(仓库+库房+sku):锁库详情
        Map<String,StoreLockDetails> lockDetail=new HashMap<>();
        // 增强for循环
        for (String string : skuSet) {
            StoreLockDetails storeLockDetails=new StoreLockDetails();
            storeLockDetails.setSkuCode(string);
            storeLockDetails.setOrderCode(order.getMainOrderCode());
            log.info("订单拆单--查询商品锁库信息，从本地查询商品orderCode={},skuCode={}",order.getMainOrderCode(),string);
            List<StoreLockDetails> storeLockDetails1 = storeLockDetailsDao.selectByOrderCodeAndSkuCode(storeLockDetails);
            log.info("订单拆单--查询商品锁库信息，从本地查询商品单行返回结果storeLockDetails1={}",storeLockDetails1);
            res.put(string,storeLockDetails1);
            for(StoreLockDetails s:storeLockDetails1){
                lockCount.put(s.getTransportCenterCode()+s.getWarehouseCode()+s.getSkuCode(),s.getChangeCount());
                lockDetail.put(s.getTransportCenterCode()+s.getWarehouseCode()+s.getSkuCode(),s);
            }
        }
        log.info("订单拆单--sku:本地锁库，res={}",res);
        log.info("订单拆单--(仓库+库房+sku):锁库数量，lockCount={}",lockCount);
        log.info("订单拆单--(仓库+库房+sku):锁库详情，lockDetail={}",lockDetail);
        if(skuDetailListMap!=null){
            for(Map.Entry<String,List<ErpOrderItem>> map:skuDetailListMap.entrySet()){
                String skuCode=map.getKey();
                List<ErpOrderItem> value = map.getValue();
                List<StoreLockDetails> list1=res.get(skuCode);
                if(list1!=null&&list1.size()>0){
                    if(list1.size()==1){//本sku分到了一个仓库
                        StoreLockDetails storeLockDetails=list1.get(0);
                        for(ErpOrderItem eoi:value){//遍历原始详情，添加行号
                            Long lineCode1=eoi.getLineCode();
                            Long count=eoi.getProductCount();
                            ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse=new ErpOrderItemSplitGroupResponse();
                            BeanUtils.copyProperties(storeLockDetails,erpOrderItemSplitGroupResponse);
                            erpOrderItemSplitGroupResponse.setLockCount(count);
                            erpOrderItemSplitGroupResponse.setLineCode(lineCode1);
                            list.add(erpOrderItemSplitGroupResponse);
                            Long cou=Long.valueOf(lockCount.get(storeLockDetails.getTransportCenterCode()+storeLockDetails.getWarehouseCode()+storeLockDetails.getSkuCode()));
                            Long ca=cou-count;
                            //map是否要清零
                            lockCount.put(storeLockDetails.getTransportCenterCode()+storeLockDetails.getWarehouseCode()+storeLockDetails.getSkuCode(),Integer.valueOf(ca.toString()));
                        }
                    }else{//本sku分到了多个仓库
                        for(ErpOrderItem eoi:value){//遍历原始详情，添加行号
                            Long lineCode1=eoi.getLineCode();
                            Long count=eoi.getProductCount();
                            String transportCenterCode = list1.get(0).getTransportCenterCode();
                            String wa=list1.get(0).getWarehouseCode();
                            String sk=list1.get(0).getSkuCode();
                            String transportCenterCode2 = list1.get(1).getTransportCenterCode();
                            String wa2=list1.get(1).getWarehouseCode();
                            String sk2=list1.get(1).getSkuCode();
                            Long cou=Long.valueOf(lockCount.get(transportCenterCode+wa+sk).toString());
                            Long cou2=Long.valueOf(lockCount.get(transportCenterCode2+wa2+sk2).toString());
                            log.info("订单拆单--第一库存是否已全部分配cou={}",cou);
                            if(cou.equals(0L)){//第一库存已全部分配,启用第二仓库
                                ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse1=new ErpOrderItemSplitGroupResponse();
                                BeanUtils.copyProperties(lockDetail.get(transportCenterCode2+wa2+sk2),erpOrderItemSplitGroupResponse1);
                                erpOrderItemSplitGroupResponse1.setLockCount(count);
                                erpOrderItemSplitGroupResponse1.setLineCode(lineCode1);
                                list.add(erpOrderItemSplitGroupResponse1);
                                Long c=cou2-count;
                                lockCount.put(transportCenterCode2+wa2+sk2,Integer.valueOf(c.toString()));
                            }
                            if(cou.compareTo(count)==0){//当前sku锁库总数量和遍历详情数量相等
                                ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse=new ErpOrderItemSplitGroupResponse();
                                BeanUtils.copyProperties(lockDetail.get(transportCenterCode+wa+sk),erpOrderItemSplitGroupResponse);
                                erpOrderItemSplitGroupResponse.setLockCount(count);
                                erpOrderItemSplitGroupResponse.setLineCode(lineCode1);
                                list.add(erpOrderItemSplitGroupResponse);
                                lockCount.put(transportCenterCode+wa+sk,0);
                            }else if(cou.compareTo(count)>0){//当前sku锁库总数量大于详情数量相等
                                ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse=new ErpOrderItemSplitGroupResponse();
                                BeanUtils.copyProperties(lockDetail.get(transportCenterCode+wa+sk),erpOrderItemSplitGroupResponse);
                                erpOrderItemSplitGroupResponse.setLockCount(count);
                                erpOrderItemSplitGroupResponse.setLineCode(lineCode1);
                                list.add(erpOrderItemSplitGroupResponse);
                                Long cha=(cou-count);
                                lockCount.put(transportCenterCode+wa+sk,Integer.valueOf(cha.toString()));
                            }else{//当前sku锁库总数量小于详情数量相等
                                ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse=new ErpOrderItemSplitGroupResponse();
                                BeanUtils.copyProperties(lockDetail.get(transportCenterCode+wa+sk),erpOrderItemSplitGroupResponse);
                                erpOrderItemSplitGroupResponse.setLockCount(cou);
                                erpOrderItemSplitGroupResponse.setLineCode(lineCode1);
                                list.add(erpOrderItemSplitGroupResponse);
                                Long cha=(count-cou);
                                lockCount.put(transportCenterCode+wa+sk,0);
                                ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse1=new ErpOrderItemSplitGroupResponse();
                                BeanUtils.copyProperties(lockDetail.get(transportCenterCode2+wa2+sk2),erpOrderItemSplitGroupResponse1);
                                erpOrderItemSplitGroupResponse1.setLockCount(cha);
                                erpOrderItemSplitGroupResponse1.setLineCode(lineCode1);
                                list.add(erpOrderItemSplitGroupResponse1);
                                Long c=cou2-cha;
                                lockCount.put(transportCenterCode2+wa2+sk2,Integer.valueOf(c.toString()));
                            }
                        }
                    }
                }
            }
        }

//        for (ErpOrderItem item : order.getItemList()) {
//            StoreLockDetails storeLockDetails=new StoreLockDetails();
//            storeLockDetails.setSkuCode(item.getSkuCode());
//            storeLockDetails.setOrderCode(order.getOrderStoreCode());
//            log.info("订单拆单--查询商品锁库信息，从本地查询商品orderCode={},skuCode={}",order.getOrderStoreCode(),item.getSkuCode());
//            List<StoreLockDetails> storeLockDetails1 = storeLockDetailsDao.selectByOrderCodeAndSkuCode(storeLockDetails);
//            log.info("订单拆单--查询商品锁库信息，从本地查询商品单行返回结果storeLockDetails1={}",storeLockDetails1);
//            if(storeLockDetails1!=null){
//                ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse=new ErpOrderItemSplitGroupResponse();
//                BeanUtils.copyProperties(storeLockDetails1,erpOrderItemSplitGroupResponse);
//                erpOrderItemSplitGroupResponse.setLockCount(Long.valueOf(storeLockDetails1.getChangeCount()));
//                list.add(erpOrderItemSplitGroupResponse);
//            }
//        }
        log.info("订单拆单--查询商品锁库信息，从本地查询返回结果list={}",list);
        return list;
    }

    @Override
    public int deleteBySkuCodeAndLockCount(StoreLockDetails record) {
        return storeLockDetailsDao.deleteBySkuCodeAndLockCount(record);
    }

    @Override
    public int deleteBySkuCode(String orderCode,String skuCode) {
        StoreLockDetails storeLockDetails=new StoreLockDetails();
        storeLockDetails.setOrderCode(orderCode);
        storeLockDetails.setSkuCode(skuCode);
        return storeLockDetailsDao.deleteByOrderCodeAndSkuCode(storeLockDetails);
    }

}
