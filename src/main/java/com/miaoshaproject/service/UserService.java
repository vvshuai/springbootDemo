package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 10:41 2020/4/29
 * @Modified By:
 */
public interface UserService {

    //通过用户ID获取对象
    UserModel getUserById(Integer id);

    //通过缓存获取对象
    UserModel getUserByIdInCache(Integer id);

    void register(UserModel userModel) throws BusinessException;

    UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException;
}
