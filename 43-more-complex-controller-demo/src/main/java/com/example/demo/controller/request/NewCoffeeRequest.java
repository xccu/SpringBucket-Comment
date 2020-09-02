package com.example.demo.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class NewCoffeeRequest {
    //非空 不满足条件会导致校验失败
    @NotEmpty
    private String name;
    //非null 不满足条件会导致校验失败
    @NotNull
    private Money price;
}
