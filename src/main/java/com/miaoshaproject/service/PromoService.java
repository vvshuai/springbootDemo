package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;
import org.springframework.stereotype.Service;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 21:35 2020/5/6
 * @Modified By:
 */

public interface PromoService {

    // 根据itemID获取即将进行的活动
    PromoModel getPromoByItemId(Integer id);

    // 活动发布
    void pushlishPromo(Integer promoId);

    // 生成秒杀用的 令牌
    String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId);
}
