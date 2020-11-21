package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.OrderDOMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dao.StockLogDOMapper;
import com.miaoshaproject.dataobject.OrderDO;
import com.miaoshaproject.dataobject.SequenceDO;
import com.miaoshaproject.dataobject.StockLogDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 12:54 2020/5/5
 * @Modified By:
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Autowired
    private StockLogDOMapper stockLogDOMapper;

    @Override
    @Transactional
    public OrderModel createOrderModel(Integer userId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws BusinessException {
        //校验下单状态
//        ItemModel itemModel = itemService.getItemById(itemId);
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);

        if(itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR,"商品信息不存在");
        }

        UserModel userModel = userService.getUserByIdInCache(userId);

        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR,"用户信息不存在");
        }

        if(amount <= 0 || amount > 99){
            throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR,"数量信息不正确");
        }

        //校验活动信息
        if(promoId != null){
            //校验对应活动是否存在
            if(promoId.intValue() != itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR,"活动信息不正确");
            }else if(itemModel.getPromoModel().getStatus().intValue()!=2){
                throw new BusinessException(EmBusinessError.PARAMETER_VAALIDATION_ERROR,"活动信息不正确");
            }
        }

        //落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if(promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        orderModel.setPromoId(promoId);
        //生成流水号
        orderModel.setId(generateOrderNo());
        OrderDO orderDO = converFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        itemService.increaseSales(itemId, amount);

        // 设置库存流水状态为成功
        StockLogDO stockLogDO = stockLogDOMapper.selectByPrimaryKey(stockLogId);
        if(stockLogDO == null){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        // 2 表示下单成功
        stockLogDO.setStatus(2);
        stockLogDOMapper.updateByPrimaryKeySelective(stockLogDO);

//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//            @Override
//            public void afterCommit() {
//                // 异步更新缓存
//                boolean mqResult = itemService.asyncDecreaseStock(itemId, amount);
//                if(!mqResult){
//                    itemService.increseaseStock(itemId, amount);
//                    throw new BusinessException(EmBusinessError.MQ_SEND_FAIL);
//                }
//            }
//        });


        //返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderNo(){
        StringBuilder stringBuilder = new StringBuilder();
        //订单号16位,前八位时间信息
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.BASIC_ISO_DATE.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);

        //自增序列

        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");

        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String sequenceString = String.valueOf(sequence);
        for(int i = 0;i < 6-sequenceString.length(); i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceString);
        //暂时写死分库分表
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private OrderDO converFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }

        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
