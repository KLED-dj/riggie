package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
* @createDate 2023-01-28 10:15:23
* @Entity com.kled.domain.DishFlavor
*/
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




