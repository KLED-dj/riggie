package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2023-01-29 11:37:25
* @Entity com.kled.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




