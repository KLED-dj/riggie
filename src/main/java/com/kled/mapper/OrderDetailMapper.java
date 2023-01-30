package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2023-01-30 09:46:45
* @Entity com.kled.domain.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




