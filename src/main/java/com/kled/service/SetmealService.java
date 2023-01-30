package com.kled.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kled.domain.Setmeal;
import com.kled.dto.SetmealDto;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void deleteByIds(List<Long> ids);
}
