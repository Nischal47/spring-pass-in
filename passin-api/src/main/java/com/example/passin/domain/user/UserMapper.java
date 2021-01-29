package com.example.passin.domain.user;

import com.example.base.mapper.BaseMapper;
import com.example.base.mapper.ReferenceMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface UserMapper extends BaseMapper<UserDto,User> {
}
