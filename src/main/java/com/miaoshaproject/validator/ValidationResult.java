package com.miaoshaproject.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 14:56 2020/5/1
 * @Modified By:
 */
public class ValidationResult {
    //检验结果
    private boolean hadErrors = false;

    private Map<String, String> errMasMap = new HashMap<>();

    public boolean isHadErrors() {
        return hadErrors;
    }

    public void setHadErrors(boolean hadErrors) {
        this.hadErrors = hadErrors;
    }

    public Map<String, String> getErrMasMap() {
        return errMasMap;
    }

    public void setErrMasMap(Map<String, String> errMasMap) {
        this.errMasMap = errMasMap;
    }

    //通用方法
    public String getErrMsg(){
        return StringUtils.join(errMasMap.values().toArray(), ",");
    }
}
