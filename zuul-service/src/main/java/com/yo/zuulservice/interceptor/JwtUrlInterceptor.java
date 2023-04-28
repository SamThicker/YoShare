package com.yo.zuulservice.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class JwtUrlInterceptor extends HandlerInterceptorAdapter {


    /*@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String url = request.getServletPath();
        String method = request.getMethod();
        String id = null;
        if (!method.equals("POST")){
            return true;
        }
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null) {
            id = (String) pathVariables.get("id");
        }
        if (id!=null){
            System.out.println("id: "+id);
        }
        return true;
    }
*/

}


