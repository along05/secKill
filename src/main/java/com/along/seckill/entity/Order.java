package com.along.seckill.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Order {
    private Long orderId;
    private String orderNo;
    private Integer orderStatus;
    private String userid;
    private String recvName;
    private String recvAddress;
    private String recvMobile;
    private Float postage;
    private Float amout;
    private Date createTime;

}
