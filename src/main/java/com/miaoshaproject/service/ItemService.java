package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 22:23 2020/5/4
 * @Modified By:
 */
public interface ItemService {

    /**
     * @Description: 创建商品
     * @return:
     */
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    /**
     * @Description: 商品列表浏览
     * @return:
     */
    List<ItemModel> listItem();

    /**
     * @Description: 商品详情浏览
     * @return: com.miaoshaproject.service.model.ItemModel
     */
    ItemModel getItemById(Integer id);

    /**
     * @Description: item 及 promo model 缓存模型
     * @return: com.miaoshaproject.service.model.ItemModel
     */
    ItemModel getItemByIdInCache(Integer id);

    /**
     * @Description: 减库存
     * @return: boolean
     */
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    /**
     * @Description: 异步更新库存
     * @return: boolean
     */
    boolean asyncDecreaseStock(Integer itemId, Integer amount);

    /**
     * @Description: 初始化库存流水
     * @return:
     */
    String initStockLog(Integer itemId, Integer amount);

    /**
     * @Description: 库存互补
     * @return:
     */
    boolean increseaseStock(Integer itemId, Integer amount);

    /**
     * @Description: 商品销量增加
     * @return: void
     */
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
