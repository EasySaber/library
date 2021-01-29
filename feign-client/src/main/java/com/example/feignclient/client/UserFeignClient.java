package com.example.feignclient.client;

import com.example.feignclient.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aleksey Romodin
 */
@RequestMapping("/api/user")
@FeignClient(name = "main-module")
public interface UserFeignClient {

    //Добавление нового пользователя
    @PostMapping("/add")
    void addNewUser(@RequestBody UserDto user);
}
