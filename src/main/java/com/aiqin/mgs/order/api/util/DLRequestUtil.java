package com.aiqin.mgs.order.api.util;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * Created by 爱亲 on 2019/3/6.
 */
@Slf4j
public class DLRequestUtil {

    public static String EncoderByMd5(String key, String data){
        String sign = "";
        try{
            //确定计算方法
            MessageDigest md5=MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            byte[] bytes = md5.digest((key + data + key).getBytes("utf-8"));
            sign = base64en.encode(bytes);
            sign = URLEncoder.encode(sign.toUpperCase(), "utf-8");
        }catch (Exception e){
            log.error("转化MD5加密错误", e);
        }
        return sign;
    }

}