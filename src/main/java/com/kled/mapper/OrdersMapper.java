package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2023-01-30 09:46:44
* @Entity com.kled.domain.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}




