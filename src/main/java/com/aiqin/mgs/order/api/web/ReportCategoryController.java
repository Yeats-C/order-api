/*****************************************************************
* 模块名称：品类报表 
* 开发人员: huangzy
* 开发时间: Mon Feb 24 10:04:13 CST 2020 
* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.Api;
import com.aiqin.mgs.order.api.service.*;

@RestController

@RequestMapping("/category/area")
@Api(tags = "品类报表")
@SuppressWarnings("all")
public class ReportCategoryController {

    @Autowired
    private ReportCategoryService service;


}