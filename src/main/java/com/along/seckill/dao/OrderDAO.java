package com.along.seckill.dao;

import com.along.seckill.entity.Order;

/**
 * @author huanglong
 * @date 2020-02-20
 */
public interface OrderDAO {

    void insert(Order order);

    Order findByOrderNo(String orderNo);

}
