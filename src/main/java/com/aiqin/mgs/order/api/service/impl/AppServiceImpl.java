package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.dao.AppVersionDao;
import com.aiqin.mgs.order.api.domain.AppVersionInfo;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.service.AppService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
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
        AppVersionInfo appVersionInfo1=appVersionDao.selectAppActive(appVersionInfo);

        return HttpResponse.successGenerics(appVersionInfo1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse appAdd(AppVersionInfo appVersionInfo) {
        AuthUtil.loginCheck();
        AuthToken auth = AuthUtil.getCurrentAuth();
        appVersionInfo.setCreateBy(auth.getPersonName());
        appVersionInfo.setCreateById(auth.getPersonId());
        appVersionInfo.setCreateTime(new Date());
        appVersionInfo.setState(1);
        appVersionInfo.setUpdateById(auth.getPersonId());
        appVersionInfo.setUpdateByName(auth.getPersonName());
        appVersionInfo.setUpdateTime(new Date());
        appVersionDao.updateStateAll(appVersionInfo);
        int n= appVersionDao.add(appVersionInfo);
        return HttpResponse.success(n);
    }

    @Override
    public HttpResponse<PageResData<AppVersionInfo>> appList(PagesRequest pagesRequest) {
        List<AppVersionInfo> appVersionInfos= appVersionDao.selectAppList(pagesRequest);
        return HttpResponse.success(new PageResData(appVersionInfos.size(), appVersionInfos));
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
