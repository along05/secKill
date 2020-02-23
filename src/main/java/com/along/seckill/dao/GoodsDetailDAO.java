package com.along.seckill.dao;


import com.along.seckill.entity.GoodsDetail;

import java.util.List;

public interface GoodsDetailDAO {
    public List<GoodsDetail> findByGoodsId(Long goodsId);
}
