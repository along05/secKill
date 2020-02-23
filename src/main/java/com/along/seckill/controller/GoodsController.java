package com.along.seckill.controller;


import com.along.seckill.entity.Evaluate;
import com.along.seckill.entity.Goods;
import com.along.seckill.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class GoodsController {

    Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;


    //拼装页面需要的数据，并展示页面
    @GetMapping("/good")
    public ModelAndView showGoods(Long gid) {
        logger.info("gid:" + gid);
        ModelAndView mav = new ModelAndView("/goods");
        Goods goods = goodsService.getGoods(gid);
        mav.addObject("goods", goods);
        mav.addObject("covers", goodsService.findCovers(gid));
        mav.addObject("details", goodsService.findDetails(gid));
        mav.addObject("params", goodsService.findParams(gid));
        return mav;
    }

    //ajax动态获取评论数据 - 涉及到后面静态化操作，所以这里是动态获取评论
    @GetMapping("/getEvalute/{gid}")
    @ResponseBody
    public List<Evaluate> getEvalute(@PathVariable("gid") Long gid) {
        List<Evaluate> evaluates = goodsService.findEvalutesByGid(gid);
        return evaluates;
    }


    //点击购买生成订单
    @GetMapping("/doOrder")
    @ResponseBody
    public Map doOrder(String userId, String goodId) {
        boolean succces = goodsService.insertInOrder(userId, goodId);
        Map returnMessage = new HashMap();
        returnMessage.put("code", succces == true ? 200 : 500);
        returnMessage.put("message", succces == true ? "购买成功~" : "该用户已经抢购过该商品，不能重复购买~");
        returnMessage.put("date", null);
        return returnMessage;
    }


}
