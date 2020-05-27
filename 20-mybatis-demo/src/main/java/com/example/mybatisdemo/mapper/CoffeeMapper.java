package com.example.mybatisdemo.mapper;

import com.example.mybatisdemo.model.Coffee;
import org.apache.ibatis.annotations.*;

//Mapper的定义
@Mapper
public interface CoffeeMapper {

    //insert注解，用于执行insert操作
    @Insert("insert into t_coffee (name, price, create_time, update_time)"
            + "values (#{name}, #{price}, now(), now())")
    @Options(useGeneratedKeys = true)  //使用生成的key
    int save(Coffee coffee);  //返回DML影响的数据条数

    //select注解，用于执行select操作
    @Select("select * from t_coffee where id = #{id}")
    @Results({ //Results注解指定结果集映射
            @Result(id = true, column = "id", property = "id"),       //指定id字段为主键
            @Result(column = "create_time", property = "createTime"), //下划线到驼峰的映射

            // map-underscore-to-camel-case = true 可以实现一样的效果
            // @Result(column = "update_time", property = "updateTime"),
    })
    Coffee findById(@Param("id") Long id); //传入参数为id
}

