/*****************************************************************

* 模块名称：封装-定时任务
* 开发人员: hzy
* 开发时间: 2018-12-10

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
public class TaskService{

	@Resource
    private OrderService orderService;
	
	
    @Scheduled(cron = "0/5 * * * * ?") // 设置每5秒执行一次

    public void getTask() {

    //未付款订单30分钟后自动取消
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
    	
    //提货码10分钟后失效.
    	List<String> nevredList = null;
    	nevredList = orderService.nevred();
    	if(nevredList !=null && nevredList.size()>0) {
    		for(String id:nevredList) {
        		orderService.reded(id);
        	}
    	}
    }
}



