package com.example.jpacomplexdemo.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//订单实体类
@Entity                     //数据实体
@Table(name = "T_ORDER")    //映射的表名T_ORDER
@Data                       //定义getter、setter、toString
@ToString(callSuper = true) //打印出父类的属性
@NoArgsConstructor          //空构造函数
@AllArgsConstructor         //包含所有参数的构造函数
@Builder                    //定义builder
public class CoffeeOrder extends BaseEntity implements Serializable {

    private String customer;

    @ManyToMany                           //多对多映射
    @JoinTable(name = "T_ORDER_COFFEE")   //映射表关系
    private List<Coffee> items;

    @Enumerated                //定义枚举
    @Column(nullable = false)  //非空
    private OrderState state;

}

