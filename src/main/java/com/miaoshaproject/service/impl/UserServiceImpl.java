package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 10:41 2020/4/29
 * @Modified By:
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;


    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        //通过用户手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        //比对加密密码
        if(!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())){
            throw  new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        return userModel;
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR);
        }

        ValidationResult validate = validator.validate(userModel);
        if(validate.isHadErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR, validate.getErrMsg());
        }
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR,"手机号已经存在");
        }
        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());

        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    @Override
    public UserModel getUserById(Integer id) {
        //获取用户对应信息
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        //获取加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO, userPasswordDO);
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        if(userPasswordDO!=null) {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
