package com.example.reactivespringbucks.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;

/**
 * JSON反序列化
 */
public class MoneyDeserializer extends StdDeserializer<Money> {
    protected MoneyDeserializer() {
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Money.ofMinor(CurrencyUnit.of("CNY"), p.getLongValue());
    }
}
