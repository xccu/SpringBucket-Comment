package com.example.mybatispagehelperdemo.mapper;

import com.example.mybatispagehelperdemo.model.Coffee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface CoffeeMapper {
    //使用rowBounds
    @Select("select * from t_coffee order by id")
    List<Coffee> findAllWithRowBounds(RowBounds rowBounds);

    //使用页码和页大小
    @Select("select * from t_coffee order by id")
    List<Coffee> findAllWithParam(@Param("pageNum") int pageNum,
                                  @Param("pageSize") int pageSize);
}
