package com.along.seckill.service;

import com.along.seckill.dao.PromotionSecKillDAO;
import com.along.seckill.entity.PromotionSecKill;
import com.along.seckill.exception.SecKillException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class PromotionSecKillService {
    @Resource
    private PromotionSecKillDAO promotionSecKillDAO;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void processSecKill(Long psId, String userid, Integer num) throws SecKillException {
        PromotionSecKill ps = promotionSecKillDAO.findById(psId);
        if (ps == null) {
            //秒杀活动不存在
            throw new SecKillException("秒杀活动不存在");
        }
        if (ps.getStatus() == 0) {
            throw new SecKillException("秒杀活动还未开始");
        } else if (ps.getStatus() == 2) {
            throw new SecKillException("秒杀活动已经结束");
        }
        Integer goodsId = (Integer) redisTemplate.opsForList().leftPop("seckill:count:" + ps.getPsId());
        if (goodsId != null) {
            //判断是否已经抢购过
            boolean isExisted = redisTemplate.opsForSet().isMember("seckill:users:" + ps.getPsId(), userid);
            if (!isExisted) {
                System.out.println("恭喜您，抢到商品啦。快去下单吧");
                redisTemplate.opsForSet().add("seckill:users:" + ps.getPsId(), userid);
            } else {
                redisTemplate.opsForList().rightPush("seckill:count:" + ps.getPsId(), ps.getGoodsId());
                throw new SecKillException("抱歉，您已经参加过此活动，请勿重复抢购！");
            }
        } else {
            throw new SecKillException("抱歉，该商品已被抢光，下次再来吧！！");
        }
    }

    /**
     * 消息生产处，在GoodController的doOrder调用
     * @param userId
     * @return
     */
    public String sendOrderToQueue(String userId) {
        System.out.println("准备向mq发送信息");
        Map data = new HashMap();
        data.put("userid", userId);
        String orderNo = UUID.randomUUID().toString();
        data.put("orderNo", orderNo);
        //附加额外的订单信息
        rabbitTemplate.convertAndSend("exchange-order", null, data);
        return orderNo;
    }
}
