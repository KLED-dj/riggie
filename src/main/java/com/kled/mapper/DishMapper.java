package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2023-01-28 10:15:23
* @Entity com.kled.domain.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




