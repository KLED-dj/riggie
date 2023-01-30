package com.kled.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kled.common.CustomException;
import com.kled.domain.Setmeal;
import com.kled.domain.SetmealDish;
import com.kled.dto.SetmealDto;
import com.kled.mapper.SetmealMapper;
import com.kled.service.SetmealDishService;
import com.kled.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 添加套餐同时保存套餐菜品
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {

        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.in(ids!=null,Setmeal::getId,ids);
        List<Setmeal> list = this.list(qw);
        for (Setmeal setmeal : list) {
            Integer status = setmeal.getStatus();
            if(status==0){
                this.removeById(setmeal.getId());
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(SetmealDish::getSetmealId,ids);
                setmealDishService.remove(queryWrapper);

            }else{
                throw new CustomException("删除的套餐中有正在发售的套餐，请停售后重试");
            }
        }

    }
}
