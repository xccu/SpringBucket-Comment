package com.example.jpademo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

//实体类基类
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {

    @Id                 //主键
    @GeneratedValue     //自动生成
    private Long id;

    @Column(updatable = false)  //不可修改
    @CreationTimestamp          //创建时间戳
    private Date createTime;

    @UpdateTimestamp            //更新时间戳
    private Date updateTime;
}
