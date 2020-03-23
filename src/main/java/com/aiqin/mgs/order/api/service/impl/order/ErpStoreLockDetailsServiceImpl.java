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

import java.util.ArrayList;
import java.util.List;

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
        log.info("查询商品锁库信息，从本地查询入参order={}",order);
        List<ErpOrderItemSplitGroupResponse> list = new ArrayList<>();
        for (ErpOrderItem item : order.getItemList()) {
            StoreLockDetails storeLockDetails=new StoreLockDetails();
            storeLockDetails.setLineCode(item.getLineCode());
            storeLockDetails.setSkuCode(item.getSkuCode());
            storeLockDetails.setChangeCount(Integer.valueOf(item.getProductCount().toString()));
            log.info("查询商品锁库信息，从本地查询商品skuCode={},lineCode={},lockCount={}",item.getSkuCode(),item.getLineCode(),item.getProductCount());
//            StoreLockDetails storeLockDetails1 = storeLockDetailsDao.selectByLineCodeAndSkuCodeAndLockCount(storeLockDetails);
            StoreLockDetails storeLockDetails1 = storeLockDetailsDao.selectBySkuCode(item.getSkuCode());
            log.info("查询商品锁库信息，从本地查询商品单行返回结果storeLockDetails1={}",storeLockDetails1);
            if(storeLockDetails1!=null){
                ErpOrderItemSplitGroupResponse erpOrderItemSplitGroupResponse=new ErpOrderItemSplitGroupResponse();
                BeanUtils.copyProperties(storeLockDetails1,erpOrderItemSplitGroupResponse);
                erpOrderItemSplitGroupResponse.setLockCount(Long.valueOf(storeLockDetails1.getChangeCount()));
                list.add(erpOrderItemSplitGroupResponse);
            }
        }
        log.info("查询商品锁库信息，从本地查询返回结果list={}",list);
        return list;
    }

    @Override
    public int deleteBySkuCodeAndLockCount(StoreLockDetails record) {
        return storeLockDetailsDao.deleteBySkuCodeAndLockCount(record);
    }

    @Override
    public int deleteBySkuCode(String skuCode) {
        return storeLockDetailsDao.deleteBySkuCode(skuCode);
    }

}
