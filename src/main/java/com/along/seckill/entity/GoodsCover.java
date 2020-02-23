package com.along.seckill.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsCover implements Serializable {
    private Long gcId;
    private Long goodsId;
    private String gcPicUrl;
    private String gcThumbUrl;
    private Long gcOrder;
}
