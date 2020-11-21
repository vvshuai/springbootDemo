package com.miaoshaproject.service.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 10:45 2020/4/29
 * @Modified By:
 */
public class UserModel implements Serializable {

    private Integer id;

    @NotBlank(message = "用户名不为空")
    private String name;

    @NotNull(message = "性别不能不填")
    @Min(value = 1,message = "性别男为1，女为2")
    @Max(value = 2,message = "性别男为1，女为2")
    private Byte gender;

    @NotNull(message = "性别不能不填")
    @Min(value = 0,message = "年龄必须大于0岁")
    @Max(value = 150,message = "年龄必须小于150岁")
    private Integer age;

    @NotBlank(message = "手机号不为空")
    private String telephone;
    private String registerMode;
    private String thirdPartyId;

    @NotBlank(message = "密码不为空")
    private String encrptPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }
}
