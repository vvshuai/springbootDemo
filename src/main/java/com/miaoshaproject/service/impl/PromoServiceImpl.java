package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoDOMapper;
import com.miaoshaproject.dataobject.PromoDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.service.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 21:37 2020/5/6
 * @Modified By:
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null){
            return null;
        }

        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    @Override
    public void pushlishPromo(Integer promoId) {
        //通过id活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if(promoDO.getItemId() == null || promoDO.getItemId().intValue() == 0){
            return ;
        }
        ItemModel itemModel = itemService.getItemById(promoDO.getItemId());

        //将库存同步redis内
        redisTemplate.opsForValue().set("promo_item_stock_" + itemModel.getId(), itemModel.getStock());
    }

    @Override
    public String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId) {
        // 获取当前活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);

        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null){
            return null;
        }

        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }

        // 判断活动是否正在进行
        if(promoModel.getStatus() != 2) {
            return null;
        }

        // 判断用户信息是否存在
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);
        if(itemModel == null) {
            return null;
        }

        UserModel userModel = userService.getUserByIdInCache(userId);

        if(userModel == null) {
            return null;
        }

        // 生成token并且存入redis
        String token = UUID.randomUUID().toString().replace("-", "");

        redisTemplate.opsForValue().set("promo_token_" + promoId, token);
        redisTemplate.expire("promo_token_" + promoId, 5, TimeUnit.MINUTES);

        return token;
    }

    private PromoModel convertFromDataObject(PromoDO promoDO){
        if(promoDO == null){
            return null;
        }

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);

        promoModel.setPromoPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
