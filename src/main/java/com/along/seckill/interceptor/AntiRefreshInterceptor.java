package com.along.seckill.interceptor;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 流量防刷和反爬虫拦截器
 */
@Component
public class AntiRefreshInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        //获取ip+请求头信息，大致可以定位到每一台机器
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        //检查当台机器是否在黑名单
        if (redisTemplate.hasKey("anti:refresh:blacklist")) {
            if (redisTemplate.opsForSet().isMember("anti:refresh:blacklist", clientIP)) {
                response.getWriter().println("检测到ip访问异常，已被加入黑名单");
                return false;
            }
        }

        //一分钟内小于30次访问就正常访问，如果30-100就暂停服务，大于100直接拉黑
        //给每台机器访问次数添加上redis-key，访问次数累加，如果超过限制就拉黑
        String key = "anti:refresh:" + DigestUtils.md5Hex(clientIP + "_" + userAgent);

        Integer num = (Integer) redisTemplate.opsForValue().get(key);
        if (num == null) {
            redisTemplate.opsForValue().set(key, 1);
        } else {
            if (num > 30 && num < 100) {
                response.getWriter().println("请求过于频繁，请稍后再试!");
                redisTemplate.opsForValue().increment(key, 1);
                return false;
            } else if (num >= 100) {
                response.getWriter().println("检测到ip异常，已被假如黑名单");
                redisTemplate.opsForSet().add("anti:refresh:blacklist", clientIP);
                return false;
            } else {
                redisTemplate.opsForValue().increment(key, 1);
            }
        }
        return true;
    }


}
