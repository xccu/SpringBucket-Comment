package com.example.jpademo.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.money.Money;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//咖啡实体类
@Entity                     //数据实体
@Table(name = "T_MENU")     //映射的表名T_MENU
@Builder                    //定义builder
@Data                       //定义getter、setter、toString
@ToString(callSuper = true) //打印出父类的属性
@NoArgsConstructor          //空构造函数
@AllArgsConstructor         //包含所有参数的构造函数
public class Coffee extends BaseEntity implements Serializable {

    /*@Id                 //主键
    @GeneratedValue     //自动生成
    private Long id;*/

    private String name;

    @Column
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;

    /*@Column(updatable = false)  //不可修改
    @CreationTimestamp          //创建时间戳
    private Date createTime;

    @UpdateTimestamp            //更新时间戳
    private Date updateTime;*/
}