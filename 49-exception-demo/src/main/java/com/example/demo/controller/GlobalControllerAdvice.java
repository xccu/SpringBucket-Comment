package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 *类似于AOP，拦截所有controller并处理指定类型的Exception
 * @ControllerAdvice优先级低于@Controller
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    //ExceptionHandler用于处理异常，此处指定为处理validationException
    @ExceptionHandler(ValidationException.class)
    //抛出ValidationException异常时，作为ResponseBody返回
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(ValidationException exception) {
        Map<String, String> map = new HashMap<>();
        //构造map对象，传入exception message
        map.put("message", exception.getMessage());
        return map;
    }
}
