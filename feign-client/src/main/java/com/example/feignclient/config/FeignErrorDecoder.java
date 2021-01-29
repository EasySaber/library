package com.example.feignclient.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.webjars.NotFoundException;

/**
 * @author Aleksey Romodin
 */
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()){
            case 400:
                return new Exception("Метод: " + s + " Error: Ошибка в переданных данных");
            case 401:
                return new Exception("Метод: " + s + " Error: Ошибка авторизации");
            case 404:
                return new NotFoundException("Метод: " + s + " Error: Запись не найдена");
            case 423:
                return new Exception("Метод: " + s + " Error: Запрет совершения действий");
            default:
                return new Exception("Метод: " + s + " Error");
        }
    }
}
