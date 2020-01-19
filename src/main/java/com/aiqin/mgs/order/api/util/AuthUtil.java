package com.aiqin.mgs.order.api.util;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.AuthToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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

        //从url中获取参数
        authToken.setPersonId(request.getParameter("person_id"));
        authToken.setPersonName(request.getParameter("person_name"));
        authToken.setAccountId(request.getParameter("account_id"));
        authToken.setTicket(request.getParameter("ticket"));
        authToken.setTicketPersonId(request.getParameter("ticket_person_id"));
        return authToken;
    }

    /**
     * 登录检查
     *
     * @param
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/3 20:02
     */
    public static void loginCheck() {
        AuthToken auth = getCurrentAuth();
        if (StringUtils.isEmpty(auth.getPersonId())) {
            throw new BusinessException("请先登录");
        }
        if (StringUtils.isEmpty(auth.getPersonName())) {
            throw new BusinessException("缺失公共参数person_name");
        }
        if (StringUtils.isEmpty(auth.getAccountId())) {
            throw new BusinessException("缺失公共参数account_id");
        }
    }

    public static void addParameter(HttpClient client, AuthToken authToken) {
        if (Objects.nonNull(authToken)) {
            if (StringUtils.isNotBlank(authToken.getPersonId())) {
                client.addParameter("person_id", authToken.getPersonId());
            }
            if (StringUtils.isNotBlank(authToken.getPersonName())) {
                client.addParameter("person_name", authToken.getPersonName());
            }
            if (StringUtils.isNotBlank(authToken.getTicket())) {
                client.addParameter("ticket", authToken.getTicket());
            }
            if (StringUtils.isNotBlank(authToken.getAccountId())) {
                client.addParameter("account_id", authToken.getAccountId());
            }
            if (StringUtils.isNotBlank(authToken.getTicketPersonId())) {
                client.addParameter("ticket_person_id", authToken.getTicketPersonId());
            }
        }
    }
}
