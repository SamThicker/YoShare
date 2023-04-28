package com.yo.zuulservice.security;

import com.alibaba.fastjson.JSON;
import com.yo.yoshare.common.api.CommonResult;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        if (exception instanceof BadCredentialsException){
            response.getWriter().write(JSON.toJSONString(CommonResult.failed("用户名或密码不正确")));
        }
        else if (exception instanceof LockedException || exception instanceof DisabledException || exception instanceof AccountExpiredException){
            response.getWriter().write(JSON.toJSONString(CommonResult.failed("账号异常")));
        }
        else {
            response.getWriter().write(JSON.toJSONString(CommonResult.failed("服务异常")));
        }

    }
}
