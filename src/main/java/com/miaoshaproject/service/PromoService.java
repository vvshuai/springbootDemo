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

    PromoModel getPromoByItemId(Integer id);

    void pushlishPromo(Integer promoId);
}
