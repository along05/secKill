package com.along.seckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Evaluate {


    private Long evaluateId ;

    private String content ;

    private Integer stars ;

    private Date createTime ;

    private Long goodId ;

}
