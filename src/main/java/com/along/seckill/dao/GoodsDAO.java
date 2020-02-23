package com.along.seckill.dao;



import com.along.seckill.entity.Goods;

import java.util.List;

public interface GoodsDAO {

    Goods findById(Long goodsId);

    List<Goods> findAllGoods();

    List<Goods> findLastFiveMinute() ;

}
