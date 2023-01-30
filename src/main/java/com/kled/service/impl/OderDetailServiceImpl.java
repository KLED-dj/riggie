package com.kled.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kled.domain.OrderDetail;
import com.kled.mapper.OrderDetailMapper;
import com.kled.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
