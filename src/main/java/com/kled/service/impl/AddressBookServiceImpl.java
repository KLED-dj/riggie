package com.kled.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kled.domain.AddressBook;
import com.kled.mapper.AddressBookMapper;
import com.kled.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
