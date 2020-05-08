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

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //减库存
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

    //商品销量增加
    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}
