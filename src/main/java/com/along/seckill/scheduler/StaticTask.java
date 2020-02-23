package com.along.seckill.scheduler;

import com.along.seckill.entity.Goods;
import com.along.seckill.service.GoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class StaticTask {

    @Resource
    private Configuration freeMarkerConfig ;

    @Resource
    private GoodsService goodsService;

    //每秒执行一次
    @Scheduled(cron = "0 0/5 * * * ?")
    public void doStatic() throws TemplateException, IOException {
        //获取模板对象
        Template template = freeMarkerConfig.getTemplate("goods.ftl") ;

        List<Goods> allGoods = goodsService.findLastFiveMinute() ;
        for (Goods good : allGoods ){
            Long gid = good.getGoodsId() ;
            Map param = new HashMap() ;
            param.put("goods" , goodsService.getGoods(gid)) ;
            param.put("covers" , goodsService.findCovers(gid)) ;
            param.put("details" , goodsService.findDetails(gid)) ;
            param.put("params" , goodsService.findParams(gid)) ;

            File targetFile = new File("/Users/along/Desktop/template/good" + gid + ".html");
            FileWriter out = new FileWriter(targetFile) ;
            template.process(param , out);
            out.close();
        }
    }

}
