package com.miaoshaproject.error;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 19:43 2020/4/29
 * @Modified By:
 */
public interface CommonError {

    public int getErrorCode();

    public String getErrMsg();

    public CommonError setErrMsg(String errMsg);
}
