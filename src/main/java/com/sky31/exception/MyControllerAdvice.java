package com.sky31.exception;

import com.sky31.utils.Md5AndJsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 11:38
 */
@ControllerAdvice(annotations = Controller.class)
public class MyControllerAdvice {

    private static final Logger logger= LoggerFactory.getLogger(MyControllerAdvice.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常"+e.getMessage());
        for (StackTraceElement element:e.getStackTrace()){
            logger.error(element.toString());
        }
        String requestHeader = request.getHeader("x-requested-with");
        if (requestHeader.equals("XMLHttpRequest")){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer=response.getWriter();
            writer.write(Md5AndJsonUtil.getJSONString(1,"服务器异常"));
        }else{
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }

}
