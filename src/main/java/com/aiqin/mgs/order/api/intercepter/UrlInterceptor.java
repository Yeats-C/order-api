package com.aiqin.mgs.order.api.intercepter;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2018/8/28.
 */
@Component
@Slf4j
public class UrlInterceptor implements HandlerInterceptor {
    @Value("${center.main.host}")
    private String centerMainHost;
    @Value("${center.main.url}")
    private String centerMainUrl;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = httpServletRequest.getParameter("ticket");
        String personId = httpServletRequest.getParameter("ticket_person_id");
        if (httpServletRequest.getRequestURL().indexOf(".jpg") != -1 ||
                httpServletRequest.getRequestURL().indexOf(".bmp") != -1 ||
                httpServletRequest.getRequestURL().indexOf(".png") != -1 ||
                httpServletRequest.getRequestURL().indexOf("no_controller") != -1) {
            return true;
        }
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isBlank(ticket) || StringUtils.isBlank(personId)) {//返回重定向地址到中控登录
            String origin = httpServletRequest.getHeader("Origin");
            httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin,Content-Type,Accept,token,X-Requested-With");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            jsonObject.put("code", "-10");
            jsonObject.put("message", "没有登录");
            returnJson(httpServletResponse, jsonObject);
            return false;
        }
        //中台校验ticket是否有效
        HttpClient httpClient = HttpClient.get(centerMainUrl + "/account/verify/ticket")
                .addParameter("ticket", ticket)
                .addParameter("ticket_person_id", personId);
        HttpResponse response = httpClient.action().result(HttpResponse.class);
        if (!"0".equals(response.getCode())) {
            String origin = httpServletRequest.getHeader("Origin");
            httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin,Content-Type,Accept,token,X-Requested-With");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            jsonObject.put("code", "-10");
            jsonObject.put("message", "登录失效");
            returnJson(httpServletResponse, jsonObject);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        httpServletResponse.setContentType("text/html;charset=utf-8");
    }

    public void returnJson(HttpServletResponse response, JSONObject jsonObject) throws Exception {
        PrintWriter writer = null;
        try {
            String origin = response.getHeader("Origin");
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/json; charset=utf-8");
            writer = response.getWriter();
            writer.print(jsonObject.toJSONString());
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
