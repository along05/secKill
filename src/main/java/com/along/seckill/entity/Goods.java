package com.along.seckill.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class Goods implements Serializable {
    private Long goodsId;
    private String title;
    private String subTitle;
    private Float originalCost;
    private Float currentPrice;
    private Float discount;
    private Integer isFreeDelivery;
    private Long categoryId;

}
