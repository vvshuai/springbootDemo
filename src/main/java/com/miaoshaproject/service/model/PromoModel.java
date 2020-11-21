package com.miaoshaproject.service.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 19:58 2020/5/5
 * @Modified By:
 */
public class PromoModel implements Serializable {

    private Integer id;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    //1为未开始 2为进行中 3为结束
    private Integer status;

    //秒杀活动名称
    private String promoName;

    //开始
    private DateTime startDate;

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    //结束
    private DateTime endDate;

    //适用商品
    private Integer itemId;

    //商品价格
    private BigDecimal promoPrice;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }
}
