package com.aiqin.mgs.order.api.util;

import com.aiqin.ground.util.protocol.http.HttpResponse;

/**
 * 请求返回值处理类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 15:36
 */
public class RequestReturnUtil {

    /***HttpResponse中的code 表示成功*/
    private static final String HTTP_RESPONSE_CODE_SUCCESS = "0";

    /**
     * 判断请求是否成功
     *
     * @param httpResponse
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 15:37
     */
    public static boolean validateHttpResponse(HttpResponse httpResponse) {
        boolean flag = true;
        if (httpResponse == null) {
            flag = false;
        } else {
            if (!HTTP_RESPONSE_CODE_SUCCESS.equals(httpResponse.getCode())) {
                flag = false;
            }
        }
        return flag;
    }
}
