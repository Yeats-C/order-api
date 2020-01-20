package com.aiqin.mgs.order.api.util;

import com.aiqin.platform.flows.client.domain.vo.FileVo;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class OssUtil {

    @Value("${oss.end.point}")
    private String endPoint;
    @Value("${oss.access.key.id}")
    private String accessKeyId;
    @Value("${oss.access.key.secret}")
    private String accessKeySecret;
    @Value("${oss.bucket.name}")
    private String bucketName;
    @Value("${oss.dir}")
    private String dir;

    private static Logger logger = LoggerFactory.getLogger(OssUtil.class);

    public OssUtil() {
    }


    public Map<String, String> uploadFile(MultipartFile file) throws Exception {
        Map<String, String> map = new HashMap<>();
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = dir + "/" + UUID.randomUUID() + type;
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(file.getBytes()));
        ossClient.shutdown();
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
        log.info("oss文件链接,{}", url);
        map.put("name", fileName);
        map.put("url", url);
        map.put("dir", dir);
        return map;
    }

    public String uploadFile(byte[] bytes) throws Exception {
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        String type = ".png";
        String fileName = dir + UUID.randomUUID() + type;
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
        ossClient.shutdown();
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
        log.info("oss文件链接,{}", url);
        return url;
    }

    public String uploadImage(String base64) {
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        String imageType = ".png";
        if (base64.startsWith("data:image")) {
            imageType = base64.substring(11, base64.indexOf(";"));
        }
        if (base64.startsWith("data:img")) {
            imageType = base64.substring(9, base64.indexOf(";"));
        }
        String fileName = dir + UUID.randomUUID() + imageType;
        if (StringUtils.indexOf(base64, ",") > 0) {
            base64 = StringUtils.substringAfterLast(base64, ",");
        }
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(Base64.getDecoder().decode(base64)));
        ossClient.shutdown();
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();
        log.info("oss文件链接,{}", url);
        return url;
    }

    public boolean deleteFile(String filePath) {
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        boolean exist = ossClient.doesObjectExist(bucketName, filePath);
        if (!exist) {
            log.error("文件不存在,filePath={}", filePath);
            return false;
        }
        log.info("删除文件,filePath={}", filePath);
        ossClient.deleteObject(bucketName, filePath);
        ossClient.shutdown();
        return true;
    }

    public List<FileVo> saveFiles(MultipartFile[] files, String businessKey) {
        List<FileVo> list = new ArrayList<>();
        try {
            FileVo fileVo;
            if (files != null && files.length != 0) {
                for (MultipartFile file : files) {
                    String encode = Base64.getEncoder().encodeToString(file.getBytes());
                    String fileName = file.getOriginalFilename();
                    String fileType = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
                    fileVo = new FileVo(fileType, fileName, encode, businessKey, file.getSize());
                    list.add(fileVo);
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return list;
    }

    private static void OSSExceptionLoggerPrint(OSSException oe) throws Exception {
        logger.error("oss服务端异常...\n");
        logger.error("errorCode:\n", oe.getErrorCode());
        logger.error("errorMsg:\n", oe.getErrorMessage());
        logger.error("requestId:\n", oe.getRequestId());
        logger.error("hostId:\n", oe.getHostId());
        throw new Exception(oe.getErrorMessage(), oe);
    }

    private static void clientExceptionLoggerPrint(ClientException ce) throws Exception {
        logger.error("oss客户端异常...\n");
        logger.error("errorCode:\n", ce.getErrorCode());
        logger.error("errorMsg:\n", ce.getErrorMessage());
        throw new Exception(ce.getErrorMessage(), ce);
    }

    public static void putObject(OSSClient client, PutObjectRequest putObjectRequest) throws Exception, IOException {
        try {
            try {
                logger.debug("上传object到名为：" + putObjectRequest.getBucketName() + "的bucket");
                PutObjectResult putObjectResult = client.putObject(putObjectRequest);
                if (putObjectRequest.getCallback() != null) {
                    byte[] buffer = new byte[1024];
                    InputStream is = putObjectResult.getCallbackResponseBody();
                    int count = 0;
                    while ((count = is.read(buffer)) > 0) {
//                        is.read(buffer);
                    }
                    putObjectResult.getCallbackResponseBody().close();
                }
            } catch (OSSException var8) {
                OSSExceptionLoggerPrint(var8);
            } catch (ClientException var9) {
                clientExceptionLoggerPrint(var9);
            }

        } finally {
            ;
        }
    }

    public static String getOssImgUrl(String bucketName, String endPoint, String key) {
        return !StringUtils.isEmpty(key) ? String.format("http://%s.%s/%s", bucketName, endPoint, key) : null;
    }
}
