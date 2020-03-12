package com.yo.zuulservice.component;

import com.yo.yoshare.common.api.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseBody
    public CommonResult badCredentilExceptionHandler(Exception e){
        logger.error("业务日志",e);
        return CommonResult.failed("亲，账号或密码不正确哦~");
    }

    @ExceptionHandler(value = AccountStatusException.class)
    @ResponseBody
    public CommonResult accountStatusExceptionHandler(Exception e){
        logger.error("业务日志",e);
        return CommonResult.failed("呀，账号状态异常！");
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public CommonResult runtimeExceptionHandler(Exception e){
        logger.error("业务日志",e);
        return CommonResult.failed("服务异常");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResult exceptionHandler(Exception e){
        logger.error("业务日志",e);
        return CommonResult.failed("阿欧，出错辣~");
    }
}
