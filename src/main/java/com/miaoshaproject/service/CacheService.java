package com.miaoshaproject.service;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 18:05 2020/9/14
 * @Modified By:
 */
public interface CacheService {

    void setCommonCache(String key, Object value);

    Object getFromCommonCache(String key);
}
