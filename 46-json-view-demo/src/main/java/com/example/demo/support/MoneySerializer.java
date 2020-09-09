package com.example.demo.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * 序列化器
 * @JsonComponent注解，springboot自动注入序列化器和反序列化器，注册到jackson对应类当中
 */
@JsonComponent
public class MoneySerializer extends StdSerializer<Money> {
    protected MoneySerializer() {
        super(Money.class);
    }

    @Override
    public void serialize(Money money, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //通过money.getAmount()将money类型序列化成BigDecimal类型
        jsonGenerator.writeNumber(money.getAmount());
    }
}
