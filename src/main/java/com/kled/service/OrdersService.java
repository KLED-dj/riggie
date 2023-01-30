package com.kled.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kled.domain.Orders;


public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
