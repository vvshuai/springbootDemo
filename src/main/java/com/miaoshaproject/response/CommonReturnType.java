package com.miaoshaproject.response;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 11:31 2020/4/29
 * @Modified By:
 */
public class CommonReturnType {

    //表明对应请求返回处理结果
    //success或者fail
    private String status;

    //返回数据
    private Object data;

    public static CommonReturnType create(Object result){

        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
