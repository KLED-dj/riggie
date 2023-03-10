package com.kled.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kled.common.R;
import com.kled.domain.Category;
import com.kled.domain.Dish;
import com.kled.domain.Setmeal;
import com.kled.domain.SetmealDish;
import com.kled.dto.SetmealDto;
import com.kled.service.CategoryService;
import com.kled.service.SetmealDishService;
import com.kled.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
@Api(tags = "套餐相关信息")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加套餐
     *
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache" ,allEntries = true)
    @PostMapping
    @ApiOperation(value = "新增套餐接口")
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("添加套餐成功");
    }

    @ApiOperation(value = "套餐分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "套餐名称",required = false)
    })
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构建分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();

        //条件查询
        qw.like(name != null, Setmeal::getName, name);
        qw.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, qw);
        //流处理

        BeanUtils.copyProperties(pageInfo, setmealPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = setmealDto.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String name1 = byId.getName();
                setmealDto.setCategoryName(name1);
            }
            return setmealDto;

        }).collect(Collectors.toList());

        setmealPage.setRecords(list);

        return R.success(setmealPage);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "套餐删除接口")
    @CacheEvict(value = "setmealCache" ,allEntries = true)
    @DeleteMapping
    public R<String> deleteById(@RequestParam List<Long> ids) {
        setmealService.deleteByIds(ids);
        return R.success("删除成功");
    }

    /**
     * 起停售套餐
     *
     * @param ids
     * @param status
     * @return
     */
    @ApiOperation(value = "套餐修改状态接口")
    @PostMapping("/status/{status}")
    public R<String> status(@RequestParam List<Long> ids, @PathVariable int status) {

        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.in(Setmeal::getId,ids);
        List<Setmeal> list = setmealService.list(qw);

        for (Setmeal setmeal : list) {
            if(setmeal!=null){
                setmeal.setStatus(status);
                setmealService.updateById(setmeal);
            }
        }

        return R.success("修改套餐售卖状态成功");
    }
    @ApiOperation(value = "套餐条件查询列表接口")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        qw.eq(Setmeal::getStatus,1);
        qw.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(qw);

//        list.stream().map((item)->{
//            SetmealDto setmealDto = new SetmealDto();
//
//            BeanUtils.copyProperties(item,setmeal);
//
//
//        })
        return R.success(list);
    }
}
