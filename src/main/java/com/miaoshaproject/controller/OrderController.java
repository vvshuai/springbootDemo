package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 14:49 2020/5/5
 * @Modified By:
 */
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/createorder", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="amount")Integer amount,
                                        @RequestParam(name="promoId", required = false)Integer promoId) throws BusinessException {
        Boolean isLogin = (Boolean)httpServletRequest.getSession().getAttribute("LOGIN");

        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还没登录");
        }

        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrderModel(userModel.getId(), itemId, promoId, amount);

        return CommonReturnType.create(orderModel);
    }
}
