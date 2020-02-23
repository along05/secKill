package com.along.seckill.dao;



import com.along.seckill.entity.GoodsCover;

import java.util.List;

public interface GoodsCoverDAO {
     List<GoodsCover> findByGoodsId(Long goodsId);
}
