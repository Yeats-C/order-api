/*****************************************************************
* 模块名称：品类报表-实现层 
* 开发人员: huangzy
* 开发时间: Mon Feb 24 10:04:13 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import javax.annotation.Resource;
import javax.validation.Valid;
import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.mgs.order.api.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aiqin.mgs.order.api.dao.ReportCategoryDao;
import java.util.List;

@SuppressWarnings("all")
@Service
public class ReportCategoryServiceImpl implements ReportCategoryService {

    private static final Logger log = LoggerFactory.getLogger(ReportCategoryServiceImpl.class);

    @Resource
    private ReportCategoryDao dao;



}