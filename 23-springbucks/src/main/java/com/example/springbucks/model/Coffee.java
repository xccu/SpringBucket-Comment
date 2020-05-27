package com.example.springbucks.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "T_COFFEE")
@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity implements Serializable {

    private String name;

    //使用PersistentMoneyMinorAmount类型
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
}
