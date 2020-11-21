package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import static com.miaoshaproject.controller.BaseController.CONTENT_TYPE_FORMED;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 10:39 2020/4/29
 * @Modified By:
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private final static String PREFIX = "item";

    /**
     * @Description: 用户登录
     * @return:
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType loin(@RequestParam(name="telephone")String telephone,
                                  @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telephone)||
                org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        //校验登录用户是否合法
        UserModel userModel = userService.validateLogin(telephone, this.EncodeByMd5(password));

        //将登录凭证加入到用户登录的session中
        this.httpServletRequest.getSession().setAttribute("LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telephone") String telephone,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender") Byte gender,
                                     @RequestParam(name="age") Integer age,
                                     @RequestParam(name="password") String password) throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {
        //验证手机号
        String InSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        if(!StringUtils.equals(InSessionOtpCode, otpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR,"短信验证码不符合");
        }

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Base64.Encoder encoder = Base64.getEncoder();

        String newStr = encoder.encodeToString(md5.digest(string.getBytes()));
        return newStr;
    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getotp", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telephone") String telephone){
        //按照规则生成otp验证码
        Random random = new Random();
        int anInt = random.nextInt(99999);
        anInt += 10000;
        String otpCode = String.valueOf(anInt);
        //将OTP验证码同对应用户手机号关联,使用httpsession
        httpServletRequest.getSession().setAttribute(telephone,otpCode);

        //otp验证码通过短信发送给用户

        System.out.println(telephone+" "+otpCode);
        return CommonReturnType.create(otpCode);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取id并返回前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO =  convertFromModel(userModel);

        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }


}
