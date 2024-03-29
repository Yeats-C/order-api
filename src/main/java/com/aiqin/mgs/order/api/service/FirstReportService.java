package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;

import java.util.Date;

public interface FirstReportService {

    //首单报表定时任务
    void reportTimedTask();
    //根据报表时间查询报表表格数据
    HttpResponse getLists(String reportTime,Integer pageNo,Integer pageSize,String personId,String resourceCode);
    //首单定时任务年报
    void reportTimedTaskYear();
}
