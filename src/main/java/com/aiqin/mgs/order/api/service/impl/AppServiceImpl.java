package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.AppVersionDao;
import com.aiqin.mgs.order.api.domain.AppVersionInfo;
import com.aiqin.mgs.order.api.service.AppService;
import com.aiqin.mgs.order.api.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jinghaibo
 * Date: 2020/6/6 11:59
 * Description:
 */
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private OssUtil ossUtil;
    @Autowired
    private AppVersionDao appVersionDao;
    @Override
    public HttpResponse<AppVersionInfo> appActive(AppVersionInfo appVersionInfo) {
        return null;
    }

    @Override
    public HttpResponse appAdd(AppVersionInfo appVersionInfo) {
        int n= appVersionDao.add(appVersionInfo);
        return HttpResponse.success(n);
    }

    @Override
    public HttpResponse<List<AppVersionInfo>> appList(AppVersionInfo appVersionInfo) {
        return null;
    }

    @Override
    public HttpResponse appUpload(MultipartFile file) {
        Map<String, String> stringMap=new HashMap<>();
        AppVersionInfo appVersionInfo=new AppVersionInfo();
        try {
            stringMap= ossUtil.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stringMap!=null){

            appVersionInfo.setAppBuild(stringMap.get("appBuild"));
            appVersionInfo.setUpdateUrl(stringMap.get("url"));
        }
        return HttpResponse.success(appVersionInfo);
    }
}
