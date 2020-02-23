package com.along.seckill.exception;

/**
 * 自定义异常 -- 主要是秒杀相关，很简陋的处理异常。
 */
public class SecKillException extends Exception{
    public SecKillException(String msg){
        super(msg);
    }
}
