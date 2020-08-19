package com.example.reactivespringbucks.converter;

import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Converter用于对money类转换
 */
@WritingConverter
public class MoneyWriteConverter implements Converter<Money, Long> {
    @Override
    public Long convert(Money money) {
        return money.getAmountMinorLong();
    }
}
