package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 20:29 2020/4/29
 * @Modified By:
 */
@ControllerAdvice
public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    //定义exception未解决的
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String, Object> responseData = new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode", businessException.getErrorCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }
        else{
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData,"fail");
    }
}
