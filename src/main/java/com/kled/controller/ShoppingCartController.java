package com.kled.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kled.common.BaseContext;
import com.kled.common.R;
import com.kled.domain.ShoppingCart;
import com.kled.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCarService shoppingCarService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询是否有数据，有则加数量，无则加数据
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null) {
            //添加到购物车的是菜品
            qw.eq(ShoppingCart::getDishId,dishId);

        } else {
            //否则是套餐
            qw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart carServiceOne = shoppingCarService.getOne(qw);

        if (carServiceOne!=null){
            //如果已经存在执行更新操作
            Integer number = carServiceOne.getNumber();
            carServiceOne.setNumber(number+1);
            shoppingCarService.updateById(carServiceOne);
        }else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCarService.save(shoppingCart);
            carServiceOne=shoppingCart;
        }

        return R.success(carServiceOne);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,currentId);
        qw.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCarService.list(qw);
        return R.success(list);
    }


    @DeleteMapping("/clean")
    public R<String> clean(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,currentId);
        shoppingCarService.remove(qw);
        return R.success("删除成功");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,currentId);
        if (shoppingCart.getDishId() != null) {
            //添加到购物车的是菜品
            qw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());

        } else {
            //否则是套餐
            qw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCarService.getOne(qw);
        if(one.getNumber()==1){
            shoppingCarService.removeById(one);
            one.setNumber(0);
        }else{
            Integer number = one.getNumber();
            one.setNumber(number-1);
            shoppingCarService.updateById(one);
        }

        return R.success(one);
    }
}
