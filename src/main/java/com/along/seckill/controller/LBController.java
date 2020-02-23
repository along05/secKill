package com.along.seckill.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * @author huanglong
 * @date 2020-02-23
 */
public class LBController {

    @Value("${server.port}")
    private String port ;


    /**
     * 根据不同端口的应用来测试负载均衡
     * @return
     */
    @GetMapping("/lb")
    @ResponseBody
    public String lb(){
        return "this port is:" + port ;
    }


    /**
     * login和check方法是用来测试session不同步问题的方法。
     */

    @GetMapping("/login")
    @ResponseBody
    public String login(String u , WebRequest request){
        request.setAttribute("user" , u , request.SCOPE_SESSION);
        return "port:" + port + ",login success" ;
    }

    @GetMapping("/check")
    @ResponseBody
    public String check(WebRequest request){
        String user = (String) request.getAttribute("user", WebRequest.SCOPE_SESSION) ;
        if (user != null){
            return "port:"+port+",user="+user;
        }else{
            return "port:"+port+"没有登录";
        }
    }

}
