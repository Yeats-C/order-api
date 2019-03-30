/*****************************************************************

* 模块名称：封装-定时任务
* 开发人员: hzy
* 开发时间: 2019-01-08

* ****************************************************************************/
package com.aiqin.mgs.order.api.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.http.HttpClientPost;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.response.SkuSaleResponse;
import com.aiqin.mgs.order.api.service.OrderDetailService;
import com.aiqin.mgs.order.api.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class TaskSkuService{

	@Resource
    private OrderService orderService;
	
	@Resource
    private OrderDetailService orderDetailService;
	
	
	//商品项目地址
    @Value("${product_ip}")
    public String product_ip;
    
    //批量添加sku销量
    @Value("${product_sku}")
    public String product_sku;
    
    
	
    @Scheduled(cron = "0 0 */2 * * ?") // 设置每2小时执行一次

    /**
     * 
     */
    public void getTask() {

    //批量添加sku销量
    	List<String> orderList = new ArrayList();
    	//查询未统计销量的已完成订单 
    	orderList = orderService.selectsukReturn();
    	
    	//查询SKU+销量
    	if(orderList !=null && orderList.size()>0) {
    		List<SkuSaleResponse> list = new ArrayList();
    		
    		list = orderDetailService.selectSkuSale(orderList);
    		
    	//同步商品
    		//访问地址
        	StringBuilder sb = new StringBuilder();
            sb.append(product_ip).append(product_sku);
            
            //返回结果
            HttpClient httpClient = HttpClientPost.post("http://"+sb.toString()).json(list);
            HttpResponse result = httpClient.action().result(new TypeReference<HttpResponse>() {});
    	    
            if (Objects.nonNull(result) && Objects.equals("0", result.getCode())) {
                
            	for(String orderId: orderList) {
            		//修改统计销量状态
                	orderService.updateSukReturn(orderId); 
            	}
            }
    	}
    }
}



