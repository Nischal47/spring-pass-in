package com.example.passin.domain.password;

import com.example.base.mapper.ReferenceMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface PasswordMapper {
}
