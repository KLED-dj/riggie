package com.kled.controller;

import com.kled.common.R;
import com.kled.domain.Orders;
import com.kled.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * @TODO 添加展示订单功能
     * @return
     */
    @GetMapping("/list")
    public R<String> list(){
        return null;
    }
}
