package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 新订单的request请求
 */
@Builder
@Getter
@Setter
public class NewOrderRequest {
    private String customer;
    private List<String> items;
}
