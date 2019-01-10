/*****************************************************************

* 模块名称：封装-定时任务
* 开发人员: hzy
* 开发时间: 2019-01-08

* ****************************************************************************/
package com.aiqin.mgs.order.api.jobs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.service.OrderService;

@Service
public class TaskSkuService{

	@Resource
    private OrderService orderService;
	
	
    @Scheduled(cron = "0/5 * * * * ?") // 设置每2小时执行一次

    public void getTask() {

    //批量添加sku销量
    	List<String> nevderList = new ArrayList();
    	nevderList = orderService.nevder();
    	if(nevderList !=null && nevderList.size()>0) {
    		for(String orderId:nevderList) {
    			Integer orderStatus = Global.ORDER_STATUS_4;
            	String updateBy = "system";
            	orderService.onlyStatus(orderId,orderStatus,updateBy);
            	System.out.println(updateBy);
    		}    		
    	}
    }
}



