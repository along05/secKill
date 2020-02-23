package com.along.seckill.service;

import com.along.seckill.dao.*;
import com.along.seckill.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    @Resource
    private GoodsDAO goodsDAO;
    @Resource
    private GoodsCoverDAO goodsCoverDAO;
    @Resource
    private GoodsDetailDAO goodsDetailDAO;
    @Resource
    private GoodsParamDAO goodsParamDAO;
    @Resource
    private EvaluteDAO evaluteDAO;
    @Resource
    private OrderDAO orderDAO;

    @Cacheable(value = "goods", key = "#goodsId")
    public Goods getGoods(Long goodsId) {
        return goodsDAO.findById(goodsId);
    }

    @Cacheable(value = "convers", key = "#goodsId")
    public List<GoodsCover> findCovers(Long goodsId) {
        return goodsCoverDAO.findByGoodsId(goodsId);
    }

    @Cacheable(value = "details", key = "#goodsId")
    public List<GoodsDetail> findDetails(Long goodsId) {
        return goodsDetailDAO.findByGoodsId(goodsId);
    }

    @Cacheable(value = "params", key = "#goodsId")
    public List<GoodsParam> findParams(Long goodsId) {
        return goodsParamDAO.findByGoodsId(goodsId);
    }

    public List<Evaluate> findEvalutesByGid(Long gid) {
        return evaluteDAO.findEvalutesByGid(gid);
    }

    public boolean insertInOrder(String userId, String goodId) {

        Order haveOrder = orderDAO.findByOrderNo(goodId);
        if (haveOrder != null) {
            return false;
        } else {
            Order order = new Order();
            order.setOrderNo(goodId);
            order.setOrderStatus(0);
            order.setUserid(userId);
            //这里用户信息，商品信息，为了简便，就随便写了，应该是从数据库查询。
            order.setRecvName("xxx");
            order.setRecvMobile("1393310xxxx");
            order.setRecvAddress("xxxxxxxxxx");
            order.setAmout(19.8f);
            order.setPostage(0f);
            order.setCreateTime(new Date());
            orderDAO.insert(order);
            return true;
        }
    }


    public List<Goods> findLastFiveMinute() {
        return goodsDAO.findLastFiveMinute();
    }

    public List<Goods> findAllGoods(){
        return goodsDAO.findAllGoods() ;
    }

}
