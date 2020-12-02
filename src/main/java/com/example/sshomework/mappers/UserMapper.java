package com.example.sshomework.mappers;

import com.example.sshomework.dto.user.UserDto;
import com.example.sshomework.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface UserMapper extends MapMapper<UserDto, User>{
    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);
}
