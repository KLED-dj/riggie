package com.kled.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kled.domain.Employee;
import com.kled.mapper.EmployeeMapper;
import com.kled.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
