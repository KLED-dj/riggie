package com.kled.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kled.domain.ShoppingCart;
import com.kled.mapper.ShoppingCartMapper;
import com.kled.service.ShoppingCarService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCarServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCarService {
}
