package com.along.seckill.dao;

import com.along.seckill.entity.PromotionSecKill;
import java.util.List;

public interface PromotionSecKillDAO {
    List<PromotionSecKill> findUnstartSecKill();
    void update(PromotionSecKill ps);
    PromotionSecKill findById(Long psId);
    List<PromotionSecKill> findExpireSecKill();
}