package com.sky31.exception;

import com.sky31.domain.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 11:38
 */
@ControllerAdvice
public class MyControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseResult handlerException(Exception e){
        String message=e.getMessage();
        ResponseResult responseResult = new ResponseResult(300,message);
        return responseResult;
    }
}
