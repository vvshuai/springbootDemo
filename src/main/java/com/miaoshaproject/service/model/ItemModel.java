package com.miaoshaproject.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 22:03 2020/5/4
 * @Modified By:
 */
public class ItemModel {

    private Integer id;


    //商品名
    @NotBlank(message = "商品名不为空")
    private String title;

    @NotNull(message = "商品价格不为空")
    @Min(value = 0, message = "价格大于0")
    private BigDecimal price;

    @NotNull(message = "库存不能不填")
    private Integer stock;

    //描述
    @NotBlank(message = "商品描述不为空")
    private String description;

    //销量
    private Integer sales;

    //图片url
    @NotBlank(message = "图片不为空")
    private String imgUrl;

    private PromoModel promoModel;

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

