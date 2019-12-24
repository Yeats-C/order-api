package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.OssProperties;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.config.OssConfig;
import com.aiqin.mgs.order.api.domain.response.FileOssResponse;
import com.aiqin.mgs.order.api.util.OssUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * description: UploadController
 * date: 2019/12/24 13:40
 * author: hantao
 * version: 1.0
 */
@RestController
@RequestMapping("/upload")
@Api(tags = "上传附件")
@Slf4j
public class UploadController {

    @Resource
    private OssConfig config;
    @Resource
    private OSSClient ossClient;
    @Resource
    private OssProperties ossProperties;
    @Resource
    private OssUtil ossUtil;

    @PostMapping("/upload")
    @ApiOperation(value = "a.图片上传")
    public HttpResponse<FileOssResponse> fileUpload(MultipartFile file) {
        String key = UUID.randomUUID().toString();
        PutObjectRequest req = null;
        ObjectMetadata objectMetadata = new ObjectMetadata();

        if (config == null || ossClient == null) {
            return HttpResponse.failure(ResultCode.OPT_ERROR);
        }

        if (file == null) {
            return HttpResponse.failure(ResultCode.OPT_ERROR);
        }

        String originalFilename = file.getOriginalFilename();
        try {
            InputStream input = file.getInputStream();

            objectMetadata.setContentType(file.getContentType());
            req = new PutObjectRequest(ossProperties.getBucketName(), key, input, objectMetadata);
        } catch (IOException e) {
            log.error("获取文件流失败", e);
            return HttpResponse.failure(ResultCode.OPT_ERROR);
        }
        try {
            OssUtil.putObject(ossClient, req);
            FileOssResponse resp = new FileOssResponse(originalFilename, key, OssUtil.getOssImgUrl(ossProperties.getBucketName(), ossProperties.getEndpoint(), key));
            return HttpResponse.success(resp);
        } catch (Exception e) {
            log.error("上传到oss出错", e);
            return HttpResponse.failure(ResultCode.OPT_ERROR);
        }
    }

}
