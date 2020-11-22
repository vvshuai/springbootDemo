package com.miaoshaproject.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.mq.MqProducer;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;

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
    private ItemService itemService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    MqProducer mqProducer;

    private ExecutorService executorService;

    // 增加令牌桶
    private RateLimiter orderCreateRateLimiter;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(20);

        orderCreateRateLimiter = RateLimiter.create(300);

    }

    // 生成秒杀令牌
    @RequestMapping(value = "/generatetoken", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="promoId", required = false)Integer promoId) throws BusinessException {
        // 待更新
        return null;
    }


    @RequestMapping(value = "/createorder", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="amount")Integer amount,
                                        @RequestParam(name="promoId", required = false)Integer promoId) throws BusinessException {
        if(!orderCreateRateLimiter.tryAcquire()){
            throw new BusinessException(EmBusinessError.RATELIMIT);
        }

        Boolean isLogin = (Boolean)httpServletRequest.getSession().getAttribute("LOGIN");

        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还没登录");
        }

        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

//        OrderModel orderModel = orderService.createOrderModel(userModel.getId(), itemId, promoId, amount);

        // 判断库存是否已经售罄

        if(redisTemplate.hasKey("promo_item_stock_invalid_" + itemId)) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        // 同步调用线程池的submit方法
        // 拥塞窗口为20的等待队列，用来队列化泄洪
        Future<Object> future = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String stockLogId = itemService.initStockLog(itemId, amount);


                // 完成下单事务型消息
                boolean b = mqProducer.transactionAsyncReduceStock(userModel.getId(), promoId, itemId, amount, stockLogId);
                //下单失败
                if (b == false) {
                    throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "下单失败");
                }
                return null;
            }
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return CommonReturnType.create(null);
    }
}
