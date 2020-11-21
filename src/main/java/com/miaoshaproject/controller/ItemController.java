package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.ItemVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.CacheService;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 22:47 2020/5/4
 * @Modified By:
 */
@Slf4j
@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private PromoService promoService;

    //创建商品
    @RequestMapping(value = "/create", method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name="title")String title,
                                       @RequestParam(name="description")String description,
                                       @RequestParam(name="price") BigDecimal price,
                                       @RequestParam(name="stock")Integer stock,
                                       @RequestParam(name="imgUrl")String imgUrl) throws BusinessException {
        //封装service请求创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        itemModel.setSales(0);

        ItemModel item = itemService.createItem(itemModel);
        ItemVO itemVO = convertVOFromModel(item);

        return CommonReturnType.create(itemVO);

    }

    @RequestMapping(value = "/publishpromo", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType publishpromo(@RequestParam(name = "id") Integer id){
        promoService.pushlishPromo(id);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listItem(){

        List<ItemModel> itemModelList = itemService.listItem();

        List<ItemVO> collect = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(collect);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name="id")Integer id){

        ItemModel itemModel = null;

        //先取本地缓存
        itemModel = (ItemModel) cacheService.getFromCommonCache("item_" + id);

        if(itemModel == null){
            //获取redis
            itemModel = (ItemModel) redisTemplate.opsForValue().get("item_" + id);
            //若redis内不存在对应的itemModel,访问下游service

            if(itemModel == null){
                itemModel = itemService.getItemById(id);
                //设置到redis中
                redisTemplate.opsForValue().set("item_"+id, itemModel);
                redisTemplate.expire("item_"+id, 10, TimeUnit.MINUTES);
            }

            //填充本地缓存
            cacheService.setCommonCache("item_"+id, itemModel);
        }

        ItemVO itemVO = convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    private ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        if(itemModel.getPromoModel()!=null){
            //有正在进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoPrice());
        }else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
