package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 12:52 2020/5/5
 * @Modified By:
 */
public interface OrderService {
    //1.前端url上传url上传秒杀id
    OrderModel createOrderModel(Integer userId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws BusinessException;

}
