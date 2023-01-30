package com.kled.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kled.domain.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2023-01-29 15:37:13
* @Entity com.kled.domain.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




