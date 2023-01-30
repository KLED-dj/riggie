package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.Category;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2023-01-17 14:52:22
* @Entity com.kled.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




