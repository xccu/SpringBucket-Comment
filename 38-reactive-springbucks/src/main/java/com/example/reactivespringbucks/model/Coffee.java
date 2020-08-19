package com.example.reactivespringbucks.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.example.reactivespringbucks.serializer.MoneyDeserializer;
import com.example.reactivespringbucks.serializer.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Table("t_coffee")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
    @Id
    private Long id;
    private String name;

    //使用MoneySerializer序列化和反序列化
    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private Money price;
    private Date createTime;
    private Date updateTime;
}
