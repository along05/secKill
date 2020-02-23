package com.along.seckill.dao;


import com.along.seckill.entity.GoodsParam;

import java.util.List;

public interface GoodsParamDAO {
    List<GoodsParam> findByGoodsId(Long goodsId);
}
