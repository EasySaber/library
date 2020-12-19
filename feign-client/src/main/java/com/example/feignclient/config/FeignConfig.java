package com.example.feignclient.config;

import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ResourceBundle;

/**
 * @author Aleksey Romodin
 */
@Configuration
public class FeignConfig {

    //UserName и Password для доступа к модулям(auth.properties)
    private final ResourceBundle auth = ResourceBundle.getBundle("auth");

    //Базовая авторизация
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(
                auth.getString("main-module.username"),
                auth.getString("main-module.password"));
    }

    //Перехватчик ошибок
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
