package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Mapper
* @createDate 2023-01-28 16:41:22
* @Entity com.kled.domain.SetmealDish
*/
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

}




