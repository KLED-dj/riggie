package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【shopping_cart(购物车)】的数据库操作Mapper
* @createDate 2023-01-29 17:00:35
* @Entity com.kled.domain.ShoppingCart
*/
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}




