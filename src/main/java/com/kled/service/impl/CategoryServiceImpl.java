package com.kled.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kled.common.CustomException;
import com.kled.domain.Category;
import com.kled.domain.Dish;
import com.kled.domain.Setmeal;
import com.kled.mapper.CategoryMapper;
import com.kled.service.CategoryService;
import com.kled.service.DishService;
import com.kled.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishqw = new LambdaQueryWrapper<>();
        //添加查询条件
        dishqw.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishqw);

        //查询当前分类是否关联菜品，如果关联，直接抛业务异常
        if (count>0){
            //抛出异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
     //查询当前分类是否关联套餐，如果关联，直接抛业务异常
        LambdaQueryWrapper<Setmeal> sqw = new LambdaQueryWrapper<>();
        sqw.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(sqw);

        if(count1>0){
            //抛出异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除

        super.removeById(id);

    }
}
