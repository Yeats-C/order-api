package com.aiqin.mgs.order.api.web.app;

import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.AppVersionInfo;
import com.aiqin.mgs.order.api.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @author jinghaibo
 * Date: 2020/6/5 10:37
 * Description: app相关功能
 */
@RestController
@RequestMapping("/app")
@Api(tags = "app相关功能")
@Slf4j
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private AppService appService;

    @PostMapping("/verson/active")
    @ApiOperation("活动当前的版本信息")
    public HttpResponse<AppVersionInfo> appActive( @RequestBody AppVersionInfo appVersionInfo) {
        return appService.appActive(appVersionInfo);
    }

    @PostMapping("/verson/upload")
    @ApiOperation("上传安装包")
    public HttpResponse<AppVersionInfo> appUpload(MultipartFile file) {
        return appService.appUpload(file);
    }

    @PostMapping("/verson/add")
    @ApiOperation("添加更新")
    public HttpResponse appAdd(@Validated @RequestBody AppVersionInfo appVersionInfo) {
        return appService.appAdd(appVersionInfo);
    }

    @PostMapping("/verson/list")
    @ApiOperation("版本列表")
    public HttpResponse<PageResData<AppVersionInfo>> appList( @RequestBody PagesRequest pagesRequest) {
        return appService.appList(pagesRequest);
    }



    @PostMapping("/verson/delete")
    @ApiOperation("删除版本")
    public HttpResponse appDelete( @RequestBody AppVersionInfo appVersionInfo) {
        return appService.appDelete(appVersionInfo);
    }
}
