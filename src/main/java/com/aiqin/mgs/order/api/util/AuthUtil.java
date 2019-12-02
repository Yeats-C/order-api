package com.aiqin.mgs.order.api.util;

import com.aiqin.mgs.order.api.domain.AuthToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 当前登录用户工具类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/16 16:07
 */
public class AuthUtil {

    /**
     * 获取当前http请求的用户参数
     *
     * @param
     * @return com.aiqin.mgs.order.api.domain.AuthToken
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 16:07
     */
    public static AuthToken getCurrentAuth() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        AuthToken authToken = new AuthToken();
        //从head中获取参数
///        Enumeration<?> enum1 = request.getHeaderNames();
///        while (enum1.hasMoreElements()) {
///            String key = (String) enum1.nextElement();
///            String value = request.getHeader(key);
///            if ("person_id".equals(key)) {
///                authToken.setPersonId(value);
///            }
///            if ("account_id".equals(key)) {
///                authToken.setAccountId(value);
///            }
///            if ("ticket".equals(key)) {
///                authToken.setTicket(value);
///            }
///            if ("ticket_person_id".equals(key)) {
///                authToken.setTicketPersonId(value);
///            }
///            if ("person_name".equals(key)) {
///                authToken.setPersonName(value);
///            }
///        }

        //从url中获取参数
        authToken.setPersonId(request.getParameter("person_id"));
        authToken.setPersonName(request.getParameter("person_name"));
        authToken.setAccountId(request.getParameter("account_id"));
        authToken.setTicket(request.getParameter("ticket"));
        authToken.setTicketPersonId(request.getParameter("ticket_person_id"));
        return authToken;
    }

    /**
     * 获取定时任务操作人
     *
     * @param
     * @return com.aiqin.mgs.order.api.domain.AuthToken
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 10:35
     */
    public static AuthToken getTaskAuth() {
        AuthToken auth = new AuthToken();
        auth.setPersonId("定时任务");
        auth.setPersonName("定时任务");
        return auth;
    }

    /**
     * 获取系统操作人
     *
     * @param
     * @return com.aiqin.mgs.order.api.domain.AuthToken
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/26 10:12
     */
    public static AuthToken getSystemAuth() {
        AuthToken auth = new AuthToken();
        auth.setPersonId("系统初始化");
        auth.setPersonName("系统初始化");
        return auth;
    }
}
