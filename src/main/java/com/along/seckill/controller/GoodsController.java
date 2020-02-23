package com.along.seckill.controller;


import com.along.seckill.entity.Evaluate;
import com.along.seckill.entity.Goods;
import com.along.seckill.exception.SecKillException;
import com.along.seckill.service.GoodsService;
import com.along.seckill.service.PromotionSecKillService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsController {

    Logger logger = LoggerFactory.getLogger(GoodsController.class);


    @Resource
    private Configuration freeMarkerConfig ;
    @Resource
    private GoodsService goodsService;
    @Resource
    private PromotionSecKillService promotionSecKillService;

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
    
    //点击购买生成订单,这里psId前端界面写死了，即就是1，
    @GetMapping("/doOrder")
    @ResponseBody
    public Map doOrder(Long psId, String userId) throws SecKillException {
        promotionSecKillService.processSecKill(psId, userId,1);
        Map returnMessage = new HashMap();
        returnMessage.put("code", 200 );
        returnMessage.put("message",  "购买成功~" );
        returnMessage.put("date", null);
        return returnMessage;
    }

    //静态化，先执行一遍doStatic，然后定时器会轮询修改过的商品重新生成
    @GetMapping("/doStatic")
    @ResponseBody
    public String doStatic() throws TemplateException, IOException {
        //获取模板对象
        Template template = freeMarkerConfig.getTemplate("goods.ftl") ;

        List<Goods> allGoods = goodsService.findAllGoods() ;
        for (Goods good : allGoods ){
            Long gid = good.getGoodsId() ;
            Map param = new HashMap() ;
            param.put("goods" , goodsService.getGoods(gid)) ;
            param.put("covers" , goodsService.findCovers(gid)) ;
            param.put("details" , goodsService.findDetails(gid)) ;
            param.put("params" , goodsService.findParams(gid)) ;

            File targetFile = new File("/Users/along/Desktop/template/good1/" + gid + ".html");
            FileWriter out = new FileWriter(targetFile) ;
            template.process(param , out);
            out.close();
        }
        return "success" ;
    }

}
