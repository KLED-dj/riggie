package com.kled.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kled.domain.Dish;
import com.kled.dto.DishDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void deleteByIds(List<Long> ids);

}
