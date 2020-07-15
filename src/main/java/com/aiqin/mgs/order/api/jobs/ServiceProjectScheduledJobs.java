package com.aiqin.mgs.order.api.jobs;

import com.aiqin.mgs.order.api.dao.ServiceProjectAssetDao;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectAsset;
import com.aiqin.mgs.order.api.service.ServiceProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ServiceProjectScheduledJobs {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProjectScheduledJobs.class);

    @Resource
    private ServiceProjectService serviceProjectService;
    @Resource
    private ServiceProjectAssetDao serviceProjectAssetDao;
    //每天凌晨一点执行
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void serviceProjectJobs(){
        try {
            LOGGER.info("开始执行服务管理定时任务");
            List<ServiceProjectAsset> serviceProjectAssetList = serviceProjectAssetDao.assetOutTime();
            if(CollectionUtils.isNotEmpty(serviceProjectAssetList)){
                serviceProjectAssetDao.updateAssetUseStatus(serviceProjectAssetList);
            }
            LOGGER.info("服务管理定时任务完成");
        }catch (Exception e){
            LOGGER.info("执行服务管理定时任务异常");
            throw new RuntimeException("执行服务管理定时任务异常");
        }
    }
}
