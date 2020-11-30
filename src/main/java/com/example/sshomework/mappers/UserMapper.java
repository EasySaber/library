package com.example.sshomework.mappers;

import com.example.sshomework.dto.UserDto;
import com.example.sshomework.entity.User;
import org.mapstruct.Mapper;

/**
 * @author Aleksey Romodin
 */
@Mapper
public interface UserMapper extends MapMapper<UserDto, User>{
}
