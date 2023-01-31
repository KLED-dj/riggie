package com.kled.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kled.common.BaseContext;
import com.kled.common.R;
import com.kled.domain.Category;
import com.kled.domain.Dish;
import com.kled.domain.DishFlavor;
import com.kled.dto.DishDto;
import com.kled.service.CategoryService;
import com.kled.service.DishFlavorService;
import com.kled.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        String key = "dish_"+dishDto.getCategoryId()+"_"+dishDto.getStatus();
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishPageInfo=new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        //添加过滤条件
        qw.like(name!=null,Dish::getName,name);
        //添加排序条件
        qw.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,qw);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishPageInfo,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category !=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());


        dishPageInfo.setRecords(list);

        return R.success(dishPageInfo);
    }

    /**
     * 根据id查询菜品信息和对应的口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> Update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);

        //清理所有菜品的缓存数据
//        Set keys = redisTemplate.keys("*");
        //清理某个分类下的菜品
        String key = "dish_"+dishDto.getCategoryId()+"_"+dishDto.getStatus();
        redisTemplate.delete(key);
        return R.success("修改菜品成功");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        //删除菜品
        dishService.deleteByIds(ids);

        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") int status,@RequestParam List<Long> ids){
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.in(ids!=null,Dish::getId,ids);
        List<Dish> list = dishService.list(qw);

        for (Dish dish : list) {
            if(dish!=null){
                dish.setStatus(status);
                dishService.updateById(dish);
                String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
                redisTemplate.delete(key);
            }
        }


        return R.success("售卖状态添加成功");
    }

//    /**
//     * 根据条件查询对应的菜品数据
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        //构造查询条件
//        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
//        qw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        qw.eq(Dish::getStatus,1);
//        //排序条件
//        qw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(qw);
//
//        return R.success(list);
//    }
    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList =null;
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //先从redis中获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        if(dishDtoList!=null){
            //如果存在直接返回
            log.info("存在缓存");
            return R.success(dishDtoList);
        }

        //如果不存在，
        //构造查询条件
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        qw.eq(Dish::getStatus,1);
        //排序条件
        qw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(qw);

        dishDtoList =list.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = dishDto.getCategoryId();

            Category category = categoryService.getById(categoryId);

            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> Flavorqw = new LambdaQueryWrapper<>();
            Flavorqw.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(Flavorqw);

            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

        //查询数据库，讲查询到的菜品数据缓存到redis

        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        log.info("不存在缓存,执行查询数据库，并缓存成功");
        return R.success(dishDtoList);
    }
}
