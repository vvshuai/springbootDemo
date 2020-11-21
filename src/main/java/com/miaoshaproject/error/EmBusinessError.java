package com.miaoshaproject.error;

import java.util.HashMap;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 19:45 2020/4/29
 * @Modified By:
 */
public enum EmBusinessError implements CommonError{
    //通用错误类型
    PARAMETER_VAALIDATION_ERROR(00001,"参数不合法"),
    UNKNOWN_ERROR(00002,"未知错误"),


    USER_NOT_EXIST(10001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003,"用户还未登录"),

    STOCK_NOT_ENOUGH(30001,"库存不足"),
    MQ_SEND_FAIL(30002,"库存异步消息失败")
    ;

    EmBusinessError(int errCode, String errMsg){
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    private int errCode;
    private String errMsg;
    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
