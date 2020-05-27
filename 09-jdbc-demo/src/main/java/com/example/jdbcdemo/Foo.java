package com.example.jdbcdemo;

import lombok.Builder;
import lombok.Data;

//数据实体类
@Data
@Builder
public class Foo {
    private Long id;
    private String bar;
}
