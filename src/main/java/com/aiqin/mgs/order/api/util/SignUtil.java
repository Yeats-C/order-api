package com.aiqin.mgs.order.api.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Map;

public class SignUtil {

    private final static String SECRET_KEY = "SECRET_KEY";

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * 一个简单的map转json字符串
     *
     * @param map
     * @return
     */
    public static String mapToJsonString(Map<String, String> map) {
        if (map.size() < 1) {
            return null;
        }
        String json = "{";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            json += "\"" + key + "\"" + ":\"" + value + "\",";
        }
        json = json.substring(0, json.length() - 1);
        return json + "}";
    }

    public static void main(String[] args) {
        String ss = getURLEncoderString("http://tas.dms.aiqin.com");
        System.out.println(ss);
    }

    public static String getURLEncoderString(String str) {//url编码
        String result = "";
        if (StringUtils.isBlank(str)) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String URLDecoderString(String str) {//url解码
        String result = "";
        if (StringUtils.isBlank(str)) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
