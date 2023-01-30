package com.kled.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kled.domain.User;
import com.kled.mapper.UserMapper;
import com.kled.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
